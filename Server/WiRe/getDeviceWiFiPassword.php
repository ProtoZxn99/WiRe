<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'modules/CryptoUtils.php';

$device_id = mysqli_real_escape_string($conn, XOR_Encrypt(base64_decode($_GET['device_id']),$xor_mac_key));

$hmac_id = substr($device_id, 0, 32);
$device_id = substr($device_id, 32);

if(MD5_HMAC($device_id, $xor_mac_key, $xor_mac_key)!=$hmac_id){
    echo -1;
    include '_footer.php';
}

$query = mysqli_query($conn, "select a.account_wifi_ssid as ssid, a.account_wifi_password as password from account a, device d where a.account_id = d.account_id and d.device_id = '".$device_id."-0' limit 1;");
$exec = mysqli_fetch_array($query);

$password = base64_encode(XOR_Encrypt(MD5_HMAC($exec['password'], $xor_mac_key, $exec['ssid']).$exec['password'], $exec['ssid']));

echo $password;

include '_footer.php';