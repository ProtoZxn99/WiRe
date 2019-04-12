<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$device_id = mysqli_real_escape_string($conn, $_POST['device_id']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$device_id = $cbc->decrypt($device_id);
$grouping_id = $cbc->decrypt($grouping_id);

$query = mysqli_query($conn, "insert into member (grouping_id, device_id) values ('".$grouping_id."','".$device_id."')");

echo 1;

include '_footer.php';