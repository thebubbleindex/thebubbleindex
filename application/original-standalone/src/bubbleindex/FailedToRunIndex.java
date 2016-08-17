/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bubbleindex;

/**
 *
 * @author green
 */
public class FailedToRunIndex extends RuntimeException {
    
    public FailedToRunIndex(final String error) {
        super(error);
    }
    
    public FailedToRunIndex(final Exception error) {
        super(error);
    }
}
