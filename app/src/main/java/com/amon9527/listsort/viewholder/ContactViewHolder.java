package com.amon9527.listsort.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amon9527.listsort.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    public TextView mTvGroupName;
    public TextView mTvName;
    public TextView mTvPhoneNumber;

    public ContactViewHolder(View itemView) {
        super(itemView);
        mTvGroupName = itemView.findViewById(R.id.group_name);
        mTvName = itemView.findViewById(R.id.name);
        mTvPhoneNumber = itemView.findViewById(R.id.phone_number);
    }
}
