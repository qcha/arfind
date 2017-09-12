package qcha.arfind.excel.qcha.arfind;

import javafx.collections.ObservableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import qcha.arfind.Sources;
import qcha.arfind.model.Source;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static qcha.arfind.utils.Constants.ConfigFileConstants.CONFIG_FILENAME;

//todo rewrite
@Ignore
public class SavingCacheToFileTest {
    private ObservableMap<String, Source> companiesCache;

    @Before
    public void setUp() throws Exception {
        companiesCache = Sources.getOrCreate();
        companiesCache.put(("Acer"), new Source("Acer", "empty.xls"));
        companiesCache.put(("Asus"), new Source("Asus", "empty.xlsx"));
    }

    @Test
    public void savingDetailsToCacheTest() throws Exception {
        Assert.assertEquals(2, companiesCache.size());
    }

    @Test
    public void savingDetailsFromCacheToFileTest() throws Exception {
//        Sources.saveCacheToFile(companiesCache);
        Assert.assertTrue(new File(CONFIG_FILENAME).exists());
        Assert.assertEquals(2, Files.readAllLines(Paths.get(CONFIG_FILENAME)).size());
    }
}
