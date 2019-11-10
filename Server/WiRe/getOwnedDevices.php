<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$result = array();
$query = mysqli_query($conn, "select device_id as id, device_name as name, device_state as state from device where account_id = ".$account_id.";");
while($row = mysqli_fetch_array($query)){
	array_push($result, array('id'=> $row['id'], 'name'=>$row['name'], 'state'=>$row['state']));
}
echo htmlspecialchars(json_encode(array("result"=>$result)));

include '_footer.php';
