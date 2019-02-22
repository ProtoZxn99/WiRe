<?php
$ip_wire = "127.0.0.1";
$conn = mysqli_connect("localhost", "root", "", "wire");

function ValidateUser($id, $pass){
    $query = mysqli_query($conn, "SELECT account_block as authority FROM account where account_id = '".$account_id."' and account_pass = '".$account_pass."';");
    $exec = mysqli_fetch_array($query);

    if(!is_null($exec['authority'])){
        if($exec['authority']>0){
            echo $error["id_block"];
            die();
        }
    }
    else{
        echo $error['wrong_format'];
        die();
    }
}