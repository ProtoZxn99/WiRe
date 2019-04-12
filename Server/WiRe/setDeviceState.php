<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$device_id = mysqli_real_escape_string($conn, $_POST['device_id']);
$device_state = mysqli_real_escape_string($conn, $_POST['device_state']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$device_id = $cbc->decrypt($device_id);
$device_state = $cbc->decrypt($device_state);

$query = mysqli_query($conn, "update device set device_state = ".$device_state." where device_id = '".$device_id."';");

echo 1;

include '_footer.php';