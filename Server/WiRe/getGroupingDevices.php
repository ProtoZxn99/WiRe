<?php
include '_header.php';

$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);

$query = mysqli_query($conn, "select d.device_id as id, d.device_name as name from device d, member m where d.device_id = m.device_id and m.grouping_id = '".$grouping_id."';");
$exec = mysqli_fetch_array($query);

echo json_encode($exec);

include '_footer.php';