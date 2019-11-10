<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$query = mysqli_query($conn, "select account_wifi_password as password from account where account_id = ".$account_id." limit 1;");
$exec = mysqli_fetch_array($query);

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

echo htmlspecialchars($cbc->encrypt($ecb->decrypt($exec['password'])));

include '_footer.php';