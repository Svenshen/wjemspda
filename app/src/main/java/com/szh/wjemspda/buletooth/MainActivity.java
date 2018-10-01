package com.szh.wjemspda.buletooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.saomiao4xinshiqi.R;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    Boolean bisClosed=false;
    public  ScaleView scaleView;
    Bundle bundle;
    private Button mButton_Zero;
    private Button mButton_Tare;
    private Button mButton_Unit;

    public Button mButton_SearchBluetooth;

    /**搜索BLE终端*/
    private BluetoothAdapter mBluetoothAdapter;
    /**读写BLE终端*/
    private Bluetooth_Scale mBl_Scale;
    private BluetoothDevice device;
    Handler mHandler;
    Boolean b_scaleIsConnect= Boolean.FALSE;
    SCALENOW scalenow=new SCALENOW();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // If the adapter is null, then Bluetooth is not supported
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //open Bluetooth
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
                        scaleView.setUnit(scalenow.sUnit);
                        if(scalenow.bOverFlag)
                        {
                            scaleView.setText("over----");
                        }
                        else {
                            scaleView.setText(scalenow.sformatNetWeight);
                        }

                        scaleView.setZero(scalenow.bZeroFlag);
                        scaleView.setStable(scalenow.bWeiStaFlag);
                        break;
                    case 2:
                        if(msg.arg1<2) {
                            scaleView.setText(getString(R.string.NoConnect));
                            b_scaleIsConnect= Boolean.FALSE;
                        }
                        else  b_scaleIsConnect= Boolean.TRUE;
                        break;

                }
            }
        };
        mBl_Scale = new Bluetooth_Scale(this,mHandler);

        scaleView=(ScaleView)findViewById(R.id.scaleView1);


        mButton_Zero=(Button)findViewById(R.id.Button_Zero);
        mButton_Tare=(Button)findViewById(R.id.Button_Tare);
        mButton_Unit=(Button)findViewById(R.id.Button_Unit);
        mButton_SearchBluetooth=(Button)findViewById(R.id.Button_Search);
        mButton_SearchBluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, 0);
            }
        });

        mButton_Zero.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if((mBl_Scale !=null)&&b_scaleIsConnect)
                {
                    Log.d(TAG, "Zero");
                    mBl_Scale.write("SZ09\r\n".getBytes());
                }
            }
        });


        mButton_Tare.setOnClickListener(new View.OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if((mBl_Scale !=null)&& b_scaleIsConnect)
                {
                    mBl_Scale.write("ST07\r\n".getBytes());
                }
            }
        });
        mButton_Unit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if((mBl_Scale !=null)&& b_scaleIsConnect)
                {
                    mBl_Scale.write("SU06\r\n".getBytes());
                }
            }
        });


    }


    @SuppressLint("SimpleDateFormat")
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
    @Override
    protected  void onRestart()
    {
        super.onRestart();

    }
    @Override
    protected  void onResume()
    {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        if (mBl_Scale != null) mBl_Scale.stop();
        bisClosed=true;
        super.onDestroy();
        System.exit(0);
    }
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
}
