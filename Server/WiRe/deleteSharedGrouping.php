<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$grouping_id = $cbc->decrypt($grouping_id);

$query = mysqli_query($conn, "delete from authority where account_id = ".$account_id." and grouping_id = ".$grouping_id.";");

echo 1;

include '_footer.php';