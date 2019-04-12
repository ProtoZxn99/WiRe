<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

//Input from Android, a hashed email and an encrypted password
$account_email = mysqli_real_escape_string($conn, $_POST['account_email']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);

//Get encrypted email data from account table in database
$query = mysqli_query($conn, "SELECT account_email as email FROM account;");

//Calls AES ECB module
$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

//Searching for the email which is then hashed and compared with the input given
while($exec = mysqli_fetch_array($query)){

    //Decrpyting the email data from database
    $real_email = $ecb->decrypt($exec['email']);
    
    //Compares the hashed email and the input parameter
    if(hash("sha256",$GLOBALS['crypto']['header_salt'].$real_email.$GLOBALS['crypto']['end_salt'])==$account_email){
        
        //Generate a unique salt for the new password
        $unique_salt = substr($GLOBALS['crypto']['more_salt'], strlen($real_email)%strlen($GLOBALS['crypto']['more_salt']));
        
        //Generate a hashed password
        $password = hash("sha256",$GLOBALS['crypto']['header_salt'].$unique_salt.$account_password.$real_email.$GLOBALS['crypto']['end_salt']);
        
        //Updates old password with new password
        $query = mysqli_query($conn, "update account set account_password = '".$password."' where account_email = '".$exec['email']."';");
        
        //Returns success message
        echo "Your password has successfully been changed";
        
        include '_footer.php';
    }
}

//Returns failure message
echo $GLOBALS['error']["email_fail"];

include '_footer.php';