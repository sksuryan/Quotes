package com.thecodelife.quotesfactory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    private static final String TAG = "TagAdapter";
    private List<String> tagList;
    private int savedPosition;
    private Activity activity;
    private Context context;

    public TagAdapter(Activity activity,List<String> tagList) {
        this.tagList = tagList;
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View newView = layoutInflater.inflate(R.layout.tag_holder,parent,false);
        return new TagViewHolder(newView);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, final int position) {
        holder.tagText.setText(tagList.get(position));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                savedPosition = position;
                notifyDataSetChanged();
                new GetQuoteData(tagList.get(position))
                        .execute();
            }
        });
        if(position == savedPosition){
            holder.relativeLayout.startAnimation(AnimationUtils.loadAnimation(context,R.anim.tag_animation));
            holder.relativeLayout.setBackgroundResource(R.drawable.tag_selected);
            holder.tagText.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark,activity.getTheme()));
        } else {
            holder.relativeLayout.setBackgroundResource(R.drawable.quote_bg);
            holder.tagText.setTextColor(activity.getResources().getColor(R.color.white,activity.getTheme()));
        }
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder{
        TextView tagText;
        RelativeLayout relativeLayout;
        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagText = itemView.findViewById(R.id.tagTextView);
            relativeLayout = itemView.findViewById(R.id.tagLayout);
            relativeLayout.setClickable(true);
        }
    }
}
