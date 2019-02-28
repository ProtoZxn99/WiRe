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

$query = mysqli_query($conn, "delete from authority where account_id = ".$account_id." and grouping_id = ".$grouping_id.";");

include '_footer.php';