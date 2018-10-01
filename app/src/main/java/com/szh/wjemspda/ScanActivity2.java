package com.szh.wjemspda;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.saomiao4xinshiqi.R;
import com.szh.wjemspda.buletooth.Bluetooth_Scale;
import com.szh.wjemspda.buletooth.DeviceListActivity;
import com.szh.wjemspda.buletooth.MainActivity;
import com.szh.wjemspda.buletooth.ScaleView;
import com.szh.wjemspda.dao.MailDao;
import com.szh.wjemspda.dao.impl.MailDaoImpl;
import com.szh.wjemspda.domain.Mail;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by szh on 2017-04-28.
 */

public class ScanActivity2 extends Activity {


    TextView textView;
    EditText editText;
    long startTime=0;

    Button button1;
    //MediaPlayer player;
    //MediaPlayer player2;
    SoundPool sp;
    int soundidchenggong;
    int soundidcuowu;
    int soundidchongfu;
    int soundidshanchu;
    //Myadapter arrayAdapter;
//    ListView listView ;

    TextView picitextview;
    //Button newbutton;
    Button shanchubutton;
    Button mButton_SearchBluetooth;
//    AlertDialog.Builder builder;
    AlertDialog.Builder builder2;
    //List<String> ll;
    //DBManager dbManager;
    MailDao mailDao ;
    //Realm realm ;
    TextView banbentextview;
    TextView shuliangtextview;
    EditText zhongliangedittext;




    Bundle bundle;
    Handler mHandler;
    BluetoothAdapter mBluetoothAdapter;

    private Bluetooth_Scale mBl_Scale;
    private BluetoothDevice device;
    Boolean bisClosed=false;
    MainActivity.SCALENOW scalenow=new MainActivity.SCALENOW();
    public ScaleView scaleView;
    Boolean b_scaleIsConnect= Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2_layout);
        mailDao =  new MailDaoImpl();
        //dbManager = new DBManager(this);
        //realm = Realm.getDefaultInstance();
        //player = MediaPlayer.create(getApplicationContext(), R.raw.success);
        //player2 = MediaPlayer.create(getApplicationContext(), R.raw.warning);
        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundidchenggong = sp.load(this,R.raw.success,1);
        soundidcuowu = sp.load(this,R.raw.warning,1);
        soundidchongfu = sp.load(this,R.raw.chongfu,1);
        soundidshanchu = sp.load(this,R.raw.shanchu,1);
        editText = (EditText)findViewById(R.id.m2edittext) ;
        textView=(TextView)findViewById(R.id.m2zttextview);
        button1 = (Button)findViewById(R.id.m2okbutton);
        picitextview = (TextView)findViewById(R.id.m2picihaotext) ;
        //newbutton = (Button)findViewById(R.id.m2newbutton);
        mButton_SearchBluetooth = findViewById(R.id.m2lianjiebutton);
        shanchubutton = (Button)findViewById(R.id.m2printbutton);
        banbentextview = (TextView)findViewById(R.id.textviewbanben);
        shuliangtextview= findViewById(R.id.textviewshuliang);
        zhongliangedittext =  findViewById(R.id.m2zhongliangedittext);
        textView.setFocusable(false);
        button1.setFocusable(false);
        picitextview.setFocusable(false);
        //newbutton.setFocusable(false);
        mButton_SearchBluetooth.setFocusable(false);
        shanchubutton.setFocusable(false);
        zhongliangedittext.setFocusable(false);
        Intent getIntent = getIntent();
        picitextview.setText(getIntent.getStringExtra("chepai"));
        banbentextview.setText(getIntent.getStringExtra("pici"));


//        builder = new AlertDialog.Builder(this);
//        builder.setMessage("你确认要新建扫描批次吗？")
//                .setCancelable(false)
//
//                .setNegativeButton("是", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        picitextview.setText(System.currentTimeMillis()+"");
//                        arrayAdapter.clear();
//                        sp.play(soundidchenggong, 1.0f, 1.0f, 0, 0, 1.0f);
//                        textView.setText(R.string.piciok);
//                    }
//                })
//                .setPositiveButton("否", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                }).create();

        builder2 = new AlertDialog.Builder(this);

        mButton_SearchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serverIntent = new Intent(ScanActivity2.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, 0);
            }
        });

//        newbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                builder.show();
//            }
//        });
        shanchubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shanchubutton(editText.getText().toString());
                shuliangtextview.setText(String.valueOf(mailDao.findByChepaiPiciCount(picitextview.getText().toString(),Integer.valueOf(banbentextview.getText().toString()))));
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okbutton(editText.getText().toString());
                shuliangtextview.setText(String.valueOf(mailDao.findByChepaiPiciCount(picitextview.getText().toString(),Integer.valueOf(banbentextview.getText().toString()))));
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                    //TODO回车键按下时要执行的操作
                    okbutton(editText.getText().toString());
                    shuliangtextview.setText(String.valueOf(mailDao.findByChepaiPiciCount(picitextview.getText().toString(),Integer.valueOf(banbentextview.getText().toString()))));
                }
                return false;
            }
        });


        //picitextview.setText(""+System.currentTimeMillis());


//        arrayAdapter = new Myadapter(this,R.layout.listview_layout);

//        listView.setAdapter(arrayAdapter);
//        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


//        if(getIntent.getBooleanExtra("isnew",true)){
//            //Log.i("sss",String.valueOf(getIntent.getBooleanExtra("isnew",true)));
//        }else{
//            ArrayList<Maildb> maildb = dbManager.querybychepaipici(picitextview.getText().toString(),Integer.valueOf(banbentextview.getText().toString()));
//
//            for(int i =1;i<=maildb.size();i++){
//                Mail m = new Mail(i,maildb.get(i-1).mailno,maildb.get(i-1).saomiaoshijian);
//                arrayAdapter.add(m);
//                //Log.i("sss",maildb.get(i-1).mailno);
//            }
//
//        }


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mBluetoothAdapter.enable();
        mHandler=new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 1:
                        bundle=msg.getData();
                        GetWeight(bundle.getByteArray("weight"));
                        zhongliangedittext.setText(scalenow.sformatNetWeight.trim());
//                        scaleView.setUnit(scalenow.sUnit);
//                        if(scalenow.bOverFlag)
//                        {
//                            scaleView.setText("over----");
//                        }
//                        else {
//                            scaleView.setText(scalenow.sformatNetWeight);
//                        }
//
//                        scaleView.setZero(scalenow.bZeroFlag);
//                        scaleView.setStable(scalenow.bWeiStaFlag);
                        break;
                    case 2:
                        if(msg.arg1<2) {
                            //scaleView.setText(getString(R.string.NoConnect));
                            b_scaleIsConnect= Boolean.FALSE;
                        }
                        else  b_scaleIsConnect= Boolean.TRUE;
                        break;

                }
            }
        };


        mBl_Scale = new Bluetooth_Scale(this,mHandler);

        scaleView=(ScaleView)findViewById(R.id.scaleView1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 0:
                if (resultCode == RESULT_OK) {
                    connectDevice(data);
                }
                break;

        }
    }

    public void shanchubutton(final String mail){
        builder2.setMessage("邮件号："+mail+",你确定删除该邮件号吗？")
                .setCancelable(false)
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int j=1;
                        ArrayList<Mail> array = new ArrayList<Mail>();
                        boolean flag =false;
                        Mail mail1 = mailDao.findByMailnoAndChepaiAndPici(mail,picitextview.getText().toString(),Integer.valueOf(banbentextview.getText().toString()));
                        if(mail1 == null){
                            sp.play(soundidcuowu, 1.0f, 1.0f, 0, 0, 1.0f);
                            textView.setText("邮件号不存在");
                        }else{
                            try {
                                mailDao.delete(mail1);
                                sp.play(soundidshanchu, 1.0f, 1.0f, 0, 0, 1.0f);
                            }catch (Exception e){
                                sp.play(soundidcuowu, 1.0f, 1.0f, 0, 0, 1.0f);
                                e.printStackTrace();
                                textView.setText(e.getMessage());
                            }
                        }
                    }
                })

                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create();

        builder2.show();
    }
    public void okbutton(String mail){
        editText.setText(mail);
        //Toast.makeText(this,getExternalStorageDirectory().toString() + "/" + picitextview.getText().toString() + ".txt",Toast.LENGTH_SHORT).show();
        if(mail.length() == 13 && mail.matches("^[0-9]+$")){

            String zhongjian = mail.substring(2, 10);
            int yanzheng =11-((Integer.valueOf(zhongjian.substring(0, 1))*8+Integer.valueOf(zhongjian.substring(1, 2))*6+
                    Integer.valueOf(zhongjian.substring(2, 3))*4+Integer.valueOf(zhongjian.substring(3, 4))*2+
                    Integer.valueOf(zhongjian.substring(4, 5))*3+Integer.valueOf(zhongjian.substring(5, 6))*5+
                    Integer.valueOf(zhongjian.substring(6, 7))*9+Integer.valueOf(zhongjian.substring(7, 8))*7)%11);
            if(yanzheng == 10){
                yanzheng = 0;
            }else if(yanzheng == 11){
                yanzheng = 5;
            }else if(yanzheng < 10){
            }
            int yanzhengmail = Integer.valueOf(mail.substring(10,11));
            if(yanzhengmail == yanzheng) {

                Mail mail2 = mailDao.findByMailnoAndChepaiAndPici(mail,picitextview.getText().toString(),Integer.valueOf(banbentextview.getText().toString()));
                if (mail2 != null) {
                    sp.play(soundidchongfu, 1.0f, 1.0f, 0, 0, 1.0f);
                    textView.setText(R.string.labelhanchu);
                    //final String shanchumail = mail;

                } else {
                    String chepai = picitextview.getText().toString();
                    int pici = Integer.valueOf(banbentextview.getText().toString());
                    //Maildb maildb = new Maildb(mail,chepai,pici,(System.currentTimeMillis()));
                    Mail mail1 = new Mail();
                    mail1.setChepai(chepai);
                    mail1.setPici(pici);
                    mail1.setShijian(new Date());
                    mail1.setMailno(mail);
                    mail1.setZhongliang(Integer.parseInt(zhongliangedittext.getText().toString()));
                    try {
                        mailDao.add(mail1);
                        sp.play(soundidchenggong, 1.0f, 1.0f, 0, 0, 1.0f);
                        textView.setText(mail + ","+zhongliangedittext.getText().toString() + ",扫描成功");

                    } catch (Exception e) {
                        sp.play(soundidcuowu, 1.0f, 1.0f, 0, 0, 1.0f);
                        textView.setText(e.getMessage());
                    }

                }
            }else{
                sp.play(soundidcuowu,1.0f,1.0f,0,0,1.0f);
                textView.setText(R.string.labelnotok);
            }
        }else{
            sp.play(soundidcuowu,1.0f,1.0f,0,0,1.0f);
            textView.setText(R.string.labelnotok);
        }
        editText.requestFocus();
        editText.selectAll();

        //editText.setText("");
    }





    @Override
    protected void onDestroy(){
        super.onDestroy();
        mailDao.close();
        if (mBl_Scale != null) mBl_Scale.stop();
        bisClosed=true;
        //realm.close();
        //printerClass.close();
    }



//    class Myadapter extends ArrayAdapter<Mail> {
//
//        private int mResourceId;
//        public Myadapter(Context context, int resource) {
//            super(context, resource);
//            this.mResourceId = resource;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            Mail m = (Mail)getItem(position);
//            LayoutInflater inflater = getLayoutInflater();
//            View view =  inflater.inflate(mResourceId, null);
//            TextView idtext = (TextView) view.findViewById(R.id.list_id);
//            TextView mailtext = (TextView) view.findViewById(R.id.list_mail);
//            TextView shijian = (TextView) view.findViewById(R.id.list_shijian);
//
//            idtext.setText(m.getId()+"");
//            mailtext.setText(m.getMail()+"");
//            SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日HH:mm:ss");
//
//            shijian.setText(formatter.format(m.getShijian()));
//
//            return view;
//
//        }
//
//
//    }


//    private class Mail{
//        private int id;
//        private String mail;
//        private long shijian;
//        public Mail(int id,String mail,long shijian){
//            this.id = id;
//            this.mail = mail;
//            this.shijian = shijian;
//        }
//
//        public int getId(){
//            return this.id;
//        }
//        public String getMail(){
//            return this.mail;
//        }
//        public long getShijian(){
//            return this.shijian;
//        }
//
//    }


    void GetWeight(byte[] databuf)
    {
        int i,j,offset=6;
        boolean	StartFalg=false;
        scalenow.bZeroFlag=true;
        scalenow.bOverFlag=false;
        scalenow.bWeiStaFlag=false;
        switch(databuf[0])
        {
            case 'o':
            case 'O':
                scalenow.bOverFlag=true;
                break;
            case 'u':
            case 'U':
                scalenow.bWeiStaFlag=false;
                offset=6;	//6
                break;
            case 's':
            case 'S':
                scalenow.bWeiStaFlag=true;
                break;
        }
        if(databuf[5]=='-')offset=5;
        for(i=0;i<14;i++)
        {
            if(databuf[i+offset]=='\'')databuf[i+offset]='.';
            if(StartFalg)
            {
                if(((databuf[i+offset]>'9')||(databuf[i+offset]<'.'))&&(!((databuf[i+offset]==' ')&&(databuf[i+offset+1]<='9'))))
                {
                    break;
                }
            }
            else if((databuf[i+offset]>='0')&&(databuf[i+offset]<='9'))
            {
                StartFalg=true;
                if(databuf[i+offset]!='0')scalenow.bZeroFlag=false;
            }
        }
        scalenow.sformatNetWeight=new String(databuf,offset,i);


        for(j=0;j<6;j++)
        {
            if(databuf[i+j+offset]<0x20)
            {
                break;
            }
        }
        scalenow.sUnit=new String(databuf,i+offset,j);

    }

    public static class SCALENOW {
        public String sformatNetWeight="0";
        public String sUnit="0";
        public boolean bWeiStaFlag;
        public boolean bZeroFlag;
        public boolean bOverFlag;
    }

    private void connectDevice(Intent data) {
        // Get the device MAC address

        mBl_Scale.stop();		//先断开前面的连接,然后延时一会再连接,保证前面的已经断开,如果是直接连接并且连接到另外一个设备,则会失败,甚至锁死
        String BluetoothAddress= data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        if(BluetoothAddress.length()>0)
        {
            // Get the BluetoothDevice object
            device= mBluetoothAdapter.getRemoteDevice(BluetoothAddress);
            if(device!=null)
            {
                mBl_Scale.connect(device);
            }
        }
    }
}
