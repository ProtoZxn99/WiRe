<?php
include '_header.php';

$device_id = mysqli_real_escape_string($conn, $_GET['device_id']);

$query = mysqli_query($conn, "SELECT device_state as state FROM device where device_id = '".$device_id."' limit 1;");
$exec = mysqli_fetch_array($query);

if(!is_null($exec['state'])){
    echo $exec['state'];
}
else{
    echo 0;
}

include '_footer.php';