package qcha.arfind.utils;

import org.apache.commons.io.FileUtils;
import qcha.arfind.Constants;
import qcha.arfind.Constants.ConfigFileConstants;
import qcha.arfind.model.Company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ConfigFileUtils {

    public static void saveDataToConfigFile(List<String> data) {
        try {
            FileUtils.writeLines(new File(Constants.ConfigFileConstants.CONFIG_FILENAME),
                    Constants.ConfigFileConstants.DEFAULT_CHARSET,
                    data);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot save data to file - config.csv", e);
        }
    }

    public static void readConfigFileToCompanyListView(List<String> data) {
        if (Files.exists(Paths.get(ConfigFileConstants.CONFIG_FILENAME))) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(ConfigFileConstants.CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(ConfigFileConstants.DEFAULT_FIELD_DELIMITER);
                    String companyName = fields[0];
                    data.add(companyName);
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file - config.csv", e);
            }
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
}
