package patrick.pramedia.wire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AnalogClock;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import patrick.pramedia.wire.adapter.AdapterDevice;
import patrick.pramedia.wire.adapter.AdapterTimer;
import patrick.pramedia.wire.adapter.interfaceRVTimer;
import patrick.pramedia.wire.entitas.EntitasDevice;
import patrick.pramedia.wire.entitas.Kelompok;
import patrick.pramedia.wire.entitas.Timer;
import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SessionManager;
import patrick.pramedia.wire.rv.CustomRVItemTouchListener;
import patrick.pramedia.wire.rv.RecyclerViewItemClickListener;

/**
 * Created by munil on 4/13/2019.
 */

public class Schedule extends Fragment implements View.OnClickListener{

    private AppBarLayout ab;
    private LinearLayout ct;

    private boolean isFabOpen = false;
    private FloatingActionButton fab, fab_add, fab_refresh;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private RecyclerView rv;

    private SessionManager ses = new SessionManager();
    private Globals g = new Globals();

    private AnalogClock analogClock;
    // Data Pegiriman
    private ArrayList<Timer> data = new ArrayList<>();
    private AdapterTimer adapterTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

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

        analogClock = (AnalogClock) v.findViewById(R.id.analogClock);

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

        rv = (RecyclerView) v.findViewById(R.id.rv);
        adapterTimer = new AdapterTimer(data, getActivity(), rv, new interfaceRVTimer() {
            @Override
            public void click(int position) {
                Timer obj = data.get(position);
                String id = obj.getId();

                Intent i = new Intent(getActivity(), SetTimerActivity.class);
                i.putExtra("MODE", "UPDATE");
                i.putExtra("KODE", id);
                startActivity(i);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapterTimer);
        adapterTimer.notifyDataSetChanged();

        loaddata(g.getServer()+"getOwnedTimers.php");

        return v;
    }

    @Override
    public void onClick(View view) {
        if(view == fab){
            animateFAB();
        } else if(view == fab_refresh){
            loaddata(g.getServer()+"getOwnedTimers.php");
        } else if(view == fab_add){
            Intent isettimer = new Intent(getActivity(), SetTimerActivity.class);
            isettimer.putExtra("MODE", "INSERT");
            startActivity(isettimer);
        }
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

    private void loaddata(String url){
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
                                    setData(id, name, true);
                                }
                            }
                            // close prosgress bar
                            adapterTimer.notifyDataSetChanged();

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
                Log.d("AAAA", ses.getPreferences(getActivity(),"ID"));
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void setData(String id, String nama, boolean status){
        Timer obj = new Timer(id, nama, status);
        data.add(obj);
        adapterTimer.notifyDataSetChanged();
    }
}
