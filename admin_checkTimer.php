<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
include 'config/conn.php';
include 'module/TimeUtils.php';

$query = mysqli_query($conn, "select m.device_id as id, t.timer_action as new_state from grouping g, member m, timer t where g.grouping_id = m.grouping_id and t.grouping_id = g.grouping_id and t.timer_start = date_format(now(),'%H:%i:00') and timer_d".getDayoftheWeek(true)." = 1;");
while($exec = mysqli_fetch_array($query)){
    $update = mysqli_query($conn, "update device set device_state = ".$exec['new_state']." where device_id = '".$exec['id']."';");
}

include 'config/footer.php';