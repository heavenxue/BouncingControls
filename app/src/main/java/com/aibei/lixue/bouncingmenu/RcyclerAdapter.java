package com.aibei.lixue.bouncingmenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/22.
 */
public class RcyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;

    public RcyclerAdapter(Context context){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mTitles = context.getResources().getStringArray(R.array.titles);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_name,null);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TextViewHolder) holder).mTextView.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        TextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.item_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TextViewHolder", "onClick--> position = " + getOldPosition());
                }
            });
        }
    }
}
