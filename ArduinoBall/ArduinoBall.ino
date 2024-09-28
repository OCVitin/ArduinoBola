#include <Wire.h>
#include <MPU6050.h>
#include <SoftwareSerial.h>

SoftwareSerial bluetooth(2, 3); // RX, TX
MPU6050 mpu;

void setup() {
  Serial.begin(9600);
  bluetooth.begin(9600); 
  Wire.begin();
  mpu.initialize();
}

void loop() {
  int16_t ax, ay, az;
  mpu.getAcceleration(&ax, &ay, &az);

  int threshold = 21000; 

  if (abs(ax) > threshold || abs(ay) > threshold || abs(az) > threshold) {
    bluetooth.println("TOSS");
    Serial.println("TOSS");
  }

  delay(500);
}
