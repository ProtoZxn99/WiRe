<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$query = mysqli_query($conn, "select t.timer_id as id, t.timer_name as name from timer t, grouping g where t.grouping_id = g.grouping_id and g.account_id = ".$account_id.";");
$exec = mysqli_fetch_array($query);

echo json_encode($exec);

include '_footer.php';