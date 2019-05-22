<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$account_new_password = mysqli_real_escape_string($conn, $_POST['account_new_password']);

ValidateUser($conn, $account_id, $account_password);

$query = mysqli_query($conn, "update account set account_password = '".$account_new_password."' where account_id = ".$account_id.";");
echo $query;

include '_footer.php';