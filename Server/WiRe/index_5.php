
<?php

    include "_header.php";

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$account_key = mysqli_real_escape_string($conn, $_POST['account_key']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);
$dummy_id = mysqli_real_escape_string($conn, $_POST['dummy_id']);

$base_key = substr(md5($account_id), 0, 16);

$cbc = new AES_128_CBC($base_key, $iv);

$dummy_id =  $cbc->decrypt($dummy_id);

echo $dummy_id;
