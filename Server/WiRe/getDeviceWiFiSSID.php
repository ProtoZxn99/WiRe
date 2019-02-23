<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'modules/CryptoUtils.php';

$device_id = mysqli_real_escape_string($conn, XOR_Encrypt(base64_decode($_GET['device_id']),$xor_mac_key));

$query = mysqli_query($conn, "select a.account_wifi_ssid as ssid from account a, device d where a.account_id = d.account_id and d.device_id = '".$device_id."' limit 1;");
$exec = mysqli_fetch_array($query);

$mac = substr($device_id, 0, strpos($device_id, '-'));
$ssid_key = generateSSIDKey($mac);
$ssid = base64_encode(XOR_Encrypt($exec['ssid'], $ssid_key));
echo $ssid;

include '_footer.php';

function generateSSIDKey($mac){
    $key = "0";
    if(!is_null($mac) && strlen($mac)>16){
        $seed = $mac[15];
        $key = "";
        if(!is_numeric($seed)){
            $seed = 0;
        }
        $key .= substr($mac,$seed+2);
        $key .= $mac;
        $key = str_replace(":", "", $key);
    }
    return $key;
}