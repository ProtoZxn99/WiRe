<?php
include "config/traffic.php";
//$ip = getIP();
//checkIP($ip);

$ip_wire = "127.0.0.1";
$conn = mysqli_connect("localhost", "root", "", "wire");

function ValidateUser($id, $pass){
    $query = mysqli_query($conn, "SELECT account_block as authority FROM account where account_id = '".$account_id."' and account_pass = '".$account_pass."';");
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
//    scheckAccount($ip, $id);
}