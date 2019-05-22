<?php
$conn = mysqli_connect("localhost", "root", "", "wire");
$id = $_POST['kode'];

$result = array();
$query = mysqli_query($conn, "SELECT a.*, b.grouping_name FROM timer a, grouping b where a.grouping_id = b.grouping_id and a.timer_id = '".$id."';");
$data = mysqli_fetch_array($query);
array_push($result, array('id'=> $data['timer_id'],
	'id_group'=> $data['grouping_id'],
	'nm_group'=> $data['grouping_name'],
	'nm_timer'=> $data['timer_name'],
	'timer_state'=> $data['timer_state'],
	'timer_start'=> $data['timer_start'],
	'timer_d0'=> $data['timer_d0'],
	'timer_d1'=> $data['timer_d1'],
	'timer_d2'=> $data['timer_d2'],
	'timer_d3'=> $data['timer_d3'],
	'timer_d4'=> $data['timer_d4'],
	'timer_d5'=> $data['timer_d5'],
	'timer_d6'=> $data['timer_d6']
));

echo json_encode(array("result"=>$result));