#include <ESP8266WiFi.h>
using namespace std; 
 
const char* ssid = "Your_SSID"; // SSID i.e. Service Set Identifier is the name of your WIFI
const char* password = "Your_PASSWORD"; // Your Wifi password, in case you have open network comment the whole statement.
WiFiServer server(80); // Creates a server that listens for incoming connections on the specified port, here in this case port is 80.

int pin1 = 9; // Sets the value for Pin1 of Relay
int pin2 = 10; // Sets the value for Pin2 of Relay
int pin3 = 4; // Sets the value for Pin3 of Relay
int pin4 = 5; // Sets the value for Pin4 of Relay
 
void setup() {
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
 
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) {
    digitalWrite(LED_BUILTIN, LOW);
    delay(500);
  }
  digitalWrite(LED_BUILTIN, HIGH);
 
  // Start the server
  server.begin();
  Serial.println("");
  Serial.println(WiFi.localIP()); //Gets the WiFi shield's IP address and Print the IP address of serial monitor
}
 
void loop() {
  
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    if(WiFi.status() != WL_CONNECTED) {
      WiFi.begin(ssid, password);
      while (WiFi.status() != WL_CONNECTED) {
        digitalWrite(LED_BUILTIN, LOW);
        delay(500);
      }
      digitalWrite(LED_BUILTIN, HIGH);
      server.begin();
      Serial.println("");
      Serial.println(WiFi.localIP()); //Gets the WiFi shield's IP address and Print the IP address of serial monitor
   }
   return;
  }
  
  // Wait until the client sends some data
  int initialTime = millis();
  while(!client.available()) if(millis()-initialTime >= 5000) return;
  
  // Read the first line of the request
  String request = client.readStringUntil('\r');
  client.flush();

       // Execute request
       if(request.indexOf("/01") != -1) digitalWrite(pin1, LOW);
  else if(request.indexOf("/02") != -1) digitalWrite(pin2, LOW);
  else if(request.indexOf("/03") != -1) digitalWrite(pin3, LOW);
  else if(request.indexOf("/04") != -1) digitalWrite(pin4, LOW);
  else if(request.indexOf("/11") != -1) digitalWrite(pin1, HIGH);
  else if(request.indexOf("/12") != -1) digitalWrite(pin2, HIGH);
  else if(request.indexOf("/13") != -1) digitalWrite(pin3, HIGH);
  else if(request.indexOf("/14") != -1) digitalWrite(pin4, HIGH);
  else if(request.indexOf("/05") != -1);
  else if(request.indexOf("/15") != -1){
    digitalWrite(pin1, HIGH);
    digitalWrite(pin2, HIGH);
    digitalWrite(pin3, HIGH);
    digitalWrite(pin4, HIGH);
  }

  // Write request on server
  client.println("<!doctype html>");
  client.println("<html>");
  client.println("<body bgcolor=\"121212\">");
  client.println("<font color=\"939393\" size=\"9\" > ");
  client.println("<p style=\"text-align:center;\"> ");
  if(digitalRead(pin1)==1) client.print(".");
  else client.print("-");
  if(digitalRead(pin2)==1) client.print(".");
  else client.print("-");
  if(digitalRead(pin3)==1) client.print(".");
  else client.print("-");
  if(digitalRead(pin4)==1) client.print(".");
  else client.print("-");
  client.print("  </p>");
  client.print("  </font>");
  client.println("</body>");
  client.println("</html>");
  Serial.println("Executed");
}
