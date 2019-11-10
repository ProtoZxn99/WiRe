<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$query = mysqli_query($conn, "select g.grouping_id as id, g.grouping_name as name, a.account_email as email from grouping g, account a, authority t where a.account_id = ".$account_id." and t.account_id=a.account_id and t.grouping_id = g.grouping_id;");

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
$result = array();
while($row = mysqli_fetch_array($query)){
	$query_state = mysqli_query($conn, "SELECT ifnull(count(*)-sum(device_state),0) as state FROM device where device_id  in( select device_id from grouping where grouping_id = '".$row['id']."');");
	$exec_state = mysqli_fetch_array($query_state);
	if($exec_state['state'] > 0){
		$status = FALSE;
	}else{
		$status = TRUE;
	}
	
	array_push($result, array('id'=> $row['id'], 'name'=> $row['name'], 'email'=> $ecb->decrypt($row['email']), 'state'=> $status));
}

echo htmlspecialchars(json_encode(array("result"=>$result)));

include '_footer.php';