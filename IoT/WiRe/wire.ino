/* 
**  Connect the ESP8266 unit to an existing WiFi access point
**  For more information see http://42bots.com
*/

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include "Base64.h"
#include <MD5.h>

// Replace these with your WiFi network settings
static String ssid = "cd4687"; //replace this with your WiFi network name
static String password = "278019560"; //replace this with your WiFi network password

static const String server_url = "http://192.168.0.29/wire/server/wire/";

static const String server_key = "ConcealM4CtoHackers";
static String encrypted_id;
static String ssid_key;

static uint8_t ledpin = D0;
static uint8_t listpin [] = {D1,D2,D3,D4,D5,D6,D7,D8};

static const int device_cooldown = 1000; //cooldown of each device cycle in milliseconds
static const int cycle_timeout = 10;

static const int wifi_cooldown = 1000; //cooldown of each wifi cycle check in milliseconds
static const int wifi_timeout = 10; //number of max loops of wifi check allowed before WiFi deemed unreachable
static const int wifi_update_cycle = 1;

static int device_cycle = 0;

void setup()
{
  Serial.begin(115200);
  generateSSIDKey();
  generateEncryptedID();
  pinMode(ledpin,OUTPUT);
  digitalWrite(ledpin,HIGH);
  for(int i = 0; i<sizeof(listpin); i++){
    pinMode(listpin[i],OUTPUT);
    digitalWrite(listpin[i],LOW);
  }
  digitalWrite(ledpin,LOW);
}

void generateSSIDKey(){
  int seed = String(WiFi.macAddress()[15]).toInt();
  ssid_key = String(WiFi.macAddress()).substring(seed+2)+WiFi.macAddress();
  ssid_key.replace(":", "");
}

void generateEncryptedID(){
  String id = WiFi.macAddress();
  String hmac_id = MD5_HMAC(WiFi.macAddress(),server_key,server_key);
  encrypted_id = Base64_Encode(XOR_Encrypt(hmac_id+id,server_key));
}

String HTTPGetRequest(String url){
  
  String payload = "";
  
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
    HTTPClient http;  //Declare an object of class HTTPClient

    http.begin(url);  //Specify request destination
    int httpCode = http.GET();//Send the request
 
    if (httpCode > 0) { //Check the returning code
 
      payload = http.getString();   //Get the request response payload
    }
    http.end();   //Close connection
  }
  
  return payload;
}

String MD5_HMAC(String input, String key1, String key2){
  String hash_1 = MD5_Hash(key1+input);
  String hash_2 = MD5_Hash(key2+hash_1);
  return hash_2;
}

String MD5_Hash(String input){
  MD5Builder md5;
  md5.begin();
  md5.add(input);
  md5.calculate();
  return md5.toString();
}

String XOR_Encrypt(String toEncrypt, String key) {
    String output = toEncrypt;
    int key_len = key.length();
    for (int i = 0; i < toEncrypt.length(); i++){
        int key_index = i % key_len;
        if(key[key_index]==toEncrypt[i]){
          output[i] = toEncrypt[i];
        }
        else{
          output[i] = toEncrypt[i] ^ key[key_index];
        }
    }
    return output;
}

String Base64_Encode(String plain){
  char* input = const_cast<char*>(plain.c_str());
  int inputLen = strlen(input);
  int encodedLen = base64_enc_len(inputLen);
  char encoded[encodedLen+1];
  base64_encode(encoded, input, inputLen); 
  
  return encoded;
}

String Base64_Decode (String encoded){
  int inputLen = encoded.length()+1;
  char input [inputLen];
  encoded.toCharArray(input,inputLen);
  int decodedLen = base64_dec_len(input, inputLen);
  char decoded[decodedLen];
  base64_decode(decoded, input, inputLen);
  String result = "";
  
  for(int i = 0; i<sizeof(decoded)/sizeof(char); i++){
    result += decoded[i];
  }
  
  return decoded;
}

void UpdateWiFiInfo(){
  String new_ssid = HTTPGetRequest(server_url+"getDeviceWiFiSSID.php?device_id="+encrypted_id);

  new_ssid = XOR_Encrypt(Base64_Decode(new_ssid), ssid_key);  

  String new_hmac = new_ssid.substring(0,32);
  new_ssid = new_ssid.substring(32);

  String check_hmac = MD5_HMAC(new_ssid,server_key,ssid_key);
  if(new_hmac==check_hmac){
    String new_password = HTTPGetRequest(server_url+"getDeviceWiFiPassword.php?device_id="+encrypted_id);
    new_password = XOR_Encrypt(Base64_Decode(new_password), new_ssid);
    new_hmac = new_password.substring(0,32);
    new_password = new_password.substring(32);
    check_hmac = MD5_HMAC(new_password,server_key,new_ssid);
    if(new_hmac==check_hmac){
      ssid = new_ssid;
      password = new_password;
    }
  }
}

void SearchUnsecuredWiFi(){
  int n = WiFi.scanNetworks();
  
  int i = 0;
   while (i < n) {
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
  int cyclecounter = cycle_timeout;
  if(WiFi.status() != WL_CONNECTED){
    digitalWrite(ledpin,HIGH);
    connectWiFi();
  }
  while(WiFi.status() != WL_CONNECTED && cyclecounter>0){
    SearchUnsecuredWiFi();
    cyclecounter--;
  }
  if(WiFi.status() == WL_CONNECTED){
    device_cycle = (device_cycle++)%wifi_update_cycle;
    if(device_cycle==0){
      UpdateWiFiInfo();
    }
    digitalWrite(ledpin,LOW);
    for(int i = 0; i<sizeof(listpin)/sizeof(int); i++){
      String state = HTTPGetRequest(server_url+"getDeviceState.php?device_id="+encrypted_id+"&device_pin="+listpin[i]);
       if(state.length()>1){
        state = XOR_Encrypt(Base64_Decode(state), WiFi.macAddress());
        String new_hmac = state.substring(0,32);
        state = state.substring(32);
        String check_hmac = MD5_HMAC(state,server_key,WiFi.macAddress());
        if(new_hmac==check_hmac){
          state = state.substring(0,1);
          if(state=="1"){
            digitalWrite(listpin[i],HIGH);
          }
          else if(state=="0"){
            digitalWrite(listpin[i],LOW);
          }
        }
      }
    }
  }
  delay(device_cooldown);
}
