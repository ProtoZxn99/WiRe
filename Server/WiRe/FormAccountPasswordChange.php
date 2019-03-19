<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>WiRe</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script type="text/javascript" >
        function isLetters(var inputid)
        { 
            var text = document.getElementById(inputid).value;
            var letters = /^[0-9a-zA-Z]+$/;
            if(text.match(letters))
            {
                document.getElementsByClassName(inputid).value = text.substring(0,str.length-1);
            }
        }
        function validateForm() {
            var password = document.forms["reset_form"]["account_password"].value;
            var confirm = document.forms["reset_form"]["account_confirm"].value;
            if (password == confirm) {
                if(isLetters("password")){
                    return true;
                }
                else{
                    alert("Password contains invalid characters.")
                    
                    return false;
                }
            }
            else{
                alert("Password doesn't match.");
                return false;
            }
        }
        </script>
    </head>
    <body>
        <form name="reset_form" action="confirmAccountPasswordChange.php" method="post" onsubmit="return validateForm()">
            <table>
                <input type="hidden" name="account_email" value="<?= $_GET['account_email'];?>">
                <tr>
                    <td align="right">New Password*</td>
                    <td align="left"><input type="password" name="account_password" id="password" onkeydown="isLetters(password)" required></td>
                </tr>
                <tr>
                    <td align="right">Confirm Password*</td>
                    <td align="left"><input type="password" name="account_confirm" id ="confirmation" required></td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <br>
                        <input type="submit" value="Reset Password">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
