#include <ArduinoJson.h>
#include <Firebase.h>  
#include <FirebaseArduino.h>  
#include <FirebaseCloudMessaging.h>  
#include <FirebaseError.h>  
#include <FirebaseHttpClient.h>  
#include <FirebaseObject.h>  
#include <ESP8266WiFi.h>  
#include <FirebaseArduino.h>  
  
// Set these to run example.  
#define FIREBASE_HOST "example.firebaseio.com"  # Replace with actial address
#define FIREBASE_AUTH "EXPAMPLE****"  # Replace with actal value  
#define WIFI_SSID "SSID"    # Replace with actal value  
#define WIFI_PASSWORD "PASS"    # Replace with actal value  


int pin1 = 9; // Sets the value for Pin1 of Relay
int pin2 = 10; // Sets the value for Pin2 of Relay
int pin3 = 4; // Sets the value for Pin3 of Relay
int pin4 = 5;  // Sets the value for Pin4 of Relay



int ledPin = pin4;                // choose the pin for the LED
int inputPin = 14;               // choose the input pin (for PIR sensor)
int pirState = HIGH;             // we start, assuming no motion detected
int val = 0;  
  
void setup() {  
  
  pinMode(ledPin, OUTPUT);      // declare LED as output
  pinMode(inputPin, INPUT); 
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
  pinMode(pin1, OUTPUT);
  digitalWrite(pin1, HIGH);
  pinMode(pin2, OUTPUT);
  digitalWrite(pin2, HIGH);
  pinMode(pin3, OUTPUT);
  digitalWrite(pin3, HIGH);
  pinMode(pin4, OUTPUT);
  digitalWrite(pin4, HIGH);
  Serial.begin(115200);
  delay(10);
 
  Serial.begin(9600);  
  
  // connect to wifi.  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);  
  Serial.print("connecting");  
  while (WiFi.status() != WL_CONNECTED) {  
    digitalWrite(LED_BUILTIN, LOW);
    Serial.print(".");  
    delay(500);  
  }  
  digitalWrite(LED_BUILTIN, HIGH);
  Serial.println(WiFi.localIP());  
    
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);  
}

void readFirebase(){
  if(Firebase.getString("NodeMCU/switch_status_phone") == "ON"){
    digitalWrite(pin1, LOW);
  }else digitalWrite(pin1, HIGH);
  if(Firebase.getString("NodeMCU/switch_status_office_fan") == "ON"){
    digitalWrite(pin2, LOW);
  }else digitalWrite(pin2, HIGH);
  if(Firebase.getString("NodeMCU/switch_status_office_light") == "ON"){
    digitalWrite(pin3, LOW);
  }else digitalWrite(pin3, HIGH);
}
  
void loop() {   
  Serial.println(val);
  val = digitalRead(inputPin);
  if (val == HIGH) {
    digitalWrite(ledPin, LOW);
    if (pirState == HIGH) {
      Serial.println("Motion detected!");
      pirState = LOW;
      Firebase.setString("NodeMCU/switch_status_office_fan", "ON");  
      digitalWrite(pin2, LOW);
      Firebase.setString("NodeMCU/switch_status_office_light", "ON");  
      digitalWrite(pin3, LOW);
      delay(300000);
    }
  } else {
    digitalWrite(ledPin, HIGH);
    if (pirState == LOW){
      Serial.println("Motion ended!");
      pirState = HIGH;
      Firebase.setString("NodeMCU/switch_status_office_fan", "OFF");  
      Firebase.setString("NodeMCU/switch_status_office_light", "OFF"); 
    }
  }
  readFirebase();
}  
