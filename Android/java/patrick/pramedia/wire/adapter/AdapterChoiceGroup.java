package patrick.pramedia.wire.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import patrick.pramedia.wire.R;
import patrick.pramedia.wire.entitas.Kelompok;

/**
 * Created by PRA on 5/12/2019.
 */

public class AdapterChoiceGroup extends RecyclerView.Adapter<AdapterChoiceGroup.MyViewHolder>  {

    private List<Kelompok> listKelompok;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_nama;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            tv_nama = (TextView) view.findViewById(R.id.tv_nama);
        }
    }

    public AdapterChoiceGroup(List<Kelompok> listData) {
        this.listKelompok = listData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group_choice, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Kelompok obj = listKelompok.get(position);
        holder.tv_nama.setText(obj.getNama_kelompok());
    }

    @Override
    public int getItemCount() {
        return listKelompok.size();
    }
}
