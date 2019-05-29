package patrick.pramedia.wire.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import patrick.pramedia.wire.R;
import patrick.pramedia.wire.entitas.EntitasDevice;
import patrick.pramedia.wire.modul.Globals;

public class AdapterDevice extends RecyclerView.Adapter<AdapterDevice.MyViewHolder>  {

    private List<EntitasDevice> list;
    private Context context;
    private Globals g = new Globals();
    private RecyclerView rv;
    private InterfacePBDevice ind;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_nama, tv_mac;
        private Switch sw_device;
        private LinearLayout ld;
//        private ImageView menu;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;

            tv_nama = view.findViewById(R.id.tv_nama);
            tv_mac = view.findViewById(R.id.tv_mac);
            sw_device = view.findViewById(R.id.sw_device);
            ld = view.findViewById(R.id.lay_utama_d);

            ld.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ajax_hapus(g.getServer() + "deleteDevice.php", list.get(MyViewHolder.this.getLayoutPosition()).getId(), MyViewHolder.this.getLayoutPosition());
                    return false;
                }
            });
//            menu = view.findViewById(R.id.menu);
        }
    }

    public AdapterDevice(List<EntitasDevice> listData, Context context, RecyclerView rv, InterfacePBDevice ind) {
        this.list = listData;
        this.context = context;
        this.rv = rv;
        this.ind = ind;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_device, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final EntitasDevice obj = list.get(position);
        holder.tv_mac.setText(obj.getId());
        holder.tv_nama.setText(obj.getNama());
        holder.sw_device.setChecked(obj.getStatus_alat());
        holder.sw_device.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String status = "";
                if(b){
                    status = "1";
                }else{
                    status = "0";
                }
                changestate(g.getServer()+"onoffdevice.php", obj.getId(), status);
                ind.click();
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
                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.optString("status").trim();
                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
                            if(status.equals("Item deleted")){
                                remove(posisi);
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

    private void changestate(String url, final String mac, final String status){
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
                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.optString("status").trim();

                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

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
                params.put("mac", mac);
                params.put("status", status);

                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
