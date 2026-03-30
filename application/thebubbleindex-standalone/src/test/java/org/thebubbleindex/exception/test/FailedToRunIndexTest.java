package org.thebubbleindex.exception.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.thebubbleindex.exception.FailedToRunIndex;

/**
 * Tests that verify {@link FailedToRunIndex} correctly stores the error
 * message or wraps an underlying cause.
 */
public class FailedToRunIndexTest {

	@Test
	public void stringConstructorShouldPreserveMessage() {
		final FailedToRunIndex ex = new FailedToRunIndex("window too large");
		assertEquals("window too large", ex.getMessage());
	}

	@Test
	public void exceptionConstructorShouldWrapCause() {
		final IllegalStateException cause = new IllegalStateException("underlying failure");
		final FailedToRunIndex ex = new FailedToRunIndex(cause);
		assertNotNull(ex.getCause());
		assertSame(cause, ex.getCause());
	}

	@Test
	public void exceptionIsInstanceOfRuntimeException() {
		final FailedToRunIndex ex = new FailedToRunIndex("test");
		assertNotNull(ex);
		assertEquals(RuntimeException.class, ex.getClass().getSuperclass());
	}
}
