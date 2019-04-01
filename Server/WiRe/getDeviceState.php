<?php
include '_header.php';
include 'modules/CryptoUtils.php';

$device_id = mysqli_real_escape_string($conn, XOR_Encrypt(base64_decode($_GET['device_id']),$GLOBALS['crypto']['xor_mac_key']));
$device_pin = mysqli_real_escape_string($conn, $_GET['device_pin']);

$hmac_id = substr($device_id, 0, 32);
$device_id = substr($device_id, 32);

if(MD5_HMAC($device_id, $GLOBALS['crypto']['xor_mac_key'], $GLOBALS['crypto']['xor_mac_key'])!==$hmac_id){
    echo $hmac_id;
    include '_footer.php';
}

$query = mysqli_query($conn, "SELECT device_state as state FROM device where device_id = '".$device_id."-".$device_pin."' limit 1;");
$exec = mysqli_fetch_array($query);

if(!is_null($exec['state'])){
    $state = $exec['state']. RandomString(5);
    $state = base64_encode(XOR_Encrypt(MD5_HMAC($state, $GLOBALS['crypto']['xor_mac_key'], $device_id).$state,$device_id));
    echo $state;
}
else{
    echo -1;
}

include '_footer.php';