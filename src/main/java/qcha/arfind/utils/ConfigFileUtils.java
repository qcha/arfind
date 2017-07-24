package qcha.arfind.utils;

import org.apache.commons.io.FileUtils;
import qcha.arfind.ConfigurationWindow;
import qcha.arfind.Constants;
import qcha.arfind.Constants.ConfigFileConstants;
import qcha.arfind.MainApplication;
import qcha.arfind.model.Company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ConfigFileUtils {

    private MainApplication mainApplication;
    private ConfigurationWindow configurationWindow;

    public ConfigFileUtils(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    public ConfigFileUtils(ConfigurationWindow configurationWindow) {
        this.configurationWindow = configurationWindow;
    }

    public void saveDataToConfigFile() {
        try {
            FileUtils.writeLines(new File(Constants.ConfigFileConstants.CONFIG_FILENAME),
                    Constants.ConfigFileConstants.DEFAULT_CHARSET,
                    configurationWindow.getCompanyData());
        } catch (IOException e) {
            throw new RuntimeException("Cannot write to this file", e);
        }
    }

    public void readConfigFileToCompanyListView() {
        if (Files.exists(Paths.get(ConfigFileConstants.CONFIG_FILENAME))) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(ConfigFileConstants.CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(ConfigFileConstants.DEFAULT_FIELD_DELIMITER);
                    String companyName = fields[0];
                    mainApplication.getCompanyList().add(companyName);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Cannot find file", e);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file with such name", e);
            }
        }
    }

    public void readConfigFileToCompanyTableView() {
        if (Files.exists(Paths.get(ConfigFileConstants.CONFIG_FILENAME))) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(ConfigFileConstants.CONFIG_FILENAME));
                String line;
                while ((Objects.nonNull(line = br.readLine()))) {
                    String[] fields = line.split(ConfigFileConstants.DEFAULT_FIELD_DELIMITER);
                    Company company = new Company(fields[0], fields[1]);
                    configurationWindow.getCompanies().add(company);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Cannot find file", e);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file with such name", e);
            }
        }
    }
}
