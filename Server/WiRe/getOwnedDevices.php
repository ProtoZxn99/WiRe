<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$query = mysqli_query($conn, "select device_id as id, device_name as name, device_state as state from device where account_id = ".$account_id.";");
$exec = mysqli_fetch_array($query);

echo json_encode($exec);

include '_footer.php';