package qcha.arfind.utils;

import org.apache.commons.io.FileUtils;
import qcha.arfind.Constants;
import qcha.arfind.Constants.ConfigFileConstants;
import qcha.arfind.model.Company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConfigFileUtils {

    public static void saveCompanies(List<Company> data) {
        try {
            FileUtils.writeLines(
                    new File(Constants.ConfigFileConstants.CONFIG_FILENAME),
                    Constants.ConfigFileConstants.DEFAULT_CHARSET,
                    data
            );
        } catch (IOException exception) {
            throw new RuntimeException(
                    String.format("Cannot save data to file: %s", Constants.ConfigFileConstants.CONFIG_FILENAME),
                    exception
            );
        }
    }

    //todo convert to companies
    public static List<String> readCompanies() {
        if (Files.exists(Paths.get(ConfigFileConstants.CONFIG_FILENAME))) {
            List<String> data = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(ConfigFileConstants.CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(ConfigFileConstants.DEFAULT_FIELD_DELIMITER);
                    String companyName = fields[0];
                    data.add(companyName);
                }

                return data;
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file - config.csv", e);
            }
        } else {
            //todo throw Exception - cause we can't read config
            return Collections.emptyList();
        }
    }

    public static void readConfigFileToCompanyTableView(List<Company> data) {
        if (Files.exists(Paths.get(ConfigFileConstants.CONFIG_FILENAME))) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(ConfigFileConstants.CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(ConfigFileConstants.DEFAULT_FIELD_DELIMITER);
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
        if (Files.exists(Paths.get(ConfigFileConstants.CONFIG_FILENAME))) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(ConfigFileConstants.CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(ConfigFileConstants.DEFAULT_FIELD_DELIMITER);
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
        if (Files.exists(Paths.get(ConfigFileConstants.CONFIG_FILENAME))) {
            try {
                for (String filePath : readFilePathsToList()) {
                    if (Files.exists(Paths.get(filePath))) {
                        BufferedReader br = new BufferedReader(new FileReader(filePath));
                        String line;
                        while ((Objects.nonNull(line = br.readLine()))) {
                            String[] fields = line.split(ConfigFileConstants.DEFAULT_FIELD_DELIMITER);
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
