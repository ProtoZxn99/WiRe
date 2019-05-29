package patrick.pramedia.wire;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import patrick.pramedia.wire.adapter.AdapterChoiceGroup;
import patrick.pramedia.wire.entitas.Kelompok;
import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.SessionManager;
import patrick.pramedia.wire.rv.CustomRVItemTouchListener;
import patrick.pramedia.wire.rv.RecyclerViewItemClickListener;

public class SetTimerActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnCancel, btnOk;
    private String idGroup = "";
    private String idTimer = "";
    private TextView txtGroup, txtTime;
    private EditText timername;
    private TimePickerDialog timePickerDialog;
    private RadioButton radioON, radioOFF;
    private RadioButton radioEveryDay, radioCustom;
    private CheckBox ckSunday, ckMonday, ckTuesday, ckWednesday, ckThursday, ckFriday, ckSaturday;
    private SessionManager ses = new SessionManager();
    private Globals g = new Globals();

    private ArrayList<Kelompok> data = new ArrayList<>();
    private AdapterChoiceGroup adapterGroup = new AdapterChoiceGroup(data);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Set Timer");

        timername = (EditText) findViewById(R.id.timername);

        txtGroup = (TextView) findViewById(R.id.txtGroup);
        txtGroup.setOnClickListener(this);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtTime.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        radioON = (RadioButton) findViewById(R.id.radioON);
        radioOFF = (RadioButton) findViewById(R.id.radioOFF);

        ckSunday = (CheckBox) findViewById(R.id.ckSunday);
        ckMonday = (CheckBox) findViewById(R.id.ckMonday);
        ckTuesday = (CheckBox) findViewById(R.id.ckTuesday);
        ckWednesday = (CheckBox) findViewById(R.id.ckWednesday);
        ckThursday = (CheckBox) findViewById(R.id.ckThursday);
        ckFriday = (CheckBox) findViewById(R.id.ckFriday);
        ckSaturday = (CheckBox) findViewById(R.id.ckSaturday);

        radioEveryDay = (RadioButton) findViewById(R.id.radioEveryDay);
        radioEveryDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioEveryDay.isChecked()){
                    ckSunday.setChecked(true);
                    ckMonday.setChecked(true);
                    ckTuesday.setChecked(true);
                    ckWednesday.setChecked(true);
                    ckThursday.setChecked(true);
                    ckFriday.setChecked(true);
                    ckSaturday.setChecked(true);
                }else{
                    ckSunday.setChecked(false);
                    ckMonday.setChecked(false);
                    ckTuesday.setChecked(false);
                    ckWednesday.setChecked(false);
                    ckThursday.setChecked(false);
                    ckFriday.setChecked(false);
                    ckSaturday.setChecked(false);
                }
            }
        });

        radioCustom = (RadioButton) findViewById(R.id.radioCustom);
        radioCustom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioEveryDay.isChecked()){
                    ckSunday.setChecked(true);
                    ckMonday.setChecked(true);
                    ckTuesday.setChecked(true);
                    ckWednesday.setChecked(true);
                    ckThursday.setChecked(true);
                    ckFriday.setChecked(true);
                    ckSaturday.setChecked(true);
                }else{
                    ckSunday.setChecked(false);
                    ckMonday.setChecked(false);
                    ckTuesday.setChecked(false);
                    ckWednesday.setChecked(false);
                    ckThursday.setChecked(false);
                    ckFriday.setChecked(false);
                    ckSaturday.setChecked(false);
                }
            }
        });
        radioCustom.setChecked(true);
        String mode = getIntent().getStringExtra("MODE");
        if(mode.equals("UPDATE")){
            loaddata(g.getServer()+"loadTimer.php");
        }
    }

    private void loaddata(String url){
        final ProgressDialog progress = new ProgressDialog(SetTimerActivity.this);
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

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String id = jsonObject.optString("id").trim();
                            String idG = jsonObject.optString("id_group").trim();
                            String nama_group = jsonObject.optString("nm_group").trim();
                            String nama_timer = jsonObject.optString("nm_timer").trim();
                            String timer_state = jsonObject.optString("timer_state").trim();
                            String timer_start = jsonObject.optString("timer_start").trim();
                            String timer_d0 = jsonObject.optString("timer_d0").trim();
                            String timer_d1 = jsonObject.optString("timer_d1").trim();
                            String timer_d2 = jsonObject.optString("timer_d2").trim();
                            String timer_d3 = jsonObject.optString("timer_d3").trim();
                            String timer_d4 = jsonObject.optString("timer_d4").trim();
                            String timer_d5 = jsonObject.optString("timer_d5").trim();
                            String timer_d6 = jsonObject.optString("timer_d6").trim();

                            idTimer = id;
                            idGroup = idG;
                            txtGroup.setText(nama_group);
                            timername.setText(nama_timer);
                            int sum = 0;
                            if(timer_state.equals("0")){
                                radioOFF.setChecked(true); radioON.setChecked(false);
                            }else{
                                radioON.setChecked(true); radioOFF.setChecked(false);
                            }
                            txtTime.setText(timer_start);

                            if(timer_d0.equals("0")){
                                ckSunday.setChecked(false);
                            }else{
                                ckSunday.setChecked(true);
                            }

                            if(timer_d1.equals("0")){
                                ckMonday.setChecked(false);
                            }else{
                                ckMonday.setChecked(true);
                            }

                            if(timer_d2.equals("0")){
                                ckTuesday.setChecked(false);
                            }else{
                                ckTuesday.setChecked(true);
                            }

                            if(timer_d3.equals("0")){
                                ckWednesday.setChecked(false);
                            }else{
                                ckWednesday.setChecked(true);
                            }

                            if(timer_d4.equals("0")){
                                ckThursday.setChecked(false);
                            }else{
                                ckThursday.setChecked(true);
                            }

                            if(timer_d5.equals("0")){
                                ckFriday.setChecked(false);
                            }else{
                                ckFriday.setChecked(true);
                            }

                            if(timer_d6.equals("0")){
                                ckSaturday.setChecked(false);
                            }else{
                                ckSaturday.setChecked(true);
                            }

                        }catch (Exception e){
                            Toast.makeText(SetTimerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SetTimerActivity.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("kode", getIntent().getStringExtra("KODE"));
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(SetTimerActivity.this).add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view == btnCancel){
            finish();
        } else if(view == btnOk){
            if((timername.getText().toString().length() > 0) && (txtGroup.getText().toString().length() > 0) && (txtTime.getText().toString().length() > 0)) {

                try {
                    String iv = g.generateRandomString();
                    String grouping_id = idGroup;
                    String timer_name = timername.getText().toString();
                    String timer_start = txtTime.getText().toString();
                    boolean timer_action = false;
                    if(radioON.isChecked()){
                        timer_action = true;
                    }else if(radioOFF.isChecked()){
                        timer_action = false;
                    }

                    boolean timer_state = true;
                    boolean timer_d0 = false;
                    if(ckSunday.isChecked()){
                        timer_d0 = true;
                    }

                    boolean timer_d1 = false;
                    if(ckMonday.isChecked()){
                        timer_d1 = true;
                    }

                    boolean timer_d2 = false;
                    if(ckTuesday.isChecked()){
                        timer_d2 = true;
                    }

                    boolean timer_d3 = false;
                    if(ckWednesday.isChecked()){
                        timer_d3 = true;
                    }

                    boolean timer_d4 = false;
                    if(ckThursday.isChecked()){
                        timer_d4 = true;
                    }

                    boolean timer_d5 = false;
                    if(ckFriday.isChecked()){
                        timer_d5 = true;
                    }

                    boolean timer_d6 = false;
                    if(ckSaturday.isChecked()){
                        timer_d6 = true;
                    }

                    SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(SetTimerActivity.this,"KEY"),iv);
                    //Adding parameters to request
                    String grouping_id1 = s1.encrypt(grouping_id);
                    String timer_name1 = s1.encrypt(timer_name);
                    String timer_start1 = s1.encrypt(timer_start);
                    String timer_action1 = s1.encrypt(String.valueOf(timer_action));
                    String timer_state1 = s1.encrypt(String.valueOf(timer_state));
                    String timer_d01 = s1.encrypt(String.valueOf(timer_d0));
                    String timer_d11 = s1.encrypt(String.valueOf(timer_d1));
                    String timer_d21 = s1.encrypt(String.valueOf(timer_d2));
                    String timer_d31 = s1.encrypt(String.valueOf(timer_d3));
                    String timer_d41 = s1.encrypt(String.valueOf(timer_d4));
                    String timer_d51 = s1.encrypt(String.valueOf(timer_d5));
                    String timer_d61 = s1.encrypt(String.valueOf(timer_d6));
                    String timer_id1 = s1.encrypt(idTimer);

                    if(idTimer.length()>0){
                        updateTimer(g.getServer() + "updateTimer.php", grouping_id1, timer_name1, timer_start1, timer_action1, timer_state1,
                                timer_d01, timer_d11, timer_d21, timer_d31, timer_d41, timer_d51, timer_d61, iv, timer_id1);
                    }
                    else{
                        registerTimer(g.getServer() + "registerTimer.php", grouping_id1, timer_name1, timer_start1, timer_action1, timer_state1,
                                timer_d01, timer_d11, timer_d21, timer_d31, timer_d41, timer_d51, timer_d61, iv);
                    }

                }catch (Exception ex){
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Please fill group name and timer name", Toast.LENGTH_SHORT).show();
            }
        } else if(view == txtTime){
            showTimeDialog();
        } else if(view == txtGroup){
            dialogGroup();
        }
    }

    private void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtTime.setText(hourOfDay+":"+minute+":00");
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    private void dialogGroup() {
        final Dialog dialog = new Dialog(SetTimerActivity.this);
        dialog.setContentView(R.layout.dialog_mygroup);
        dialog.setTitle("Group Name");

        final RecyclerView rv = (RecyclerView) dialog.findViewById(R.id.rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SetTimerActivity.this);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapterGroup);
        adapterGroup.notifyDataSetChanged();
        rv.addOnItemTouchListener(new CustomRVItemTouchListener(SetTimerActivity.this, rv, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Kelompok obj = data.get(position);
                String id = obj.getId_kelompok();
                String nama = obj.getNama_kelompok();

                idGroup = id;
                txtGroup.setText(nama);
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getOwnedGrouping(g.getServer()+"getOwnedGroupings.php");

        dialog.show();
    }

    private void getOwnedGrouping(String url){
        final ProgressDialog progress = new ProgressDialog(SetTimerActivity.this);
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
                            // bersihkan array list
                            data.clear();

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                            for(int i=0; i< jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.optString("id").trim();
                                String name = jsonObject.optString("name").trim();
                                boolean status_group = jsonObject.optBoolean("state");
                                // insert data into array list
                                loadData(id, name, status_group);
                            }
                            // close prosgress bar
                            adapterGroup.notifyDataSetChanged();
                            // close prosgress bar
                            progress.dismiss();
                        }catch (Exception e){
                            Toast.makeText(SetTimerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SetTimerActivity.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(SetTimerActivity.this,"ID"));
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(SetTimerActivity.this).add(stringRequest);
    }

    private void loadData(String kode, String tanggal, boolean status){
        Kelompok obj = new Kelompok();
        obj.setId_kelompok(kode);
        obj.setNama_kelompok(tanggal);
        obj.setStatus_group(status);

        data.add(obj);
        adapterGroup.notifyDataSetChanged();
    }

    private void updateTimer(String url, final String grouping_id, final String timer_name, final String timer_start,
                               final String timer_action, final String timer_state, final String timer_d0, final String timer_d1, final String timer_d2,
                               final String timer_d3, final String timer_d4, final String timer_d5, final String timer_d6, final String iv, final String timer_id){
        final ProgressDialog progress = new ProgressDialog(SetTimerActivity.this);
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

                            Toast.makeText(SetTimerActivity.this, response, Toast.LENGTH_SHORT).show();

                            if(response.equals("1")){
                                Toast.makeText(SetTimerActivity.this, "Times saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(SetTimerActivity.this, "Times save failed", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(SetTimerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SetTimerActivity.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("account_id", ses.getPreferences(SetTimerActivity.this,"ID"));
                params.put("account_password", ses.getPreferences(SetTimerActivity.this,"PASS"));
                params.put("grouping_id", grouping_id);
                params.put("timer_name", timer_name);
                params.put("timer_start", timer_start);
                params.put("timer_action", timer_action);
                params.put("timer_state", timer_state);
                params.put("timer_d0", timer_d0);
                params.put("timer_d1", timer_d1);
                params.put("timer_d2", timer_d2);
                params.put("timer_d3", timer_d3);
                params.put("timer_d4", timer_d4);
                params.put("timer_d5", timer_d5);
                params.put("timer_d6", timer_d6);
                params.put("iv", iv);
                params.put("timer_id", timer_id);
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(SetTimerActivity.this).add(stringRequest);
    }

    private void registerTimer(String url, final String grouping_id, final String timer_name, final String timer_start,
                               final String timer_action, final String timer_state, final String timer_d0, final String timer_d1, final String timer_d2,
                               final String timer_d3, final String timer_d4, final String timer_d5, final String timer_d6, final String iv){
        final ProgressDialog progress = new ProgressDialog(SetTimerActivity.this);
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

                            Toast.makeText(SetTimerActivity.this, response, Toast.LENGTH_SHORT).show();

                            if(response.equals("1")){
                                Toast.makeText(SetTimerActivity.this, "Times saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(SetTimerActivity.this, "Times save failed", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(SetTimerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SetTimerActivity.this, "The server unreachable", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("account_id", ses.getPreferences(SetTimerActivity.this,"ID"));
                params.put("account_password", ses.getPreferences(SetTimerActivity.this,"PASS"));
                params.put("grouping_id", grouping_id);
                params.put("timer_name", timer_name);
                params.put("timer_start", timer_start);
                params.put("timer_action", timer_action);
                params.put("timer_state", timer_state);
                params.put("timer_d0", timer_d0);
                params.put("timer_d1", timer_d1);
                params.put("timer_d2", timer_d2);
                params.put("timer_d3", timer_d3);
                params.put("timer_d4", timer_d4);
                params.put("timer_d5", timer_d5);
                params.put("timer_d6", timer_d6);
                params.put("iv", iv);
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(SetTimerActivity.this).add(stringRequest);
    }
}
