package devbrat.anand;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Chatting extends AppCompatActivity {

   static boolean status=false;
   static boolean check=false;
    static Context ct;
    Button b1;
    TextView t1;
    EditText e1;
   static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ct=Chatting.this;
        b1=findViewById(R.id.connect);
        t1=findViewById(R.id.msg);
        listView=findViewById(R.id.list);
        ListAdapter listAdapter=new ListAdapter();
        listView.setAdapter(listAdapter);
       WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        int ip=wifiInfo.getIpAddress();
        String ipaddr= Formatter.formatIpAddress(ip);
        DhcpInfo wi=wifiManager.getDhcpInfo();
        String ip1=wifiInfo.getBSSID();
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
       // String sp=Formatter.formatIpAddress(ip1);


        try {
               if(wifiManager.isWifiEnabled())
               {
                   AsyncTaskManager.inetAddress=InetAddress.getByName(ipaddr);

               }
               else
               {
                   AsyncTaskManager.inetAddress=InetAddress.getByName("192.168.43.1");


               }



        }
        catch(Exception e)
        {

        }

        if(status==true)
        {
            b1.setText("DISCONNECT");

            t1.setText(AsyncTaskManager.state);

        }
        e1=findViewById(R.id.text_to_send);


    }

    public void connect(View view) {


          if(status==false) {
              Fragment fragment = new ConsentFragment();
              FragmentManager fragmentManager = getSupportFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              fragmentTransaction.replace(R.id.containerf, fragment);
              fragmentTransaction.commit();
          }
          else
          {


                  Toast.makeText(this,"SUCCESSFULLY DISCONNECTED",Toast.LENGTH_SHORT).show();
                  check=false;
                  ListAdapter.chat.clear();
                  AsyncTaskManager.multicastSocket.close();
                  b1.setText("CONNECT");
                  t1.setText("Click to Connect to a group");
                  AsyncTaskManager.multicastSocket=null;
                  status=false;


          }

    }

  public void send(View view) {

      final  String st=e1.getText().toString();
     e1.setText("");
     final  WifiManager wifiManager=(WifiManager)ct.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
         Thread thread=new Thread(){
        public void run()
        {
            String p;
            p=AsyncTaskManager.id+"#"+AsyncTaskManager.name+AsyncTaskManager.inetAddress+"#"+st;

            try {



                String []ss=AsyncTaskManager.inetAddress.getHostAddress().split("\\.");


                    DatagramPacket datagramPacket=new DatagramPacket(p.getBytes(),p.length());
                    if(AsyncTaskManager.multicastSocket!=null) {

                         for(int i=0;i<255;i++)
                         {

                                 datagramPacket.setAddress(InetAddress.getByName(ss[0]+"."+ss[1]+"."+ss[2]+"."+i));
                                 datagramPacket.setPort(3843);
                                 AsyncTaskManager.multicastSocket.send(datagramPacket);

                         }

                    }

            }
            catch (IOException e)
            {
                Log.e("chatting",e+"");
            }
        }

       };
        thread.start();



    }
}
