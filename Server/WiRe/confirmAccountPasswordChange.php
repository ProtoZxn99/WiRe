<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';

$account_email = mysqli_real_escape_string($conn, $_POST['account_email']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);

$query = mysqli_query($conn, "SELECT account_email as email FROM account;");

while($exec = mysqli_fetch_array($query)){
    if(hash("sha256",$header_salt.$exec['email'].$end_salt)==$account_email){
        $unique_salt = substr($more_salt, strlen($email)%strlen($more_salt));
        $query = mysqli_query($conn, "update account set account_password = '".hash("sha256",$header_salt.$unique_salt.$account_password.$account_email.$end_salt)."' where account_email = '".$exec['user']."';");
        echo "Your password has successfully been changed";
        
        include 'config/footer.php';
    }
}
echo $GLOBALS['error']["email_fail"];

include '_footer.php';