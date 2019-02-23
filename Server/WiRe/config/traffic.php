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

function checkIP($ip){
    $query = mysqli_query($conn, "select traffic_block as blocked from traffic where traffic_ip = '".$ip."' limit 1;");

    $exec = mysqli_fetch_array($query);

    if(is_null($exec['blocked'])){
        $query1 = mysqli_query($conn, "insert into traffic (traffic_ip, traffic_block) values('".$ip."',0);");
    }
    else{
        if($exec['blocked']>0){
            echo $GLOBALS['error']['traffic_block'];
            die();
        }
    }
    $query2 = mysqli_query($conn, "insert into traffic_detail (traffic_ip, traffic_time) values (".$ip.",now());");
    $query3 = mysqli_query($conn, "select count(*) as traffic from traffic_detail where traffic_ip = ".$ip." and traffic_time > date_sub(now(), interval ".$GLOBALS['traffic']['time']." second);");
    $count = mysqli_fetch_array($query3);
    if($count['traffic']>$GLOBALS['traffic']['limit']){
        echo $GLOBALS['error']['traffic_busy'];
        die();
    }
}

function checkAccount($ip, $id){
    $query = mysqli_query($conn, "select count(*) as traffic from traffic_detail where traffic_ip = ".$ip." and traffic_time > date_sub(now(), interval ".$GLOBALS['traffic']['time']." second);");
    $count = mysqli_fetch_array($query);
    if($count['traffic']>$GLOBALS['traffic']['limit']*$GLOBALS['traffic']['max']){
        $query2 = mysqli_query($conn, "update account set account_block = 1 where account_id = ".$id.";");
        echo $GLOBALS['error']['traffic_busy'];
        die();
    }
    $query4 = mysqli_query($conn, "update traffic set account_id = ".$id." where traffic_ip = '".$ip."';");
}
    