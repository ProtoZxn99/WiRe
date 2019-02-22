<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include 'config/conn.php';

$device_id = mysqli_real_escape_string($conn, $_GET['device_id']);

$query = mysqli_query($conn, "insert into device (device_id, device_state) values ('".$device_id."',0);");
$exec = mysqli_fetch_array($query);

include 'config/footer.php';