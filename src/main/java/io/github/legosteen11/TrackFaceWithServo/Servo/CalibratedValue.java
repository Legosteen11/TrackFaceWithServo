package io.github.legosteen11.TrackFaceWithServo.Servo;

import java.io.Serializable;

/**
 * Created by wouter on 30-12-16.
 */
public class CalibratedValue implements Serializable{
    public int percentage;
    public int calibratedValue;

    public CalibratedValue(int percentage, int calibratedValue) {
        this.percentage = percentage;
        this.calibratedValue = calibratedValue;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getCalibratedValue() {
        return calibratedValue;
    }
}
