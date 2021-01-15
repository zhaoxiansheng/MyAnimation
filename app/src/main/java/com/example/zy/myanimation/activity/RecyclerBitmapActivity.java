package com.example.zy.myanimation.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.utils.bitmap.RecyclingImageView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class RecyclerBitmapActivity extends AppCompatActivity {

    private static final String TAG = RecyclerBitmapActivity.class.getSimpleName();

    RecyclingImageView recyclerImage;
    private ArrayList<Drawable> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_bitmap);

        recyclerImage = findViewById(R.id.recycler_image);


        arrayList = new ArrayList<>();
        arrayList.add(getResources().getDrawable(R.drawable.book));
        arrayList.add(getResources().getDrawable(R.drawable.micphone));
        arrayList.add(getResources().getDrawable(R.drawable.car));
        arrayList.add(getResources().getDrawable(R.drawable.camera));
        arrayList.add(getResources().getDrawable(R.drawable.bg));

      /*  Observable<Drawable> listObservable = Observable.fromIterable(arrayList);
        Observable<Long> timeObservable = Observable.interval(300, TimeUnit.MILLISECONDS);
        Observable.zip(listObservable, timeObservable, new BiFunction<Drawable, Long, Object>() {
            @Override
            public Object apply(Drawable drawable, Long aLong) throws Exception {
                recyclerImage.setImageDrawable(drawable);
                Log.d(TAG, "apply: " + aLong);
                return aLong;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
