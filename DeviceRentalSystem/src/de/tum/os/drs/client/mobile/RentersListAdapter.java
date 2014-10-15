package de.tum.os.drs.client.mobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import de.tum.os.drs.client.mobile.model.Renter;

public class RentersListAdapter extends ArrayAdapter<Renter> implements
		Filterable {

	private List<Renter> list;
	private Context context;

	public RentersListAdapter(Context context, List<Renter> objects) {
		super(context, android.R.layout.simple_list_item_1, objects);

		list = objects;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.renter_list_item, parent,
				false);

		TextView name = (TextView) rowView.findViewById(R.id.renter_name);
		name.setText(list.get(position).getName());
		
		return rowView;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Filter getFilter() {

		return myFilter;
	}

	Filter myFilter = new Filter() {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			ArrayList<Renter> tempList = new ArrayList<Renter>();

			if (constraint != null && list != null) {

				int length = list.size();
				int i = 0;
				while (i < length) {
					Renter item = list.get(i);
					// do whatever you wanna do here
					// adding result set output array

					String lower = constraint.toString().toLowerCase();

					if (item.getName() != null
							&& item.getName().toLowerCase().contains(lower)) {
						tempList.add(item);
					}

					i++;
				}
				// following two lines is very important
				// as publish result can only take FilterResults objects
				filterResults.values = tempList;
				filterResults.count = tempList.size();

				Log.i("Test", "Count: " + tempList.size());
			}
			return filterResults;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence contraint,
				FilterResults results) {
			list = (List<Renter>) results.values;

			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	};

}
