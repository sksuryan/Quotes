package com.thecodelife.quotesfactory;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuotesViewHolder> {
    private static final String TAG = "QuotesAdapter";
    private List<QuoteData> quoteData;
    private Activity activity;
    private Context context;
    QuotesAdapter(List<QuoteData> quoteData, Activity activity) {
        this.quoteData = quoteData;
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @NonNull
    @Override
    public QuotesAdapter.QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View newView =layoutInflater.inflate(R.layout.quote_holder,parent,false);
        return new QuotesViewHolder(newView);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuotesAdapter.QuotesViewHolder holder, int position) {
        holder.quoteLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.quote_animation));
        holder.quote.setText(quoteData.get(position).getQuote());
        holder.authorName.setText(quoteData.get(position).getAuthorName());
        View.OnLongClickListener newLongClickListener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("quote",holder.quote.getText());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context,"Copied!",Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        holder.quoteLayout.setOnLongClickListener(newLongClickListener);
        if(position == getItemCount()-1) {
            GetQuoteData getQuoteData = new GetQuoteData("last");
            getQuoteData.execute();
        }
    }

    @Override
    public int getItemCount() {
        return quoteData.size();
    }

    static class QuotesViewHolder extends RecyclerView.ViewHolder {
        TextView quote, authorName;
        RelativeLayout quoteLayout;
        QuotesViewHolder(@NonNull View itemView) {
            super(itemView);
            quote = itemView.findViewById(R.id.quote_holder);
            authorName = itemView.findViewById(R.id.author_name);
            quoteLayout = itemView.findViewById(R.id.quoteLayout);
            quoteLayout.setLongClickable(true);
        }
    }
}
