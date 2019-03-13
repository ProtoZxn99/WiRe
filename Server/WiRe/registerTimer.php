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
$timer_name = mysqli_real_escape_string($conn, $_POST['timer_name']);
$timer_start = mysqli_real_escape_string($conn, $_POST['timer_start']);
$timer_action = mysqli_real_escape_string($conn, $_POST['timer_action']);
$timer_state = mysqli_real_escape_string($conn, $_POST['timer_state']);
$timer_d0 = mysqli_real_escape_string($conn, $_POST['timer_d0']);
$timer_d1 = mysqli_real_escape_string($conn, $_POST['timer_d1']);
$timer_d2 = mysqli_real_escape_string($conn, $_POST['timer_d2']);
$timer_d3 = mysqli_real_escape_string($conn, $_POST['timer_d3']);
$timer_d4 = mysqli_real_escape_string($conn, $_POST['timer_d4']);
$timer_d5 = mysqli_real_escape_string($conn, $_POST['timer_d5']);
$timer_d6 = mysqli_real_escape_string($conn, $_POST['timer_d6']);

ValidateUser($account_id, $account_password);

$query = mysqli_query($conn, "insert into timer (grouping_id, timer_name, timer_start, timer_action, timer_state, timer_d0, timer_d1, timer_d2, timer_d3, timer_d4, timer_d5, timer_d6) values ('".$grouping_id."','".$timer_name."','". $timer_start."',".$timer_action.",".$timer_state.",".$timer_d0.",".$timer_d1.",".$timer_d2.",".$timer_d3.",".$timer_d4.",".$timer_d5.",".$timer_d6.");");

include '_footer.php';