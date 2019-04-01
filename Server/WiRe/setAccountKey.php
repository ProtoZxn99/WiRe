<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include '_header.php';
include 'config/crypto.php';
include 'modules/CryptoUtils.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$account_key = mysqli_real_escape_string($conn, $_POST['account_key']);

ValidateUser($account_id, $account_password);

echo DiffieHellman_Count($account_key, $account_id);

include '_footer.php';

function DiffieHellman_Count($pub_client, $account_id){
	$pri_server = RandomInt($diffiehellman_length);
	$pub_server = bcpowmod($diffiehellman_base,$pri_server,$diffiehellman_limit);
	$shared = bcpowmod($pub_client,$pri_server,$diffiehellman_limit);
	Save_Shared($shared, $account_id);
	
	return $pub_server;
}

function Save_Shared($key, $account_id){
        $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
        $key = substr(base64_encode($key), 0, 16);
        $ekey = $ecb->encrypt($key);
	$query = mysqli_query($conn, "update account set account_key = '".$key."' where account_id = ".$account_id.";");
	$exec = mysqli_fetch_array($query);
}