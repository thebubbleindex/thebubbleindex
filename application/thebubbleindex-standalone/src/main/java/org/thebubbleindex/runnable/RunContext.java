package org.thebubbleindex.runnable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author thebubbleindex
 */
public class RunContext {

	private int threadNumber = 1;
	private boolean isGUI;
	private boolean forceCPU;
	private volatile boolean stop;
	private final AtomicInteger numberOfLines = new AtomicInteger();

	public RunContext() {
	}

	public RunContext(final boolean isGUI, final boolean forceCPU, final int threadNumber) {
		this.threadNumber = threadNumber;
		this.isGUI = isGUI;
		this.forceCPU = forceCPU;
	}

	public boolean isForceCPU() {
		return forceCPU;
	}

	public void setForceCPU(final boolean forceCPU) {
		this.forceCPU = forceCPU;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(final boolean stop) {
		this.stop = stop;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public boolean isGUI() {
		return isGUI;
	}

	public boolean isCPU() {
		return forceCPU;
	}

	public void setThreadNumber(final int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public void setGUI(final boolean isGUI) {
		this.isGUI = isGUI;
	}

	public void setCPU(final boolean forceCPU) {
		this.forceCPU = forceCPU;
	}

	public int getNumberOfLines() {
		return numberOfLines.get();
	}

	public int incrementAndGetNumberOfLines() {
		return numberOfLines.incrementAndGet();
	}

	public void resetNumberOfLines() {
		numberOfLines.set(0);
	}
}
