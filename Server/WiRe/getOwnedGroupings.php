<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$result = array();
$query = mysqli_query($conn, "select grouping_id as id, grouping_name as name from grouping where account_id = ".$account_id.";");
while($row = mysqli_fetch_array($query)){
	$query_state = mysqli_query($conn, "select count(*) as jml from device where device_state = '1' and device_id in(select device_id from member where grouping_id = '".$row['id']."');");
	$exec_state = mysqli_fetch_array($query_state);
	
	if($exec_state['jml'] > 0){
		$status = TRUE;
	}else{
		$status = FALSE;
	}
	
	array_push($result, array('id'=> $row['id'], 'name'=>$row['name'], 'state'=>$status));
}
echo htmlspecialchars(json_encode(array("result"=>$result)));


include '_footer.php';