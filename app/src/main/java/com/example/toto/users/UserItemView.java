package com.example.toto.users;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toto.R;
import com.example.toto.utils.ListViewItem;


public class UserItemView extends ListViewItem<User> {
    private View mView;


    //Event<User> event;

//    private final View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            //record like
//            element.setLike(!element.isLike());
//            event.hasChanged();
//            if (element.isLike())
//                likeButton.setImageBitmap(BitmapFactory.decodeResource(Resources.getSystem(),R.mipmap.like_true));
//            else
//                likeButton.setImageBitmap(BitmapFactory.decodeResource(Resources.getSystem(),R.mipmap.like));
//        }
//    };

    public UserItemView(Context context, User user, int layout) {
        super(context,user,layout);
//        event = new Event<>();
//        event.addObserver(new LikeEventHandler(meme,null));
    }

    @Override
    public View getView() {
        mView=super.getView();

        //check what type of layout: admin_user_list or normal user_list
        if (resourceLayout==R.layout.user_admin_item_layout){
            //admin controls are present
            TextView username = (TextView) mView.findViewById(R.id.username_item_id);
            TextView status = (TextView) mView.findViewById(R.id.status_item_id);
            Button acceptButton = (Button) mView.findViewById(R.id.item_accept_button);
            Button declineButton = (Button) mView.findViewById(R.id.item_decline_button);

            //need to add status to user model
            username.setText(element.getUsername());
            status.setText("Pending");
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //change status in user model
                }
            });
            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //change status
                }
            });
        }

//        final ImageView imageView = (ImageView) mView.findViewById(R.id.imageView);
//        likeButton = (ImageButton) mView.findViewById(R.id.likeButton);
//        likeButton.setOnClickListener(clickListener);
//
//        if(element.isLike()){
//            likeButton.setImageBitmap(BitmapFactory.decodeResource(Resources.getSystem(),R.mipmap.like_true));
//        }
//
//        //load bitmap from url
//        //Configurating imageLoader
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
//
//        imageLoader.displayImage(element.getUrl().get(0),imageView,options);

        return mView;
    }

}
