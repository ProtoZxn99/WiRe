<?php
include '_header.php';
include 'modules/CryptoUtils.php';

$device_id = mysqli_real_escape_string($conn, XOR_Encrypt(base64_decode($_GET['device_id']),$xor_mac_key));
$device_pin = mysqli_real_escape_string($conn, $_GET['device_pin']);

$device_id = $device_id."-".$device_pin;

$query = mysqli_query($conn, "SELECT device_state as state FROM device where device_id = '".$device_id."' limit 1;");
$exec = mysqli_fetch_array($query);

if(!is_null($exec['state'])){
    echo $exec['state'];
}
else{
    echo 0;
}

include '_footer.php';