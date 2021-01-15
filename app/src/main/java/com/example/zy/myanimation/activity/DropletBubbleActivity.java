package com.example.zy.myanimation.activity;

import android.os.Bundle;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.droplet_bubble.DropletBubbles;

import androidx.appcompat.app.AppCompatActivity;

public class DropletBubbleActivity extends AppCompatActivity {

    DropletBubbles dropletBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droplet_bubble);

        dropletBubble = findViewById(R.id.droplet_bubble);

        init();
    }

    private void init() {
        dropletBubble.setOnMeasureBaseLineCallback(height ->
                dropletBubble.setBaseLine(0.7f)
        );
        dropletBubble.setOnClickListener(view -> {
            dropletBubble.setBaseLine(0.5f);
        });
    }
}
