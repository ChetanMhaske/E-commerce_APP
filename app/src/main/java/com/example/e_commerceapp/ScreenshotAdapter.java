package com.example.e_commerceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ScreenshotViewHolder> {
    private Context context;
    private List<String> screenshots;

    public ScreenshotAdapter(Context context, List<String> screenshots) {
        this.context = context;
        this.screenshots = screenshots;
    }

    @NonNull
    @Override
    public ScreenshotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_screenshot, parent, false);
        return new ScreenshotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenshotViewHolder holder, int position) {
        String imageUrl = screenshots.get(position);
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions().transform(new RoundedCorners(12)))
                .into(holder.screenshotImage);
    }

    @Override
    public int getItemCount() {
        return screenshots.size();
    }

    static class ScreenshotViewHolder extends RecyclerView.ViewHolder {
        ImageView screenshotImage;

        public ScreenshotViewHolder(@NonNull View itemView) {
            super(itemView);
            screenshotImage = itemView.findViewById(R.id.screenshotImage);
        }
    }
}