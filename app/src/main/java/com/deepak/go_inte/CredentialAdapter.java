package com.deepak.go_inte;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HP LAPTOP on 01-03-2018.
 */

public class CredentialAdapter extends RecyclerView.Adapter<CredentialAdapter.MyViewHolder>{

    private Context context;
    private List<Credentials> credentialsListList;

    public CredentialAdapter(Context context, List<Credentials> credentialsListList) {
        this.context = context;
        this.credentialsListList = credentialsListList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_cred_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Credentials cr = credentialsListList.get(position);
        holder.name.setText(cr.getName());
        holder.email.setText(cr.getEmail());
        holder.pass.setText(cr.getPassword());
        holder.mob.setText(cr.getMobile());
    }

    @Override
    public int getItemCount() {
        return credentialsListList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,email,pass,mob;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_tv);
            email = (TextView) view.findViewById(R.id.email_tv);
            pass = (TextView) view.findViewById(R.id.pass_tv);
            mob = (TextView) view.findViewById(R.id.mob_tv);
        }
    }



}
