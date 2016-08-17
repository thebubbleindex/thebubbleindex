__kernel void hq_derivative(__global float* values, __global const float* meanArray,
    __global const float* LogTimeValues,
    __global const float* TestFrequencies,
    __global const float* HQDerivativeData,
    const int Q_SIZE, const int H_SIZE,
    const int FREQ_SIZE, const int NUMBER_OF_DAYS){
        
    const int globalid = get_global_id(0);
    const int localid = get_global_id(1);
    const int indexvalue = globalid + localid * (Q_SIZE);
    
    if (indexvalue >= Q_SIZE*H_SIZE) {
        return;
    } else {

        const float Mean = meanArray[indexvalue];

        float MaxValue = 0.0f;
        for (int k = 0; k < FREQ_SIZE; k++) {

            const float OmegaSecond = 2.0f * 3.14159f * TestFrequencies[k];
            float2 tempSum = (float2)(0.0f);

            for (int p = 0; p < NUMBER_OF_DAYS; p++) {
                float tempValue = 2.0f * OmegaSecond * LogTimeValues[p];
                tempSum = tempSum + (float2)(sin(tempValue), cos(tempValue));
            }

            const float Tau = atan2(tempSum.x, tempSum.y) * 1.0f / (2.0f * OmegaSecond);

            float4 tot = (float4)(0.0f);

            for (int p = 0; p < NUMBER_OF_DAYS; p++) {
                                        
                float Value = HQDerivativeData[indexvalue*NUMBER_OF_DAYS + p] - Mean;
                float OmegaSecondLTMT = OmegaSecond * (LogTimeValues[p] - Tau);
                float cosOmegaSecondLTMT = cos(OmegaSecondLTMT);
                float sinOmegaSecondLTMT = sin(OmegaSecondLTMT);

                tot = tot + (float4)(Value * cosOmegaSecondLTMT, 
                    Value * sinOmegaSecondLTMT, sinOmegaSecondLTMT * sinOmegaSecondLTMT, 
                    cosOmegaSecondLTMT * cosOmegaSecondLTMT); 
            }

            tot.x = tot.x * tot.x;
            tot.y = tot.y * tot.y;

            float tempfloat = tot.x * 1.0f / tot.w + tot.y * 1.0f / tot.z;
            
            if (tempfloat > MaxValue) {
                MaxValue = tempfloat;
            }    
        }
        values[indexvalue] = MaxValue;           
    }
}      
