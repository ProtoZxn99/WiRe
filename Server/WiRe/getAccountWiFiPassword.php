<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);

ValidateUser($account_id, $account_password);

$query = mysqli_query($conn, "select account_wifi_password as password from account where account_id = ".$account_id." limit 1;");
$exec = mysqli_fetch_array($query);

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

echo $ecb->decrypt($exec['password']);

include '_footer.php';