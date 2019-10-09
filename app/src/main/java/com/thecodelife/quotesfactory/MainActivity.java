package com.thecodelife.quotesfactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

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
        GetQuoteData getQuoteData = new GetQuoteData(GetQuoteData.DEFAULT);
        GetQuoteData.setActivity(this);
        getQuoteData.execute();
    }

    void createTagList(){
        RecyclerView tagRecyclerView = findViewById(R.id.tagRecyclerView);
        TagAdapter tagAdapter = new TagAdapter(this,tagList);
        tagRecyclerView.setHasFixedSize(true);
        tagRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        tagRecyclerView.setAdapter(tagAdapter);
    }
}
