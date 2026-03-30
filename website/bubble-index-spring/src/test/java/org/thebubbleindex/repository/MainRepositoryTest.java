package org.thebubbleindex.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thebubbleindex.model.BubbleIndexTimeseries;
import org.thebubbleindex.website.BubbleIndexSpringApplication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BubbleIndexSpringApplication.class)
public class MainRepositoryTest {

    @Autowired
    private MainRepository mainRepository;

    @BeforeEach
    public void setUp() {
        saveEntry("Apple Inc.", "AAPL", "Stocks", "AAPL technology apple");
        saveEntry("Microsoft", "MSFT", "Stocks", "MSFT technology microsoft");
        saveEntry("S&P 500", "GSPC", "Indices", "SANDP SPX SP500 AMERICA UNITED STATES");
        saveEntry("Dow Jones", "DJIA", "Indices", "DOW JONES AMERICA UNITED STATES");
        saveEntry("EUR/USD", "EURUSD", "Currencies", "euro dollar EURUSD forex");
        saveEntry("Gold", "GLD", "Commodities", "gold precious metals GLD");
        saveEntry("0005.HK", "0005.HK", "HongKong", "0005 0005.HK hongkong");
        saveEntry("Ping An", "601318.SS", "China", "601318 601318.SS 601318.SZ china");
        saveEntry("S&P 500 Composite 50", "SP500", "CompositeFifty", "SP500 AMERICA COMPOSITE FIFTY");
    }

    private void saveEntry(String name, String symbol, String dtype, String keywords) {
        BubbleIndexTimeseries ts = new BubbleIndexTimeseries();
        ts.setName(name);
        ts.setSymbol(symbol);
        ts.setDtype(dtype);
        ts.setKeywords(keywords);
        mainRepository.save(ts);
    }

    @Test
    public void testFindByType_returnsResultsForKnownType() {
        List<BubbleIndexTimeseries> results = mainRepository.findByType("Stocks");
        assertNotNull(results);
        assertFalse(results.isEmpty());
        results.forEach(r -> assertEquals("Stocks", r.getDtype()));
    }

    @Test
    public void testFindByType_returnsEmptyListForUnknownType() {
        List<BubbleIndexTimeseries> results = mainRepository.findByType("UnknownType");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindBySymbol_matchesSuffix() {
        List<BubbleIndexTimeseries> results = mainRepository.findBySymbol("Stocks", "AAPL");
        assertNotNull(results);
        assertFalse(results.isEmpty());
        results.forEach(r -> assertEquals("Stocks", r.getDtype()));
    }

    @Test
    public void testFindBySymbol_returnsEmptyForNoMatch() {
        List<BubbleIndexTimeseries> results = mainRepository.findBySymbol("Stocks", "NOSUCHSYMBOLXYZ");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindByName_returnsResults() {
        // The name must end with the search term due to the `like %?2` query pattern
        List<BubbleIndexTimeseries> results = mainRepository.findByName("Stocks", "Microsoft");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testFindByKeywords_matchesPartialKeyword() {
        List<BubbleIndexTimeseries> results = mainRepository.findByKeywords("Indices", "SP500");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testFindByKeywords_returnsEmptyForNoMatch() {
        List<BubbleIndexTimeseries> results = mainRepository.findByKeywords("Indices", "NOSUCHKEYWORDXYZ");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindByDualKeywords_matchesFirstKeyword() {
        List<BubbleIndexTimeseries> results = mainRepository.findByDualKeywords("HongKong", "0005", "0005.HK");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testFindByDualKeywords_returnsEmptyForNoMatch() {
        List<BubbleIndexTimeseries> results = mainRepository.findByDualKeywords("HongKong", "NOSUCH1", "NOSUCH2");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindByTriKeywords_returnsResults() {
        List<BubbleIndexTimeseries> results = mainRepository.findByTriKeywords("China", "601318", "601318.SZ", "601318.SS");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testFindByTriKeywords_returnsEmptyForNoMatch() {
        List<BubbleIndexTimeseries> results = mainRepository.findByTriKeywords("China", "NOSUCH1", "NOSUCH2", "NOSUCH3");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSaveAndRetrieve() {
        BubbleIndexTimeseries ts = new BubbleIndexTimeseries();
        ts.setName("Test Name");
        ts.setSymbol("TESTUNIQ");
        ts.setDtype("Stocks");
        ts.setKeywords("TESTUNIQ test keyword");

        BubbleIndexTimeseries saved = mainRepository.save(ts);
        assertNotNull(saved.getId());

        List<BubbleIndexTimeseries> found = mainRepository.findBySymbol("Stocks", "TESTUNIQ");
        assertFalse(found.isEmpty());
        assertEquals("Test Name", found.get(0).getName());
    }
}
