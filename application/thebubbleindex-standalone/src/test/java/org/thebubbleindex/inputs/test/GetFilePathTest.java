package org.thebubbleindex.inputs.test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.thebubbleindex.inputs.Indices;

/**
 * Tests that verify {@link Indices#getFilePath()} returns a non-empty path
 * string that points to the directory containing the application JAR.
 */
public class GetFilePathTest {
	@Test
	public void shouldGetPathOfJar() throws UnsupportedEncodingException {
		final Indices indices = new Indices();
		final String path = indices.getFilePath();
		assertTrue(path.length() > 0);
	}
}
