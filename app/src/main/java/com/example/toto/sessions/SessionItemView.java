package com.example.toto.sessions;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.example.toto.R;
import com.example.toto.utils.ListViewItem;


public class SessionItemView extends ListViewItem<Pair<String, String>> {

    public SessionItemView(Context context, Pair<String, String> info_date, int layout) {
        super(context,info_date,layout);
    }

    @Override
    public View getView() {
        View mView = super.getView();

        //check what type of layout
        if (resourceLayout==R.layout.session_item_layout) {
            TextView date = (TextView) mView.findViewById(R.id.session_date_id);
            TextView time = (TextView) mView.findViewById(R.id.session_time_id);

            //need to add status to user model
            date.setText(element.first);
            time.setText(element.second);
        }

        return mView;
    }

}
