<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_name = mysqli_real_escape_string($conn, $_POST['grouping_name']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$grouping_name = $cbc->decrypt(grouping_name);

$check =  mysqli_query($conn, "select count(*) as total from grouping where account_id=".$account_id." and grouping_name='".$grouping_name."');");
$result = mysqli_fetch_array($check);
if($result['total']<1){
    $query = mysqli_query($conn, "insert into grouping (account_id,grouping_name) values (".$account_id.",'".$grouping_name."');");
    $check =  mysqli_query($conn, "select grouping_id as id from grouping where account_id=".$account_id." and grouping_name='".$grouping_name."');");
    $result = mysqli_fetch_array($check);
    echo $result['id'];
}
else{
    echo $GLOBALS['error']["group_duplicate"];
}

include '_footer.php';