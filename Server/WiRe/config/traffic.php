<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$traffic_limit = 10;
$traffic_time = 1;

$ip = "....";

//if ($_SERVER['HTTP_CLIENT_IP']!="") 
//{
//    $ip = $_SERVER['HTTP_CLIENT_IP'];
//} 
//elseif ($_SERVER['HTTP_X_FORWARDED_FOR']!="") 
//{
//    $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
//}
//else 
//{
//    $ip = $_SERVER['REMOTE_ADDR'];
//}
//
//$query = mysqli_query($conn, "select traffic_block as blocked from traffic where traffic_ip = '".$ip."' limit 1;");
//
//$exec = mysqli_fetch_array($query);
//
//if(is_null($exec['blocked'])){
//    $query = mysqli_query($conn, "insert into traffic (traffic_ip, traffic_counter, traffic_block, traffic_time) values('".$ip."',1,0);");
//}
//else{
//    if($exec['blocked']>0){
//        die();
//    }
//    $exec['counter']++;
//    $query2 = mysqli_query($conn, "update traffic set traffic_counter = ".$exec['counter']." where traffic_ip = '".$ip."';");
//}
//$query3 = mysqli_query($conn, "select count(*) as traffic from traffic_detail where traffic_ip = ".$ip." and traffic_time > date_sub(now(), interval ".$traffic_time." minute);");
//$count = mysqli_fetch_array($query3);
//if($count['traffic']>$traffic_limit){
//    die();
//}
//$query4 = mysqli_query($conn, "insert into traffic_detail (traffic_ip, traffic_time) values (".$ip.",now());");