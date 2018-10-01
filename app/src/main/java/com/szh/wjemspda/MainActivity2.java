package com.szh.wjemspda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.saomiao4xinshiqi.R;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by szh on 2017-05-08.
 */

public class MainActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        GridView gridView =(GridView)findViewById(R.id.gridview);
        ArrayList mItemlist = new ArrayList<>();
        HashMap map = new HashMap();
        map.put("mImageView",R.drawable.scan);
        map.put("mTextView","扫描");
        mItemlist.add(map);
        map = new HashMap();
        map.put("mImageView",R.drawable.print_empty);
        map.put("mTextView","查询打印");
        mItemlist.add(map);
        map = new HashMap();
        map.put("mImageView",R.drawable.data);
        map.put("mTextView","数据管理");
        mItemlist.add(map);
        map = new HashMap();
        map.put("mImageView",R.drawable.shezhi);
        map.put("mTextView","设置");
        mItemlist.add(map);
        SimpleAdapter mAdaper = new SimpleAdapter(this, mItemlist,R.layout.main_item_layout, new String[] { "mImageView", "mTextView" }, new int[] { R.id.img_shoukuan, R.id.txt_shoukuan });
        gridView.setAdapter(mAdaper);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(MainActivity2.this,ScanActivity.class));
                        break;
                    case 1:


                        Toast.makeText(getApplicationContext(),getExternalStorageDirectory().toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity2.this,ShujuguanliActivity.class));

                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),"功能开发中...",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"功能开发中...",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        Realm.init(this);
    }
}
