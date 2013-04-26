package com.granddatesofmylife;

import java.util.ArrayList;
import java.util.Calendar;

import com.entities.Event;
import com.sqlhelper.DBController;
import android.os.Bundle;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class List extends ListActivity {

	DBController dbController = new DBController(this);
	private ArrayList<Event> mEventList; 
	private EventListArrayAdapter mEventListArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		registerForContextMenu(getListView());
		bindListView();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	            .getMenuInfo();
		final Event selectedEvent = (Event) getListAdapter().getItem(info.position);

	    switch (item.getItemId()) {
	    
	    case R.id.delete_item:
			dbController.deleteEvent(selectedEvent.getId());
			dbController.close();
	    	mEventList.remove(info.position);
	    	mEventListArrayAdapter.notifyDataSetChanged();
	        return true;

	    case R.id.edit_item :
	    	final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.add);
			dialog.setTitle("Edit");
			Button save = (Button) dialog.findViewById(R.id.save);
			
			
			EditText nameText = (EditText) dialog.findViewById(R.id.name);
			EditText dateText = (EditText) dialog.findViewById(R.id.date);
			
			nameText.setText(selectedEvent.getName());
			dateText.setText(selectedEvent.getDate());
			
			save.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (validateBeforeSave(dialog)) {
						Update(dialog,selectedEvent.getId());
						dialog.dismiss();
						bindListView();
					}
				}
			});
			dialog.show();
		    return true;
		    
	    case R.id.add_to_calendar_item:
	    	Calendar cal = Calendar.getInstance();              
	    	Intent intent = new Intent(Intent.ACTION_EDIT);
	    	intent.setType("vnd.android.cursor.item/event");
	    	intent.putExtra("beginTime", cal.getTimeInMillis());
	    	intent.putExtra("allDay", true);
	    	intent.putExtra("rrule", "FREQ=YEARLY");
	    	intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
	    	intent.putExtra("title", selectedEvent.getName());
	    	startActivity(intent);
	    	return true;
	    }
	    return false;
	}
	

	private void bindListView() {
		mEventList = dbController.getAllEvents();
		dbController.close();
		mEventListArrayAdapter = new EventListArrayAdapter(this, mEventList); 
		setListAdapter(mEventListArrayAdapter);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.items, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Add:
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.add);
			dialog.setTitle("Add");
			Button save = (Button) dialog.findViewById(R.id.save);

			save.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (validateBeforeSave(dialog)) {
						Save(dialog);
						dialog.dismiss();
						bindListView();
					}
				}
			});
			dialog.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void Update(final Dialog dialog, int eventId) {
		EditText nameText = (EditText) dialog.findViewById(R.id.name);
		EditText dateText = (EditText) dialog.findViewById(R.id.date);

		Event event = new Event();
		event.setName(nameText.getText().toString().trim());
		event.setDate(dateText.getText().toString().trim());
		event.setId(eventId);
		
		dbController.updateEvent(event);
		dbController.close();
		if (eventId <= 0) {
			Toast.makeText(this, "An error occurred saving the data",
					Toast.LENGTH_LONG).show();
		}
	}

	
	private void Save(final Dialog dialog) {
		EditText nameText = (EditText) dialog.findViewById(R.id.name);
		EditText dateText = (EditText) dialog.findViewById(R.id.date);

		Event event = new Event();
		event.setName(nameText.getText().toString().trim());
		event.setDate(dateText.getText().toString().trim());
		long eventId = dbController.insertEvent(event);
		dbController.close();
		if (eventId <= 0) {
			Toast.makeText(this, "An error occurred saving the data",
					Toast.LENGTH_LONG).show();
		}
	}

	private boolean validateBeforeSave(final Dialog dialog) {
		EditText nameText = (EditText) dialog.findViewById(R.id.name);
		EditText dateText = (EditText) dialog.findViewById(R.id.date);

		String name = nameText.getText().toString().trim();
		if (name.length() == 0) {
			Toast.makeText(this, "Oops! Name missing", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		String date = dateText.getText().toString().trim();
		if (date.length() == 0) {
			Toast.makeText(this, "Oops! Date missing", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		return true;
	}

}
