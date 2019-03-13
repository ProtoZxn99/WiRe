<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$confirmation_email = mysqli_real_escape_string($conn, $_GET['confirmation_email']);

$query = mysqli_query($conn, "SELECT confirmation_email as email, account_pass as pass FROM confirmation;");

while($exec = mysqli_fetch_array($query)){
    if(hash("sha256",$header_salt.$exec['user'].$end_salt)==$confirmation_email){
        $moving = mysqli_query($conn, "insert into account (account_email, account_pass, account_block, account_key, account_time) values ('".$exec["email"]."','".$exec["pass"]."', 0, '".substr(str_pad($exec['email'],16,'w',STR_PAD_LEFT),0,16)."',now());");
        $delete = mysqli_query($conn, "delete from confirmation where confirmation_email = '".$exec["email"]."';");
        echo 1;
        die();
    }
}
echo $GLOBALS['error']['registration_fail'];

include '_footer.php';