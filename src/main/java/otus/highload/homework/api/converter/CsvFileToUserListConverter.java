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
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvFileToUserListConverter {
    private final BCryptPasswordEncoder passwordEncoder;

    @NonNull
    public List<User> convertAll(@NonNull InputStream is) {
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             var csvParser = new CSVParser(bReader, CSVFormat.DEFAULT)) {
            var stuList = new ArrayList<User>();
            var csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                var secondFirstNameArray = csvRecord.get(0).split(" ");
                var userBuilder = User.builder();
                var user = userBuilder
                        .firstName(secondFirstNameArray[1])
                        .secondName(secondFirstNameArray[0])
                        .password(passwordEncoder.encode(csvRecord.get(0)))
                        .city(csvRecord.get(2))
                        .biography("Registered from CSV user")
                        .birthdate(LocalDate.parse(csvRecord.get(1)))
                        .build();

                stuList.add(user);
            }
            return stuList;
        } catch (IOException e) {
            throw new RuntimeException("CSV data is failed to parse: " + e.getMessage());
        }
    }
}
