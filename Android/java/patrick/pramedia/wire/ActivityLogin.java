package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.SecurityMD5;
import patrick.pramedia.wire.modul.SecuritySHA256;
import patrick.pramedia.wire.modul.SessionManager;
import patrick.pramedia.wire.modul.VarGlobals;

/**
 * Created by munil on 4/13/2019.
 */

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPassword, edtEmail;
    private Button btnLogin;
    private SessionManager ses = new SessionManager();
    private Globals g = new Globals();
    private SecurityAES128CBC s1;
    private SecurityMD5 m1 = new SecurityMD5();
    private TextView link_forgot, link_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        link_forgot = (TextView)findViewById(R.id.link_forgot);
        link_forgot.setOnClickListener(this);

        link_register = (TextView)findViewById(R.id.link_register);
        link_register.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){
            if(edtEmail.getText().toString().length() < 1){
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }else if(edtPassword.getText().toString().length() < 1){
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }else{
                String email = edtEmail.getText().toString();
                String pass = edtPassword.getText().toString();

                String unique_salt = VarGlobals.more_salt.substring(email.length()%VarGlobals.more_salt.length());
                SecuritySHA256 s = new SecuritySHA256();
                String password = s.hash(VarGlobals.header_salt+unique_salt+pass+email+VarGlobals.end_salt);

                login(g.getServer() + "getAccountId.php", email, password);
            }
        }
        else if(view == link_forgot){

            Intent i_forgot = new Intent(ActivityLogin.this, ActivityForgot.class);
            startActivity(i_forgot);

            finish();
        }
        else if(view == link_register){
            Intent i_register = new Intent(ActivityLogin.this, RegisterActivity.class);
            startActivity(i_register);

            finish();
        }
    }

    private void login(String url, final String email, final String pass){
        final ProgressDialog progress = new ProgressDialog(ActivityLogin.this);
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

                            if(VarGlobals.notError(response)){
                                s1 = new SecurityAES128CBC(m1.hash(email).substring(0,16),m1.hash(pass).substring(0,16));

                                response = s1.decrypt(response);
                                // set session
                                ses.setPreferences(ActivityLogin.this,"USERNAME", edtEmail.getText().toString());
                                ses.setPreferences(ActivityLogin.this,"ID", response);
                                ses.setPreferences(ActivityLogin.this,"PASS", pass);

                                Intent i_register = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i_register);
                                finish();

                            }else{
                                Toast.makeText(ActivityLogin.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(ActivityLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityLogin.this, "The server unreachable", Toast.LENGTH_SHORT).show();

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
                params.put("account_password", pass);

                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
