<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'modules/CryptoUtils';

$device_id = mysqli_real_escape_string($conn, $_GET['device_id']);

$query = mysqli_query($conn, "select a.account_wifi_ssid as ssid from account a, device d where a.account_id = d.account_id and d.device_id = '".$device_id."' limit 1;");
$exec = mysqli_fetch_array($query);

$mac = substr($device_id, 0, strpos($device_id, '-'));
$ssid = base64_encode(XOR_Encrypt($exec['ssid'], $mac));

echo $ssid;

include '_footer.php';