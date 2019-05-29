package patrick.pramedia.wire.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import patrick.pramedia.wire.ActivityLogin;
import patrick.pramedia.wire.MainActivity;
import patrick.pramedia.wire.R;
import patrick.pramedia.wire.entitas.EntitasDevice;
import patrick.pramedia.wire.entitas.RemoveDevice;
import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.VarGlobals;

/**
 * Created by PRA on 5/11/2019.
 */

public class AdapterRemoveDevice extends RecyclerView.Adapter<AdapterRemoveDevice.MyViewHolder>  {

    private List<RemoveDevice> list;
    private Globals g = new Globals();
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_nama;
        private ImageView imgremove;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            tv_nama = (TextView) view.findViewById(R.id.tv_namaDevice);
            imgremove = (ImageView) view.findViewById(R.id.imgremove);
        }
    }

    public AdapterRemoveDevice(List<RemoveDevice> listData, Context context) {
        this.list = listData;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_remove_device, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RemoveDevice obj = list.get(position);
        holder.tv_nama.setText(obj.getDevice_name());
        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteitem(g.getServer()+"removeItemGroup.php", obj.getGroup_id(), obj.getDevice_id(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void deleteitem(String url, final String group_id, final String device_id, final int posisi){
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
                        try {
                            progress.dismiss();
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                            list.remove(posisi);
                            notifyItemRemoved(posisi);
                            notifyItemRangeChanged(posisi,list.size());

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
                //Adding parameters to request
                params.put("grouping_id", group_id);
                params.put("device_id", device_id);
                //returning parameter
                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
