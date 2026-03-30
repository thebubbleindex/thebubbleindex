package org.thebubbleindex.runnable.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.thebubbleindex.runnable.RunContext;

/**
 * Tests that verify {@link RunContext} correctly initialises default values,
 * stores and returns configuration via setters and getters, and correctly
 * manages the atomic output-line counter.
 */
public class RunContextTest {

	@Test
	public void defaultConstructorShouldSetExpectedDefaults() {
		final RunContext ctx = new RunContext();
		assertEquals(1, ctx.getThreadNumber());
		assertFalse(ctx.isGUI());
		assertFalse(ctx.isForceCPU());
		assertFalse(ctx.isStop());
		assertEquals(0, ctx.getNumberOfLines());
	}

	@Test
	public void parameterizedConstructorShouldStoreValues() {
		final RunContext ctx = new RunContext(true, true, 8);
		assertEquals(8, ctx.getThreadNumber());
		assertTrue(ctx.isGUI());
		assertTrue(ctx.isForceCPU());
		assertFalse(ctx.isStop());
	}

	@Test
	public void setAndGetThreadNumberShouldWork() {
		final RunContext ctx = new RunContext();
		ctx.setThreadNumber(4);
		assertEquals(4, ctx.getThreadNumber());
	}

	@Test
	public void setAndGetGUIShouldWork() {
		final RunContext ctx = new RunContext();
		ctx.setGUI(true);
		assertTrue(ctx.isGUI());
		ctx.setGUI(false);
		assertFalse(ctx.isGUI());
	}

	@Test
	public void setAndGetForceCPUShouldWork() {
		final RunContext ctx = new RunContext();
		ctx.setForceCPU(true);
		assertTrue(ctx.isForceCPU());
		assertTrue(ctx.isCPU());
		ctx.setForceCPU(false);
		assertFalse(ctx.isForceCPU());
		assertFalse(ctx.isCPU());
	}

	@Test
	public void setCPUShouldBehaveIdenticallyToSetForceCPU() {
		final RunContext ctx = new RunContext();
		ctx.setCPU(true);
		assertTrue(ctx.isForceCPU());
		ctx.setCPU(false);
		assertFalse(ctx.isForceCPU());
	}

	@Test
	public void setAndGetStopShouldWork() {
		final RunContext ctx = new RunContext();
		assertFalse(ctx.isStop());
		ctx.setStop(true);
		assertTrue(ctx.isStop());
		ctx.setStop(false);
		assertFalse(ctx.isStop());
	}

	@Test
	public void incrementAndGetNumberOfLinesShouldReturnIncrementedValue() {
		final RunContext ctx = new RunContext();
		assertEquals(0, ctx.getNumberOfLines());
		assertEquals(1, ctx.incrementAndGetNumberOfLines());
		assertEquals(1, ctx.getNumberOfLines());
		assertEquals(2, ctx.incrementAndGetNumberOfLines());
		assertEquals(2, ctx.getNumberOfLines());
	}

	@Test
	public void resetNumberOfLinesShouldResetCounterToZero() {
		final RunContext ctx = new RunContext();
		ctx.incrementAndGetNumberOfLines();
		ctx.incrementAndGetNumberOfLines();
		ctx.incrementAndGetNumberOfLines();
		assertEquals(3, ctx.getNumberOfLines());
		ctx.resetNumberOfLines();
		assertEquals(0, ctx.getNumberOfLines());
	}
}
