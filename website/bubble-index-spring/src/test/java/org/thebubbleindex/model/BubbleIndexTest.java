package org.thebubbleindex.model;

import org.junit.jupiter.api.Test;
import org.thebubbleindex.model.bubbleindex.BubbleIndex;

import static org.junit.jupiter.api.Assertions.*;

public class BubbleIndexTest {

    @Test
    public void testDefaultConstructorInitializesFieldsToNull() {
        BubbleIndex bi = new BubbleIndex();
        assertNull(bi.dtype);
        assertNull(bi.name);
        assertNull(bi.symbol);
        assertNull(bi.pdfLocation);
        assertNull(bi.plotLocation);
        assertNull(bi.contourPDFLocation);
        assertNull(bi.contourPNGLocation);
        assertNull(bi.threejsLocation);
    }

    @Test
    public void testFindLocation() {
        BubbleIndex bi = new BubbleIndex();
        bi.dtype = "Stocks";
        bi.symbol = "AAPL";

        bi.findLocation();

        assertEquals(
                "https://cdn.thebubbleindex.com/TheBubbleIndex/Stocks/AAPL/AAPL.html",
                bi.threejsLocation);
        assertEquals(
                "https://cdn.thebubbleindex.com/TheBubbleIndex/Stocks/AAPL/AAPL.pdf",
                bi.pdfLocation);
        assertEquals(
                "//cdn.thebubbleindex.com/Stocks/AAPL/AAPLContour.pdf",
                bi.contourPDFLocation);
        assertEquals(
                "//cdn.thebubbleindex.com/Stocks/AAPL/AAPLContour.png",
                bi.contourPNGLocation);
    }

    @Test
    public void testFindPlotLocation() {
        BubbleIndex bi = new BubbleIndex();
        bi.dtype = "Indices";
        bi.symbol = "SP500";
        bi.name = "S&P 500";

        bi.findPlotLocation();

        assertEquals("/plot?type=Indices&symbol=SP500&name=S&P 500", bi.plotLocation);
    }

    @Test
    public void testFindLocationWithDifferentValues() {
        BubbleIndex bi = new BubbleIndex();
        bi.dtype = "Currencies";
        bi.symbol = "EURUSD";

        bi.findLocation();

        assertTrue(bi.threejsLocation.contains("Currencies"));
        assertTrue(bi.threejsLocation.contains("EURUSD"));
        assertTrue(bi.pdfLocation.contains("EURUSD.pdf"));
        assertTrue(bi.contourPDFLocation.contains("EURUSDContour.pdf"));
        assertTrue(bi.contourPNGLocation.contains("EURUSDContour.png"));
    }

    @Test
    public void testFindPlotLocationWithDifferentValues() {
        BubbleIndex bi = new BubbleIndex();
        bi.dtype = "Commodities";
        bi.symbol = "GOLD";
        bi.name = "Gold";

        bi.findPlotLocation();

        assertTrue(bi.plotLocation.contains("/plot?type=Commodities"));
        assertTrue(bi.plotLocation.contains("symbol=GOLD"));
        assertTrue(bi.plotLocation.contains("name=Gold"));
    }
}
