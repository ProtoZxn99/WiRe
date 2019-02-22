<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$account_key = mysqli_real_escape_string($conn, $_POST['account_key']);

ValidateUser($account_id, $account_password);

$query = mysqli_query($conn, "update account set account_key = '".$account_key."' where account_id = ".$account_id.";");
$exec = mysqli_fetch_array($query);

echo $exec['ssid'];

include '_footer.php';