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
        <script>
        function validateForm() {
          var password = document.forms["reset_form"]["account_password"].value;
          var confirm = document.forms["reset_form"]["account_confirm"].value;
          if (password == confirm) {
            return true;
          }
          else{
              alert("Password doesn't match");
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
                    <td align="left"><input type="password" name="account_password" required></td>
                </tr>
                <tr>
                    <td align="right">Confirm Password*</td>
                    <td align="left"><input type="password" name="account_confirm" required></td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <br>
                        <input type="submit" value="Reset Password" required>
                    </td>
                </tr>
                
            </table>
        </form>
    </body>
</html>
