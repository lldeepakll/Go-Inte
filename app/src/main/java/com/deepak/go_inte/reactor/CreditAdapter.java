package com.deepak.go_inte.reactor;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepak.go_inte.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;



public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.MyViewHolder> {
    private Context context;
    private List<Credit> contactList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        SpinKitView loader;

        public MyViewHolder(View view) {
            super(view);

            loader = (SpinKitView)view.findViewById(R.id.loader);
            name = (TextView) view.findViewById(R.id.name_lbl);

        }
    }

    public CreditAdapter(Context context, List<Credit> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Credit ticket = contactList.get(position);

        if (ticket.getName() != null) {
            holder.name.setText(ticket.getName());
            holder.loader.setVisibility(View.INVISIBLE);
        } else {
            holder.loader.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

}