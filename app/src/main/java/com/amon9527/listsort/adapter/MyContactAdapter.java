package com.amon9527.listsort.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.amon9527.listsort.R;
import com.amon9527.listsort.model.ContactBean;
import com.amon9527.listsort.viewholder.ContactViewHolder;

import java.util.List;
import java.util.Map;

public class MyContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private Context mContext;
    private List<ContactBean> mContactList;
    Map<Integer, String> mGroupIndexMap;

    public MyContactAdapter(Context context, List<ContactBean> contactList,
                            Map<Integer, String> groupIndexMap) {
        mContext = context;
        mContactList = contactList;
        mGroupIndexMap = groupIndexMap;
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.contact_list_item, null);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactBean contactBean = mContactList.get(position);
        if (mGroupIndexMap.containsKey(position)) {
            String groupName = mGroupIndexMap.get(position);
            holder.mTvGroupName.setText(groupName);
            holder.mTvGroupName.setVisibility(View.VISIBLE);
        } else {
            holder.mTvGroupName.setVisibility(View.GONE);
        }
        holder.mTvName.setText(contactBean.getName());
        holder.mTvPhoneNumber.setText(contactBean.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return (mContactList == null) ? 0 : mContactList.size();
    }
}
