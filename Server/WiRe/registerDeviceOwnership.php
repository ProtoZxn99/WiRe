<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$device_id = mysqli_real_escape_string($conn, $_POST['device_id']);
$device_name = mysqli_real_escape_string($conn, $_POST['device_name']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$device_id = $cbc->decrypt($device_id);
$device_name = $cbc->decrypt($device_name);

$ecb = new AES_128_ECB($GLOBALS['crypto']['device_aes']);
echo $device_id;die();
$device_id = $ecb->decrypt($device_id);

$query_check = mysqli_query($conn, "select count(*) as c from device where device_id = '".$device_id."' and account_id is NULL;");
$c = mysqli_fetch_array($query_check);
if($c['c']>0){
	$query = mysqli_query($conn, "update device set account_id = '".$account_id."', device_name = '".$device_name."' where device_id = '".$device_id."';");
	echo $query;
}
else{
	echo $GLOBALS['error']["device_duplicate"];
}
include '_footer.php';
