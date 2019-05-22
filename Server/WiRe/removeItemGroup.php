<?php
include '_header.php';

$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$device_id = mysqli_real_escape_string($conn, $_POST['device_id']);

$hapus = mysqli_query($conn, "delete from member where grouping_id = '".$grouping_id."' and device_id = '".$device_id."';");
if($hapus == 1){
	echo "Item deleted";
}else{
	echo "Item not deleted";
}

include '_footer.php';
