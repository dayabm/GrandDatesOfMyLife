package com.granddatesofmylife;

import java.util.ArrayList;
import com.entities.Event;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventListArrayAdapter extends ArrayAdapter<Event> {
	private final Context context;
	private final ArrayList<Event> eventList;
	
	public EventListArrayAdapter(Context context, ArrayList<Event> eventlist){
		super(context, R.layout.list_dates, eventlist);
		this.context = context;
		this.eventList = eventlist;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_dates, parent, false);
		
		TextView date  = (TextView)rowView.findViewById(R.id.date);
		TextView name  = (TextView)rowView.findViewById(R.id.name);
		
		name.setText(eventList.get(position).getName());
		date.setText(eventList.get(position).getDate());
		
		return rowView;
	}
		
}
