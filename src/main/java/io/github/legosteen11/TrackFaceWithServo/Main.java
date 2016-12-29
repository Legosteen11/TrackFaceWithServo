package io.github.legosteen11.TrackFaceWithServo;

import io.github.legosteen11.TrackFaceWithServo.Serial.SerialController;
import io.github.legosteen11.TrackFaceWithServo.Servo.Servo;
import io.github.legosteen11.TrackFaceWithServo.Servo.ServoConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by wouter on 26-12-16.
 */
public class Main {
    private static SerialController serialController;
    private boolean configure;
    private Scanner scanner;
    
    public static void main(String[] args) {
        Main main = new Main(args);
    }

    private Main(String[] args) {
        String serialPortName = "/dev/ttyUSB0";
        configure = false;
        scanner = new Scanner(System.in);
        if(args.length > 0){
            if(args[0].equals("rpi")){
                serialPortName = "/dev/ttyACM0";
            }
            if(args[0].equals("configure") || args[0].equals("configure")) {
                configure = true;
            }
        }
        serialController = new SerialController(serialPortName);
        
        // Create servo map from config names:
        String[] servoNames = new String[]{"servoX", "servoY"};
        HashMap<String, Servo> servoHashMap = new HashMap<>();

        for (String servoName :
                servoNames) {
            try {
                String configUrl = servoName + "Config.txt";
                FileInputStream fileInputStream = new FileInputStream(configUrl);
                ObjectInputStream in = new ObjectInputStream(fileInputStream);
                servoHashMap.put(servoName, new Servo((ServoConfig) in.readObject(), servoName));
                in.close();
                fileInputStream.close();

                System.out.println("Created a new servo with name: " + servoName + ", and config url: " + configUrl);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        if(configure) {
            int[] percentages = new int[]{0, 25, 50, 75, 100};
            ServoConfig servoConfig = new ServoConfig();
            System.out.println("Starting calibration:");
            for (int percentage :
                    percentages) {
                System.out.println("Calibrating servo X for " + percentage + "%.");
                System.out.println("What should the value be for " + percentage + "%? Please enter an integer between 0 and 180.");
                boolean valueSet = false;
                while (!valueSet){
                    String input = scanner.next();
                    if(isInteger(input)) {
                        int inputInt = Integer.parseInt(input);
                        if(inputInt <= 180 || inputInt >= 0) {
                            System.out.println("Setting servo to: " + input);
                            // TODO: Set servo to position.
                            System.out.println("Is this ok? [NY]");
                            if (scanner.next().toLowerCase().equals("y")) {
                                servoConfig.addValueForPercentage(percentage, inputInt);
                                valueSet = true;
                                System.out.println("Value set!");
                            }
                        } else {
                            System.out.println("Please make sure your input lies between 0 and 180!");
                        }
                    } else {
                        System.out.println("Please enter an integer between 0 and 180.");
                    }
                }
            }
            
        } else {
            
        }
    }
    
    public void stop() {
        serialController.close();
    }

    private boolean isInteger(String s) {
        return isInteger(s,10);
    }

    private boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
