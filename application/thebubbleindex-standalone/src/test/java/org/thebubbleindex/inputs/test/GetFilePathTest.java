package org.thebubbleindex.inputs.test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.thebubbleindex.inputs.Indices;

public class GetFilePathTest {
	@Test
	public void shouldGetPathOfJar() throws UnsupportedEncodingException {
		final String path = Indices.getFilePath();
		assertTrue(path.length() > 0);
	}
}
