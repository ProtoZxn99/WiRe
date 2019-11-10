<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
//$account_id = 3;

$result = array();
$query = mysqli_query($conn, "select t.timer_id as id, t.timer_name as name, t.timer_state as state from timer t, grouping g where t.grouping_id = g.grouping_id and g.account_id = ".$account_id.";");
while($row = mysqli_fetch_array($query)){
	array_push($result, array('id'=> $row['id'], 'name'=>$row['name'], 'state'=>$row['state']));
}
echo htmlspecialchars(json_encode(array("result"=>$result)));

include '_footer.php';