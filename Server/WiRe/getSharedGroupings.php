<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);

$query = mysqli_query($conn, "select g.grouping_id, g.grouping_name from grouping g, authority a where a.account_id = ".$account_id.";");
$exec = mysqli_fetch_array($query);

echo json_encode($exec);

include '_footer.php';