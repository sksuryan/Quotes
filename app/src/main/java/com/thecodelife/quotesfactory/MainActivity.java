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
        createTagList();
        GetQuoteData.setActivity(this);
        GetQuoteData getQuoteData = new GetQuoteData(GetQuoteData.DEFAULT);
        getQuoteData.execute();
    }

    void createTagList(){
        RecyclerView tagRecyclerView = findViewById(R.id.tagRecyclerView);
        TagAdapter tagAdapter = new TagAdapter(this,tagList);
        tagRecyclerView.setHasFixedSize(true);
        tagRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        tagRecyclerView.setAdapter(tagAdapter);
    }
}
