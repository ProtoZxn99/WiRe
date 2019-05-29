package patrick.pramedia.wire.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import patrick.pramedia.wire.ActivityLogin;
import patrick.pramedia.wire.MainActivity;
import patrick.pramedia.wire.R;
import patrick.pramedia.wire.SetTimerActivity;
import patrick.pramedia.wire.entitas.EntitasDevice;
import patrick.pramedia.wire.entitas.Timer;
import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.SessionManager;
import patrick.pramedia.wire.modul.VarGlobals;

/**
 * Created by PRA on 5/11/2019.
 */

public class AdapterTimer extends RecyclerView.Adapter<AdapterTimer.MyViewHolder>  {

    private List<Timer> list;
    private Context context;
    private Globals g = new Globals();
    private SessionManager ses = new SessionManager();
    private RecyclerView rv;
    private interfaceRVTimer it;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_id, tv_name;
        private Switch sw_timer;
        private LinearLayout lt;
//        private ImageView menu;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;

            tv_id = view.findViewById(R.id.tv_id);
            tv_name = view.findViewById(R.id.tv_name);
            sw_timer = view.findViewById(R.id.sw_timer);
            lt = view.findViewById(R.id.lay_utama_t);
//            menu = view.findViewById(R.id.menu);
            lt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    it.click(MyViewHolder.this.getLayoutPosition());
                }
            });
            lt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ajax_hapus(g.getServer() + "deleteTimer.php", list.get(MyViewHolder.this.getLayoutPosition()).getId(), MyViewHolder.this.getLayoutPosition());
                    return false;
                }
            });
        }
    }

    public AdapterTimer(List<Timer> listData, Context context, RecyclerView rv, interfaceRVTimer it) {
        this.list = listData;
        this.context = context;
        this.rv = rv;
        this.it = it;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_timer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Timer obj = list.get(position);
        holder.tv_id.setText(obj.getId());
        holder.tv_name.setText(obj.getName());
        holder.sw_timer.setChecked(obj.getState());
        holder.sw_timer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    String iv = g.generateRandomString();
                    SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(context,"KEY"),iv);

                    String timer_id = s1.encrypt(obj.getId());
                    String timer_state = s1.encrypt(String.valueOf(b));

                    setTimerState(g.getServer()+"setTimerState.php", timer_id, timer_state, iv);

                }catch (Exception ex){
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void ajax_hapus(String url, final String kode, final int posisi){
        final ProgressDialog progress = new ProgressDialog(context);
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
                             remove(posisi);
                        }catch (Exception e){
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "The server unreachable", Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void remove(int position){
        list.remove(position);
        rv.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    private void setTimerState(String url, final String timer_id, final String timer_state, final String iv){
        final ProgressDialog progress = new ProgressDialog(context);
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
                                Toast.makeText(context, "Timer changed", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Timer not changed", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "The server unreachable", Toast.LENGTH_SHORT).show();

                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(context,"ID"));
                params.put("account_password", ses.getPreferences(context, "PASS"));
                params.put("timer_id", timer_id);
                params.put("timer_state", timer_state);
                params.put("iv", iv);

                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
