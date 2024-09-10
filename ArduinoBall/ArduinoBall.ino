/*
  Copyright (c) 2015 Intel Corporation. All rights reserved.

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-
  1301 USA
*/
// byte val = 6;
// byte oldval = 6;

// #include <CurieBLE.h>

// #include <EducationShield.h>

// IMU imu; // the IMU

// byte dir;


// BLEPeripheral blePeripheral; // create peripheral instance

// BLEService ledService("19B10000-E8F2-537E-4F6C-D104768A1214"); // create service

// // create switch characteristic and allow remote device to read and write
// BLECharCharacteristic switchChar("19B10001-E8F2-537E-4F6C-D104768A1214", BLERead | BLEWrite);

// void setup() {
//   Serial.begin(9600);
//   /* Initialise the IMU */

//   imu.begin();
//   imu.detectShock();
//   imu.attachCallback(shockCallback);

  

//   // set the local name peripheral advertises
//   blePeripheral.setLocalName("APKM");
//   // set the UUID for the service this peripheral advertises
//   blePeripheral.setAdvertisedServiceUuid(ledService.uuid());

//   // add service and characteristic
//   blePeripheral.addAttribute(ledService);
//   blePeripheral.addAttribute(switchChar);

//   // assign event handlers for connected, disconnected to peripheral
//   blePeripheral.setEventHandler(BLEConnected, blePeripheralConnectHandler);
//   blePeripheral.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);

//   // assign event handlers for characteristic
//   // switchChar.setEventHandler(BLEWritten, switchCharacteristicWritten);
//   // set an initial value for the characteristic
//   switchChar.setValue(0);

//   // advertise the service
//   blePeripheral.begin();
//   Serial.println(("Bluetooth device active, waiting for connections..."));
// }

// void loop() {
//   // poll peripheral
//   blePeripheral.poll();

//     dir = 5;

//   Serial.print(dir);
 
   
//     if(oldval != val) {
//       dir = val; 
//       Serial.println(dir);

//   }
 
// switchChar.setValue(dir);
//   oldval = val;
// delay(350);
// }

// void blePeripheralConnectHandler(BLECentral& central) {
//   // central connected event handler
//   Serial.print("Connected event, central: ");
//   Serial.println(central.address());
// }

// void blePeripheralDisconnectHandler(BLECentral& central) {
//   // central disconnected event handler
//   Serial.print("Disconnected event, central: ");
// }
// static void shockCallback(void)
// {

//   val++;

// }
#include <Wire.h>
#include <MPU6050.h>
#include <SoftwareSerial.h>

// Configura o módulo Bluetooth HC-05
SoftwareSerial bluetooth(2, 3); // RX, TX

// Cria uma instância do MPU6050
MPU6050 mpu;

void setup() {
  // Inicializa a comunicação serial
  Serial.begin(9600);
  bluetooth.begin(9600); // Inicializa a comunicação com o módulo Bluetooth

  // Inicializa o MPU6050
  Wire.begin();
  mpu.initialize();

  // Verifica se o MPU6050 está conectado
  if (!mpu.testConnection()) {
    Serial.println("MPU6050 não conectado!");
    while (1); // Para o código se o sensor não estiver conectado
  }

  Serial.println("MPU6050 conectado e funcionando.");
  bluetooth.println("MPU6050 conectado e funcionando.");
}

void loop() {
  // Lê dados do MPU6050
  int16_t ax, ay, az;
  int16_t gx, gy, gz;

  mpu.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);

  // Envia dados para o monitor serial
  Serial.print("aX: "); Serial.print(ax); 
  Serial.print(" | aY: "); Serial.print(ay); 
  Serial.print(" | aZ: "); Serial.print(az); 
  Serial.print(" | gX: "); Serial.print(gx); 
  Serial.print(" | gY: "); Serial.print(gy); 
  Serial.print(" | gZ: "); Serial.println(gz);

  // Envia dados para o módulo Bluetooth
  bluetooth.print("aX: "); bluetooth.print(ax); 
  bluetooth.print(" | aY: "); bluetooth.print(ay); 
  bluetooth.print(" | aZ: "); bluetooth.print(az); 
  bluetooth.print(" | gX: "); bluetooth.print(gx); 
  bluetooth.print(" | gY: "); bluetooth.print(gy); 
  bluetooth.print(" | gZ: "); bluetooth.println(gz);

  // Verifica se há dados recebidos via Bluetooth
  if (bluetooth.available() > 0) {
    char receivedChar = bluetooth.read();
    Serial.print("Recebido via Bluetooth: ");
    Serial.println(receivedChar);
    // Adicione lógica para lidar com comandos recebidos aqui
  }

  delay(500); // Ajuste o atraso conforme necessário
}
