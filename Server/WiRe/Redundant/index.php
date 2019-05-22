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
        <?php
            
            include "_header.php";
			$DHbase = "2035802523820057";
			$DHlim = "9999999900000001";
			$a = $_POST['a'];
			$b = "1231231231231231";
			echo "public_client: ".$a."<br>";
			echo "private_server: ".$b."<br>";
			$pub = bcpowmod($DHbase, $b, $DHlim);
			echo "public_server: ".$pub."<br>";
			echo "shared: ".bcpowmod($a, $b, $DHlim);
        ?>
    </body>
</html>
