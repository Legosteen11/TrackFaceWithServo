package io.github.legosteen11.TrackFaceWithServo.Servo;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by wouter on 29-12-16.
 */
public class ServoConfig implements Serializable {
    /**
     * HashMap containing calibrated data:
     * First is percentage, second is calibrated value.
     */
    private HashMap<Integer, Integer> calibratedValuesMap;

    /**
     * Creates a new ServoConfig object with calibratedValuesMap
     * @param calibratedValuesMap HashMap<Integer, Integer>, first value is the percentage, second is the calibrated value for that percentage.
     */
    public ServoConfig(HashMap<Integer, Integer> calibratedValuesMap) {
        this.calibratedValuesMap = calibratedValuesMap;
    }

    /**
     * Creates a new ServoConfig object with empty calibratedValuesMap.
     */
    public ServoConfig(){
        this.calibratedValuesMap = new HashMap<>();
    }

    /**
     * Returns the calibrated (or calculated) value for the percentage.
     * @param percentage Percentage to get calibrated (or calculated) value for.
     * @return Calibrated (or calculated) value for percentage.
     */
    public int getValueForPercentage(int percentage) {
        // Percentage is already in calibratedValuesMap;
        if(calibratedValuesMap.containsKey(percentage)) {
            return calibratedValuesMap.get(percentage);
        }
        
        int[] closestValues = findClosestValues(percentage, calibratedValuesMap);
        
        if(closestValues[0] == Integer.MIN_VALUE || closestValues[1] == Integer.MAX_VALUE) {
            return 50;
        }
        
        // (highestValue - lowestValue) * (percentage / 100%) + lowestValue 
        return (closestValues[1] - closestValues[0]) * (percentage / 100) + closestValues[0];
    }
    
    private int[] findClosestValues(int value, HashMap<Integer, Integer> map){
        if(map == null || map.values().size() == 0){
            return null;
        }
        
        int highValue = Integer.MAX_VALUE;
        int lowValue = Integer.MIN_VALUE;
        
        
        for (int currentValue :
                map.values()) {
            if(currentValue > value && currentValue < highValue) {
                highValue = currentValue;
            } else if(currentValue < value && currentValue > lowValue) {
                lowValue = currentValue;
            }
        }
        
        if(lowValue == Integer.MIN_VALUE) {
            lowValue = value;
        } else if(highValue == Integer.MAX_VALUE) {
            highValue = value;
        }
        
        return new int[]{lowValue, highValue};
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
        calibratedValuesMap.put(percentage, value);
    }

    /**
     * Returns the calibratedValuesMap
     * @return calibratedValuesMap
     */
    public HashMap<Integer, Integer> getCalibratedValuesMap() {
        return calibratedValuesMap;
    }

    /**
     * Sets the calibratedValuesMap
     * @param calibratedValuesMap HashMap<Integer, Integer>, first value is the percentage, second is the calibrated value for that percentage.
     */
    public void setCalibratedValuesMap(HashMap<Integer, Integer> calibratedValuesMap) {
        this.calibratedValuesMap = calibratedValuesMap;
    }
}
