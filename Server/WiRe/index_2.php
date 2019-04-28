
<?php

    include "_header.php";

//	$a = bcpowmod("2035802523820057","9876543210123456","9999999900000001");
//	$b = bcpowmod($a,"1188726412135657","9999999900000001");
//        $e = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
//        $f = $e->encrypt($b);
//        $g = $e->decrypt("EsXnrtdhG/D9boaOgtrTo85ricRoqLIwbis3fRqp6FM=");
////echo $a." - ".$b."<br>";
//echo $g."<br>";
 $e = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
 echo $e->decrypt("8NGykpzMqSYIe48EJWv/ZM5ricRoqLIwbis3fRqp6FM=");
/*
$query = mysqli_query($conn, "SELECT account_block as authority FROM account where account_id = 4;");
    $exec = mysqli_fetch_array($query);
    if(isset($exec['authority'])){
        if($exec['authority']>0){
            echo $GLOBALS['error']["id_block"];
            include '_footer.php';
        }
        else{
            $query_key = mysqli_query($conn, "SELECT account_key as chat_key FROM account where account_id = 4;");
            $exec_key = mysqli_fetch_array($query_key);
            
            if(strlen($exec_key['chat_key'])>0){
                $ecb = new AES_128_ECB($GLOBALS['crypto']['server_aes']);
                echo $ecb->decrypt($exec_key['chat_key']);
            }
            else{
                echo $GLOBALS['error']["id_fail"];
                include '_footer.php';
            }
        }
    }
    else{
        echo $GLOBALS['error']['wrong_format'];
        include '_footer.php';
    }
*/