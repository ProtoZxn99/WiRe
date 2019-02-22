<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function checkEmailFormat($email) {
   $find1 = strpos($email, '@');
   $find2 = strrpos($email, '.');
   return ($find1 !== false && $find2 !== false && $find2 > $find1 ? true : false);
}