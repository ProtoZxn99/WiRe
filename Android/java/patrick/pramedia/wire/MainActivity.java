package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import patrick.pramedia.wire.adapter.AdapterKelompok;
import patrick.pramedia.wire.entitas.Kelompok;
import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.SecurityMD5;
import patrick.pramedia.wire.modul.SessionManager;

/**
 * Created by munil on 4/13/2019.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, STTFragment.OnFragmentInteractionListener  {

    private SessionManager ses = new SessionManager();
    private Globals g = new Globals();

    // enkripsi
    private String DHbase = "2035802523820057";
    private String DHlim = "9999999900000001";
    private String privateClient = "";
    private String publicClient = "";
    private String ID = "";
    private String PASS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // bagian kiri
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        // bagian atas
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("GROUP"));
        tabLayout.addTab(tabLayout.newTab().setText("SCHEDULE"));
        tabLayout.addTab(tabLayout.newTab().setText("DEVICE"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ID = ses.getPreferences(MainActivity.this, "ID");
        PASS = ses.getPreferences(MainActivity.this, "PASS");
        setKey(g.getServer()+"setAccountKey.php",ID,PASS);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent iprofile = new Intent(MainActivity.this, ActivityProfile.class);
            startActivity(iprofile);

        } else if (id == R.id.nav_logout) {
            logout(g.getServer() + "setAccountUse.php");
        }
        else if(id == R.id.nav_support){
            Intent isupport = new Intent(MainActivity.this, SupportActivity.class);
            startActivity(isupport);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void logout(String url){
        final ProgressDialog progress = new ProgressDialog(MainActivity.this);
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
                                ses.setPreferences(MainActivity.this,"ID","");
                                ses.setPreferences(MainActivity.this,"PASS","");
                                ses.setPreferences(MainActivity.this,"KEY","");

                                finish();

                                Intent ilogin = new Intent(MainActivity.this, ActivityLogin.class);
                                startActivity(ilogin);

                            }else{
                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(MainActivity.this, "ID"));
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onClick(View view) {

    }

    private void setKey(String url, final String id, final String pass){
        final ProgressDialog progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Loading key..."); // Setting Message
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

                            String publicServer = response;
                            String shared = powmod(publicServer, privateClient, DHlim);
                            while(shared.length()<16){
                                shared = "0"+shared;
                            }
                            Log.d("AAASETKEY", shared);
                            ses.setPreferences(MainActivity.this,"KEY", shared);

                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", id);
                params.put("account_password", pass);

                //Random 16 angka
                privateClient = g.generateRandomInt();
                Log.d("AAA", publicClient);
                publicClient = powmod(DHbase, privateClient, DHlim);

                params.put("account_key", publicClient);

                String bk = "";
                SecurityMD5 m1 = new SecurityMD5();
                try {
                    bk = m1.hash(publicClient).substring(0,16);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                String iv = g.generateRandomString();

                SecurityAES128CBC s1 = new SecurityAES128CBC(bk,iv);

                params.put("iv", iv);

                String dummy_id = "";

                try {
                    dummy_id = s1.encrypt(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                params.put("dummy_id", dummy_id);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public String powmod(String base, String exp, String mod){
        BigInteger b = new BigInteger(base);
        BigInteger e = new BigInteger(exp);
        BigInteger m = new BigInteger(mod);

        BigInteger result = b.modPow(e, m);

        return String.valueOf(result);
    }

    @Override
    public void onFragmentInteraction(ArrayList<String> str) {
        if(str.size()>0){
            Group.txtSpeechInput.setText(str.get(0));
        }
        else{
            Group.txtSpeechInput.setText("Error");
        }
        for(String s:str){
            String temp = s.toLowerCase();
            if (temp.substring(0, 4).equals("turn")&&temp.length()>8){
                if(temp.substring(5, 7).equals("on")){
                    String groupname = temp.substring(8);
                    for(Kelompok k : AdapterKelompok.listKelompok){
                        if(k.getNama_kelompok().equals(groupname)){
                            Group.txtSpeechInput.setText(temp);
                            setGroupingState(g.getServer()+"setGroupingState.php", k.getId_kelompok(), "1");
                            break;
                        }
                    }
                }
                else if(temp.substring(5, 8).equals("off")&&temp.length()>9){
                    String groupname = temp.substring(9);
                    for(Kelompok k : AdapterKelompok.listKelompok){
                        if(k.getNama_kelompok().equals(groupname)){
                            if(k.getNama_kelompok().equals(groupname)){
                                Group.txtSpeechInput.setText(temp);
                                setGroupingState(g.getServer()+"setGroupingState.php", k.getId_kelompok(), "0");
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void setGroupingState(String url, final String grouping_id, final String device_state){
        final ProgressDialog progress = new ProgressDialog(MainActivity.this);
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
                                Toast.makeText(MainActivity.this, "Item saved", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Item not saved", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    String iv = g.generateRandomString();
                    SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(MainActivity.this,"KEY"),iv);
                    String enkrip_grouping_id = s1.encrypt(grouping_id);
                    String enkrip_device_state = s1.encrypt(device_state);

                    //Adding parameters to request
                    params.put("account_id", ses.getPreferences(MainActivity.this,"ID"));
                    params.put("account_password", ses.getPreferences(MainActivity.this,"PASS"));
                    params.put("grouping_id", enkrip_grouping_id);
                    params.put("device_state", enkrip_device_state);
                    params.put("iv", iv);

                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }
}
