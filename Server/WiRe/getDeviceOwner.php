<?php
include '_header.php';

$device_id = mysqli_real_escape_string($conn, $_POST['device_id']);

$query = mysqli_query($conn, "SELECT account_id as id from device where device_id = '".$device_id."';");
$exec = mysqli_fetch_array($query);

if(!is_null($exec['user'])){
    echo htmlspecialchars($exec['user']);
}
else{
    echo -1;
}

include '_footer.php';