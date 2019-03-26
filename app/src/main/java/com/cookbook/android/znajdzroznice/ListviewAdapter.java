package com.cookbook.android.znajdzroznice;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;

import java.util.List;

public class ListviewAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    String userName, time;

    public ListviewAdapter(Context context, String userName, String time){
        this.userName = userName;
        this.time = time;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
//        return songList[position-1];
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listView = convertView;

        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            listView = inflater.inflate(R.layout.user_score_layout,null);
        }

        TextView username = (TextView) listView.findViewById(R.id.username_score);
        username.setText(userName);


        TextView time = (TextView) listView.findViewById(R.id.time_score);
        time.setText(this.time);



        return listView;
    }
}
