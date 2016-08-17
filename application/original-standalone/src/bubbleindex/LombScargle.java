package bubbleindex;

import static info.yeppp.Core.Add_V64fS64f_V64f;
import static info.yeppp.Core.Add_V64fV64f_V64f;
import static info.yeppp.Core.Multiply_V64fS64f_V64f;
import static info.yeppp.Core.Multiply_V64fV64f_V64f;
import static info.yeppp.Math.Cos_V64f_V64f;
import static info.yeppp.Math.Log_V64f_V64f;
import static info.yeppp.Math.Sin_V64f_V64f;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.math3.util.FastMath;

/**
 * LombScargle class provides the values and calculations which
 * are needed to find the strength of the H,Q derivative based on
 * an a desired frequency.
 * <p>
 * For more information see:
 * <a href="http://en.wikipedia.org/wiki/Least-squares_spectral_analysis">Least Squares spectral analysis</a>
 * 
 * @author ttrott
 */
public class LombScargle {
      
    public int freqSize;
    public int qSize;
    public int hSize;
    public final double[] testFrequencies;
    public final double[] Q;
    public final double[] H;
    
    public final double[] logQi;
    public final double[] QiM;
    public final double[] cOne;
    public final double[] cTwo;
    public final double[][] powTempVar;
    
    public final float omegaFloat;
    public final double omegaDouble;
    public final float mCoeffFloat;
    public final double mCoeffDouble;
    
    /**
     * LombScargle constructor
     * 
     * @param freqSize
     * @param qSize
     * @param hSize
     * @param omegaDouble
     * @param mCoeffDouble 
     */
    public LombScargle(final int freqSize, final int qSize, final int hSize, final double omegaDouble,
            final double mCoeffDouble) {

        this.freqSize = freqSize;
        this.qSize = qSize;
        this.hSize = hSize;
        this.omegaDouble = omegaDouble;
        this.omegaFloat = (float) omegaDouble;
        this.mCoeffDouble = mCoeffDouble;
        this.mCoeffFloat = (float) mCoeffDouble;
        
        try {
            Logs.myLogger.info("Reading lombscargle.properties file.");
            final Properties lsProperties = new Properties();
        
            final InputStream input = new FileInputStream(Indices.getFilePath() + 
                    System.getProperty("file.separator") + "ProgramData" + 
                    System.getProperty("file.separator") + "lombscargle.properties");
            
            lsProperties.load(input);

            this.freqSize = Integer.parseInt(lsProperties.getProperty("freqsize").trim());
            this.qSize = Integer.parseInt(lsProperties.getProperty("qsize").trim());
            this.hSize = Integer.parseInt(lsProperties.getProperty("hsize").trim());
            
        } catch (final FileNotFoundException ex) {
            Logs.myLogger.error("Could not find lombscargle.properties file. {}", ex);
        } catch (final IOException ex) {
            Logs.myLogger.error("Error while reading lombscargle.properties file. {}", ex);
        }         
        
        testFrequencies = new double[this.freqSize];
        Q = new double [this.qSize];
        H = new double [this.hSize];
    
        logQi = new double[this.qSize];
        QiM = new double[this.qSize];
        cOne = new double[this.qSize];
        cTwo = new double[this.qSize];
        powTempVar = new double[this.qSize][this.hSize];
        
        double StartingPoint = omegaDouble / (2 * 3.14159) - 0.2;
        double Increments = 0.01;
        
        for (int i = 0; i < freqSize; i++) {
            testFrequencies[i] = StartingPoint + i * Increments;
        }
        
        StartingPoint = 0.1;
        Increments = 0.05;
        for (int i = 0; i < qSize; i++) {
            Q[i] = StartingPoint + i * Increments;
        }
        
        StartingPoint = -1.0 * 0.9;
        Increments = 0.1;
        for (int i = 0; i < hSize; i++) {
            H[i] = StartingPoint + i * Increments;
        }
        
        for (int i = 0; i < qSize; i++) {        
            logQi[i] = FastMath.log(Q[i]) * omegaDouble;
            QiM[i] = FastMath.pow(Q[i], mCoeffDouble);
            cOne[i] = 1.0 - QiM[i] * FastMath.cos(logQi[i]);
            cTwo[i] = FastMath.sin(logQi[i]);

        }
        
        for (int i = 0; i < qSize; i++) {
            for (int j = 0; j < hSize; j++) {
                powTempVar[i][j] = FastMath.pow((1.0 - Q[i]), H[j]);
            }
        }   
        

    }

    /**
     * hqDerivative calculates the the largest value of the
     * periodogram given all test values of H and Q.
     * 
     * @param TimeValues
     * @param Coef
     * @param SIZE
     * @return 
     */
    public float hqDerivative(final double[] TimeValues, final double[] Coef,
                final int SIZE) {
        
        float value = 0.0f;
                
        final double[] LogTimeValues = new double[SIZE];
        final double[] tempFour = new double[SIZE];
        
        Log_V64f_V64f(TimeValues, 0, tempFour, 0, SIZE);
            
        System.arraycopy(tempFour, 0, LogTimeValues, 0, SIZE);
        
        double[] tempFive = new double[SIZE];
        double[] tempSix = new double[SIZE];

        Multiply_V64fS64f_V64f(LogTimeValues, 0, omegaDouble, tempFour, 0, SIZE);

        Cos_V64f_V64f(tempFour, 0, tempFive, 0, SIZE);
        Sin_V64f_V64f(tempFour, 0, tempSix, 0, SIZE);
        
        for (int i = 0; i < qSize; i++) {
            
            final double Qi_M = QiM[i];
            final double C_one = cOne[i];
            final double C_two = cTwo[i];
            final double[] tempSeven = new double[SIZE];
            final double[] tempEight = new double[SIZE];
            final double[] tempNine = new double[SIZE];
            Multiply_V64fS64f_V64f(tempFive, 0, C_one, tempSeven, 0, SIZE);
            Multiply_V64fS64f_V64f(tempSix, 0, C_two, tempEight, 0, SIZE);
            Add_V64fV64f_V64f(tempSeven, 0, tempEight, 0, tempNine, 0, SIZE);
             
            for (int j = 0; j < hSize; j++) {
                
                final double tempVar = powTempVar[i][j];

                final double B_prime = 1.0 * (Coef[1]) * (1.0 - Qi_M) * 1.0 / tempVar;

                //Took out the negative sign for B_Prime

                final double C_prime = (Coef[2]) * 1.0 / tempVar;
                
                final double[] HQDerivativeData = new double[SIZE];
                
                for (int k = 0; k < SIZE; k++) {
                    
                    HQDerivativeData[k] = FastMath.pow(TimeValues[k], 
                            mCoeffDouble - H[j]) *
                            (B_prime + C_prime * tempNine[k]);
                }
                
                final float[] SpectralDensity = new float[freqSize];
                
                computeLombScargle(LogTimeValues, HQDerivativeData,
                        SpectralDensity, SIZE);
                
                final float Temp = MaxOneDim(SpectralDensity, freqSize);
                if (Temp > value)
                    value = Temp;
            }
        }
        
        return value;
    }
    
    /**
     * MaxOneDim calculates the maximum value in a 1-d array
     * @param Array The array containing the values
     * @param SIZE The size of the array
     * @return The maximum of the array
     */
    public static float MaxOneDim(final float[] Array, final int SIZE) {
        float MaxValue = 0.0f;
        for (int i = 0; i < SIZE; i++) {
            if (Array[i] > MaxValue) {
                MaxValue = Array[i];
            }
        }
        return MaxValue;        
    }
    
    /**
     * computeLombScargle computes the Lomb-Scargle periodogram for CPU version
     * 
     * @param TimeValues
     * @param TimeSeries
     * @param SpectralDensity
     * @param SIZE 
     */
    public void computeLombScargle(final double[] TimeValues,
            final double[] TimeSeries, final float[] SpectralDensity,
            final int SIZE) {
        
        double SinSum, CosSum, CosResidSum, SinResidSum;        
        final double[] TwoOmegaT = new double[SIZE];
        final double[] OmegaTMinusTau = new double[SIZE];
        final double[] Residual = new double[SIZE];
        
        //Find mean of Time Series
        double Sum = 0;
        for (int i = 0; i < SIZE; i++) {
            Sum = Sum + TimeSeries[i];
        }
        
        final double Mean = -1.0 * Sum * 1.0 / SIZE;
        
        //TimeSeries becomes the residual
        //Subtract_IV64fS64f_IV64f(TimeSeries, 0, Mean, SIZE);
        Add_V64fS64f_V64f(TimeSeries, 0, Mean, Residual, 0, SIZE);
       // for (int i = 0; i < SIZE; i++) {
         //   Residual[i] = TimeSeries[i] - Mean;
        //}
        
        for (int i = 0; i < freqSize; i++) {
            
            final double Omega = 2.0 * 3.14159 * testFrequencies[i];
            SinSum = 0.0; CosSum = 0.0; 
            
            final double tempValue = 2.0 * Omega;
            final double[] tempArrayOne = new double[SIZE];
            final double[] tempArrayTwo = new double[SIZE];
            
            Multiply_V64fS64f_V64f(TimeValues, 0, tempValue, TwoOmegaT, 0, SIZE);
            Cos_V64f_V64f(TwoOmegaT, 0, tempArrayOne, 0, SIZE);
            Sin_V64f_V64f(TwoOmegaT, 0, tempArrayTwo, 0, SIZE);
            
            for (int j = 0; j < SIZE; j++) {
                //TwoOmegaT[j] = 2.0 * Omega * TimeValues[j];
                //SinSum = SinSum + FastMath.sin(TwoOmegaT[j]);
                //CosSum = CosSum + FastMath.cos(TwoOmegaT[j]);
                SinSum = SinSum + tempArrayTwo[j];
                CosSum = CosSum + tempArrayOne[j];
            }
            
            final double Tau = -1.0 * FastMath.atan2(SinSum, CosSum) * 1.0 / (2.0 * Omega);
            CosResidSum = 0.0; SinResidSum = 0.0; SinSum = 0.0; CosSum = 0.0;
            
            final double[] tempArrayThree = new double[SIZE];
            final double[] tempArrayFour = new double[SIZE];
                     
            Add_V64fS64f_V64f(TimeValues, 0, Tau, tempArrayOne, 0, SIZE);
            Multiply_V64fS64f_V64f(tempArrayOne, 0, Omega, OmegaTMinusTau, 0, SIZE);
            
            Cos_V64f_V64f(OmegaTMinusTau, 0, tempArrayOne, 0, SIZE);
            Sin_V64f_V64f(OmegaTMinusTau, 0, tempArrayTwo, 0, SIZE);
            
            Multiply_V64fV64f_V64f(tempArrayOne, 0, Residual, 0, tempArrayThree, 0, SIZE);
            Multiply_V64fV64f_V64f(tempArrayTwo, 0, Residual, 0, tempArrayFour, 0, SIZE);
            
            for (int j = 0; j < SIZE; j++) {
                //OmegaTMinusTau[j] = Omega * (TimeValues[j] - Tau);
                CosResidSum = CosResidSum + tempArrayThree[j];
                //CosResidSum = CosResidSum + Residual[j] * 
                //        FastMath.cos(OmegaTMinusTau[j]);
                SinResidSum = SinResidSum + tempArrayFour[j];
                //SinResidSum = SinResidSum + Residual[j] *
                  //      FastMath.sin(OmegaTMinusTau[j]);
                SinSum = SinSum + FastMath.pow(tempArrayTwo[j], 2.0);
                CosSum = CosSum + FastMath.pow(tempArrayOne[j], 2.0);
                //SinSum = SinSum + FastMath.pow(FastMath.sin(OmegaTMinusTau[j]), 2.0);
                //CosSum = CosSum + FastMath.pow(FastMath.cos(OmegaTMinusTau[j]), 2.0);
            }
            
            CosResidSum = FastMath.pow(CosResidSum, 2.0);
            SinResidSum = FastMath.pow(SinResidSum, 2.0);
            
            SpectralDensity[i] = (float) (CosResidSum * 1.0f / CosSum + 
                    SinResidSum * 1.0f / SinSum);
        }
    }
}