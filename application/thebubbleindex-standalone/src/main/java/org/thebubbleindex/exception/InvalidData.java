package org.thebubbleindex.exception;

/**
 * InvalidData is a checked exception thrown when the input data for a
 * calculation is missing, malformed, or otherwise does not meet the
 * requirements of the algorithm. For example, it is thrown when a previously
 * saved results file exists but none of its dates can be matched against the
 * current daily price data series.
 *
 * @author thebubbleindex
 */
public class InvalidData extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 677356278019390081L;

	/**
	 * Constructs a new InvalidData exception with the given error message.
	 *
	 * @param error a human-readable description of the data problem
	 */
	public InvalidData(final String error) {
		super(error);
	}

	/**
	 * Constructs a new InvalidData exception that wraps the given cause.
	 *
	 * @param error the underlying exception that triggered this error
	 */
	public InvalidData(final Exception error) {
		super(error);
	}
}
