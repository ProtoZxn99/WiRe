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

$check = mysqli_query($conn, "select count(*) as count from grouping where account_id = ".$account_id." and grouping_id = ".$grouping_id.";");
$result = mysqli_fetch_array($check);
if($result['count']<1){
    $query = mysqli_query($conn, "insert into authority (account_id, grouping_id) values (".$account_id.",".$grouping_id.");");
	echo 1;
}
else{
    echo $GLOBALS['error']["group_duplicate"];
}
include '_footer.php';