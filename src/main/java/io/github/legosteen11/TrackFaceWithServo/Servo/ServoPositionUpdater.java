package io.github.legosteen11.TrackFaceWithServo.Servo;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by wouter on 29-12-16.
 */
public class ServoPositionUpdater {
    private ArrayList<Servo> servoList;
    private OutputStream outputStream;
    private int delay;

    /**
     * Creates a new ServoPositionUpdater object with servo's already set
     * @param servos Servo's to control.
     * @param outputStream OutputStream to write positions to.
     * @param delay Time to wait in milliseconds before writing new instruction to outputStream.
     */
    public ServoPositionUpdater(Servo[] servos, OutputStream outputStream, int delay) {
        this.servoList = new ArrayList<>(Arrays.asList(servos));
        this.outputStream = outputStream;
        this.delay = delay;
    }

    /**
     * Creates a new ServoPositionUpdater object with empty servo list.
     * @param outputStream OutputStream to write positions to.
     * @param delay Time to wait in milliseconds before writing new instruction to outputStream.
     */
    public ServoPositionUpdater(OutputStream outputStream, int delay) {
        this.servoList = new ArrayList<>();
        this.outputStream = outputStream;
        this.delay = delay;
    }

    /**
     * Adds a servo to the list of servo's to control
     * @param servo servo to be added
     */
    public void addServo(Servo servo) {
        this.servoList.add(servo);
    }

    /**
     * Updates the positions of the servo's
     */
    public void updatePositions() throws InterruptedException, IOException {
        for (Servo servo :
                servoList) {
            outputStream.write((servo.getName() + servo.getCurrentPosition()).getBytes());
            TimeUnit.MILLISECONDS.sleep(delay);
        }
    }
}
