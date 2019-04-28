<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include "../config/crypto.php";
include "../config/errors.php";
include "../modules/cryptoutils.php";

$device_id = $_GET['device_id'];

$ecb = new AES_128_ECB($GLOBALS['crypto']['device_aes']);
$device_id = $ecb->encrypt($device_id);

echo '<img src="https://api.qrserver.com/v1/create-qr-code/?data='.$device_id.'&amp;size=100x100" alt="" title="" /><br>';
echo $device_id;