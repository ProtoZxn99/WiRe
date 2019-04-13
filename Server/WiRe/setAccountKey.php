<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$account_key = mysqli_real_escape_string($conn, $_POST['account_key']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);
$dummy_id = mysqli_real_escape_string($conn, $_POST['dummy_id']);

ValidateUser($conn, $account_id, $account_password);

$base_key = substr(md5($account_id), 0, 16);

$cbc = new AES_128_CBC($base_key, $iv);

$dummy_id = $cbc->decrypt($dummy_id);

if(dummy_id!=account_id){
    echo $GLOBALS['error']["id_fail"];
    include '_footer.php';
}

echo DiffieHellman_Count($account_key, $account_id);

include '_footer.php';

function DiffieHellman_Count($pub_client, $account_id){
	$pri_server = RandomInt($diffiehellman_length);
	$pub_server = bcpowmod($diffiehellman_base,$pri_server,$diffiehellman_limit);
	$shared = bcpowmod($pub_client,$pri_server,$diffiehellman_limit);
        $shared = substr($shared, 0, 16);
	Save_Shared($shared, $account_id);
	
	return $pub_server;
}

function Save_Shared($key, $account_id){
        $ecb = new AES_128_ECB($GLOBALS['server']['server_aes']);
        $key = $ecb->encrypt($key);
	$query = mysqli_query($conn, "update account set account_key = '".$key."' where account_id = ".$account_id.";");
	$exec = mysqli_fetch_array($query);
}