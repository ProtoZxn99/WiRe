<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include "../config/crypto.php";
include "../config/errors.php";
include "../modules/CryptoUtils.php";
 
$conn = mysqli_connect("localhost", "root", "", "wire");

if(mysqli_connect_errno()){
	echo $GLOBALS['error']['db_fail'];
	mysqli_close($conn);
	die();
} 

$device_id = mysqli_real_escape_string($conn, $_GET['device_id']);

$query = mysqli_query($conn, "insert into device (device_id, device_state) values ('".$device_id."',0);");

mysqli_close($conn);

$ecb = new AES_128_ECB($GLOBALS['crypto']['device_aes']);
$device_id = $ecb->encrypt($device_id);

echo '<img src="https://api.qrserver.com/v1/create-qr-code/?data='.$device_id.'&amp;size=100x100" alt="" title="" /><br>';
echo $device_id;
