<?php
include '_header.php';

$account_id = mysqli_real_escape_string($conn, $_POST['account_id']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);
$grouping_name = mysqli_real_escape_string($conn, $_POST['grouping_name']);
$owner_email = mysqli_real_escape_string($conn, $_POST['owner_email']);
$owner_password = mysqli_real_escape_string($conn, $_POST['owner_password']);
$iv = mysqli_real_escape_string($conn, $_POST['iv']);

$chat_key = ValidateUser($conn, $account_id, $account_password);

$cbc = new AES_128_CBC($chat_key, $iv);

$grouping_name = $cbc->decrypt($grouping_name);
$owner_email = $cbc->decrypt($owner_email);

/*
$account_id = 3;
$account_password = '4eb0c45a92051ff39e37976ff84a0bfff14c6b5ca8505db834678dda432cf647';
$grouping_name = 'aaa';
$owner_email = 'a@a.com';
$owner_password = '4eb0c45a92051ff39e37976ff84a0bfff14c6b5ca8505db834678dda432cf647';
*/
$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

$check = mysqli_query($conn, "select count(*) as count from account a, grouping g, authority t where t.account_id = a.account_id and t.grouping_id = g.grouping_id and a.account_id = ".$account_id." and g.grouping_id = (select g2.grouping_id from grouping g2, account a2 where g2.grouping_name = '".$grouping_name."' and a2.account_id = g2.account_id and a2.account_email= '".$ecb->encrypt($owner_email)."');");
$result = mysqli_fetch_array($check);
if($result['count']<1){
		$query_id = mysqli_query($conn, "select grouping_id as id from account a, grouping g where g.account_id = a.account_id and a.account_email= '".$ecb->encrypt($owner_email)."' and a.account_password = '".$owner_password."' and g.grouping_name = '".$grouping_name."';");
        $result_id =  mysqli_fetch_array($query_id);
        if(isset($result_id['id'])){
            $query = mysqli_query($conn, "insert into authority (account_id, grouping_id) values (".$account_id.",".$result_id['id'].");");
            echo 1;
        }
        else{
            echo $GLOBALS['error']["group_fail"];
        }
}
else{
    echo $GLOBALS['error']["group_duplicate"];
}
include '_footer.php';
