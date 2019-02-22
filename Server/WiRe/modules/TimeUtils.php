<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function setTimezone($timezone){
    date_default_timezone_set($timezone);
}

function getDayoftheWeek($numerical){
    if($numerical){
        return date("N");
    }
    else{
        return date("l");
    }
}

function getTime(){
    return date("H:i",time());
}