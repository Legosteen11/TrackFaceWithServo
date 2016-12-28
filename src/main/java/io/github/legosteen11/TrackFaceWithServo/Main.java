package io.github.legosteen11.TrackFaceWithServo;

import io.github.legosteen11.TrackFaceWithServo.Serial.SerialController;

/**
 * Created by wouter on 26-12-16.
 */
public class Main {
    private static SerialController serialController;

    public Main() {
        
    }
    
    public void stop() {
        serialController.close();
    }
}
