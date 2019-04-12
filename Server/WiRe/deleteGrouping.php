<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$query = mysqli_query($conn, "delete from timer where grouping_id = ".$grouping_id.";");
$query2 = mysqli_query($conn, "delete from authority where grouping_id = ".$grouping_id.";");
$query3 = mysqli_query($conn, "delete from member where grouping_id = ".$grouping_id.";");
$query4 = mysqli_query($conn, "delete from grouping where grouping_id = ".$grouping_id.";");

include '_footer.php';