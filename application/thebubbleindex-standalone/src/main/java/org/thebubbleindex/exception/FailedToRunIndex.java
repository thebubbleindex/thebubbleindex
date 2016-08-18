/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thebubbleindex.exception;

/**
 *
 * @author green
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
