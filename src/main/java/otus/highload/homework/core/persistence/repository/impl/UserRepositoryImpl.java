package otus.highload.homework.core.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import otus.highload.homework.core.persistence.entity.UserEntity;
import otus.highload.homework.core.persistence.repository.UserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
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

    private final UserEntityMapper userEntityMapper = new UserEntityMapper();

    @Override
    @NonNull
    public Optional<UserEntity> findById(@NonNull UUID userId) {
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(true);
        List<UserEntity> query = jdbcTemplate.query("SELECT * FROM public.user u WHERE u.id = ?",
                userEntityMapper,
                userId);
        if (query.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.get(0));
    }

    @Override
    @NonNull
    public List<UserEntity> findAll() {
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(true);
        return jdbcTemplate.query("SELECT * FROM public.user u",
                userEntityMapper);
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

    @Override
    @NonNull
    public void saveAll(@NonNull List<UserEntity> users) {
        jdbcTemplate.batchUpdate("""
                        INSERT INTO public.user(id, 
                            first_name, 
                            second_name, 
                            birth_date,
                            biography,
                            city, 
                            password) 
                        VALUES(?, ?, ?, ?, ?, ?, ?)
                        """,
                users, 500, (PreparedStatement ps, UserEntity user) -> {
                    ps.setObject(1, user.getUserId());
                    ps.setString(2, user.getFirstName());
                    ps.setString(3, user.getSecondName());
                    ps.setDate(4, Date.valueOf(user.getBirthdate()));
                    ps.setString(5, user.getBiography());
                    ps.setString(6, user.getCity());
                    ps.setString(7, user.getPassword());
                });
    }

    @NonNull
    @Override
    public List<UserEntity> search(@NonNull String firstName, @NonNull String lastName) {
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(true);
        return jdbcTemplate.query("""
                        SELECT * FROM public.user u
                        WHERE u.first_name LIKE ? AND u.second_name LIKE ?
                        ORDER BY u.id
                        """,
                userEntityMapper,
                firstName + "%",
                lastName + "%");
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
