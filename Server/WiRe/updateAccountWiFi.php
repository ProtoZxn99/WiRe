<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$account_wifi_ssid = mysqli_real_escape_string($conn, $_POST['account_wifi_ssid']);
$account_wifi_password = mysqli_real_escape_string($conn, $_POST['account_wifi_password']);

ValidateUser($account_id, $account_password);

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

$query = mysqli_query($conn, "update into account set account_wifi_ssid = '".$ecb->encrypt($account_wifi_ssid)."', account_wifi_password = '".$ecb->encrypt($account_wifi_password)."' where account_id = ".$account_id.";");
$exec = mysqli_fetch_array($query);

echo 1;

include '_footer.php';