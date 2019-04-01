<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$grouping_name = mysqli_real_escape_string($conn, $_POST['grouping_name']);

ValidateUser($conn, $account_id, $account_password);

$query = mysqli_query($conn, "update grouping set grouping_name = '".$grouping_name."' where grouping_id = ".$grouping_id.";");

echo 1;

include '_footer.php';