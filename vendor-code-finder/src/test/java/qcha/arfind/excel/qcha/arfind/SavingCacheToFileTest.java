package qcha.arfind.excel.qcha.arfind;

import javafx.collections.ObservableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import qcha.arfind.SearchModelCache;
import qcha.arfind.model.SearchDetails;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static qcha.arfind.utils.Constants.ConfigFileConstants.CONFIG_FILENAME;

public class SavingCacheToFileTest {
    private ObservableMap<String, SearchDetails> companiesCache;

    @Before
    public void setUp() throws Exception {
        companiesCache = SearchModelCache.getOrCreateCache();
        companiesCache.put(("Acer"), new SearchDetails("Acer", "empty.xls"));
        companiesCache.put(("Asus"), new SearchDetails("Asus", "empty.xlsx"));
    }

    @Test
    public void savingDetailsToCacheTest() throws Exception {
        Assert.assertEquals(2, companiesCache.size());
    }

    @Test
    public void savingDetailsFromCacheToFileTest() throws Exception {
        SearchModelCache.saveCacheToFile(companiesCache);
        Assert.assertTrue(new File(CONFIG_FILENAME).exists());
        Assert.assertEquals(2, Files.readAllLines(Paths.get(CONFIG_FILENAME)).size());
    }
}
