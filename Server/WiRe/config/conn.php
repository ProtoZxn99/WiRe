<?php
include "config/crypto.php";
include "config/errors.php";
include "config/traffic.php";
include 'modules/CryptoUtils.php';

//checkIP();

$ip_wire = "127.0.0.1";
$conn = mysqli_connect("localhost", "root", "", "wire");
if(mysqli_connect_errno()){
        echo $GLOBALS['error']['db_fail'];
		include '_footer.php';
    } 
function ValidateUser($id, $pass){
    
    $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
    
    $query = mysqli_query($conn, "SELECT account_block as authority FROM account where account_id = '".$account_id."' and account_pass = '".$ecb->encrypt($account_pass)."';");
    $exec = mysqli_fetch_array($query);

    if(!is_null($exec['authority'])){
        if($exec['authority']>0){
            echo $GLOBALS['error']["id_block"];
            include '_footer.php';
        }
    }
    else{
        echo $GLOBALS['error']['wrong_format'];
        include '_footer.php';
    }
//    linkAccount($id);
}