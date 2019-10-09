package com.thecodelife.quotesfactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class GetQuoteData extends AsyncTask<Void, Void, List<QuoteData>> {
    static final String DEFAULT = "random";
    private static final String TAG = "getQuoteData";
    private static List<QuoteData> quoteData;
    private static boolean firstRun = true;
    private static String lastTag = "";
    private static int page = 1;
    private static QuotesAdapter quotesAdapter;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView quotesView;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    private String newTag;
    private boolean tagCheck;

    public static void setActivity(Activity activityRef){
        activity = activityRef;
    }

    GetQuoteData(String tag) {
        if(tag.contentEquals("last"))
            newTag = lastTag;
        else
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
                StringBuffer response = new StringBuffer();
                while ((readLine = in .readLine()) != null) {
                    response.append(readLine);
                }
                in .close();
                JSONObject newJSONObject = new JSONObject(response.toString());
                JSONArray quotes = newJSONObject.getJSONArray("quotes");
                for(int i = 0; i<quotes.length();i++){
                    quoteData.add(new QuoteData(quotes.getJSONObject(i).getString("body"),quotes.getJSONObject(i).getString("author")));
                }
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
        if(firstRun){ //ChangeType = 0
            quoteData = Data;
            updateRecyclerView(0,-1,-1);
            firstRun = false;
        } else if(tagCheck){ //ChangeType = 1
            int currentSize = quoteData.size();
            quoteData.addAll(quoteData.size(),Data);
            updateRecyclerView(1,currentSize,Data.size());

        } else {//ChangeType = 2
            Log.d(TAG, "onPostExecute: Change Type 2");
            quoteData.clear();
            quoteData.addAll(quoteData.size(),Data);
            updateRecyclerView(2,-1,-1);
        }
    }

    private void updateRecyclerView(int changeType, int currentSize, int dataSize){
        if(changeType == 0) {
            quotesView = activity.findViewById(R.id.quoteRecyclerView);
            quotesAdapter = new QuotesAdapter(quoteData, activity);
            quotesView.setHasFixedSize(true);
            quotesView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
            quotesView.setAdapter(quotesAdapter);
        } else
        if(changeType == 1)
            quotesAdapter.notifyItemRangeInserted(currentSize,dataSize);
        else
        if(changeType == 2)
            quotesAdapter.notifyDataSetChanged();
    }
}