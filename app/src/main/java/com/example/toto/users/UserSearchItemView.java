package com.example.toto.users;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.example.toto.R;
import com.example.toto.utils.ListViewItem;


public class UserSearchItemView extends ListViewItem<Pair<User, String>> {

    public UserSearchItemView(Context context, Pair<User, String> info_tutor, int layout) {
        super(context,info_tutor,layout);
    }

    @Override
    public View getView() {
        View mView = super.getView();

        //check what type of layout
        if (resourceLayout==R.layout.tutor_search_item_layout) {
            TextView username = (TextView) mView.findViewById(R.id.username_item_val_id);
            TextView subjects_list = (TextView) mView.findViewById(R.id.subject_list_id_val);

            //need to add status to user model
            username.setText(element.first.getUsername());
            subjects_list.setText(element.second);
        }

        return mView;
    }

}
