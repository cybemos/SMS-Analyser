package com.cybemos.analyser.ui.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cybemos.analyser.R;

/**
 * @version 1.0
 * @author <a href="mailto:sonet.e1301490@etud.univ-ubs.fr">Nicolas Sonet</a>
 */

public class SimpleAdapter<T> extends BaseAdapter {

    private final List<T> data;
    private final Context context;

    public SimpleAdapter(Context c, List<T> data) {
        super();
        context = c;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView == null) {
            view = inflater.inflate(R.layout.item_simple_text, parent, false);
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(data.get(position).toString());
        return view;
    }

}
