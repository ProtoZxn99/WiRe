<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$GLOBALS['traffic'] = array();
$GLOBALS['traffic']['limit'] = 15;
$GLOBALS['traffic']['time'] = 2;
$GLOBALS['traffic']['max'] = 3;

function getIP(){
    $ip = "....";
    if ($_SERVER['HTTP_CLIENT_IP']!="") 
    {
        $ip = $_SERVER['HTTP_CLIENT_IP'];
    } 
    elseif ($_SERVER['HTTP_X_FORWARDED_FOR']!="") 
    {
        $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
    }
    else 
    {
        $ip = $_SERVER['REMOTE_ADDR'];
    }
    return $ip;
}

function checkIP(){
    $ip = getIP();
    $query_get_status = mysqli_query($conn, "select traffic_block as blocked from traffic where traffic_ip = '".$ip."' limit 1;");

    $exec_get_status = mysqli_fetch_array($query_get_status);

    if(is_null($exec_get_status['blocked'])){
        $query1 = mysqli_query($conn, "insert into traffic (traffic_ip, traffic_block) values('".$ip."',0);");
    }
    else{
        if($exec_get_status['blocked']>0){
            echo $GLOBALS['error']['traffic_block'];
            die();
        }
    }
    $query_insert = mysqli_query($conn, "insert into traffic_detail (traffic_ip, traffic_time) values (".$ip.",now());");
    $query_check = mysqli_query($conn, "select count(*) as traffic from traffic_detail where traffic_ip = ".$ip." and traffic_time > date_sub(now(), interval ".$GLOBALS['traffic']['time']." second);");
    $count = mysqli_fetch_array($query_check);
    if($count['traffic']>$GLOBALS['traffic']['limit']*$GLOBALS['traffic']['max']){
        $query_block = mysqli_query($conn, "update traffic set traiffc_block = 1 where traffic_ip = ".$ip.";");
        echo $GLOBALS['error']['traffic_busy'];
        include '_footer.php';
    }
    if($count['traffic']>$GLOBALS['traffic']['limit']){
        echo $GLOBALS['error']['traffic_busy'];
        include '_footer.php';
    }
}

function linkAccount($id){
    $ip = getIP();
    $query4 = mysqli_query($conn, "update traffic set account_id = ".$id." where traffic_ip = '".$ip."';");
}
    