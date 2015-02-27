package com.airmh.soundllysdktest;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 결과값을 List로 보여주기 위한 Adpater
 * @author AirMH
 *
 */
public class ResultListAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private ArrayList<AttributesParcelable> itemList;
	
	public ResultListAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
		itemList = new ArrayList<AttributesParcelable>();
	}
	
	public void setData(ArrayList<AttributesParcelable> itemList){
		this.itemList = itemList;
	}

	@Override
	public int getCount() {
		return itemList == null ? 0 : itemList.size();
	}

	@Override
	public Object getItem(int position) {
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_item, null);
		
		TextView typeTextView = (TextView)convertView.findViewById(R.id.typeTextView);
		TextView keyTextView = (TextView)convertView.findViewById(R.id.keyTextView);
		TextView valueTextView = (TextView)convertView.findViewById(R.id.valueTextView);
		
		typeTextView.setText(itemList.get(position).getType());
		keyTextView.setText(itemList.get(position).getKey());
		valueTextView.setText(itemList.get(position).getValue());
		
		return convertView;
	}

}
