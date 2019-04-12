<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$timer_id = mysqli_real_escape_string($conn, $_POST['timer_id']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$timer_id = $cbc->decrypt($timer_id);

$query = mysqli_query($conn, "delete from timer where timer_id = ".$timer_id.";");

echo 1;

include '_footer.php';