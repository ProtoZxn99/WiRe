<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'modules/CryptoUtils.php';

$confirmation_email = mysqli_real_escape_string($conn, $_GET['confirmation_email']);

$query = mysqli_query($conn, "SELECT confirmation_email as email, account_pass as pass FROM confirmation;");

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

while($exec = mysqli_fetch_array($query)){
    $real_email = $ecb->decrypt($exec['email']);
    if(hash("sha256",$GLOBALS['crypto']['header_salt'].$real_email.$GLOBALS['crypto']['end_salt'])==$confirmation_email){
        $moving = mysqli_query($conn, "insert into account (account_email, account_pass, account_block, account_key, account_time) values ('".$exec["email"]."','".$exec["pass"]."', 0, '',now());");
        $delete = mysqli_query($conn, "delete from confirmation where confirmation_email = '".$exec["email"]."';");
        echo 1;
        include '_footer.php';
    }
}
echo $GLOBALS['error']['registration_fail'];

include '_footer.php';