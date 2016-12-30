package io.github.legosteen11.TrackFaceWithServo.Servo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by wouter on 29-12-16.
 */
public class ServoConfig implements Serializable {
    /**
     * HashMap containing calibrated data:
     * First is percentage, second is calibrated value.
     */
    @SerializedName("calibrated-values")
    private ArrayList<CalibratedValue> calibratedValues;

    /**
     * Create a new ServoConfig object with default calibratedValues
     */
    public ServoConfig() {
        this.calibratedValues = new ArrayList<CalibratedValue>();
    }

    /**
     * Creates a new ServoConfig object with calibratedValues set
     * @param calibratedValues CalibratedValues array
     */
    public ServoConfig(CalibratedValue[] calibratedValues) {
        this.calibratedValues = new ArrayList<>(Arrays.asList(calibratedValues));
    }

    /**
     * Returns the calibrated (or calculated) value for the percentage.
     * @param percentage Percentage to get calibrated (or calculated) value for.
     * @return Calibrated (or calculated) value for percentage.
     */
    public int getValueForPercentage(int percentage) {
        // Percentage is already in calibratedValuesMap;
        for (CalibratedValue calibratedValue :
                calibratedValues) {
            if (calibratedValue.getPercentage() == percentage) {
                return calibratedValue.getCalibratedValue();
            }
        }
        
        CalibratedValue[] closestValues = findClosestValues(percentage, calibratedValues);
        
        int lowestCalibratedValue;
        int highestCalibratedValue;
        
        if(closestValues[0].getCalibratedValue() > closestValues[1].getCalibratedValue()) {
            lowestCalibratedValue = closestValues[1].getCalibratedValue();
            highestCalibratedValue = closestValues[0].getCalibratedValue();
        } else {
            lowestCalibratedValue = closestValues[0].getCalibratedValue();
            highestCalibratedValue = closestValues[1].getCalibratedValue();
        }
        
        // (highestValue - lowestValue) * (percentage / 100%) + lowestValue
        float percentageToMultiplyBy = (float)percentage / 100;
        
        int returnValue = (int) Math.ceil(((highestCalibratedValue - lowestCalibratedValue) * percentageToMultiplyBy) + lowestCalibratedValue);
        return returnValue;
    }
    
    private CalibratedValue[] findClosestValues(int value, ArrayList<CalibratedValue> calibratedValues){
        if(calibratedValues.size() == 0){
            return null;
        }
        
        CalibratedValue highValue = new CalibratedValue(100, 180);
        CalibratedValue lowValue = new CalibratedValue(0, 0);

        for (CalibratedValue calibratedValue :
                calibratedValues) {
            if(calibratedValue.getPercentage() > value && calibratedValue.getPercentage() < highValue.getPercentage()) {
                highValue = calibratedValue;
            } else if(calibratedValue.getPercentage() < value && calibratedValue.getPercentage() > lowValue.getPercentage()) {
                lowValue = calibratedValue;
            }
        }
        
        return new CalibratedValue[]{lowValue, highValue};
    }

    /**
     * Adds a calibrated value for a certain percentage to the config.
     * @param percentage Percentage to calibrate
     * @param value Value to set
     */
    public void addValueForPercentage(int percentage, int value) {
        if(percentage < 0 || percentage > 180 || value < 0 || value > 180){
            return;
        }
        calibratedValues.add(new CalibratedValue(percentage, value));
    }

    /**
     * Returns the calibratedValues array
     * @return Returns the calibratedValues array
     */
    public CalibratedValue[] getCalibratedValues() {
        return calibratedValues.toArray(new CalibratedValue[0]);
    }

    /**
     * Set's the calibratedValues array
     * @param calibratedValues Calibrated values array to set.
     */
    public void setCalibratedValues(CalibratedValue[] calibratedValues) {
        this.calibratedValues = new ArrayList<>(Arrays.asList(calibratedValues));
    }
}
