package com.thecodelife.quotesfactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static List<String> tagList;

    static{
        tagList = new ArrayList<>();
        tagList.add("random");
        tagList.add("funny");
        tagList.add("love");
        tagList.add("life");
        tagList.add("motivation");
        tagList.add("happiness");
        tagList.add("nature");
        tagList.add("truth");
        tagList.add("knowledge");
        tagList.add("science");
        tagList.add("fear");
        tagList.add("faith");
        tagList.add("history");
        tagList.add("education");
        tagList.add("money");
        tagList.add("relationship");
        tagList.add("freedom");
        tagList.add("art");
        tagList.add("movies");
        tagList.add("poetry");
        tagList.add("morning");
        tagList.add("night");
        tagList.add("imagination");
        tagList.add("famous");
        tagList.add("positive");
        tagList.add("leadership");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetQuoteData(this, tagList.get(0)).execute();
    }

    public static class GetQuoteData extends AsyncTask<Void, Void, List<QuoteData>> {
        private StringBuffer response;
        private static final String TAG = "getQuoteData";
        private static boolean firstRun = true;
        private boolean tagCheck;
        private static List<QuoteData> quoteData;
        static String lastTag = "";
        private static int page=1;
        private String newTag;
        @SuppressLint("StaticFieldLeak")
        private Activity activity;
        @SuppressLint("StaticFieldLeak")
        private static QuotesAdapter quotesAdapter;
        @SuppressLint("StaticFieldLeak")
        private static RecyclerView quotesView;

        GetQuoteData(Activity activity, String tag) {
            this.activity = activity;
            newTag = tag;
        }

        @Override
        protected List<QuoteData> doInBackground(Void... voids) {
            List quoteData = new ArrayList<>();
            try {
                String readLine;
                String url;
                URL requestURL;
                if(lastTag.contentEquals(newTag)) {
                    page += 1;
                    tagCheck=true;
                }
                else{
                    lastTag=newTag;
                    page=1;
                    tagCheck=false;
                }
                if(newTag.contentEquals("random"))
                    requestURL= new URL("https://favqs.com/api/quotes");
                else {
                    url = "https://favqs.com/api/quotes?filter=" + newTag + "&type=tag&page=" +page;
                    requestURL = new URL(url);
                }
                HttpsURLConnection newConnection = (HttpsURLConnection)requestURL.openConnection();
                newConnection.setRequestMethod("GET");
                newConnection.setRequestProperty("Authorization",activity.getString(R.string.API));
                int responseCode = newConnection.getResponseCode();

                if(responseCode == HttpsURLConnection.HTTP_OK){
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(newConnection.getInputStream()));
                    response = new StringBuffer();
                    while ((readLine = in .readLine()) != null) {
                        response.append(readLine);
                    } in .close();
                }
                JSONObject newJSONObject = new JSONObject(response.toString());
                JSONArray quotes = newJSONObject.getJSONArray("quotes");
                for(int i = 0; i<quotes.length();i++){
                    quoteData.add(new QuoteData(quotes.getJSONObject(i).getString("body"),quotes.getJSONObject(i).getString("author")));
                }
            } catch (MalformedURLException malformedURLException){
                Log.e(TAG, "doInBackground: MalformedURLException ",malformedURLException );
            } catch (IOException ioe) {
                Log.e(TAG, "doInBackground: IOException ", ioe);
            }catch (JSONException jsonException){
                Log.e(TAG, "doInBackground: JSONException", jsonException);
            }
            return quoteData;
        }

        @Override
        protected void onPostExecute(List<QuoteData> Data) {
            super.onPostExecute(Data);
                if(firstRun){
                    quoteData = Data;
                    initializeRecyclerView();
                    firstRun = false;
                } else if(tagCheck){
                    int currentSize = quoteData.size();
                    quoteData.addAll(quoteData.size(),Data);
                    quotesAdapter.notifyItemRangeInserted(currentSize,Data.size());
                } else {
                    quoteData.clear();
                    quoteData.addAll(quoteData.size(),Data);
                    quotesAdapter.notifyDataSetChanged();
                }
        }

        private void initializeRecyclerView(){
            createTagList();
            quotesView = activity.findViewById(R.id.quoteRecyclerView);
            quotesAdapter = new QuotesAdapter(quoteData, activity);
            quotesView.setHasFixedSize(true);
            quotesView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
            quotesView.setAdapter(quotesAdapter);
        }

        void createTagList(){
            RecyclerView tagRecyclerView = activity.findViewById(R.id.tagRecyclerView);
            TagAdapter tagAdapter = new TagAdapter(activity,tagList);
            tagRecyclerView.setHasFixedSize(true);
            tagRecyclerView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            tagRecyclerView.setAdapter(tagAdapter);
        }
    }
}
