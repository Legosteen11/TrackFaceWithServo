package io.github.legosteen11.TrackFaceWithServo.Servo;

/**
 * Created by wouter on 29-12-16.
 */
public class ServoPair {
    private Servo servoX;
    private Servo servoY;
    private int xPercentage;
    private int yPercentage;
    private boolean complete;
    
    public ServoPair(Servo servoX, Servo servoY) {
        this.servoX = servoX;
        this.servoY = servoY;
        this.complete = true;
    }
    
    public ServoPair() {
        this.complete = false;
    }

    public Servo getServoX() {
        return servoX;
    }

    public void setServoX(Servo servoX) {
        this.servoX = servoX;
    }

    public Servo getServoY() {
        return servoY;
    }

    public void setServoY(Servo servoY) {
        this.servoY = servoY;
    }

    public int getxPercentage() {
        return xPercentage;
    }

    public void setxPercentage(int xPercentage) {
        this.xPercentage = xPercentage;
        servoX.setCurrentPositionInPercentages(xPercentage);
    }

    public int getyPercentage() {
        return yPercentage;
    }

    public void setyPercentage(int yPercentage) {
        this.yPercentage = yPercentage;
        servoY.setCurrentPositionInPercentages(yPercentage);
    }

    /**
     * Set's the percentages, please make sure that the first value of percentages is the x percentage and the second is the y!
     * @param percentages First value is the x percentage, second is the y percentage.
     */
    public void setPercentages(int[] percentages) {
        setxPercentage(percentages[0]);
        setyPercentage(percentages[1]);
    }
    
    public boolean isComplete() {
        return complete;
    }
    
    
}
