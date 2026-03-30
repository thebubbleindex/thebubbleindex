package org.thebubbleindex.inputs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.inputs.InputCategory;

/**
 * Tests that verify {@link InputCategory} correctly constructs itself, stores
 * names and locations via setters, and reads component lists from disk.
 */
public class InputCategoryTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void nameLocationConstructorShouldStoreValues() {
		final InputCategory cat = new InputCategory("Stocks", "/some/path/Stocks.csv");
		assertEquals("Stocks", cat.getName());
		assertEquals("/some/path/Stocks.csv", cat.getLocation());
	}

	@Test
	public void defaultComponentsListShouldBeEmpty() {
		final InputCategory cat = new InputCategory("Currencies", "/path/Currencies.csv");
		assertNotNull(cat.getComponents());
		assertTrue(cat.getComponents().isEmpty());
	}

	@Test
	public void setNameShouldUpdateName() {
		final InputCategory cat = new InputCategory("Stocks", "/path/Stocks.csv");
		cat.setName("Indices");
		assertEquals("Indices", cat.getName());
	}

	@Test
	public void setLocationShouldUpdateLocation() {
		final InputCategory cat = new InputCategory("Stocks", "/old/Stocks.csv");
		cat.setLocation("/new/Stocks.csv");
		assertEquals("/new/Stocks.csv", cat.getLocation());
	}

	@Test
	public void setComponentsShouldReadLinesFromFile() throws IOException {
		final File csv = tempFolder.newFile("Stocks.csv");
		Files.write(csv.toPath(), "AAPL\nGOOGL\nMSFT\n".getBytes(StandardCharsets.UTF_8));

		final InputCategory cat = new InputCategory("Stocks", csv.getAbsolutePath());
		cat.setComponents();

		assertEquals(3, cat.getComponents().size());
		assertEquals("AAPL", cat.getComponents().get(0));
		assertEquals("GOOGL", cat.getComponents().get(1));
		assertEquals("MSFT", cat.getComponents().get(2));
	}

	@Test
	public void getComponentsAsArrayShouldReturnCorrectArray() throws IOException {
		final File csv = tempFolder.newFile("Currencies.csv");
		Files.write(csv.toPath(), "EURUSD\nGBPUSD\n".getBytes(StandardCharsets.UTF_8));

		final InputCategory cat = new InputCategory("Currencies", csv.getAbsolutePath());
		cat.setComponents();

		final String[] arr = cat.getComponentsAsArray();
		assertEquals(2, arr.length);
		assertEquals("EURUSD", arr[0]);
		assertEquals("GBPUSD", arr[1]);
	}

	@Test
	public void setComponentsWithMissingFileShouldCreateFile() throws IOException {
		final File csv = new File(tempFolder.getRoot(), "NewCategory.csv");

		final InputCategory cat = new InputCategory("NewCategory", csv.getAbsolutePath());
		cat.setComponents();

		// File should have been created even if it did not exist before
		assertTrue(csv.exists());
		assertTrue(cat.getComponents().isEmpty());
	}

	@Test
	public void fileEndingConstantShouldBeDotCsv() {
		assertEquals(".csv", InputCategory.fileEnding);
	}
}
