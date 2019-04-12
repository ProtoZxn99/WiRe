<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$query = mysqli_query($conn, "update into account set account_password = '".$account_password."' where account_id = ".$account_id.";");
$exec = mysqli_fetch_array($query);

echo 1;

include '_footer.php';