<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'config/crypto.php';
include 'modules/CryptoUtils.php';

$account_email = mysqli_real_escape_string($conn, $_POST['account_email']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);

$query = mysqli_query($conn, "SELECT account_email as email FROM account;");

$ecb = new AES_128_ECB($server_aes);

while($exec = mysqli_fetch_array($query)){

    $real_email = $ecb->triple_decrypt($exec['email']);
    if(hash("sha256",$header_salt.$real_email.$end_salt)==$account_email){
        $unique_salt = substr($more_salt, strlen($real_email)%strlen($more_salt));
        $password = hash("sha256",$header_salt.$unique_salt.$account_password.$real_email.$end_salt);
        $query = mysqli_query($conn, "update account set account_password = '".$ecb->encrypt($password)."' where account_email = '".$exec['email']."';");
        echo "Your password has successfully been changed";
        
        include '_footer.php';
    }
}
echo $GLOBALS['error']["email_fail"];

include '_footer.php';