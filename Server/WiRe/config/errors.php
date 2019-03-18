<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$GLOBALS['error'] = array();
$GLOBALS['error']['db_fail'] = "01: Failed to connect to database.";
$GLOBALS['error']["traffic_block"] = "02 The IP Address you are using is blocked from accessing this service, please contact us.";
$GLOBALS['error']["traffic_busy"] = "03: Please wait a few moment before reusing again.";
$GLOBALS['error']["id_fail"] = "04: ID not found, please log in again, if this problem persists, please contact us.";
$GLOBALS['error']["id_block"] = "05: This ID is blocked from using this service, please contact us.";
$GLOBALS['error']["id_use"] = "06: This ID is currently in use.";
$GLOBALS['error']["change_fail"] = "07: Your changes hasn't been saved.";
$GLOBALS['error']["registration_fail"] = "08: Failed to register information.";
$GLOBALS['error']["email_not_unique"] = "09: Email information has already been registered, please use a different email.";
$GLOBALS['error']["wrong_format"] = "10: Please check your email input";
$GLOBALS['error']["email_fail"] = "11: Email not found";
$GLOBALS['error']["group_duplicate"] = "12: Group already existed";