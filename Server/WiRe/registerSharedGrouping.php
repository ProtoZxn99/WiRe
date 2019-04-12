<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_id = mysqli_real_escape_string($conn, $_POST['grouping_id']);
$owner_email = mysqli_real_escape_string($conn, $_POST['owner_email']);
$owner_password = mysqli_real_escape_string($conn, $_POST['owner_password']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$ecb = new AES_128_CBC($GLOBALS['crypto']['server_aes']);

$check = mysqli_query($conn, "select count(*) as count from account a, grouping g where a.account_id = g.account_id and a.account_email = ".$ecb->encrypt($owner_email)." and a.account_password = '".$ecb->encrypt($owner_password)."' and g.grouping_id = ".$grouping_id.";");
$result = mysqli_fetch_array($check);
if($result['count']<1){
	$owner = mysqli_query($conn, "select count(*) as count from account a, grouping g where g.account_id = ".$account_id." and g.grouping_id = ".$grouping_id." ;");
        $query = mysqli_query($conn, "insert into authority (account_id, grouping_id) values (".$account_id.",".$grouping_id.");");
	echo 1;
}
else{
    echo $GLOBALS['error']["group_duplicate"];
}
include '_footer.php';
