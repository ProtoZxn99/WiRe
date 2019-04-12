<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_name = mysqli_real_escape_string($conn, $_POST['grouping_name']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$query = mysqli_query($conn, "insert into grouping (account_id,grouping_name) values (".$account_id.",'".$grouping_name."')");

echo 1;

include '_footer.php';