package com.example.toto.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.toto.R;

import java.util.List;
import java.util.Map;

/**
 * TODO Improve comment with correct reference
 * Reference: Adapted from https://github.com/vogellacompany/codeexamples-android/blob/master/com.vogella.android.test.traceview.list/src/de/vogella/android/listactivity/MySimpleArrayAdapter.java
 */
public class CheckboxArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private Map<String, Boolean> subject_map;

    public CheckboxArrayAdapter(Context context, List<String> values, Map<String, Boolean> subject_map) {
        super(context, R.layout.checkboxrow, values);
        this.context = context;
        this.values = values;
        this.subject_map = subject_map;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.checkboxrow, parent, false);
        final CheckBox checkbox=(CheckBox)rowView.findViewById(R.id.checkBox);
        checkbox.setText(values.get(position));
        if (subject_map.get(values.get(position))) {
            checkbox.setChecked(true);
        }

        // Register listener
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject_map.put(checkbox.getText().toString(), checkbox.isChecked()); //Cannot use map.replace because such call requires API level 24 (current min is 19)
            }
        });

        return rowView;
    }

    public Map<String, Boolean> getSubject_map() {
        return subject_map;
    }
}