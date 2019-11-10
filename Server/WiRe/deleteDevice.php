<?php
include '_header.php';

$device_id = $_POST['kode'];

$result = array();
$hapus = mysqli_query($conn, "update device set account_id = NULL where device_id = '".$device_id."';");
if($hapus == 1){
	array_push($result, array('status'=> "Item deleted"));
}else{
	array_push($result, array('status'=> "Item delete failed"));
}

echo htmlspecialchars(json_encode(array("result"=>$result)));

include '_footer.php';