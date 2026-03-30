package org.thebubbleindex.website;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.thebubbleindex.model.BubbleIndexTimeseries;
import org.thebubbleindex.repository.MainRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MainRepository mainRepository;

    @BeforeEach
    public void setUpSearchData() {
        // Insert test data for search endpoint tests
        saveEntry("S&P 500", "GSPC", "Indices", "SANDP SPX SP500 AMERICA UNITED STATES");
        saveEntry("Dow Jones", "DJIA", "Indices", "DOW JONES AMERICA UNITED STATES");
        saveEntry("Wilshire 5000", "WILL5000IND", "Indices", "WILSHIRE 5000 USA UNITED STATES AMERICAN YORK");
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
    public void testRootReturnsIndexView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/index"));
    }

    @Test
    public void testIndexReturnsIndexView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/index"));
    }

    @Test
    public void testPagesMethod() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/pages/method"))
                .andExpect(status().isOk())
                .andExpect(view().name("Pages/about"));
    }

    @Test
    public void testPagesLinks() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/pages/links"))
                .andExpect(status().isOk())
                .andExpect(view().name("Pages/links"));
    }

    @Test
    public void testPagesExamples() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/pages/examples"))
                .andExpect(status().isOk())
                .andExpect(view().name("Pages/examples"));
    }

    @Test
    public void testPagesContact() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/pages/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("Pages/contact"));
    }

    @Test
    public void testPagesSearch() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/pages/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("Pages/search"));
    }

    @Test
    public void testPagesLegal() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/pages/legal"))
                .andExpect(status().isOk())
                .andExpect(view().name("Pages/legal"));
    }

    @Test
    public void testBrowse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browse"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browse"));
    }

    @Test
    public void testBrowseEurope() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browseEurope"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browseEurope"));
    }

    @Test
    public void testBrowseMoreEurope() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browseMoreEurope"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browseMoreEurope"));
    }

    @Test
    public void testBrowseAmericas() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browseAmericas"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browseAmericas"));
    }

    @Test
    public void testBrowseAsia() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browseAsia"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browseAsia"));
    }

    @Test
    public void testBrowsePacific() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browsePacific"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browsePacific"));
    }

    @Test
    public void testBrowseComposite() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browseComposite"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browseComposite"));
    }

    @Test
    public void testBrowselistByType() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browselist/Stocks"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browselist"));
    }

    @Test
    public void testBrowselistByTypeEmpty() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/browselist/UnknownType"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browselist"));
    }

    @Test
    public void testNoResults() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/noResults"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/noResults"));
    }

    @Test
    public void testSearchResultsNoMatch() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/searchResults")
                .param("search", "NOSUCHSYMBOLXYZ"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/noResults"));
    }

    @Test
    public void testSearchResultsWithMultipleMatches() throws Exception {
        // "AMERICA" appears in multiple Indices keywords inserted in setUp
        mvc.perform(MockMvcRequestBuilders.get("/searchResults")
                .param("search", "AMERICA"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/searchResults"));
    }

    @Test
    public void testPlotEndpointRedirects() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/plot")
                .param("type", "Stocks")
                .param("symbol", "AAPL")
                .param("name", "Apple Inc."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("https://cdn.thebubbleindex.com/**"));
    }

    @Test
    public void testApplicationBrowseRedirect() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/application/browse"))
                .andExpect(status().isOk())
                .andExpect(view().name("Application/browse"));
    }
}
