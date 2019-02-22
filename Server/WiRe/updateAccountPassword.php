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

$query = mysqli_query($conn, "update into account set account_password = '".$account_password."' where account_id = ".$account_id.";");
$exec = mysqli_fetch_array($query);

include '_footer.php';