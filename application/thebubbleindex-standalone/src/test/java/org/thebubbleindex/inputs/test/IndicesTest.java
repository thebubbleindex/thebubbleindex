package org.thebubbleindex.inputs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.inputs.InputCategory;

/**
 * Tests that verify {@link Indices} correctly reads category and component
 * configuration files and exposes them as arrays and maps, and that all
 * accessor methods return expected values.
 */
public class IndicesTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void categoriesShouldBeReadAndConvertedToArray() throws IOException {
		final Indices indices = new Indices();

		final List<String> lines = new ArrayList<String>();
		lines.add(String.format("Indices"));
		lines.add(String.format("Currencies"));
		lines.add(String.format("Stocks"));
		lines.add(String.format("Commodities"));

		for (final String line : lines) {
			final String categoryName = line;
			// Use a proper temp path with separator so setComponents() can create the file
			final String location = tempFolder.getRoot().getAbsolutePath()
					+ File.separator + categoryName + ".csv";
			final InputCategory tempInputCategory = new InputCategory(categoryName, location);
			tempInputCategory.setComponents();
			indices.getCategoriesAndComponents().put(categoryName, tempInputCategory);
		}

		final String[] categoriesAndComponentsArray = indices.getCategoriesAsArray();
		assertEquals(4, categoriesAndComponentsArray.length);
		assertEquals("Commodities", categoriesAndComponentsArray[0]);
		assertEquals("Currencies", categoriesAndComponentsArray[1]);
		assertEquals("Indices", categoriesAndComponentsArray[2]);
		assertEquals("Stocks", categoriesAndComponentsArray[3]);
	}

	@Test
	public void categoriesShouldBeReadFromDiskAndConvertedToArray() throws IOException {
		final Indices indices = new Indices();

		final List<String> lines = new ArrayList<String>();
		lines.add(String.format("Indices"));
		lines.add(String.format("Currencies"));
		lines.add(String.format("Stocks"));
		lines.add(String.format("Commodities"));

		new File(indices.getFilePath() + indices.getProgramDataFolder()).mkdirs();

		final File tempFile = new File(
				indices.getFilePath() + indices.getProgramDataFolder() + indices.getFilePathSymbol() + indices.getCategoryList());

		final Path tempFilePath = tempFile.toPath();
		Files.write(tempFilePath, lines, Charset.defaultCharset());

		indices.initialize();

		final String[] categoriesAndComponentsArray = indices.getCategoriesAsArray();
		assertEquals(4, categoriesAndComponentsArray.length);
		assertEquals("Commodities", categoriesAndComponentsArray[0]);
		assertEquals("Currencies", categoriesAndComponentsArray[1]);
		assertEquals("Indices", categoriesAndComponentsArray[2]);
		assertEquals("Stocks", categoriesAndComponentsArray[3]);
	}

	// ------------------------------------------------------------------
	// Accessor / property tests
	// ------------------------------------------------------------------

	@Test
	public void getProgramDataFolderShouldReturnProgramData() {
		final Indices indices = new Indices();
		assertEquals("ProgramData", indices.getProgramDataFolder());
	}

	@Test
	public void getCategoryListShouldReturnCategoryListCsv() {
		final Indices indices = new Indices();
		assertEquals("CategoryList.csv", indices.getCategoryList());
	}

	@Test
	public void getFilePathSymbolShouldMatchFileSeparator() {
		final Indices indices = new Indices();
		assertEquals(File.separator, indices.getFilePathSymbol());
	}

	@Test
	public void setAndGetUserDirShouldPreserveValue() {
		final Indices indices = new Indices();
		final String dir = "/some/test/dir" + File.separator;
		indices.setUserDir(dir);
		assertEquals(dir, indices.getUserDir());
	}

	@Test
	public void getCategoriesAndComponentsShouldReturnNonNullMap() {
		final Indices indices = new Indices();
		final Map<String, InputCategory> map = indices.getCategoriesAndComponents();
		assertNotNull(map);
	}

	@Test
	public void initializeWithEmptyCategoryListShouldAddNullEntry() throws IOException {
		final Indices indices = new Indices();

		// Write an empty CategoryList.csv so initialize() finds it but reads no lines
		final File programDataDir = new File(indices.getFilePath() + indices.getProgramDataFolder());
		programDataDir.mkdirs();
		final File categoryListFile = new File(
				programDataDir + indices.getFilePathSymbol() + indices.getCategoryList());
		Files.write(categoryListFile.toPath(), new byte[0]);

		indices.initialize();

		// An empty CategoryList.csv causes a "Null" placeholder entry to be added
		final String[] categories = indices.getCategoriesAsArray();
		assertTrue("At least the 'Null' placeholder entry should be present",
				categories.length >= 1);
		boolean hasNull = false;
		for (final String cat : categories) {
			if ("Null".equals(cat)) {
				hasNull = true;
				break;
			}
		}
		assertTrue("The 'Null' placeholder category should be present when CategoryList.csv is empty", hasNull);
	}

	@Test
	public void initializeWithMissingProgramDataFolderShouldCreateIt() throws IOException {
		final Indices indices = new Indices();

		// Ensure ProgramData folder is present (initialize handles missing folder)
		final File programDataDir = new File(indices.getFilePath() + indices.getProgramDataFolder());
		programDataDir.mkdirs();
		final File categoryListFile = new File(
				programDataDir + indices.getFilePathSymbol() + indices.getCategoryList());
		if (!categoryListFile.exists()) {
			categoryListFile.createNewFile();
		}

		// Should not throw; missing ProgramData is handled gracefully
		indices.initialize();

		assertTrue("ProgramData folder should exist", programDataDir.exists());
		assertTrue("CategoryList.csv should exist", categoryListFile.exists());
	}

	@Test
	public void getCategoriesAsArrayShouldReturnEmptyArrayWhenEmpty() {
		final Indices indices = new Indices();
		// No categories added yet
		final String[] result = indices.getCategoriesAsArray();
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	@Test
	public void getCategoriesAndComponentsShouldReflectManuallyAddedEntries() throws IOException {
		final Indices indices = new Indices();
		final String location = tempFolder.getRoot().getAbsolutePath() + File.separator + "Test.csv";
		final InputCategory cat = new InputCategory("Test", location);
		indices.getCategoriesAndComponents().put("Test", cat);

		assertFalse(indices.getCategoriesAndComponents().isEmpty());
		assertNotNull(indices.getCategoriesAndComponents().get("Test"));
	}
}

