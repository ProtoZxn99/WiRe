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
        TUTORIAL DIFFIE HELLMAN KEY EXCHANGE<br>
        <?php
            
            include "_header.php";
			$pri_client = "1111222211113333";
			echo $pri_client."<br>";
			$pub_client = bcpowmod($GLOBALS['crypto']['diffiehellman_base'],$pri_client,$GLOBALS['crypto']['diffiehellman_limit']);
			echo $pub_client."<br>";
			$pri_server = "1234567890123456";
			echo $pri_server."<br>";
			$pub_server = bcpowmod($GLOBALS['crypto']['diffiehellman_base'],$pri_server,$GLOBALS['crypto']['diffiehellman_limit']);
			echo $pub_server."<br>";
			$sha_server = bcpowmod($pub_client,$pri_server,$GLOBALS['crypto']['diffiehellman_limit']);
			echo $sha_server."<br>";
			$sha_client = bcpowmod($pub_server,$pri_client,$GLOBALS['crypto']['diffiehellman_limit']);
			echo $sha_client."<br>";
			$e = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
			echo $e->decrypt("G2EhSHtnxFQyY4OFyy5VTc5ricRoqLIwbis3fRqp6FM=");
        ?>
    </body>
</html>
