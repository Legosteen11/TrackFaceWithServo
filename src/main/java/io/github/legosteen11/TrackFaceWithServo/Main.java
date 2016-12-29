package io.github.legosteen11.TrackFaceWithServo;

import com.google.gson.Gson;
import io.github.legosteen11.TrackFaceWithServo.Serial.SerialController;
import io.github.legosteen11.TrackFaceWithServo.Servo.Servo;
import io.github.legosteen11.TrackFaceWithServo.Servo.ServoConfig;
import io.github.legosteen11.TrackFaceWithServo.Servo.ServoPair;
import io.github.legosteen11.TrackFaceWithServo.Servo.ServoPositionUpdater;
import io.github.legosteen11.TrackFaceWithServo.Vision.FaceFinder;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by wouter on 26-12-16.
 */
public class Main {
    private static SerialController serialController;
    private ServoPositionUpdater servoPositionUpdater;
    private boolean configure;
    private int delay;
    private boolean disableGson;

    public static void main(String[] args) {
        Main main = new Main(args);
    }

    private Main(String[] args) {
        String serialPortName = "/dev/ttyUSB0";
        configure = false;
        delay = 100;
        disableGson = false;

        for (String argument :
                args) {
            if(argument.equals("rpi")){
                serialPortName = "/dev/ttyACM0";
            }
            if(argument.equals("configure")) {
                configure = true;
            }
            if(argument.contains("delay=")) {
                String afterDelay = argument.toLowerCase().replaceAll("\\Qdelay=\\E", "");
                if(isInteger(afterDelay)){
                    delay = Integer.parseInt(afterDelay);
                }
            }
            if(argument.equals("json=off")) {
                disableGson = true;
            }
        }
        
        serialController = new SerialController(serialPortName);

        try {
            servoPositionUpdater = new ServoPositionUpdater(serialController.getOutput(), delay);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create servo map from config names:
        String[] servoNames = new String[]{"servoX"};
        HashMap<String, Servo> servoHashMap = new HashMap<>();
        
        if(configure) {
            for (String servoName :
                    servoNames) {
                servoHashMap.put(servoName, new Servo(servoName));
            }

            for (Servo servo :
                    servoHashMap.values()) {
                servoPositionUpdater.addServo(servo);
            }
            
            Scanner scanner = new Scanner(System.in);
            
            int[] percentages = new int[]{0, 25, 50, 75, 100};
            System.out.println("Starting calibration:");
            for (Servo servo :
                    servoHashMap.values()) {
                ServoConfig servoConfig = new ServoConfig();

                boolean agree = false;
                while(!agree) {
                for (int percentage :
                        percentages) {
                        printDashes();
                        System.out.println("Calibrating servo " + servo.getName() + " for " + percentage + "%.");
                        boolean valueSet = false;
                        while (!valueSet) {
                            System.out.println("What should the value be for " + percentage + "%? Please enter an integer between 0 and 180.");
                            String input = scanner.next();
                            if (isInteger(input)) {
                                int inputInt = Integer.parseInt(input);
                                if((inputInt <= 180) && (inputInt >= 0)) {
                                    System.out.println("Setting servo to: " + inputInt);
                                    servo.setServoPosition(inputInt);
                                    try {
                                        servoPositionUpdater.updatePositionsWithoutDelay();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("Is this ok? [NY]");
                                    if (scanner.next().toLowerCase().equals("y")) {
                                        servoConfig.addValueForPercentage(percentage, inputInt);
                                        valueSet = true;
                                        System.out.println("Value for " + percentage + "% set to " + inputInt + "!");
                                        
                                    }
                                } else {
                                    System.out.println("Please make sure your input lies between 0 and 180!");
                                }
                            } else {
                                System.out.println("Please enter an integer between 0 and 180.");
                            }
                        }
                    }
                    printDashes();
                    System.out.println("These are your current settings for servo " + servo.getName() + ":");
                    for (int percentage :
                            percentages) {
                        System.out.println(percentage + "% is calibrated to: " + servoConfig.getValueForPercentage(percentage));
                    }
                    System.out.println("Are these correct? [NY]");
                    if(scanner.next().toLowerCase().equals("y")) {
                        agree = true;
                    }
                }
                servo.setConfig(servoConfig);
                System.out.println("Saving config to disk...");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.home") + "/Documents/" + servo.getName() + "Config.txt");
                    if(!disableGson) {
                        Gson gson = new Gson();
                        String json = gson.toJson(servoConfig);
                        fileOutputStream.write(json.getBytes());
                    } else {
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(servoConfig);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            scanner.close();
        } 
        for (String servoName :
                servoNames) {
            try {
                System.out.println("Loading config for servo " + servoName);
                String configUrl = System.getProperty("user.home") + "/Documents/" + servoName + "Config.txt";
                
                ServoConfig servoConfig;
                if(!disableGson) {
                    String fileContent = FileUtils.readFileToString(new File(configUrl), "UTF-8");
                    Gson gson = new Gson();
                    System.out.println("JSON: " + fileContent);
                    servoConfig = gson.fromJson(fileContent, ServoConfig.class);
                } else {
                    FileInputStream fileInputStream = new FileInputStream(configUrl);
                    ObjectInputStream in = new ObjectInputStream(fileInputStream);
                    servoConfig = (ServoConfig) in.readObject();
                    in.close();
                    fileInputStream.close();
                }
                Servo servoObject = new Servo(servoConfig, servoName);
                servoHashMap.put(servoName, servoObject);
                

                System.out.println("Created a new servo with name: " + servoName + ", and config url: " + configUrl);

                System.out.println("These are your current settings for servo " + servoObject.getName() + ":");
                for(int i = 0; i <= 100; i = i + 25) {
                    System.out.println(i + "% is calibrated to " + servoObject.getConfig().getValueForPercentage(i) + ".");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        ServoPair servoPair = new ServoPair(servoHashMap.get("servoX"), servoHashMap.get("servoY"));

        FaceFinder faceFinder = new FaceFinder(1, 40);
        
        boolean stop = false;
        while (!stop) {
            try {
                int[] percentages = faceFinder.findFaces(ImageIO.read(new File("/home/wouter/Pictures/lul.jpg")));
                
                servoPair.setPercentages(percentages);
                servoPositionUpdater.updatePositions();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
    
    private void printDashes() {
        System.out.println("-------------------------------");
    }
}
