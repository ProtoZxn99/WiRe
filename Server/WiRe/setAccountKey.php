<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$account_key = mysqli_real_escape_string($conn, $_POST['account_key']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);
$dummy_id = mysqli_real_escape_string($conn, $_POST['dummy_id']);

$account_key = $account_key; //RSA

$base_key = substr(md5($account_key), 0, 16);

$cbc = new AES_128_CBC($base_key, $iv);

$dummy_id = $cbc->decrypt($dummy_id);

if($dummy_id!=$account_id){
    echo $GLOBALS['error']["id_fail"];
    include '_footer.php';
}

ValidateUser($conn, $account_id, $account_password);

//echo "1234567890123456";

echo htmlspecialchars(DiffieHellman_Count($conn, $account_key, $account_id));

include '_footer.php';

function DiffieHellman_Count($conn, $pub_client, $account_id){
		$pri_server = RandomInt($GLOBALS['crypto']['diffiehellman_length']);
		$pub_server = bcpowmod($GLOBALS['crypto']['diffiehellman_base'],$pri_server,$GLOBALS['crypto']['diffiehellman_limit']);
		$shared = bcpowmod($pub_client,$pri_server,$GLOBALS['crypto']['diffiehellman_limit']);
		while(strlen($shared)<16){
			$shared = "0".$shared;
		}
		Save_Shared($conn, $shared, $account_id);
		return $pub_server;
}

function Save_Shared($conn, $key, $account_id){
        $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
        $key = $ecb->encrypt($key);
        $query = mysqli_query($conn, "update account set account_key = '".$key."' where account_id = ".$account_id.";");
}