package com.example.pssin.auction;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryListAdapter extends BaseAdapter {

    private Context context;
    private List<Category> userList;

    //생성자 생성
    public CategoryListAdapter(Context context, List<Category> userList){
        Log.i("CategoryListAdapter", "리스트 뷰 생성한다?");
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        //현재사용자의 개수 반환
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //그대로 반환
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //하나의 사용자에대한 view를 보여주는 부분
        //한명의 사용자에대한 view가 만들어진다.
        @SuppressLint("ViewHolder") View v = View.inflate(context, R.layout.activity_category,null);
        TextView boardNumber = (TextView)v.findViewById(R.id.boardNumber);
        TextView id = (TextView)v.findViewById(R.id.id);
        TextView title = (TextView)v.findViewById(R.id.title);
        TextView category = (TextView)v.findViewById(R.id.category);
        TextView tradeLocation = (TextView)v.findViewById(R.id.tradeLocation);
        TextView auctionStarTime = (TextView)v.findViewById(R.id.auctionStartTime);
        TextView auctionTotalTime = (TextView)v.findViewById(R.id.auctionTimeLimit);

        boardNumber.setText(userList.get(position).getboardNumber());
        id.setText(userList.get(position).getid());
        title.setText(userList.get(position).gettitle());
        category.setText(userList.get(position).getcategory());
        tradeLocation.setText(userList.get(position).gettradeLocation());
        auctionStarTime.setText(userList.get(position).getauctionStartTime());
        auctionTotalTime.setText(userList.get(position).getauctionTimeLimit());


        //특정 user에 아이디값을 그대로 반환할수 있게 해준다
        v.setTag(userList.get(position).getboardNumber());
        Log.i("CategoryListAdapter", "리스트 뷰) getView 동작 끝");

        return v;

    }
}
