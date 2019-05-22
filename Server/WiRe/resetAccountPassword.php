<?php
include '_header.php';
include 'modules/EmailUtils.php';
include 'modules/FormatUtils.php';

$account_email = mysqli_real_escape_string($conn, $_POST['account_email']);

$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

$query = mysqli_query($conn, "select count(*) as users from account where account_email = '".$ecb->encrypt($account_email)."';");
$exec = mysqli_fetch_array($query);

if($exec['users']>0){
    $msg = "<p align='center'>Please click this link to reset your password at WiRe: </p><br>";
    $url = "http://".$ip_wire."/WiRe/FormAccountPasswordChange.php?account_email=".hash("sha256",$GLOBALS['crypto']['header_salt'].$account_email.$GLOBALS['crypto']['end_salt']);
    $img = "<img src = 'https://i.ytimg.com/vi/V015SjjbYXE/maxresdefault.jpg'>";
    $closure = "If you didn't remember applying for a password reset, you can leave this email alone.";
    sendeMail("wire@noreply.com", $account_email, "Reset WiRe Password", $msg."<a href ='".$url."'>".$img."</a>".$closure);
    echo 1;
}
else{
    echo $GLOBALS['error']['email_fail'];
}

include '_footer.php';
