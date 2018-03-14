package org.thebubbleindex.util.test;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.junit.Test;
import org.thebubbleindex.util.Utilities;

public class ZipUnZipTest {

	@Test
	public void zipUnZipTest() throws IOException, DataFormatException {
		final String testString = "This is a true statement.";
		final byte[] testBytes = testString.getBytes();

		final byte[] compressedBytes = Utilities.zipBytes("testZip", testBytes);

		final byte[] inflatedBytes = Utilities.unZipBytes(compressedBytes);

		System.out.println(new String(inflatedBytes));

		assert new String(inflatedBytes).equals(testString);
	}

}
