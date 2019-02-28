<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$device_state = mysqli_real_escape_string($conn, $_POST['device_state']);

ValidateUser($account_id, $account_password);

$query = mysqli_query($conn, "update device set device_state = ".$device_state." where account_id = ".$account_id.";");

include '_footer.php';