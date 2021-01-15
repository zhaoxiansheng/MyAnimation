package com.example.zy.myanimation.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.zy.myanimation.R;

import androidx.appcompat.app.AppCompatActivity;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

public class GpuImageActivity extends AppCompatActivity {

    ImageView gpuImage;
    SeekBar gpuImageSeekBar;
    private GPUImage gpuImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu_image);

        gpuImage = findViewById(R.id.gpu_image);

        gpuImageSeekBar = findViewById(R.id.gpu_image_seek_bar);

        init();
    }

    private void init() {
        gpuImageSeekBar.setMax(10);
        gpuImageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                gpuImage.setImageBitmap(getGpuImage(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        gpuImage.setImageBitmap(getGpuImage(0));
    }

    private Bitmap getGpuImage(int progress) {
        gpuImg = new GPUImage(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
        gpuImg.setImage(bitmap);
        gpuImg.setFilter(new GPUImageSaturationFilter(progress));
        bitmap = gpuImg.getBitmapWithFilterApplied();
        return bitmap;
    }
}
