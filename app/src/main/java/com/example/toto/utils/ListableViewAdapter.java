package com.example.toto.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.toto.R;
import com.example.toto.users.User;
import com.example.toto.users.UserItemView;

import java.util.List;


public class ListableViewAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> data;
    //private ImageLoader imageLoader;
    private ListViewItem viewItem;
    private boolean isAdmin;

    public ListableViewAdapter(Context a, List<T> d, boolean isAdmin){
        context=a;
        data=d;
        this.isAdmin = isAdmin;
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .build();
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(config);
    }

    @Override
    public int getCount() {
        if(data.size()<=0)
            return 0;
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (data.size()>0) {
            if (data.get(0) instanceof User && isAdmin)
                return new UserItemView(context, (User) data.get(i), R.layout.user_admin_item_layout).getView();
            //add other cases
            return null;
        }
        return null;
    }
}
