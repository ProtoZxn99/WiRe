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

$ecb = new AES_128_ECB($server_aes);

echo DiffieHellman_Count($ecb, $account_key,$account_id);

include '_footer.php';

function DiffieHellman_Count($ecb, $pub_client, $account_id){
	$pri_server = RandomInt($diffiehellman_length);
	$pub_server = bcpowmod($diffiehellman_base,$pri_server,$diffiehellman_limit);
	$shared = bcpowmod($pub_client,$pri_server,$diffiehellman_limit);
	Save_Shared($ecb, $shared, $account_id);
	
	return $pub_server;
}

function Save_Shared($ecb, $key, $account_id){
	$query = mysqli_query($conn, "update account set account_key = '".$key."' where account_id = ".$account_id.";");
	$exec = mysqli_fetch_array($query);
}