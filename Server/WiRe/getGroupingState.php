<?php
include '_header.php';

$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);

$query = mysqli_query($conn, "SELECT count(*)-sum(device_state) as off_device FROM device where device_id  in( select device_id from grouping where grouping_id = '".$grouping_id."');");
$exec = mysqli_fetch_array($query);

if(!is_null($exec['off_device'])){
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
