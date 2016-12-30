#include <Servo.h>
#include <ctype.h>

Servo servoX;
String servoXName = "servoX";
int servoXPos = 0;

Servo servoY;
String servoYName = "servoY";
int servoYPos = 0;


String incomingString;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  servoX.attach(9);
  servoY.attach(10);
}

void loop() {
  if(Serial.available() > 0) {
    incomingString = Serial.readString();

    Serial.println("Read: " + incomingString);
    
    if(incomingString.indexOf(servoXName) != -1) {
      int newInt = incomingString.substring(servoXName.length()).toInt();
      if(newInt <= 180 && newInt >= 0){
        servoXPos = newInt;
        servoX.write(servoXPos);
      }
    }
    if(incomingString.indexOf(servoYName) != -1) {
      int newInt = incomingString.substring(servoYName.length()).toInt();
      if(newInt <= 180 && newInt >= 0){
        servoYPos = newInt;
        servoY.write(servoYPos);
      }
    }

    Serial.println(servoXName + " on pos: " + servoXPos + ". " + servoYName + " on pos: " + servoYPos);
  }
}
