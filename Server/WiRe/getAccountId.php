<?php
include '_header.php';

$account_email = mysqli_real_escape_string($conn, $_POST['account_email']);
$account_pass = mysqli_real_escape_string($conn, $_POST['account_pass']);

$query = mysqli_query($conn, "SELECT account_id as id, account_block as authority, account_use as use FROM account where account_email = '".$account_email."' and account_pass = '".$account_pass."';");
$exec = mysqli_fetch_array($query);

if(!is_null($exec['authority'])){
    if($exec['authority']<1){
		if($exec['use']<1){
			$query = mysqli_query($conn, "update set account_use = 1 where account_id = '".$exec['id']."'");
			echo $exec['id'];
		}
		else{
			echo $GLOBALS['error']['id_use'];
		}
    }
    else{
        echo $GLOBALS['error']['id_block'];
    }
}
else{
    echo $GLOBALS['error']['wrong_format'];
}

include '_footer.php';