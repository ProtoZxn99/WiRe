/* 
**  Connect the ESP8266 unit to an existing WiFi access point
**  For more information see http://42bots.com
*/

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include "Base64.h"

// Replace these with your WiFi network settings
static String ssid = "Poi"; //replace this with your WiFi network name
static String password = "1sampai8"; //replace this with your WiFi network password

static const String server_url = "http://192.168.43.235/wire/server/wire/";

static uint8_t listpin [] = {D1,D2,D3,D4,D5,D6,D7,D8};

static const int device_cooldown = 1000; //cooldown of each device cycle in milliseconds

static const int wifi_cooldown = 1000; //cooldown of each wifi cycle check in milliseconds
static const int wifi_timeout = 10; //number of max loops of wifi check allowed before WiFi deemed unreachable

void setup()
{
  
  Serial.begin(115200);
  String a = XOR_Encrypt("278019560",WiFi.macAddress());
  Serial.println(a);
  String b = XOR_Encrypt(a,WiFi.macAddress());
  Serial.println(b);
  pinMode(D0,OUTPUT);
  digitalWrite(D0,HIGH);
  for(int i = 0; i<sizeof(listpin); i++){
    pinMode(listpin[i],OUTPUT);
    digitalWrite(listpin[i],LOW);
  }
  digitalWrite(D0,LOW);
}

String HTTPGetRequest(String url){
  
  String payload = "";
  
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
    HTTPClient http;  //Declare an object of class HTTPClient

    http.begin(url);  //Specify request destination
    int httpCode = http.GET();                                                                  //Send the request
 
    if (httpCode > 0) { //Check the returning code
 
      payload = http.getString();   //Get the request response payload
    }
    http.end();   //Close connection
  }
  
  return payload;
}

String XOR_Encrypt(String toEncrypt, String key) {
  
    String output = toEncrypt;
    
    for (int i = 0; i < toEncrypt.length(); i++){
        output[i] = toEncrypt[i] ^ key[i % (sizeof(key) / sizeof(char))];
    }
    return output;
}

String Base64_Decode(String encoded){

  char input [encoded.length()];
  encoded.toCharArray(input,encoded.length());
  int inputLen = sizeof(input);
  
  int decodedLen = base64_dec_len(input, inputLen);
  char decoded[decodedLen];
  
  base64_decode(decoded, input, inputLen);
  
  return String(decoded);
}

void UpdateWiFiInfo(){
  ssid = HTTPGetRequest(server_url+"getAccountWiFiSSID.php?device_id="+WiFi.macAddress()+"-"+listpin[0]);\
  Serial.print("Received: ");
  Serial.println(ssid);
  ssid = Base64_Decode(ssid);
  Serial.print("Decoded: ");
  Serial.println(ssid);
  ssid = XOR_Encrypt(ssid, WiFi.macAddress());
  Serial.print("Decrypted: ");
  Serial.println(ssid);
  password = HTTPGetRequest(server_url+"getAccountWiFiPassword.php?device_id="+WiFi.macAddress()+"-"+listpin[0]);
  Serial.print("Received: ");
  Serial.println(password);
  password = Base64_Decode(password);
  Serial.print("Decoded: ");
  Serial.println(password);
  password = XOR_Encrypt(password, ssid);
  Serial.print("Decrypted: ");
  Serial.println(password);
}

void SearchUnsecuredWiFi(){
  int n = WiFi.scanNetworks();
  
  int i = 0;
   while (i < n && WiFi.status() != WL_CONNECTED) {
      // Print SSID and RSSI for each network found
      WiFi.begin(WiFi.SSID(i));
      
      int counter = 0;
      while (WiFi.status() != WL_CONNECTED && counter<wifi_timeout)
      {
        delay(wifi_cooldown);
        counter++;
      }
      i++;
   }
}

void connectWiFi(){
  WiFi.disconnect();

  if(password.length()<1){
    WiFi.begin(ssid);
  }
  else{
    WiFi.begin(ssid, password);
  }

  int counter = 0;
  
  while (WiFi.status() != WL_CONNECTED && counter<wifi_timeout)
  {
    delay(wifi_cooldown);
    counter++;
  }
}

void loop() {
  if(WiFi.status() != WL_CONNECTED){
    digitalWrite(D0,HIGH);
    connectWiFi();
  }
  Serial.println(WiFi.macAddress());
  while(WiFi.status() != WL_CONNECTED){
    SearchUnsecuredWiFi();
  }
  UpdateWiFiInfo();
  digitalWrite(D0,LOW);
  for(int i = 0; i<sizeof(listpin); i++){
    String state = HTTPGetRequest(server_url+"getDeviceState.php?device_id="+WiFi.macAddress()+"-"+listpin[i]);
    if(state=="1"){
      digitalWrite(listpin[i],HIGH);
    }
    else{
      digitalWrite(listpin[i],LOW);
    }
  }
  
  delay(device_cooldown);
}
