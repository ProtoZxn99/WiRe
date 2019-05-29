package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import patrick.pramedia.wire.adapter.AdapterDevice;
import patrick.pramedia.wire.adapter.InterfacePBDevice;
import patrick.pramedia.wire.entitas.EntitasDevice;
import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SessionManager;
import patrick.pramedia.wire.rv.CustomRVItemTouchListener;
import patrick.pramedia.wire.rv.RecyclerViewItemClickListener;

/**
 * Created by munil on 4/13/2019.
 */

public class Device extends Fragment implements View.OnClickListener{

    private AppBarLayout ab;
    private LinearLayout ct;

    private boolean isFabOpen = false;
    private FloatingActionButton fab, fab_add, fab_refresh;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private RecyclerView rv_device;
    private TextView txt_prosen;

    private SessionManager ses = new SessionManager();
    private Globals g = new Globals();

    // progress bar
    private ProgressBar progressBar;
    private TextView txtProgress;
    private int pStatus = 0;
    private Handler handler = new Handler();

    // Data Pegiriman
    private ArrayList<EntitasDevice> data = new ArrayList<>();
    private AdapterDevice adapterDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device, container, false);

        ab = v.findViewById(R.id.appbar);
        ct = v.findViewById(R.id.lay_atas);
        ct.setVisibility(View.INVISIBLE);

        ab.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i)-appBarLayout.getTotalScrollRange() == 0) {
                    ct.setVisibility(View.VISIBLE);
                } else {
                    ct.setVisibility(View.INVISIBLE);
                }
            }
        });

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        fab_add = (FloatingActionButton) v.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);

        fab_refresh = (FloatingActionButton) v.findViewById(R.id.fab_refresh);
        fab_refresh.setOnClickListener(this);

        fab_open = AnimationUtils.loadAnimation(v.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(v.getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(v.getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(v.getContext(),R.anim.rotate_backward);

        rv_device = (RecyclerView) v.findViewById(R.id.rv_device);
        adapterDevice = new AdapterDevice(data, getActivity(), rv_device, new InterfacePBDevice() {
            @Override
            public void click() {
                loopprogress = false;
                loaddata(g.getServer()+"getOwnedDevices.php");
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_device.setLayoutManager(mLayoutManager);
        rv_device.setItemAnimator(new DefaultItemAnimator());
        rv_device.setAdapter(adapterDevice);
        adapterDevice.notifyDataSetChanged();

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        txtProgress = (TextView) v.findViewById(R.id.txtProgress);
        txt_prosen = (TextView) v.findViewById(R.id.txt_prosen);
        rv_device.addOnItemTouchListener(new CustomRVItemTouchListener(getActivity(), rv_device, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                EntitasDevice obj = data.get(position);
                String id = obj.getId();
                ajax_hapus(g.getServer() + "deleteDevice.php", id);
            }
        }));

        loaddata(g.getServer()+"getOwnedDevices.php");

        return v;
    }

    private void ajax_hapus(String url, final String kode){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.optString("status").trim();
                            Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();

                            loaddata(g.getServer()+"getOwnedDevices.php");

                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kode", kode);
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    public boolean loopprogress = false;
    private void setNilaiProgress(final int nilai){
        progressBar.setProgress(pStatus);
        txtProgress.setText(progressBar.getProgress() + " %");
        txt_prosen.setText(progressBar.getProgress() + " %");
        loopprogress = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus <= nilai && nilai>0 && loopprogress) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(pStatus);
                            txtProgress.setText(progressBar.getProgress() + " %");
                            txt_prosen.setText(progressBar.getProgress() + " %");
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus++;
                }
            }
        }).start();
    }

    private void animateFAB(){
        if(isFabOpen){
            fab.startAnimation(rotate_backward);

            fab_add.startAnimation(fab_close);
            fab_refresh.startAnimation(fab_close);

            fab_add.setClickable(false);
            fab_refresh.setClickable(false);

            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);

            fab_add.startAnimation(fab_open);
            fab_refresh.startAnimation(fab_open);

            fab_add.setClickable(true);
            fab_refresh.setClickable(true);

            isFabOpen = true;
        }
    }


    @Override
    public void onClick(View view) {
        if(view == fab){
            animateFAB();
        } else if(view == fab_add){
            Intent i_barcode = new Intent(getActivity(), ActivityScanqr.class);
            Globals.id = new InterfacePBDevice() {
                @Override
                public void click() {
                    loaddata(g.getServer()+"getOwnedDevices.php");
                }
            };
            startActivity(i_barcode);
        } else if(view == fab_refresh){
            loaddata(g.getServer()+"getOwnedDevices.php");
        }
    }

    public void loaddata(String url){
        final ProgressDialog progress = new ProgressDialog(getActivity());
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
                                String status = jsonObject.optString("state");
                                if(status.equals("0")){
                                    setData(id, name, false);
                                }else{
                                    // insert data into array list
                                    setData(id, name, true);
                                }

                            }
                            // close prosgress bar
                            adapterDevice.notifyDataSetChanged();
                            // close prosgress bar
                            pStatus = 0;
                            prosentase(g.getServer()+"prosentasedevice.php");

                            progress.dismiss();
                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void setData(String mac, String nama_ruang, boolean status){
        EntitasDevice obj = new EntitasDevice(mac, nama_ruang, status);
        data.add(obj);
        adapterDevice.notifyDataSetChanged();
    }


    private void prosentase(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            int prosentase = Integer.parseInt(jsonObject.optString("p").trim());

                            setNilaiProgress(prosentase);

                        }catch (Exception e){
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
