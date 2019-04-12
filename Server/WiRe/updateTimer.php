<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$timer_id = mysqli_real_escape_string($conn, $_POST['timer_id']);
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$timer_name = mysqli_real_escape_string($conn, $_POST['timer_name']);
$timer_state = mysqli_real_escape_string($conn, $_POST['timer_state']);
$timer_start = mysqli_real_escape_string($conn, $_POST['timer_start']);
$timer_action = mysqli_real_escape_string($conn, $_POST['timer_action']);
$timer_d0 = mysqli_real_escape_string($conn, $_POST['timer_d0']);
$timer_d1 = mysqli_real_escape_string($conn, $_POST['timer_d1']);
$timer_d2 = mysqli_real_escape_string($conn, $_POST['timer_d2']);
$timer_d3 = mysqli_real_escape_string($conn, $_POST['timer_d3']);
$timer_d4 = mysqli_real_escape_string($conn, $_POST['timer_d4']);
$timer_d5 = mysqli_real_escape_string($conn, $_POST['timer_d5']);
$timer_d6 = mysqli_real_escape_string($conn, $_POST['timer_d6']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$timer_id = $cbc->decrypt($timer_id);
$grouping_id = $cbc->decrypt($grouping_id);
$timer_name = $cbc->decrypt($timer_name);
$timer_start = $cbc->decrypt($timer_start);
$timer_action = $cbc->decrypt($timer_action);
$timer_state = $cbc->decrypt($timer_state);
$timer_d0 = $cbc->decrypt($timer_d0);
$timer_d1 = $cbc->decrypt($timer_d1);
$timer_d2 = $cbc->decrypt($timer_d2);
$timer_d3 = $cbc->decrypt($timer_d3);
$timer_d4 = $cbc->decrypt($timer_d4);
$timer_d5 = $cbc->decrypt($timer_d5);
$timer_d6 = $cbc->decrypt($timer_d6);

$query = mysqli_query($conn, "update timer set grouping_id = ".$grouping_id.", timer_name = '".$timer_name."', timer_start = '".$timer_start."', timer_action = ".$timer_action.", timer_state = '".$timer_state."', timer_d0 = ".$timer_d0.", timer_d1 = ".$timer_d1.", timer_d2 = ".$timer_d2.", timer_d3 = ".$timer_d3.", timer_d4 = ".$timer_d4.", timer_d5 = ".$timer_d5.", timer_d6 = ".$timer_d6." where timer_id = ".$timer_id.";");

echo 1;

include '_footer.php';