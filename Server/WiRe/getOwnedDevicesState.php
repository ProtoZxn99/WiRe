<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$query = mysqli_query($conn, "SELECT count(*)-sum(device_state) as off_device FROM device where device_id in (select device_id from device where account_id = ".$account_id.");");
$exec = mysqli_fetch_array($query);

if(!is_null($exec['state'])){
    if($exec['off_device']==0){
        echo 1;
    }
    else{
        echo 0;
    }
}
else{
    echo 0;
}

include '_footer.php';