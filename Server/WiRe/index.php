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
            
            $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
            echo $GLOBALS['crypto']['server_aes']."<Br>";
            echo "cd4687 = ".$ecb->encrypt("cd4687")."<br>";
            echo $ecb->decrypt("1ja6PhSigN1MGTpb1L4cxw==");
            echo "278019560 = ".$ecb->encrypt("278019560")."<br>";
            
            $real_password = "a";
            $real_email = "a@a.com";
    
            $unique_salt = substr($GLOBALS['crypto']['more_salt'], strlen($real_email)%strlen($GLOBALS['crypto']['more_salt']));
            echo $unique_salt."<br>";
			$password = hash("sha256",$GLOBALS['crypto']['header_salt'].$unique_salt.$real_password.$real_email.$GLOBALS['crypto']['end_salt']);

            $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
            echo $real_email." ".$password."<br>";
            echo $ecb->encrypt($real_email)." ".$ecb->encrypt($password);
            echo "<hr>";
			echo "http://".$ip_wire."/WiRe/confirmAccountRegistration.php?confirmation_user=".hash("sha256",$GLOBALS['crypto']['header_salt'].$real_email.$GLOBALS['crypto']['end_salt']);
        
            
        ?>
    </body>
</html>
