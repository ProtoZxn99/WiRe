<?php
include '_header.php';

$account_id = $_POST['account_id'];

$qjml_nyala = mysqli_query($conn, "SELECT count(*) as jml FROM device where account_id = '".$account_id."' and device_state = '1';");
$jmlnyala = mysqli_fetch_array($qjml_nyala)['jml'];

$qjml = mysqli_query($conn, "SELECT count(*) as jml FROM device where account_id = '".$account_id."';");
$jml = mysqli_fetch_array($qjml)['jml'];

$prosentase = ($jmlnyala / $jml)*100;

$result = array();
array_push($result, array('prosentase'=>$prosentase));
echo json_encode(array("result"=>$result));