<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'modules/EmailUtils.php';
include 'modules/FormatUtils.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$query = mysqli_query($conn, "update account set account_use = 0 where account_id = ".$account_id.";");

include '_footer.php';