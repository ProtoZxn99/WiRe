<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_name = mysqli_real_escape_string($conn, $_POST['grouping_name']);

ValidateUser($account_id, $account_password);

$query = mysqli_query($conn, "insert into grouping (account_id,grouping_name) values (".$account_id.",'".$grouping_name."')");

include '_footer.php';