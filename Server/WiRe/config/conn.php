<?php
include "config/crypto.php";
include "config/errors.php";
include "config/traffic.php";
include 'modules/CryptoUtils.php';

//checkIP();

$ip_wire = "127.0.0.1";
$conn = mysqli_connect($ip_wire, "root", "", "wire");

if(mysqli_connect_errno()){
        echo $GLOBALS['error']['db_fail'];
	include '_footer.php';
} 
    
function ValidateUser($conn, $account_id, $account_password){
    
    $query = mysqli_query($conn, "SELECT account_block as authority FROM account where account_id = '".$account_id."' and account_password = '".$account_password."';");
    $exec = mysqli_fetch_array($query);
    if(isset($exec['authority'])){
        if($exec['authority']>0){
            echo $GLOBALS['error']["id_block"];
            include '_footer.php';
        }
        else{
            $query_key = mysqli_query($conn, "SELECT account_key as chat_key FROM account where account_id = ".$account_id.";");
            $exec_key = mysqli_fetch_array($query_key);
            
            if(strlen($exec_key['chat_key'])>0){
                $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
                return $ecb->decrypt($exec_key['chat_key']);
            }
            else{
                echo $GLOBALS['error']["id_fail"];
                include '_footer.php';
            }
        }
    }
    else{
        echo $GLOBALS['error']['wrong_format'];
        include '_footer.php';
    }
//    linkAccount($id);
}
