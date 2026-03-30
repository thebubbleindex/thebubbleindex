package org.thebubbleindex.driver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.thebubbleindex.driver.noGUI;

/**
 * Tests that verify the {@link noGUI.RunType} enumeration values are
 * well-formed and can be looked up by name.
 */
public class noGUIRunTypeTest {

	@Test
	public void runTypeSingleShouldBeResolvable() {
		final noGUI.RunType type = noGUI.RunType.valueOf("Single");
		assertNotNull(type);
		assertEquals(noGUI.RunType.Single, type);
	}

	@Test
	public void runTypeCategoryShouldBeResolvable() {
		final noGUI.RunType type = noGUI.RunType.valueOf("Category");
		assertNotNull(type);
		assertEquals(noGUI.RunType.Category, type);
	}

	@Test
	public void runTypeAllShouldBeResolvable() {
		final noGUI.RunType type = noGUI.RunType.valueOf("All");
		assertNotNull(type);
		assertEquals(noGUI.RunType.All, type);
	}

	@Test
	public void runTypeUpdateShouldBeResolvable() {
		final noGUI.RunType type = noGUI.RunType.valueOf("Update");
		assertNotNull(type);
		assertEquals(noGUI.RunType.Update, type);
	}

	@Test
	public void runTypeShouldHaveFourValues() {
		assertEquals(4, noGUI.RunType.values().length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void valueOfWithUnknownNameShouldThrowIllegalArgumentException() {
		noGUI.RunType.valueOf("Unknown");
	}
}
