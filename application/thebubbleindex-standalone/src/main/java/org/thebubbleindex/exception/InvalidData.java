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
public class InvalidData extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 677356278019390081L;

	public InvalidData(final String error) {
		super(error);
	}

	public InvalidData(final Exception error) {
		super(error);
	}
}
