package org.thebubbleindex.exception;

/**
 *
 * @author bigttrott
 */
public class FailedToRunIndex extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3927464519188070677L;

	public FailedToRunIndex(final String error) {
		super(error);
	}

	public FailedToRunIndex(final Exception error) {
		super(error);
	}
}
