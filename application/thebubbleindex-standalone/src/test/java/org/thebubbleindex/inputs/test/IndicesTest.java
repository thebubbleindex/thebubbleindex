package org.thebubbleindex.inputs.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.inputs.InputCategory;

public class IndicesTest {

	@Test
	public void categoriesShouldBeReadAndConvertedToArray() throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(String.format("Indices"));
		lines.add(String.format("Currencies"));
		lines.add(String.format("Stocks"));
		lines.add(String.format("Commodities"));

		for (final String line : lines) {
			final String categoryName = line;
			final InputCategory tempInputCategory = new InputCategory(categoryName,
					System.getProperty("java.io.tmpdir") + categoryName + ".csv");
			tempInputCategory.setComponents();
			Indices.categoriesAndComponents.put(categoryName, tempInputCategory);
		}

		final String[] categoriesAndComponentsArray = Indices.getCategoriesAsArray();
		assertEquals(4, categoriesAndComponentsArray.length);
		assertEquals("Commodities", categoriesAndComponentsArray[0]);
		assertEquals("Currencies", categoriesAndComponentsArray[1]);
		assertEquals("Indices", categoriesAndComponentsArray[2]);
		assertEquals("Stocks", categoriesAndComponentsArray[3]);
	}

	@Test
	public void categoriesShouldBeReadFromDiskAndConvertedToArray() throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(String.format("Indices"));
		lines.add(String.format("Currencies"));
		lines.add(String.format("Stocks"));
		lines.add(String.format("Commodities"));

		new File(Indices.getFilePath() + Indices.programDataFolder).mkdirs();

		final File tempFile = new File(
				Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol + Indices.categoryList);

		final Path tempFilePath = tempFile.toPath();
		Files.write(tempFilePath, lines, Charset.defaultCharset());

		Indices.initialize();

		final String[] categoriesAndComponentsArray = Indices.getCategoriesAsArray();
		assertEquals(4, categoriesAndComponentsArray.length);
		assertEquals("Commodities", categoriesAndComponentsArray[0]);
		assertEquals("Currencies", categoriesAndComponentsArray[1]);
		assertEquals("Indices", categoriesAndComponentsArray[2]);
		assertEquals("Stocks", categoriesAndComponentsArray[3]);
	}
}
