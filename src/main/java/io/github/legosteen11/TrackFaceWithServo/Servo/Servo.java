package io.github.legosteen11.TrackFaceWithServo.Servo;

/**
 * Created by wouter on 29-12-16.
 */
public class Servo {
    private int currentPosition;
    private ServoConfig config;
    private String name;

    /**
     * Creates a Servo object with config.
     * @param config Config to use.
     * @param name Name for the servo.
     */
    public Servo(ServoConfig config, String name) {
        this.config = config;
        this.name = name;
    }

    /**
     * Set's the position for the servo to update to.
     * @param position Position to go to.
     */
    public void setServoPosition(int position) {
        currentPosition = position;
    }

    /**
     * Returns the current position to update to.
     * @return the current position of the servo to update to.
     */
    public int getCurrentPosition() {
        return this.currentPosition; 
    }

    /**
     * Sets the current position in percentages, calculates the position from the Config file.
     * @param positionInPercentages Position to go to in percentages
     */
    public void setCurrentPositionInPercentages(int positionInPercentages) {
        this.currentPosition = config.getValueForPercentage(positionInPercentages);
    }

    /**
     * Returns the name of the servo
     * @return name of the servo
     */
    public String getName() {
        return name;
    }
}
