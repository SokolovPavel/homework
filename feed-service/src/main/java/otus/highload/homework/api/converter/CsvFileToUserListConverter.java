package otus.highload.homework.api.converter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import otus.highload.homework.core.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvFileToUserListConverter {
    private final BCryptPasswordEncoder passwordEncoder;

    @NonNull
    public List<User> convertAll(@NonNull InputStream is) {
        var password = passwordEncoder.encode("password");
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

             var csvParser = new CSVParser(bReader, CSVFormat.DEFAULT)) {
            return csvParser.stream()
                    .map(csvRecord -> mapToUser(password, csvRecord))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("CSV data is failed to parse: " + e.getMessage());
        }
    }

    @NonNull
    private User mapToUser(String password, @NonNull CSVRecord csvRecord) {
        var secondFirstNameArray = csvRecord.get(0).split(" ");
        return User.builder()
                .firstName(secondFirstNameArray[1])
                .secondName(secondFirstNameArray[0])
                .password(password)
                .city(csvRecord.get(2))
                .biography("Registered from CSV user")
                .birthdate(LocalDate.parse(csvRecord.get(1)))
                .build();
    }
}
