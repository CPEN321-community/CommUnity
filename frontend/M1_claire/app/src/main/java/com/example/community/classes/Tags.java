package com.example.community.classes;

import android.graphics.Color;
import android.widget.TextView;

import org.json.JSONArray;

public class Tags {
    private TextView fruit;
    private TextView veg;
    private TextView nut;
    private boolean fruitClicked;
    private boolean vegClicked;
    private boolean nutClicked;
    public Tags(TextView fruit, TextView veg, TextView nut) {
        this.fruit = fruit;
        this.veg = veg;
        this.nut = nut;
        fruit.setOnClickListener(v -> {
                fruitClicked = !fruitClicked;
            if (fruitClicked) {
                fruit.setBackgroundColor(Color.parseColor("#00ff00"));;
            }
            else {
                fruit.setBackgroundColor(Color.parseColor("#c4c4c4"));;
            }
        });

        veg.setOnClickListener(v -> {
            vegClicked = !vegClicked;
            if (vegClicked) {
                veg.setBackgroundColor(Color.parseColor("#00ff00"));;
            }
            else {
                veg.setBackgroundColor(Color.parseColor("#c4c4c4"));;
            }
        });

        nut.setOnClickListener(v -> {
            nutClicked = !nutClicked;
            if (nutClicked) {
                nut.setBackgroundColor(Color.parseColor("#00ff00"));;
            }
            else {
                nut.setBackgroundColor(Color.parseColor("#c4c4c4"));;
            }
        });

    }

    public JSONArray getJSONArr() {
        JSONArray arr = new JSONArray();
        if (fruitClicked) {
            arr.put("Fruit");
        }
        if (vegClicked) {
            arr.put("Vegetable");
        }
        if (nutClicked) {
            arr.put("Nut");
        }
        return arr;
    }
}
