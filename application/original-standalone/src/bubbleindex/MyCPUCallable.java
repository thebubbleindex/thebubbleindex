package bubbleindex;

import java.util.concurrent.Callable;
import org.apache.commons.math3.util.FastMath;

/**
 * MyCPUCallable is the actual numerical calculation of The Bubble Index.
 * The value returned from the Run method of this class is the value of
 * The Bubble Index for a specific date.
 * 
 * @author thebubbleindex
 */
public class MyCPUCallable implements Callable<Float> {
     
    private final int numberOfDays;
    private final int index;
    private final LombScargle lombScargle;
    private final double tCritDouble;
    private final double mCoeffDouble;
    private double omegaDouble;
    private final double[] dailyPriceValues;
    private final String displayPeriodString;
    private final String selectionName;
    private final BubbleIndexWorker bubbleIndexWorker;
    
    /**
     * MyCPUCallable constructor
     * 
     * @param bubbleIndexWorker
     * @param index
     * @param numberOfDays
     * @param lombScargle
     * @param tCritDouble
     * @param mCoeffDouble
     * @param dailyPriceValues
     * @param displayPeriodString
     * @param selectionName 
     */
    public MyCPUCallable (final BubbleIndexWorker bubbleIndexWorker, final int index, final int numberOfDays, final LombScargle lombScargle,
            final double tCritDouble, final double mCoeffDouble, final double[] dailyPriceValues, 
            final String displayPeriodString, final String selectionName) {
        this.numberOfDays = numberOfDays;
        this.index = index;
        this.lombScargle = lombScargle;
        this.tCritDouble = tCritDouble;
        this.mCoeffDouble = mCoeffDouble;
        this.dailyPriceValues = dailyPriceValues;
        this.displayPeriodString = displayPeriodString;
        this.selectionName = selectionName;
        this.bubbleIndexWorker = bubbleIndexWorker;
    }
    
    /**
     * call method to execute the calculation.
     * 
     * @return the value of The Bubble Index at a specific date
     */
    @Override
    public Float call() {

        if (!RunContext.Stop) {
            final double[] TimeValues = new double[numberOfDays];
            final double[] TimeValues_M_Power = new double[numberOfDays];
            final double[] LogCosTimeValues = new double[numberOfDays];
            final double[] SelectedData = new double[numberOfDays];
            final double[] Coef = new double[3];

            for (int k = 0; k < numberOfDays; k++) {
                TimeValues[k] = numberOfDays + tCritDouble - k;
                TimeValues_M_Power[k] = FastMath.pow(TimeValues[k], 
                        mCoeffDouble);
                LogCosTimeValues[k] = FastMath.cos(omegaDouble * 
                    FastMath.log(TimeValues[k])) * TimeValues_M_Power[k];
                    SelectedData[k] = dailyPriceValues[k + index + 1];        
            }

            //Normalize data to a price starting at 100
            Utilities.Normalize(SelectedData, numberOfDays);

            Utilities.DataReverse(SelectedData, numberOfDays);

            Utilities.LinearFit(SelectedData, TimeValues_M_Power, LogCosTimeValues,
                        Coef, numberOfDays);

            final float Temp = lombScargle.hqDerivative(TimeValues, 
                    Coef, numberOfDays);
            
            final float output = Temp;
            
            if (RunContext.isGUI) {
                bubbleIndexWorker.publishText("Name: " + selectionName + " Date: " + displayPeriodString +
                            " Value: " + output + " Length: " + numberOfDays);
            }
            else {
                System.out.println("Name : " + selectionName + " Date: " + displayPeriodString + 
                        " Value: " + output + " Length: " + numberOfDays);
            }
            return Temp;
        }
                
        else {            
            return 0.0f;      
        }
    }          
}