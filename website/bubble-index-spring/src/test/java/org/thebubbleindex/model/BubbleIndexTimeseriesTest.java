package org.thebubbleindex.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BubbleIndexTimeseriesTest {

    @Test
    public void testGettersAndSetters() {
        BubbleIndexTimeseries ts = new BubbleIndexTimeseries();
        ts.setId(42L);
        ts.setName("Apple Inc.");
        ts.setSymbol("AAPL");
        ts.setKeywords("AAPL technology");
        ts.setDtype("Stocks");

        assertEquals(42L, ts.getId());
        assertEquals("Apple Inc.", ts.getName());
        assertEquals("AAPL", ts.getSymbol());
        assertEquals("AAPL technology", ts.getKeywords());
        assertEquals("Stocks", ts.getDtype());
    }

    @Test
    public void testFieldsDefaultToNull() {
        BubbleIndexTimeseries ts = new BubbleIndexTimeseries();
        assertNull(ts.getId());
        assertNull(ts.getName());
        assertNull(ts.getSymbol());
        assertNull(ts.getKeywords());
        assertNull(ts.getDtype());
    }

    @Test
    public void testIndicesSubclass() {
        Indices indices = new Indices("S&P 500", "SP500", "Indices", "US market");
        assertEquals("S&P 500", indices.getName());
        assertEquals("SP500", indices.getSymbol());
        assertEquals("Indices", indices.getDtype());
        assertEquals("US market", indices.getKeywords());
    }

    @Test
    public void testStocksSubclass() {
        Stocks stocks = new Stocks("Apple Inc.", "AAPL", "Stocks", "technology apple");
        assertEquals("Apple Inc.", stocks.getName());
        assertEquals("AAPL", stocks.getSymbol());
        assertEquals("Stocks", stocks.getDtype());
        assertEquals("technology apple", stocks.getKeywords());
    }

    @Test
    public void testCurrenciesSubclass() {
        Currencies currencies = new Currencies("Euro/USD", "EURUSD", "Currencies", "euro dollar");
        assertEquals("Euro/USD", currencies.getName());
        assertEquals("EURUSD", currencies.getSymbol());
        assertEquals("Currencies", currencies.getDtype());
    }

    @Test
    public void testCommoditiesSubclass() {
        Commodities commodities = new Commodities("Gold", "GLD", "Commodities", "gold precious metals");
        assertEquals("Gold", commodities.getName());
        assertEquals("GLD", commodities.getSymbol());
        assertEquals("Commodities", commodities.getDtype());
    }
}
