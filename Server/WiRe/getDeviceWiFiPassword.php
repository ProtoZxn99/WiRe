<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$device_id = mysqli_real_escape_string($conn, XOR_Encrypt(base64_decode($_GET['device_id']),$GLOBALS['crypto']['xor_mac_key']));

$hmac_id = substr($device_id, 0, 32);
$device_id = substr($device_id, 32);

if(MD5_HMAC($device_id, $GLOBALS['crypto']['xor_mac_key'], $GLOBALS['crypto']['xor_mac_key'])!=$hmac_id){
    echo -1;
    include '_footer.php';
}

$query = mysqli_query($conn, "select a.account_wifi_ssid as ssid, a.account_wifi_password as password from account a, device d where a.account_id = d.account_id and d.device_id = '".$device_id."-0' limit 1;");
$exec = mysqli_fetch_array($query);

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

$ssid = $ecb->decrypt($exec['ssid']);
$password = $ecb->decrypt($exec['password']);
$epassword = base64_encode(XOR_Encrypt(MD5_HMAC($password, $GLOBALS['crypto']['xor_mac_key'], $ssid).$password, $ssid));

echo $epassword;

include '_footer.php';