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
public class RunContext {
    
    public static int threadNumber;
    public static boolean isGUI;
    public static boolean forceCPU;
    public static volatile boolean Stop;
    
    public RunContext(final boolean isGUI, final boolean forceCPU, final int threadNumber) {
        RunContext.threadNumber = threadNumber;
        RunContext.isGUI = isGUI;
        RunContext.forceCPU = forceCPU;
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
        RunContext.threadNumber = threadNumber;
    }
    
    public void setGUI(final boolean isGUI) {
        RunContext.isGUI = isGUI;
    }
    
    public void setCPU(final boolean forceCPU) {
        RunContext.forceCPU = forceCPU;
    }
}
