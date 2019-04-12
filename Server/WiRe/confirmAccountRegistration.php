<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'modules/CryptoUtils.php';

//Input from Android, a hashed email
$confirmation_email = mysqli_real_escape_string($conn, $_GET['confirmation_email']);

//Gets data from confirmation table
$query = mysqli_query($conn, "SELECT confirmation_email as email, account_pass as pass FROM confirmation;");

//Calls AES ECB module
$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

//Searching for the email which is then hashed and compared with the input given
while($exec = mysqli_fetch_array($query)){
    
    //Decrpyting the email data from database
    $real_email = $ecb->decrypt($exec['email']);
    
    //Compares the hashed email and the input parameter
    if(hash("sha256",$GLOBALS['crypto']['header_salt'].$real_email.$GLOBALS['crypto']['end_salt'])==$confirmation_email){
        
        //Move the data from confirmation table to account table
        $moving = mysqli_query($conn, "insert into account (account_email, account_pass, account_block, account_key, account_time) values ('".$exec["email"]."','".$exec["pass"]."', 0, '',now());");
        
        //Deletes the moved record 
        $delete = mysqli_query($conn, "delete from confirmation where confirmation_email = '".$exec["email"]."';");
        echo 1;
        include '_footer.php';
    }
}
echo $GLOBALS['error']['registration_fail'];

include '_footer.php';