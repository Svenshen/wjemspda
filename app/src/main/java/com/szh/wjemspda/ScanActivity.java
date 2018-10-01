package com.szh.wjemspda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.administrator.saomiao4xinshiqi.R;
import com.szh.wjemspda.dao.MailDao;
import com.szh.wjemspda.dao.impl.MailDaoImpl;

import java.util.List;


public class ScanActivity extends Activity {
//    DBManager dbManager;
    Button newbutton;Button querybutton;Button shiyonbutton;
    EditText chepaiText ;
    Spinner picispinner;
    ArrayAdapter<Integer> arr_adapter;
    MailDao mailDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dbManager = new DBManager(this);
        setContentView(R.layout.activity_scan);
        mailDao = new MailDaoImpl();
        newbutton = (Button) findViewById(R.id.scan_xinjianbutton);

        querybutton = (Button) findViewById(R.id.scan_chaxunbutton);
        shiyonbutton = (Button) findViewById(R.id.scan_quedingbutton);
        chepaiText = (EditText) findViewById(R.id.scan_chepaieditText);
        picispinner = (Spinner) findViewById(R.id.scan_spinner);
        newbutton.setEnabled(false);
        shiyonbutton.setEnabled(false);
        querybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //ArrayList<Integer> list = dbManager.querybychepai(chepaiText.getText().toString());
                List<Integer> list =mailDao.findByChepaidistinctCheci(chepaiText.getText().toString());

                arr_adapter= new ArrayAdapter<Integer>(ScanActivity.this,android.R.layout.simple_spinner_item,list);
                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                picispinner.setAdapter(arr_adapter);
                newbutton.setEnabled(true);
                if(!list.isEmpty()){
                    shiyonbutton.setEnabled(true);
                }else{
                    shiyonbutton.setEnabled(false);
                }
            }

        });

        newbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanActivity.this,ScanActivity2.class);
                intent.putExtra("isnew",true);
                intent.putExtra("pici",String.valueOf(picispinner.getAdapter().getCount()+1));
                intent.putExtra("chepai",chepaiText.getText().toString());
                startActivity(intent);
            }
        });

        shiyonbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanActivity.this,ScanActivity2.class);
                intent.putExtra("isnew",false);
                intent.putExtra("pici",picispinner.getSelectedItem().toString());
                intent.putExtra("chepai",chepaiText.getText().toString());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mailDao.close();
//        dbManager.closeDB();// 释放数据库资源
    }
}
