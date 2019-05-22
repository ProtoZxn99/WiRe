<?php
$conn = mysqli_connect("localhost", "root", "", "wire");
$device_id = $_POST['kode'];

$result = array();
$hapus = mysqli_query($conn, "delete from device where device_id = '".$device_id."';");
if($hapus == 1){
	array_push($result, array('status'=> "Item deleted"));
}else{
	array_push($result, array('status'=> "Item delete failed"));
}

echo json_encode(array("result"=>$result));