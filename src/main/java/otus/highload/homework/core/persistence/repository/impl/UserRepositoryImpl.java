package otus.highload.homework.core.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import otus.highload.homework.core.persistence.entity.UserEntity;
import otus.highload.homework.core.persistence.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    @NonNull
    private final JdbcTemplate jdbcTemplate;

    @Override
    @NonNull
    public Optional<UserEntity> findById(@NonNull UUID userId) {
        UserEntityMapper userEntityMapper = new UserEntityMapper();
        List<UserEntity> query = jdbcTemplate.query("SELECT * FROM public.user u WHERE u.id = ?",
                userEntityMapper,
                userId);
        if(query.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(query.get(0));
    }

    @Override
    @NonNull
    public UserEntity save(@NonNull UserEntity user) {
        jdbcTemplate.update("""
                        INSERT INTO public.user(id, 
                            first_name, 
                            second_name, 
                            birth_date,
                            biography,
                            city, 
                            password) 
                        VALUES(?, ?, ?, ?, ?, ?, ?)
                        """,
                user.getUserId(),
                user.getFirstName(),
                user.getSecondName(),
                user.getBirthdate(),
                user.getBiography(),
                user.getCity(),
                user.getPassword());

        return user;
    }

    public static class UserEntityMapper implements RowMapper<UserEntity> {
        @Override
        @NonNull
        public UserEntity mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            var userEntity = new UserEntity();
            userEntity.setUserId(UUID.fromString(rs.getString("id")))
                    .setFirstName(rs.getString("first_name"))
                    .setPassword(rs.getString("password"))
                    .setSecondName(rs.getString("second_name"))
                    .setBirthdate(rs.getDate("birth_date").toLocalDate())
                    .setCity(rs.getString("city"))
                    .setBiography(rs.getString("biography"));
            return userEntity;
        }
    }
}
