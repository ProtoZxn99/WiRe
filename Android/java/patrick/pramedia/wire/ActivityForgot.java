package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ActivityForgot extends AppCompatActivity implements View.OnClickListener {

    private Globals g = new Globals();
    private EditText txtEmail;
    private Button btnSendRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnSendRecover = (Button) findViewById(R.id.btnSendRecover);
        btnSendRecover.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnSendRecover){
            if(txtEmail.getText().toString().length() > 0){
                sendrecover(g.getServer()+"resetAccountPassword.php");
            }else{
                Toast.makeText(this, "Please fill your email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendrecover(String url){
        final ProgressDialog progress = new ProgressDialog(ActivityForgot.this);
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
                                Toast.makeText(ActivityForgot.this, "Send password recovery success", Toast.LENGTH_SHORT).show();
                                finish();

                                Intent ilogin = new Intent(ActivityForgot.this, ActivityLogin.class);
                                startActivity(ilogin);

                            }else{
                                Toast.makeText(ActivityForgot.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(ActivityForgot.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityForgot.this, "The server unreachable", Toast.LENGTH_SHORT).show();

                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_email", txtEmail.getText().toString());

                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(ActivityForgot.this).add(stringRequest);
    }
}
