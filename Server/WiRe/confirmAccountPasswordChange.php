<?php
include '_header.php';

$account_email = mysqli_real_escape_string($conn, $_POST['account_email']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);

$query = mysqli_query($conn, "SELECT account_email as email FROM account;");

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

while($exec = mysqli_fetch_array($query)){

    $real_email = $ecb->decrypt($exec['email']);
    
    if(hash("sha256",$GLOBALS['crypto']['header_salt'].$real_email.$GLOBALS['crypto']['end_salt'])==$account_email){
        $unique_salt = substr($GLOBALS['crypto']['more_salt'], strlen($real_email)%strlen($GLOBALS['crypto']['more_salt']));
        $password = hash("sha256",$GLOBALS['crypto']['header_salt'].$unique_salt.$account_password.$real_email.$GLOBALS['crypto']['end_salt']);
        $query = mysqli_query($conn, "update account set account_password = '".$password."' where account_email = '".$exec['email']."';");
        
        echo "Your password has successfully been changed";
        include '_footer.php';
    }
}

echo $GLOBALS['error']["email_fail"];
include '_footer.php';