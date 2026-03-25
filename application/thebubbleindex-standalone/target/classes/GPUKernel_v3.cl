__kernel void hq_derivative(__global float* values, __global float* meanArray,
    __global const float* LogTimeValues,
    __global const float* TimeValues,  
    __global const float* TestFrequencies,
    __global float* HQDerivativeData,
    __global float* Q, __global float* H, float M, float Omega, 
    float Coef_Two, float Coef_Three,
    int Q_SIZE, int H_SIZE,
    int FREQ_SIZE, int NUMBER_OF_DAYS){
        
    int globalid = get_global_id(0);
    int localid = get_global_id(1);
    int indexvalue = globalid + localid * (Q_SIZE);
    
    if (indexvalue >= Q_SIZE*H_SIZE) {
        return;
    } else {
                      
        float OmegaSecond, SinSum, CosSum, CosResidSum, SinResidSum,
            Tau;
        Tau = 0.0f;

        float Mean;
        Mean = meanArray[indexvalue];

        float MaxValue;
        MaxValue = 0.0f;
        for (int k = 0; k < FREQ_SIZE; k++) {

            OmegaSecond = 2.0f * 3.14159f * TestFrequencies[k];
            SinSum = 0.0f; CosSum = 0.0f;

            for (int p = 0; p < NUMBER_OF_DAYS; p++) {
                float tempValue = 2.0f * OmegaSecond * LogTimeValues[p];
                SinSum = SinSum + sin(tempValue);
                CosSum = CosSum + cos(tempValue);
            }

            Tau = atan2(SinSum, CosSum) * 1.0f / (2.0f * OmegaSecond);
            CosResidSum = 0.0f; SinResidSum = 0.0f; SinSum = 0.0f; CosSum = 0.0f;

            for (int p = 0; p < NUMBER_OF_DAYS; p++) {
                                        
                float Value = HQDerivativeData[indexvalue*NUMBER_OF_DAYS + p] - Mean;
                float OmegaSecondLTMT = OmegaSecond * (LogTimeValues[p] - Tau);
                float cosOmegaSecondLTMT = cos(OmegaSecondLTMT);
                float sinOmegaSecondLTMT = sin(OmegaSecondLTMT);

                CosResidSum = CosResidSum + Value * cosOmegaSecondLTMT;
                SinResidSum = SinResidSum + Value * sinOmegaSecondLTMT;
                SinSum = SinSum + sinOmegaSecondLTMT * sinOmegaSecondLTMT;
                CosSum = CosSum + cosOmegaSecondLTMT * cosOmegaSecondLTMT;
            }

            CosResidSum = CosResidSum * CosResidSum;
            SinResidSum = SinResidSum * SinResidSum;

            float tempfloat;
            tempfloat = CosResidSum * 1.0f / CosSum + 
                    SinResidSum * 1.0f / SinSum;

            if (tempfloat > MaxValue) {
                MaxValue = tempfloat;
            }    

        }
        values[indexvalue] = MaxValue;           
    }
}      
