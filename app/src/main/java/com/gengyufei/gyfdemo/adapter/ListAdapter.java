package com.gengyufei.gyfdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gengyufei.gyfdemo.R;
import com.gengyufei.gyfdemo.entity.NoteBean;
import com.gengyufei.gyfdemo.utils.Util;

import java.util.List;

/**
 * Created by FineFan on 2017/6/12.
 */

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private List<NoteBean> mData;

    private LayoutInflater inflater;

    public ListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(List<NoteBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public NoteBean getItem(int position) {
        return mData != null && position < mData.size() ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        NoteBean item = getItem(position);
        return item != null ? item.id : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_note_list, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NoteBean noteBean = getItem(position);
        holder.tv_title.setText(noteBean.content);
        holder.tv_type.setText(noteBean.type);
        holder.tv_time.setText(Util.formatDate(noteBean.updateTime));
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;
        TextView tv_time;
        TextView tv_type;
    }
}
