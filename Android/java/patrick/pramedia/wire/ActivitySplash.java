package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.SecurityMD5;
import patrick.pramedia.wire.modul.SessionManager;

/**
 * Created by munil on 4/13/2019.
 */

public class ActivitySplash extends AppCompatActivity {

    private Globals g = new Globals();


    private SessionManager manager = new SessionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                String username = manager.getPreferences(ActivitySplash.this,"ID");

                if (!username.equals("")){
                    Intent i = new Intent(ActivitySplash.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(ActivitySplash.this, ActivityLogin.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, 1*1000); // wait for 1 seconds
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
    }
}
