<?php
$conn = mysqli_connect("localhost", "root", "", "wire");
$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$result = array();
$query = mysqli_query($conn, "select device_id as id, device_name as name, device_state as state from device where account_id = ".$account_id." and device_id not in(select device_id from member);");
while($row = mysqli_fetch_array($query)){
	array_push($result, array('id'=> $row['id'], 'name'=>$row['name'], 'state'=>$row['state']));
}
echo json_encode(array("result"=>$result));

include '_footer.php';
