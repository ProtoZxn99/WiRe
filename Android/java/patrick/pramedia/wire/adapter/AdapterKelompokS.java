package patrick.pramedia.wire.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import patrick.pramedia.wire.R;
import patrick.pramedia.wire.entitas.Kelompok;
import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.SessionManager;

/**
 * Created by munil on 4/19/2019.
 */

public class AdapterKelompokS extends RecyclerView.Adapter<AdapterKelompokS.MyViewHolder>  {

    public static List<Kelompok> listKelompok;
    private Context context;
    private SessionManager ses = new SessionManager();
    private Globals g = new Globals();
    public RecyclerView rv;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_nama;
        private Switch sw_state;
        private LinearLayout lg;
//        private ImageView menu;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            tv_nama = view.findViewById(R.id.tv_nama);
            sw_state = view.findViewById(R.id.sw_group);
            lg = view.findViewById(R.id.lay_utama_g);
            lg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ajax_hapus(g.getServer() + "deleteSharedGrouping.php", listKelompok.get(MyViewHolder.this.getLayoutPosition()).getId_kelompok(), MyViewHolder.this.getLayoutPosition());
                    return false;
                }
            });
        }
    }

    public AdapterKelompokS(List<Kelompok> listData, Context context, RecyclerView rv) {
        this.listKelompok = listData;
        this.context = context;
        this.rv = rv;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Kelompok obj = listKelompok.get(position);
        holder.tv_nama.setText(obj.getNama_kelompok());
        holder.sw_state.setChecked(obj.getStatus_group());
        holder.sw_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setGroupingState(g.getServer()+"setGroupingState.php", obj.getId_kelompok(), String.valueOf(b));
            }
        });

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

                try {
                    String iv = g.generateRandomString();
                    SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(context,"KEY"),iv);
                    String enkrip_grouping_id = s1.encrypt(kode);

                    //Adding parameters to request
                    params.put("account_id", ses.getPreferences(context,"ID"));
                    params.put("account_password", ses.getPreferences(context,"PASS"));
                    params.put("grouping_id", enkrip_grouping_id);
                    params.put("iv", iv);

                }catch (Exception ex){
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void remove(int position){
        listKelompok.remove(position);
        rv.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listKelompok.size());
    }

    @Override
    public int getItemCount() {
        return listKelompok.size();
    }

    private void setGroupingState(String url, final String grouping_id, final String device_state){
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
                        try {
                            progress.dismiss();
                            if(response.equals("1")){
                                Toast.makeText(context, "Item saved", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Item not saved", Toast.LENGTH_SHORT).show();
                            }
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
                    SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(context,"KEY"),iv);
                    String enkrip_grouping_id = s1.encrypt(grouping_id);
                    String enkrip_device_state = s1.encrypt(device_state);

                    //Adding parameters to request
                    params.put("account_id", ses.getPreferences(context,"ID"));
                    params.put("account_password", ses.getPreferences(context,"PASS"));
                    params.put("grouping_id", enkrip_grouping_id);
                    params.put("device_state", enkrip_device_state);
                    params.put("iv", iv);

                }catch (Exception ex){
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
