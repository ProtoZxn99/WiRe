<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <meta charset="UTF-8">
        <title></title>
    </head>
    <body>
        NGAPAIN LIHAT<sup>2</sup>
        <?php
            
            include "_header.php";

            $real_password = "a";
            $real_email = "a@a.com";
    
            $unique_salt = substr($GLOBALS['crypto']['more_salt'], strlen($real_email)%strlen($GLOBALS['crypto']['more_salt']));
            echo $unique_salt."<br>";
			$password = hash("sha256",$GLOBALS['crypto']['header_salt'].$unique_salt.$real_password.$real_email.$GLOBALS['crypto']['end_salt']);

            $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
            echo $real_email." ".$password."<br>";
            echo $ecb->encrypt($real_email)." ".$ecb->encrypt($password);
            echo "<hr>".$ecb->encrypt("1111111111111111")."<br><hr>";
            echo "http://".$ip_wire."/WiRe/confirmAccountRegistration.php?confirmation_user=".hash("sha256",$GLOBALS['crypto']['header_salt'].$real_email.$GLOBALS['crypto']['end_salt'])."<br>";
    
            
$account_email = 'a@a.com';
$account_password = '4eb0c45a92051ff39e37976ff84a0bfff14c6b5ca8505db834678dda432cf647';

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
            
            echo $exec['id']."<br>".$eid."<br>";
            
            $real_id = $cbc->decrypt($eid);
            echo $real_id."<br>";
            
            //
            echo "<hr>";
            
            $account_id = mysqli_real_escape_string($conn, $real_id);
            $account_password = mysqli_real_escape_string($conn, $account_password);
            $account_key = mysqli_real_escape_string($conn, "1214161810121416");
            $iv = mysqli_real_escape_string($conn, "9090580815875456");

        ValidateUser($conn, $account_id, $account_password);
        
        $base_key = substr(md5($account_id), 0, 16);
        $cbc = new AES_128_CBC($base_key, $iv);
        
        $dummy_id = $cbc->encrypt($account_id);
        
        $dummy_id = $cbc->decrypt($dummy_id);
        
        if($dummy_id!=$account_id){
            echo $GLOBALS['error']["id_fail"];
            include '_footer.php';
        }

        $c = DiffieHellman_Count($conn, $account_key, $account_id);
        echo "DH ".$c."<br>";
            //
            
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


        function DiffieHellman_Count($conn, $pub_client, $account_id){
                $pri_server = RandomInt($GLOBALS['crypto']['diffiehellman_length']);
                
                $pub_server = bcpowmod($GLOBALS['crypto']['diffiehellman_base'],$pri_server,$GLOBALS['crypto']['diffiehellman_limit']);
                
                $shared = bcpowmod($pub_client,$pri_server,$GLOBALS['crypto']['diffiehellman_limit']);
                
                
                $shared = substr(base64_encode($shared), 0, 16);
                
                
                Save_Shared($conn, $shared, $account_id);
                
                return $pub_server;
        }

        function Save_Shared($conn, $key, $account_id){
                $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
                
                $key = $ecb->encrypt($key);
                
                $query = mysqli_query($conn, "update account set account_key = '".$key."' where account_id = ".$account_id.";");
                
        }

        ?>
    </body>
</html>
