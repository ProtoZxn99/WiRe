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

ValidateUser($account_id, $account_password);

$query = mysqli_query($conn, "delete from timer where grouping_id = ".$grouping_id.";");
$query2 = mysqli_query($conn, "delete from authority where grouping_id = ".$grouping_id.";");
$query3 = mysqli_query($conn, "delete from member where grouping_id = ".$grouping_id.";");
$query4 = mysqli_query($conn, "delete from grouping where grouping_id = ".$grouping_id.";");

include '_footer.php';