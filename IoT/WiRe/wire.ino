#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include "Base64.h"
#include <MD5.h>

// Replace these with your WiFi network settings
static String ssid = "network"; //replace this with your WiFi network name
static String password = "password"; //replace this with your WiFi network password

//Server link
static const String server_url = "http://192.168.0.25/wire/";

//Key variables for encryption
static const String server_key = "ConcealM4CtoHackers"; 
static String encrypted_id;
static String ssid_key;

//Arduino Pins
static uint8_t ledpin = D0; //Led Pin
static uint8_t listpin [] = {D1,D2,D3,D4,D5,D6,D7,D8}; //Relay pins

static const int device_cooldown = 1000; //cooldown of each device cycle in milliseconds
static const int cycle_timeout = 10; //Number of cycles before a task is seen as a failed task

static const int wifi_cooldown = 1000; //cooldown of each wifi cycle check in milliseconds
static const int wifi_timeout = 10; //number of max loops of wifi check allowed before WiFi deemed unreachable
static const int wifi_update_cycle = 1; //number of device cycles in every wifi update cycles

static int device_cycle = 0;

void setup()
{
  generateSSIDKey(); //Generates an encryption key
  generateEncryptedID(); //Generatess an encrypted MAC Address
  pinMode(ledpin,OUTPUT); //Declare LED pin as output
  digitalWrite(ledpin,HIGH); //Turns on the LED

  //Declares and turns off every relay pins
  for(int i = 0; i<sizeof(listpin); i++){
    pinMode(listpin[i],OUTPUT);
    digitalWrite(listpin[i],LOW);
  }

  //Turns off the LED
  digitalWrite(ledpin,LOW);
}

//Generates an encryption key made of altered MAC Address
void generateSSIDKey(){
  int seed = String(WiFi.macAddress()[15]).toInt(); //Declares seed from the 15th character of the MAC address, if the address is not a number, it'll be 0
  ssid_key = String(WiFi.macAddress()).substring(seed+2)+WiFi.macAddress(); //Appends a part of the MAC address to itself to be a new key
  ssid_key.replace(":", ""); //Removes colon from string
}

//Encrypts the MAC Address of the device, and store it in a global variable
void generateEncryptedID(){
  String id = WiFi.macAddress();
  String hmac_id = MD5_HMAC(WiFi.macAddress(),server_key,server_key); //Generates HMAC for the device's MAC Address
  encrypted_id = Base64_Encode(XOR_Encrypt(hmac_id+WiFi.macAddress(),server_key)); //Generates a formatted MAC Address to be used in communication with web service.
}

////GAK PENTING, tapi karena sudah aku komentari, buat kamu baca aja, Function to get a result from web service using GET method
String HTTPGetRequest(String url){
  
  String payload = "";//Default result
  
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
    HTTPClient http;  //Declare an object of class HTTPClient

    http.begin(url);  //Specify request destination
    int httpCode = http.GET();//Send the request
 
    if (httpCode > 0) { //Check the returning code
 
      payload = http.getString();   //Get the request response payload
    }
    http.end();   //Close connection
  }
  
  return payload; //Returns result
}


//Generates a HMAC of a message using MD5 of 2 keys and the message itself
String MD5_HMAC(String input, String key1, String key2){
  String hash_1 = MD5_Hash(key1+input);
  String hash_2 = MD5_Hash(key2+hash_1);
  return hash_2;
}

//GAK PENTING
String MD5_Hash(String input){
  MD5Builder md5;
  md5.begin();
  md5.add(input);
  md5.calculate();
  return md5.toString();
}

//GAK PENTING
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

//GAK PENTING
String Base64_Encode(String plain){
  char* input = const_cast<char*>(plain.c_str());
  int inputLen = strlen(input);
  int encodedLen = base64_enc_len(inputLen);
  char encoded[encodedLen+1];
  base64_encode(encoded, input, inputLen); 
  
  return encoded;
}

//GAK PENTING
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

//Updates the WiFi data in Arduino to match with the server's
void UpdateWiFiInfo(){
  String new_ssid = HTTPGetRequest(server_url+"getDeviceWiFiSSID.php?device_id="+encrypted_id); //Asks the webservice for the encrypted newest SSID
  new_ssid = XOR_Encrypt(Base64_Decode(new_ssid), ssid_key);  //Decrypts the message
  if(new_ssid.length()>32){//Checks if the length of the message is more than 32, because the message will be have its first 32 characters cut from it
    String new_hmac = new_ssid.substring(0,32); //Gets the HMAC of the message, which is appended to the first 32 characters of the message
    new_ssid = new_ssid.substring(32); //Replaces the message with itself without its HMAC
    String check_hmac = MD5_HMAC(new_ssid,server_key,ssid_key); //Generates the HMAC of the message received
  
    if(new_hmac==check_hmac){//Checks if the generated HMAC is the same with the HMAC from the server
      String new_password = HTTPGetRequest(server_url+"getDeviceWiFiPassword.php?device_id="+encrypted_id); //Asks the webservice for the encrypted newest WiFi password
      new_password = XOR_Encrypt(Base64_Decode(new_password), new_ssid); //Decrypts the message
      if(new_password.length()>32){ //Checks if the length of the message is more than 32, because the message will be have its first 32 characters cut from it
        new_hmac = new_password.substring(0,32); //Gets the HMAC of the message, which is appended to the first 32 characters of the message
        new_password = new_password.substring(32); //Replaces the message with itself without its HMAC
        check_hmac = MD5_HMAC(new_password,server_key,new_ssid); //Generates the HMAC of the message received
        if(new_hmac==check_hmac){ //Checks if the generated HMAC is the same with the HMAC from the server
          //Replaces the SSID and password of the WiFi that is currently saved in Arduino
          ssid = new_ssid;
          password = new_password;
        }
      }
    }
  }
}

//Gak penting
void SearchUnsecuredWiFi(){
  int n = WiFi.scanNetworks();
  
  int i = 0;
   while (i < n) {
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

//Gak penting
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
  int cyclecounter = cycle_timeout; //Redeclares the timeout limit locally
  if(WiFi.status() != WL_CONNECTED){ //Checks if the Arduino is connected to a WiFi
    digitalWrite(ledpin,HIGH); //Turns on the LED
    connectWiFi(); //Connects Arduino to the currently saved WiFi in the Arduino itsekf
  }
  while(WiFi.status() != WL_CONNECTED && cyclecounter>0){ //Checks if the Arduino is connected to a WiFi and the repetition doesn't occur more than declared
    SearchUnsecuredWiFi(); //Connects Arduino to WiFis without password
    cyclecounter--;
  }
  if(WiFi.status() == WL_CONNECTED){ //Checks if Arduino is connected to a WiFi
    device_cycle = (device_cycle++)%wifi_update_cycle; //Updates the device_cycle counter
    if(device_cycle==0){ //Checks if it's time to also update WiFi's data in Arduino
      UpdateWiFiInfo(); //Updates WiFi's data
    }
    digitalWrite(ledpin,LOW); //Turn off the LED
    
    //Checks every pin's state in the server
    for(int i = 0; i<sizeof(listpin); i++){
      String state = HTTPGetRequest(server_url+"getDeviceState.php?device_id="+encrypted_id+"&device_pin="+listpin[i]); //Asks the webservice about a pin's state
      state = XOR_Encrypt(Base64_Decode(state), WiFi.macAddress()); //Decrypts the message from the server
      if(state.length()>32){ //Checks if the length of the message is more than 32, because the message will be have its first 32 characters cut from it
        String new_hmac = state.substring(0,32);//Gets the HMAC of the message, which is appended to the first 32 characters of the message
        state = state.substring(32); //Replaces the message with itself without its HMAC
        String check_hmac = MD5_HMAC(state,server_key,WiFi.macAddress()); //Generates a HMAC of the message
        if(new_hmac==check_hmac){ //Checks if the HMAC of the message is the same with the one locally generated
          state = state.substring(0,1); //Substrings the first character of the message
          if(state=="1"){ //Checks if the message is equals to "1"
            digitalWrite(listpin[i],HIGH); //Turns on the said pin relay
          }
          else if(state=="0"){ //Checks if the message is equals to "0"
            digitalWrite(listpin[i],LOW); //Turns off the said pin relay
          }
        }
      }
    }
  }
  delay(device_cooldown); //Delays the next operation by the declared miliseconds
}
