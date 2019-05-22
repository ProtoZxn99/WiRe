<?php
$conn = mysqli_connect("localhost", "root", "", "wire");
$id = $_POST['kode'];

$result = array();
$hapus = mysqli_query($conn, "delete from timer where timer_id = '".$id."';");
if($hapus == 1){
	array_push($result, array('status'=> "Item deleted"));
}else{
	array_push($result, array('status'=> "Item delete failed"));
}

echo json_encode(array("result"=>$result));