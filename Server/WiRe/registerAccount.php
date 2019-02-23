<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'modules/EmailUtils.php';
include 'modules/FormatUtils.php';

$account_email = mysqli_real_escape_string($conn, $_POST['account_email']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);

if(checkEmailFormat($account_email)){
    if(isUniqueUser($account_email)){
        $query = mysqli_query($conn, "insert into confirmation (confirmation_email, account_password) values ('".$account_email."','".$account_password."');");
        $msg = "<p align='center'>Please click this link to confirm your registration at WiRe: </p><br>";
        $url = "http://".$ip_wire."/WiRe/confirmAccountRegistration.php?confirmation_user=".hash("sha256",$header_salt.$account_email.$end_salt);
        $img = "<img src = 'https://i.ytimg.com/vi/V015SjjbYXE/maxresdefault.jpg'>";
        sendeMail("wire@noreply.com", $account_email, "Confirming WiRe account registration", $msg."<a href ='".$url."'>".$img."</a>");
        echo 1;
    }
    else{
        echo $GLOBALS['error']['email_not_unique'];
    }
}
else{
    echo $GLOBALS['error']['wrong_format'];
}

include '_footer.php';

function isUniqueUser($user) {
    include 'config/conn.php';
    
    $query = mysqli_query($conn, "SELECT count(*) as users FROM account where account_email = '".$user."';");
    $exec = mysqli_fetch_array($query);
    
    $total = $exec['users'];
    
    $query = mysqli_query($conn, "SELECT count(*) as users FROM confirmation where confirmation_email = '".$user."';");
    $exec = mysqli_fetch_array($query);
    
    $total += $exec['users'];
    
    if($total<1){
        return true;
    }
    else{
        return false;
    }
}



