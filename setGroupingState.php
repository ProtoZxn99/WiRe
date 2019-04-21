<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$device_state = mysqli_real_escape_string($conn, $_POST['device_state']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$grouping_name = $cbc->decrypt(grouping_name);
$device_state = $cbc->decrypt($device_state);

$query = mysqli_query($conn, "update device set device_state = ".$device_state." where device_id in (select device_id from member where grouping_id = ".$grouping_id.");");

echo 1;

include '_footer.php';