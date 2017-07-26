package qcha.arfind.utils;

import org.apache.commons.io.FileUtils;
import qcha.arfind.model.Company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static qcha.arfind.Constants.ConfigFileConstants.*;

public class ConfigFileUtils {

    public static void saveCompanies(List<Company> data) {
        try {
            FileUtils.writeLines(
                    new File(CONFIG_FILENAME),
                    DEFAULT_CHARSET,
                    data
            );
        } catch (IOException exception) {
            throw new RuntimeException(
                    String.format("Cannot save data to file: %s", CONFIG_FILENAME),
                    exception
            );
        }
    }

    public static List<Company> readCompanies() {
        if (Files.exists(Paths.get(CONFIG_FILENAME))) {
            try {
                return FileUtils.readLines(new File(CONFIG_FILENAME), DEFAULT_CHARSET)
                        .stream()
                        .map(line -> {
                            String[] fields = line.split(DEFAULT_FIELD_DELIMITER);
                            return new Company(fields[0], fields[1]);
                        })
                        .collect(Collectors.toList());

            } catch (IOException exception) {
                throw new RuntimeException(
                        String.format("Cannot read file - %s", CONFIG_FILENAME),
                        exception
                );
            }
        } else {
            return Collections.emptyList();
        }
    }

    //todo
    public static void readConfigFileToCompanyTableView(List<Company> data) {
        if (Files.exists(Paths.get(CONFIG_FILENAME))) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(DEFAULT_FIELD_DELIMITER);
                    Company company = new Company(fields[0], fields[1]);
                    data.add(company);
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file - config.csv", e);
            }
        }
    }

    private static List<String> readFilePathsToList() {
        List<String> data = new ArrayList<>();
        if (Files.exists(Paths.get(CONFIG_FILENAME))) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(DEFAULT_FIELD_DELIMITER);
                    String filePath = fields[1];
                    data.add(filePath);
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file - config.csv", e);
            }
        }
        return data;
    }


    public static void readFullDataToTableView(List<Company> data) {
        if (Files.exists(Paths.get(CONFIG_FILENAME))) {
            try {
                for (String filePath : readFilePathsToList()) {
                    if (Files.exists(Paths.get(filePath))) {
                        BufferedReader br = new BufferedReader(new FileReader(filePath));
                        String line;
                        while ((Objects.nonNull(line = br.readLine()))) {
                            String[] fields = line.split(DEFAULT_FIELD_DELIMITER);
                            Company company = new Company(fields[0], fields[1], fields[2]);
                            data.add(company);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file - config.csv", e);
            }
        }
    }
}
