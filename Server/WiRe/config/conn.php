<?php
include "config/traffic.php";
include "config/errors.php";

//$ip = getIP();
//checkIP($ip);

$ip_wire = "127.0.0.1";
$conn = mysqli_connect("localhost", "root", "", "wire");
if(mysqli_connect_errno()){
        echo $GLOBALS['error']['db_fail'];
		include '_footer.php';
    } 
function ValidateUser($id, $pass){
    
    include 'config/crypto.php';
    include 'modules/CryptoUtils.php';
        
    $ecb = new AES_128_ECB($server_aes);
    
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
//    checkAccount($ip, $id);
}