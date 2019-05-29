package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import patrick.pramedia.wire.modul.SessionManager;

/**
 * Created by munil on 5/2/2019.
 */

public class Activitysetdevice extends AppCompatActivity implements View.OnClickListener {

    private EditText txtDeviceName;
    private Button btnOk, btnCancel;
    private SessionManager ses = new SessionManager();
    private Globals g = new Globals();
    private SecurityAES128CBC s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysetdevice);

        txtDeviceName = (EditText) findViewById(R.id.txtDeviceName);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnCancel){
            finish();
        } else if(view == btnOk){
            try {
                String iv = g.generateRandomString();
                String device = txtDeviceName.getText().toString();
                String qr = getIntent().getStringExtra("KODEQR");

                s1 = new SecurityAES128CBC(ses.getPreferences(Activitysetdevice.this,"KEY"),iv);
                String enkrip_device = s1.encrypt(device);
                String enkrip_qr = s1.encrypt(qr);

                saveDevice(g.getServer()+"registerDeviceOwnership.php", enkrip_qr, enkrip_device, iv);

            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDevice(String url, final String enkrip_id, final String enkrip_name, final String iv){
        final ProgressDialog progress = new ProgressDialog(Activitysetdevice.this);
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progress.dismiss();
                            if(response.equals("1")){
                                Toast.makeText(Activitysetdevice.this, "Register device success", Toast.LENGTH_SHORT).show();
                                Globals.id.click();
                                finish();
                            }else{
                                Toast.makeText(Activitysetdevice.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(Activitysetdevice.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activitysetdevice.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(Activitysetdevice.this,"ID"));
                params.put("account_password", ses.getPreferences(Activitysetdevice.this,"PASS"));
                params.put("device_id", enkrip_id);
                params.put("device_name", enkrip_name);
                params.put("iv", iv);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(Activitysetdevice.this).add(stringRequest);
    }
}
