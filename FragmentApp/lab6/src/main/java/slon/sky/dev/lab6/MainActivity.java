package slon.sky.dev.lab6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private Button mLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] urls = new String[]{
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_53_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_239_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_309_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_551_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_672_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_61_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_707_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_710_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_780_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_65_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_383_sm1.jpg",
                "http://www.fcdynamo.kiev.ua/content/catalog/item2_581_sm1.jpg"

        };

        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(35);
        nums.add(9);
        nums.add(26);
        nums.add(42);
        nums.add(40);
        nums.add(34);
        nums.add(5);
        nums.add(8);
        nums.add(32);
        nums.add(10);
        nums.add(11);
        nums.add(41);

        ArrayList<String> names = new ArrayList<>();
        names.add("Koval");
        names.add("Morozuk");
        names.add("Lukyanchuk");
        names.add("Tymchik");
        names.add("Hacheridi");
        names.add("Antunesh");
        names.add("Shepelev");
        names.add("Fedorchuk");
        names.add("Yarmolenko");
        names.add("Yaremchuk");
        names.add("Besedin");
        names.add("Buryalsky");

        GridView grid = (GridView) findViewById(R.id.grid);
        GridAdapter adapter = new GridAdapter(this, nums, names);
        grid.setAdapter(adapter);

        final LoadTask loadTask = new LoadTask(this, adapter);

        mLoad = (Button) findViewById(R.id.load);
        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadTask.execute(urls);
                    }
                }).start();

                mLoad.setEnabled(false);
            }
        });
    }
}
