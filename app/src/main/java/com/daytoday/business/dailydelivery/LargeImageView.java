package com.daytoday.business.dailydelivery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LargeImageView extends PopupWindow {

    View view;
    Context mContext;
    PhotoView photoView;
    ViewGroup parent;

    public LargeImageView(Context ctx, View v, String imageUrl, Uri uri) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.large_profile_view, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.view = getContentView();
        ImageButton closeButton = this.view.findViewById(R.id.ib_close);
        ProgressBar progressBar = this.view.findViewById(R.id.progressbar);
        closeButton.setOnClickListener(view -> dismiss());
        setOutsideTouchable(true);
        setFocusable(true);
        photoView = view.findViewById(R.id.image);
        photoView.setMaximumScale(6);
        parent = (ViewGroup) photoView.getParent();
        showAtLocation(v, Gravity.CENTER, 0, 0);
        if(uri!=null)
        {
            progressBar.setVisibility(View.GONE);
            photoView.setImageURI(uri);
            return;
        }
        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .resize(5000, 5000)
                    .centerCrop()
                    .into(photoView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }else
        {
            progressBar.setVisibility(View.GONE);
            Picasso.get().load(R.drawable.profile001).into(photoView);
        }
    }
}
