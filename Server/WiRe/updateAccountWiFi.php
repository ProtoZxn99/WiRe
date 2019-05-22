<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$account_wifi_ssid = mysqli_real_escape_string($conn, $_POST['account_wifi_ssid']);
$account_wifi_password = mysqli_real_escape_string($conn, $_POST['account_wifi_password']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$account_wifi_ssid = $cbc->decrypt($account_wifi_ssid);
$account_wifi_password = $cbc->decrypt($account_wifi_password);

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

$query = mysqli_query($conn, "update account set account_wifi_ssid = '".$ecb->encrypt($account_wifi_ssid)."', account_wifi_password = '".$ecb->encrypt($account_wifi_password)."' where account_id = ".$account_id.";");

echo 1;

include '_footer.php';