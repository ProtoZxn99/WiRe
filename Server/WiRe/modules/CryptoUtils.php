<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function RandomString($length)
{
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $randstring = '';
    for ($i = 0; $i < $length; $i++) {
        $randstring .= $characters{rand(0, strlen($characters)-1)};
    }
    return $randstring;
}

function RandomInt($length)
{
    $characters = '0123456789';
    $randstring = '';
    for ($i = 0; $i < $length; $i++) {
        $randstring .= $characters{rand(0, strlen($characters)-1)};
    }
    return $randstring;
}

function XOR_Encrypt($toEncrypt, $key) {

    $output = '';
    $key_len = strlen($key);
    for ($i = 0; $i < strlen($toEncrypt); $i++) {
        $key_index = $i%$key_len;
        if($toEncrypt{$i}==$key{$key_index}){
            $output .= $toEncrypt{$i};
        }
        else{
            $output .= $toEncrypt{$i} ^ $key{$key_index};
        }
    }
    return $output;
}


function MD5_HMAC($text, $key1, $key2){
    return MD5($key2.MD5($key1.$text));
}

class AES_128_ECB
{
    private $key;
    
    function __construct($key)
    {
        $this->key = $key;
    }
    
//    public function triple_encrypt($str){
//        return $this->encrypt($this->encrypt($this->encrypt($str)));
//    }
    
    public function encrypt($str)
    {
        $block = mcrypt_get_block_size("rijndael_128", "ecb");
        $pad   = $block - (strlen($str) % $block);
        $str .= str_repeat(chr($pad), $pad);
        $result =  base64_encode(mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $this->key, $str, MCRYPT_MODE_ECB));
        return $result;
    }
    
//    public function triple_decrypt($str){
//        return $this->decrypt($this->decrypt($this->decrypt($str)));
//    }
    
    public function decrypt($str)
    {
        $str = base64_decode($str);
        $str = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $this->key, $str, MCRYPT_MODE_ECB);

        $len = strlen($str);
        $pad = ord($str[$len - 1]);
        $result = substr($str, 0, strlen($str) - $pad);
        return $result;
    }
} 

class AES_128_CBC
    {
    
//        HOW TO USE IT (PHP)
//
//        $aescrypt = new AES('0123456789abcdef','fedcba9876543210');
//        #Encrypt
//        $encrypted = $aescrypt->encrypt("Text to encrypt");
//        #Decrypt
//        $decrypted = $aescrypt->decrypt($encrypted);

    private $iv; #Same as in JAVA 16 char
    private $key; #Same as in JAVA

    function __construct($key, $iv)
    {
        $this->key = $key;
        $this->iv = $iv;
    }

    function encrypt($str) {

      //$key = $this->hex2bin($key);
        $iv = $this->iv;

        $td = mcrypt_module_open('rijndael-128', '', 'cbc', $iv);

        mcrypt_generic_init($td, $this->key, $iv);
        $encrypted = mcrypt_generic($td, $str);

        mcrypt_generic_deinit($td);
        mcrypt_module_close($td);

        return base64_encode($encrypted);
    }

    function decrypt($code) {
        //$key = $this->hex2bin($key);
        $code = base64_decode($code);
        $iv = $this->iv;

        $td = mcrypt_module_open('rijndael-128', '', 'cbc', $iv);

        mcrypt_generic_init($td, $this->key, $iv);
        $decrypted = mdecrypt_generic($td, $code);

        mcrypt_generic_deinit($td);
        mcrypt_module_close($td);

        return utf8_encode(trim($decrypted));
    }
}
    
class RSA
{
    private $pub = '...public key here...';
    private $pri= '...private key here...';
	
	
    function setPublicKey($pub)
    {
        $this->pub = $pub;
    }
	
	function loadPublicKey($dir){
		$fp=fopen($dir,"r");
		$pub_key=fread($fp,8192);
		fclose($fp);
		$publickey = openssl_get_publickey($pub_key);
		return $publickey;
	}
	
	function setPrivateKey($ri)
    {
        $this->pri = $pri;
    }
    
	function loadPrivateKey($dir){
		$fp=fopen($dir,"r");
		$priv_key=fread($fp,8192);
		fclose($fp);
		// $passphrase is required if your key is encoded (suggested)
		$passphrase = "";
		$privatekey = openssl_get_privatekey($priv_key,$passphrase);
		return $privatekey;
	}
	
    public function encrypt($data)
    {
        if (openssl_public_encrypt($data, $encrypted, $this->pub))
            $data = base64_encode($encrypted);
        else
            throw new Exception('File larger than key');

        return $data;
    }

    public function decrypt($data)
    {
        if (openssl_private_decrypt(base64_decode($data), $decrypted, $this->pri))
            $data = $decrypted;
        else
            $data = '';

        return $data;
    }
}