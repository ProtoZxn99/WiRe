<?php
include '_header.php';
include 'modules/CryptoUtils.php';

$device_id = mysqli_real_escape_string($conn, XOR_Encrypt(base64_decode($_GET['device_id']),$xor_mac_key));
$device_pin = mysqli_real_escape_string($conn, $_GET['device_pin']);

$hmac_id = substr($device_id, 0, 32);
$device_id = substr($device_id, 32);
//
//echo($hmac_id."<br>".MD5_HMAC($device_id, $xor_mac_key, $xor_mac_key)."<br>");
if(MD5_HMAC($device_id, $xor_mac_key, $xor_mac_key)!==$hmac_id){
    echo $hmac_id;
    include '_footer.php';
}

//echo $device_id;

$query = mysqli_query($conn, "SELECT device_state as state FROM device where device_id = '".$device_id."-".$device_pin."' limit 1;");
$exec = mysqli_fetch_array($query);

if(!is_null($exec['state'])){
    $state = $exec['state'];
    $state = base64_encode(XOR_Encrypt(MD5_HMAC($state, $device_id, $device_id).$state,$device_id));
    echo $state;
}
else{
    echo 0;
}

function RandomString($length)
{
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $randstring = '';
    for ($i = 0; $i < $length; $i++) {
        $randstring .= $characters{rand(0, strlen($characters)-1)};
    }
    return $randstring;
}

include '_footer.php';