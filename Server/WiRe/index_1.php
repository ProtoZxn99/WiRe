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
echo "Angka dasar dan angka limit dipunyai kedua belah pihak<br>";
echo "Angka dasar: 1371531631272555111023204740958191409520471023511255127633115731<br>";
echo "Angka limit: 6260585756555452515049484645444240393836353433323028272625242221<br>";
echo "Semua berjanji beroperasi di panjang angka 64<br>";
echo "<hr>";
echo "Client megenerate private key sepanjang 64<br>";
$pri_client_android = RandomInt($GLOBALS['crypto']['diffiehellman_length']);
echo "Private Client (Client saja yang tahu): ".$pri_client_android."<br><br>";
echo "Public Client: Angka dasar ^ Private Client % Angka limit<br>";
$pub_client_android = bcpowmod($GLOBALS['crypto']['diffiehellman_base'],$pri_client_android,$GLOBALS['crypto']['diffiehellman_limit']);
echo "Public Client: ".$pub_client_android."<br>";          
echo "Public Client dikirim ke server<hr>";
$account_id = mysqli_real_escape_string($conn,"2"); //id asli, nanti di RSA (kalau sempat)
$account_password = mysqli_real_escape_string($conn,"4eb0c45a92051ff39e37976ff84a0bfff14c6b5ca8505db834678dda432cf647"); //hash dari password
$account_key = mysqli_real_escape_string($conn, $pub_client_android); //Public Key Client
$iv = mysqli_real_escape_string($conn, RandomInt(16)); //16 angka random dari client, hanya untuk enkripsi dummy id
//$dummy_id = mysqli_real_escape_string($conn, $_POST['dummy_id']); //hasil enkripsi dari id, membuktikan sender adalah pemilik id asli
//dummy_id adalah enkripsi id dengan kunci = substr(md5($account_id), 0, 16) dan iv = $iv
//
//$base_key = substr(md5($account_id), 0, 16); //Generate kunci untuk dekripsi
//
//$cbc = new AES_128_CBC($base_key, $iv); //Panggil class CBC
//
//$dummy_id = $cbc->decrypt($dummy_id); //Dekripsi dummy_id
//
//if($dummy_id!=$account_id){ //Mengecek apa ID sama dengan hasil dekripsi dummy_id
//    echo $GLOBALS['error']["id_fail"];
//    include '_footer.php';
//}

$d = DiffieHellman_Count($conn, $account_key, $account_id);
echo "Public Server dikembalikan ke Client<br>";
echo "Public Server: ".$d."<br><br>";//Hasil diffie hellman yang dikirim dari server
echo "<hr>";
echo "Public Server diterima Client<br>";
echo "Public Server: ".$d."<br><br>";
$sharedc = bcpowmod($d,$pri_client_android,$GLOBALS['crypto']['diffiehellman_limit']); 
echo "Kunci akhir bagi client (dalam bentuk angka) adalah Public Server ^ Private Client % Angka limit<br>";
echo "Shared Number (Client saja yang tahu): ".$sharedc."<br>";
$sharedc = substr(base64_encode($sharedc), 0, 16); //Sedikit modifikasi saja, dengan base64 agar hasil lebih unik (ada huruf2nya)
echo "Menggunakan base64 agar kunci lebih unik dan kunci di potong hanya 16 angka karena AES butuh 16 saja<br>";
echo "Final Key (Client saja yang tahu): ".$sharedc."<br>";

function DiffieHellman_Count($conn, $pub_client, $account_id){
        echo "Public Client: ".$pub_client."<br><br>";
        echo "Server menegenerate private key sepanjang 64 angka<br>";
        $pri_server = RandomInt($GLOBALS['crypto']['diffiehellman_length']); //Generate angka random sebanyak X
        echo "Private Server (Server saja yang tahu): ".$pri_server."<br><br>";
        echo "Public Client: Angka dasar ^ Private Server % Angka limit<br>";
        $pub_server = bcpowmod($GLOBALS['crypto']['diffiehellman_base'],$pri_server,$GLOBALS['crypto']['diffiehellman_limit']); //Pangkat angka random dan modulus, angka ini menjadi "public" key server
        echo "Public Server: ".$pub_server."<br><br>";
        echo "Kunci akhir bagi server (dalam bentuk angka) adalah Public Client ^ Private Server % Angka limit<br>";
        $shared = bcpowmod($pub_client,$pri_server,$GLOBALS['crypto']['diffiehellman_limit']); //Pangkat public key clent dengan private key server, menjadi key akhir
        echo "Shared Number (Server saja yang tahu): ".$shared."<br><br>";
        $shared = substr(base64_encode($shared), 0, 16); //Sedikit modifikasi saja, dengan base64 agar hasil lebih unik (ada huruf2nya)
        echo "Menggunakan base64 agar kunci lebih unik dan kunci di potong hanya 16 angka karena AES butuh 16 saja<br>";
        echo "Final Key (Server saja yang tahu): ".$shared."<br><br>";
        Save_Shared($conn, $shared, $account_id); //Simpan kunci gabungan ke database
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
