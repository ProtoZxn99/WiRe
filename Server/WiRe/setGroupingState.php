<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$device_state = mysqli_real_escape_string($conn, $_POST['device_state']);

ValidateUser($conn, $account_id, $account_password);

$query = mysqli_query($conn, "SELECT device_id as id FROM device where device_id in (select device_id from member where grouping_id = ".$grouping_id.");");

while($exec = mysqli_fetch_array($query)){
    $update = mysqli_query($conn, "update device set device_state = ".$device_state." where device_id = '".$exec['id']."';");
}

echo 1;

include '_footer.php';