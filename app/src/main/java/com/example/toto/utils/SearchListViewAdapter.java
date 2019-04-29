package com.example.toto.utils;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.example.toto.R;
import com.example.toto.users.User;
import com.example.toto.users.UserSearchItemView;

import java.util.List;

public class SearchListViewAdapter extends ListableViewAdapter {

    public SearchListViewAdapter(Context a, List d) {
        super(a, d);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (data.size()>0) {
            return new UserSearchItemView(context, (Pair<User, String>) data.get(i), R.layout.tutor_search_item_layout).getView();
        }
        return null;
    }
}
