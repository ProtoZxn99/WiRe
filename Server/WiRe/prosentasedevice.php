<?php
$conn = mysqli_connect("localhost", "root", "", "wire");
$account_id = $_POST['account_id'];

$query_on = mysqli_query($conn, "SELECT count(*) as nyala FROM device where account_id = '".$account_id."' and device_state = '1';");
$on = mysqli_fetch_array($query_on)['nyala'];

$query_total = mysqli_query($conn, "SELECT count(*) as total FROM device where account_id = '".$account_id."';");
$total = mysqli_fetch_array($query_total)['total'];

$p = ($on / $total)*100;

$result = array();
array_push($result, array('p'=>$p));
echo htmlspecialchars(json_encode(array("result"=>$result)));