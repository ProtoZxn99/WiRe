package patrick.pramedia.wire.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import patrick.pramedia.wire.R;
import patrick.pramedia.wire.entitas.EntitasDevice;
import patrick.pramedia.wire.modul.Globals;

/**
 * Created by PRA on 5/11/2019.
 */

public class AdapterChooseDevice extends RecyclerView.Adapter<AdapterChooseDevice.MyViewHolder>  {

    private List<EntitasDevice> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_nama, tv_mac;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;

            tv_nama = (TextView) view.findViewById(R.id.tv_nama);
            tv_mac = (TextView) view.findViewById(R.id.tv_mac);
        }
    }

    public AdapterChooseDevice(List<EntitasDevice> listData) {
        this.list = listData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_choose_device, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final EntitasDevice obj = list.get(position);
        holder.tv_mac.setText(obj.getId());
        holder.tv_nama.setText(obj.getNama());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
