<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$query = mysqli_query($conn, "select grouping_id as id, grouping_name as name from grouping where account_id = ".$account_id.";");
$exec = mysqli_fetch_array($query);

echo json_encode($exec);

include '_footer.php';