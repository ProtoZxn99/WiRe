<?php
include '_header.php';

$mac = $_POST['mac'];
$status = $_POST['status'];

$result = array();
$update = mysqli_query($conn, "update device set device_state = '".$status."' where device_id = '".$mac."';");
if($update == 1){
	array_push($result, array('status'=>"Device changed"));
}else{
	array_push($result, array('status'=>"Device not changed"));
}
echo json_encode(array("result"=>$result));