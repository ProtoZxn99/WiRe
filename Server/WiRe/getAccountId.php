<?php
include '_header.php';

$account_email = mysqli_real_escape_string($conn, $_POST['account_email']);
$account_password = mysqli_real_escape_string($conn, $_POST['account_password']);

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

$query = mysqli_query($conn, "SELECT account_id as id, account_block as block, account_use as used FROM account where account_email = '".$ecb->encrypt($account_email)."' and account_password = '".$account_password."' limit 1;");
$exec = mysqli_fetch_array($query);

if(strlen($exec['block']) > 0){
    if($exec['block']<1){
        if($exec['used']<1){
            $query = mysqli_query($conn, "update set account_use = 1 where account_id = ".$exec['id'].";");
            
            $base_key = substr(md5($account_email),0,16);
            $iv = substr(md5($account_password),0,16);
            
            $cbc = new AES_128_CBC($base_key, $iv);
                    
            $eid = $cbc->encrypt($exec['id']);
            
            echo $eid;
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