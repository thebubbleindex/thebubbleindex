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
public class InvalidData extends Exception {
    
    public InvalidData(final String error) {
        super(error);
    }
    
    public InvalidData(final Exception error) {
        super(error);
    }
}
