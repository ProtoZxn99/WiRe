<?php
$conn = mysqli_connect("localhost", "root", "", "wire");
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);

$result = array();
$query = mysqli_query($conn, "select a.grouping_id, b.device_id ,b.device_name from member a, device b where a.device_id = b.device_id and a.grouping_id = '".$grouping_id."';");
while($row = mysqli_fetch_array($query)){
	array_push($result, array('grouping_id'=> $row['grouping_id'], 'device_id'=>$row['device_id'], 'device_name'=>$row['device_name']));
}
echo htmlspecialchars(json_encode(array("result"=>$result)));

include '_footer.php';
