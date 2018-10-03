package com.szh.wjemspda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.saomiao4xinshiqi.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.szh.wjemspda.dao.MailDao;
import com.szh.wjemspda.dao.impl.MailDaoImpl;
import com.szh.wjemspda.domain.Mail;

public class ShujuguanliActivity extends Activity implements View.OnClickListener {




    MailDao mailDao;


    EditText chepaiedittext;
    Spinner picitext;
    Button daochubutton;
    Button qingkongbutton;
    Button qingkongallbutton;
    Button chaxunbutton;
    Button daochuallbutton;

    final DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shujuguanli);
        mailDao = new MailDaoImpl();
        chepaiedittext = findViewById(R.id.shuju_chepaieditText);
        picitext = findViewById(R.id.shuju_spinner);
        daochubutton = findViewById(R.id.shuju_daochubutton);
        qingkongbutton = findViewById(R.id.shuju_qingkongbutton);
        qingkongallbutton = findViewById(R.id.shuju_qingkongallbutton);
        chaxunbutton = findViewById(R.id.shuju_chaxunbutton);
        daochuallbutton = findViewById(R.id.shuju_daochuallbutton);


        daochubutton.setOnClickListener(this);
        qingkongbutton.setOnClickListener(this);
        qingkongallbutton.setOnClickListener(this);
        chaxunbutton.setOnClickListener(this);
        daochuallbutton.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mailDao.close();
    }

    @Override
    public void onClick(View view) {
        String mainpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        System.out.println(mainpath);
        switch(view.getId()){
            case R.id.shuju_daochubutton:
                ProgressDialog pd1 = ProgressDialog.show(this, "提示", "导出中");
                List<Mail> daochulist = mailDao.findByChepaiPici(chepaiedittext.getText().toString(),Integer.valueOf(picitext.getSelectedItem().toString()));
                Boolean flag = true;
                File file = new File(mainpath + "/dfn/" );
                file.mkdirs();
                try {
                    FileWriter bw = new FileWriter(mainpath + "/dfn/" + chepaiedittext.getText().toString()+ "-" + picitext.getSelectedItem().toString() + ".csv");
                    for(Mail m:daochulist){
                        bw.append(m.getMailno() + "," + m.getZhongliang() + "," + m.getChepai() + "," + m.getPici() + "," + format2.format(m.getShijian()) + "\r\n");
                        bw.flush();
                    }
                    bw.close();
                    pd1.cancel();
                    Toast.makeText(this, "导出成功", Toast.LENGTH_LONG).show();
                }catch (IOException e) {
                    e.printStackTrace();
                    pd1.cancel();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.shuju_qingkongbutton:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("请输入清空密码");
                View viewx = LayoutInflater.from(this).inflate(R.layout.dialog_mima, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                dialog.setView(viewx);
                final EditText mimaedittext2 = viewx.findViewById(R.id.mimainfo);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if("1234".equals(mimaedittext2.getText().toString())){
                            mailDao.delete(chepaiedittext.getText().toString(),Integer.valueOf(picitext.getSelectedItem().toString()));
                            Toast.makeText(ShujuguanliActivity.this,"清除成功",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ShujuguanliActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                break;
            case R.id.shuju_qingkongallbutton:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("请输入清空密码");
                View viewx2 = LayoutInflater.from(this).inflate(R.layout.dialog_mima, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                dialog2.setView(viewx2);

                final EditText mimaedittext = viewx2.findViewById(R.id.mimainfo);
                dialog2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if("1234".equals(mimaedittext.getText().toString())){
                            mailDao.deleteall();
                            Toast.makeText(ShujuguanliActivity.this,"清除所有成功",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ShujuguanliActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog2.show();

                break;
            case R.id.shuju_chaxunbutton:
                List<Integer> list = mailDao.findByChepaidistinctCheci(chepaiedittext.getText().toString());
                ArrayAdapter<Integer> arr_adapter= new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,list);
                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                picitext.setAdapter(arr_adapter);
                //newbutton.setEnabled(true);

                break;
            case R.id.shuju_daochuallbutton:
                ProgressDialog pd2 = ProgressDialog.show(this, "提示", "导出中");
                List<Mail> daochualllist = mailDao.findAll();
                File file2 = new File(mainpath + "/dfn/"  );
                file2.mkdirs();
                try {
                    FileWriter bw = new FileWriter(mainpath + "/dfn/daochuall.csv");
                    for (Mail m : daochualllist) {
                        bw.append(m.getMailno()+","+m.getZhongliang()+","+m.getChepai()+","+m.getPici()+","+format2.format(m.getShijian())+"\r\n");
                        bw.flush();
                    }
                    bw.close();
                    pd2.cancel();
                    Toast.makeText(this, "导出成功", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    pd2.cancel();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }


                break;
        }
    }
}
