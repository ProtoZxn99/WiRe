<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$device_id = mysqli_real_escape_string($conn, $_POST['device_id']);

ValidateUser($conn, $account_id, $account_password);

$query = mysqli_query($conn, "update into device set account_id = ".$account_id." where device_id like '".$device_id."-%';");

echo 1;

include '_footer.php';