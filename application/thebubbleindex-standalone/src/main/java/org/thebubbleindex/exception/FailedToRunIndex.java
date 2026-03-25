package org.thebubbleindex.exception;

/**
 * FailedToRunIndex is an unchecked exception thrown when The Bubble Index
 * calculation cannot be executed. Common causes include a time window that is
 * larger than the available data set, GPU initialisation failures, or
 * unrecoverable errors during numerical computation.
 *
 * @author thebubbleindex
 */
public class FailedToRunIndex extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 3927464519188070677L;

	/**
	 * Constructs a new FailedToRunIndex exception with the given error message.
	 *
	 * @param error a human-readable description of the failure
	 */
	public FailedToRunIndex(final String error) {
		super(error);
	}

	/**
	 * Constructs a new FailedToRunIndex exception that wraps the given cause.
	 *
	 * @param error the underlying exception that caused this failure
	 */
	public FailedToRunIndex(final Exception error) {
		super(error);
	}
}
