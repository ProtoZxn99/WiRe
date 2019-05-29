package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import patrick.pramedia.wire.modul.SessionManager;
import patrick.pramedia.wire.modul.VarGlobals;

public class ActivityProfile extends AppCompatActivity implements View.OnClickListener {

    private SessionManager ses = new SessionManager();
    private EditText txtUsername, txtPass, txtPass2, txtSSID, txtWiFiPass;
    private Button btnSimpan;
    private ImageView btnshowpass, btnshowpasswifi;
    private Globals g = new Globals();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtUsername.setEnabled(false);
        txtUsername.setText(ses.getPreferences(ActivityProfile.this, "USERNAME"));

        txtPass = (EditText) findViewById(R.id.txtPass);
        txtPass2 = (EditText) findViewById(R.id.txtPass2);


        btnshowpass = (ImageView) findViewById(R.id.btnshowpass);
        btnshowpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtPass.setInputType(InputType.TYPE_CLASS_TEXT);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    txtPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                return true;
            }
        });

        txtSSID = (EditText) findViewById(R.id.txtSSID);
        txtWiFiPass = (EditText) findViewById(R.id.txtWiFiPass);

        btnshowpasswifi = (ImageView) findViewById(R.id.btnshowpasswifi);

        btnshowpasswifi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    txtWiFiPass.setInputType(InputType.TYPE_CLASS_TEXT);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    txtWiFiPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                return true;
            }
        });

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(this);

        getAccWifi(g.getServer()+"getAccountWiFiSSID.php");
        getAccPass(g.getServer()+"getAccountWiFiPassword.php");
    }

    @Override
    public void onClick(View view) {
        if(view == btnSimpan){
            if(txtPass.getText().length() > 0){
                if(txtPass.getText().equals(txtPass2.getText())){
                    updateAccPass(g.getServer()+"updateAccountPassword.php");
                }
                else{
                    Toast.makeText(ActivityProfile.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
            if(txtSSID.getText().length() > 0&&txtWiFiPass.getText().length()>0){
                updateAccWiFi(g.getServer()+"updateAccountWiFi.php");
            }
        }
    }

    private void updateAccPass(String url){
        final ProgressDialog progress = new ProgressDialog(ActivityProfile.this);
        progress.setMessage("Loading..."); // Setting Message
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
                                Toast.makeText(ActivityProfile.this, "Update Account Success", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ActivityProfile.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(ActivityProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityProfile.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(ActivityProfile.this, "ID"));
                params.put("account_password", ses.getPreferences(ActivityProfile.this, "PASS"));
                String unique_salt = VarGlobals.more_salt.substring(txtUsername.getText().length()%VarGlobals.more_salt.length());
                SecuritySHA256 s = new SecuritySHA256();
                String password = s.hash(VarGlobals.header_salt+unique_salt+txtPass.getText().toString()+txtUsername.getText()+VarGlobals.end_salt);
                params.put("account_new_password", password);
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void updateAccWiFi(String url){
        final ProgressDialog progress = new ProgressDialog(ActivityProfile.this);
        progress.setMessage("Loading..."); // Setting Message
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
                                Toast.makeText(ActivityProfile.this, "Update WiFi Success", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ActivityProfile.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(ActivityProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityProfile.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(ActivityProfile.this, "ID"));
                params.put("account_password", ses.getPreferences(ActivityProfile.this, "PASS"));

                String iv = g.generateRandomString();

                SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(ActivityProfile.this,"KEY"),iv);

                String enkrip_ssid = "";
                String enkrip_wifi = "";
                try {
                    enkrip_ssid = s1.encrypt(txtSSID.getText().toString());
                    enkrip_wifi = s1.encrypt(txtWiFiPass.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Adding parameters to request
                params.put("account_wifi_ssid", enkrip_ssid);
                params.put("account_wifi_password", enkrip_wifi);
                params.put("iv", iv);
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getAccWifi(String url){
        final ProgressDialog progress = new ProgressDialog(ActivityProfile.this);
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);
        final String iv = g.generateRandomString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // close prosgress bar
                            progress.dismiss();
                            SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(ActivityProfile.this,"KEY"), iv);
                            String ssid = s1.decrypt(response);
                            txtSSID.setText(ssid);

                        }catch (Exception e){
                            Toast.makeText(ActivityProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityProfile.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(ActivityProfile.this, "ID"));
                params.put("account_password", ses.getPreferences(ActivityProfile.this, "PASS"));

                params.put("iv", iv);
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getAccPass(String url){
        final ProgressDialog progress = new ProgressDialog(ActivityProfile.this);
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);
        final String iv = g.generateRandomString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // close prosgress bar
                            progress.dismiss();
                            SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(ActivityProfile.this,"KEY"), iv);
                            String pass = s1.decrypt(response);
                            txtWiFiPass.setText(pass);

                        }catch (Exception e){
                            Toast.makeText(ActivityProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityProfile.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(ActivityProfile.this, "ID"));
                params.put("account_password", ses.getPreferences(ActivityProfile.this, "PASS"));

                params.put("iv", iv);
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
