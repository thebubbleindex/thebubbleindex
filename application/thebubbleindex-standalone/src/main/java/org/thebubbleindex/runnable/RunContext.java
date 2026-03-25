package org.thebubbleindex.runnable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * RunContext holds the runtime configuration and shared state for a single
 * execution of The Bubble Index. It controls whether the application is running
 * in GUI mode, whether execution should be limited to CPU only, the number of
 * worker threads to use, and whether a stop signal has been issued.
 *
 * @author thebubbleindex
 */
public class RunContext {

	private int threadNumber = 1;
	private boolean isGUI;
	private boolean forceCPU;
	private volatile boolean stop;
	private final AtomicInteger numberOfLines = new AtomicInteger();

	/**
	 * RunContext default constructor. Creates a context with one thread, GUI
	 * mode disabled, and CPU-forcing disabled.
	 */
	public RunContext() {
	}

	/**
	 * RunContext constructor with explicit configuration values.
	 *
	 * @param isGUI        {@code true} if the application is running in GUI
	 *                     mode; {@code false} for headless / command-line mode
	 * @param forceCPU     {@code true} to skip GPU execution and use only CPU
	 *                     threads
	 * @param threadNumber the number of worker threads to use for parallel
	 *                     computation
	 */
	public RunContext(final boolean isGUI, final boolean forceCPU, final int threadNumber) {
		this.threadNumber = threadNumber;
		this.isGUI = isGUI;
		this.forceCPU = forceCPU;
	}

	/**
	 * isForceCPU returns whether GPU execution has been disabled and all
	 * computations should run on the CPU only.
	 *
	 * @return {@code true} if GPU execution is forced off
	 */
	public boolean isForceCPU() {
		return forceCPU;
	}

	/**
	 * setForceCPU enables or disables GPU execution. When set to {@code true},
	 * only CPU threads are used for the numerical calculations.
	 *
	 * @param forceCPU {@code true} to disable GPU execution
	 */
	public void setForceCPU(final boolean forceCPU) {
		this.forceCPU = forceCPU;
	}

	/**
	 * isStop returns whether a stop signal has been issued. Worker threads
	 * check this flag periodically to cancel long-running computations
	 * gracefully.
	 *
	 * @return {@code true} if execution should halt as soon as possible
	 */
	public boolean isStop() {
		return stop;
	}

	/**
	 * setStop issues or clears a stop signal. Setting this to {@code true}
	 * requests that all active worker threads finish their current unit of work
	 * and exit.
	 *
	 * @param stop {@code true} to request that execution stops
	 */
	public void setStop(final boolean stop) {
		this.stop = stop;
	}

	/**
	 * getThreadNumber returns the number of worker threads used for parallel
	 * computation.
	 *
	 * @return the thread pool size
	 */
	public int getThreadNumber() {
		return threadNumber;
	}

	/**
	 * isGUI returns whether the application is running in GUI mode.
	 *
	 * @return {@code true} if a Swing GUI is active
	 */
	public boolean isGUI() {
		return isGUI;
	}

	/**
	 * isCPU returns whether GPU execution has been disabled. This is an alias
	 * for {@link #isForceCPU()}.
	 *
	 * @return {@code true} if GPU execution is forced off
	 */
	public boolean isCPU() {
		return forceCPU;
	}

	/**
	 * setThreadNumber sets the number of worker threads used for parallel
	 * computation.
	 *
	 * @param threadNumber the desired thread pool size
	 */
	public void setThreadNumber(final int threadNumber) {
		this.threadNumber = threadNumber;
	}

	/**
	 * setGUI sets whether the application is running in GUI mode.
	 *
	 * @param isGUI {@code true} if a Swing GUI is active
	 */
	public void setGUI(final boolean isGUI) {
		this.isGUI = isGUI;
	}

	/**
	 * setCPU enables or disables GPU execution. This is an alias for
	 * {@link #setForceCPU(boolean)}.
	 *
	 * @param forceCPU {@code true} to disable GPU execution
	 */
	public void setCPU(final boolean forceCPU) {
		this.forceCPU = forceCPU;
	}

	/**
	 * getNumberOfLines returns the current count of output lines that have been
	 * written to the GUI text area. This is used to determine when the text
	 * area should be cleared to prevent unbounded growth.
	 *
	 * @return the current output line count
	 */
	public int getNumberOfLines() {
		return numberOfLines.get();
	}

	/**
	 * incrementAndGetNumberOfLines atomically increments the output line count
	 * and returns the updated value.
	 *
	 * @return the incremented output line count
	 */
	public int incrementAndGetNumberOfLines() {
		return numberOfLines.incrementAndGet();
	}

	/**
	 * resetNumberOfLines resets the output line counter to zero. Typically
	 * called after the GUI text area has been cleared.
	 */
	public void resetNumberOfLines() {
		numberOfLines.set(0);
	}
}
