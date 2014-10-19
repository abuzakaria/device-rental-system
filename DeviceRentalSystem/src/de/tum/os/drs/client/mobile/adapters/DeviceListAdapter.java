package de.tum.os.drs.client.mobile.adapters;

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
import android.widget.ImageView;
import android.widget.TextView;
import de.tum.os.drs.client.mobile.MainActivity;
import de.tum.os.drs.client.mobile.R;
import de.tum.os.drs.client.mobile.R.id;
import de.tum.os.drs.client.mobile.R.layout;
import de.tum.os.drs.client.mobile.model.Device;

public class DeviceListAdapter extends ArrayAdapter<Device> implements
		Filterable {

	private List<Device> list;
	private MainActivity context;
	private List<Device> original;

	public DeviceListAdapter(MainActivity context, List<Device> objects) {
		super(context, R.layout.device_list_item, objects);

		list = new ArrayList<Device>(objects);
		original = new ArrayList<Device>(objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.device_list_item, parent,
				false);

		TextView name = (TextView) rowView.findViewById(R.id.device_name);
		TextView imei = (TextView) rowView.findViewById(R.id.device_imei);
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.device_image);
		name.setText(list.get(position).getName());
		imei.setText(list.get(position).getImei());

		imageView.setImageResource(context.getDeviceImage(list.get(position).getName()));
		
		return rowView;
	}

	@Override
	public Device getItem(int position) {
		return list.get(position);
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
			ArrayList<Device> tempList = new ArrayList<Device>();

			String lower = constraint.toString().toLowerCase();

			if (lower == null || lower.isEmpty()) {
				filterResults.values = original;
				filterResults.count = original.size();

			} else {

				if (list != null) {

					int length = list.size();
					int i = 0;
					while (i < length) {
						Device item = list.get(i);
						// do whatever you wanna do here
						// adding result set output array

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

				}

			}

			return filterResults;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence contraint,
				FilterResults results) {
			list = (List<Device>) results.values;

			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	};

}
