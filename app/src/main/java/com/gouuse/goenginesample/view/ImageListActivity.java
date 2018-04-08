package com.gouuse.goenginesample.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gouuse.goenginesample.R;
import com.gouuse.goenginesample.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    private RecyclerView mRvMain;
    private List<String> mImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        mRvMain = findViewById(R.id.rv_main);
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b28220efb5e.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b28221b6398.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b28222885ec.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b2822346a2f.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b2822452175.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b282251c07c.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b282265c815.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b2822713af2.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b2822953fe5.jpg");
        mImageList.add("http://img.juemei.com/album/2016-08-16/57b2822b2c64d.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5fa59f52a.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5fa835387.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5fa911860.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5fa9e226e.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5faad1799.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5faba7649.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5fac8c5c6.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5fad34f13.jpg");
        mImageList.add("http://img.juemei.com/album/2016-09-04/57cb5faf282a5.jpg");
        ImageAdapter mAdapter = new ImageAdapter(mImageList);
        mRvMain.setLayoutManager(new GridLayoutManager(this, 2));
        mRvMain.setAdapter(mAdapter);
    }
}
