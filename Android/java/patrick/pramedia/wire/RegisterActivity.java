package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.SecuritySHA256;
import patrick.pramedia.wire.modul.VarGlobals;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtEmail, txtPassword, txtConfPassword;
    private Button btnRegister;
    private Globals g = new Globals();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfPassword = (EditText) findViewById(R.id.txtConfPassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnRegister){
            if(txtEmail.getText().toString().length() < 1){
                Toast.makeText(this, "Please fill your email", Toast.LENGTH_SHORT).show();
            }else if(txtPassword.getText().toString().length() < 1){
                Toast.makeText(this, "Please fill your password", Toast.LENGTH_SHORT).show();
            }else if(txtConfPassword.getText().toString().length() < 1){
                Toast.makeText(this, "Please fill your confirm password", Toast.LENGTH_SHORT).show();
            }else{
                String email = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();
                String conf_pass = txtConfPassword.getText().toString();

                if(pass.equals(conf_pass)){
                    String unique_salt = VarGlobals.more_salt.substring(email.length()%VarGlobals.more_salt.length());
                    SecuritySHA256 s = new SecuritySHA256();
                    String password = s.hash(VarGlobals.header_salt+unique_salt+pass+email+VarGlobals.end_salt);

                    register(g.getServer() + "registerAccount.php", email, password);
                }else{
                    Toast.makeText(this, "Password and confirm password not match", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void register(String url, final String email, final String password){
        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("Loading ID..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // close prosgress bar
                            progress.dismiss();
                            if(response.equals("1")){
                                Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                                finish();

                                Intent i = new Intent(RegisterActivity.this, ActivityLogin.class);
                                startActivity(i);

                            }else{
                                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "The server unreachable", Toast.LENGTH_SHORT).show();

                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_email", email);
                params.put("account_password", password);

                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(RegisterActivity.this).add(stringRequest);
    }
}
