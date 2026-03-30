package org.thebubbleindex.exception.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.thebubbleindex.exception.InvalidData;

/**
 * Tests that verify {@link InvalidData} correctly stores the error message or
 * wraps an underlying cause.
 */
public class InvalidDataTest {

	@Test
	public void stringConstructorShouldPreserveMessage() {
		final InvalidData ex = new InvalidData("no date match found");
		assertEquals("no date match found", ex.getMessage());
	}

	@Test
	public void exceptionConstructorShouldWrapCause() {
		final IllegalArgumentException cause = new IllegalArgumentException("underlying problem");
		final InvalidData ex = new InvalidData(cause);
		assertNotNull(ex.getCause());
		assertSame(cause, ex.getCause());
	}

	@Test
	public void invalidDataIsCheckedExceptionSubtype() {
		final InvalidData ex = new InvalidData("test");
		assertNotNull(ex);
		assertEquals(Exception.class, ex.getClass().getSuperclass());
	}
}
