<?php
include '_header.php';

$device_id = mysqli_real_escape_string($conn, XOR_Encrypt(base64_decode($_GET['device_id']),$GLOBALS['crypto']['xor_mac_key']));

$hmac_id = substr($device_id, 0, 32);
$device_id = substr($device_id, 32);

if(MD5_HMAC($device_id, $GLOBALS['crypto']['xor_mac_key'], $GLOBALS['crypto']['xor_mac_key'])!=$hmac_id){
    echo -1;
    include '_footer.php';
}

$query = mysqli_query($conn, "select a.account_wifi_ssid as ssid from account a, device d where a.account_id = d.account_id and d.device_id = '".$device_id."-5' limit 1;");
$exec = mysqli_fetch_array($query);

if(isset($exec['ssid'])){
	$ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);

	$ssid = $ecb->decrypt($exec['ssid']);

	$mac = substr($device_id, 0, 17);

	$ssid_key = generateSSIDKey($mac);
	$essid = base64_encode(XOR_Encrypt(MD5_HMAC($ssid, $GLOBALS['crypto']['xor_mac_key'], $ssid_key).$ssid, $ssid_key));

	echo $essid;
}
else{
	echo 0;
}



include '_footer.php';

function generateSSIDKey($mac){
    $key = "0";
    if(!is_null($mac) && strlen($mac)>16){
        $seed = $mac{15};
        $key = "";
        if(!is_numeric($seed)){
            $seed = 0;
        }
        $key .= substr($mac,$seed+2);
        $key .= $mac;
        $key = str_replace(":", "", $key);
    }
    return $key;
}
