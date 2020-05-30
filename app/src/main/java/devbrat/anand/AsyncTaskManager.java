package devbrat.anand;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class AsyncTaskManager extends AsyncTask<String,byte[],String> {

    static MulticastSocket multicastSocket;
    static InetAddress inetAddress;
    static InetAddress group;
    static String name;
    static String state;
    static  String id="224";
    static boolean connectedstatussend;

    byte b[] = new byte[500];
    String s1[];
    Button b1;
    TextView t1;
    Context ct;
    ListView listView;

    AsyncTaskManager(Button b1, TextView t1, ListView l) {
        this.b1 = b1;
        this.t1 = t1;
        ct = Chatting.ct;
        listView = l;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected String doInBackground(String... param) {
        try {
            WifiManager wifiManager=(WifiManager)ct.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo=wifiManager.getConnectionInfo();
            int ip=wifiInfo.getIpAddress();
            String ipaddr= Formatter.formatIpAddress(ip);
            DhcpInfo wi=wifiManager.getDhcpInfo();
            wifiManager.getWifiState();


            try {
                if(wifiManager.isWifiEnabled())
                {
                    AsyncTaskManager.inetAddress=InetAddress.getByName(ipaddr);

                }
                else {
                   int y = 0;

                      for (int i = 1; i < 255; i++) {

                        try {
                            new MulticastSocket(new InetSocketAddress(InetAddress.getByName("192.168.43." + i), 9055));

                        } catch (Exception e) {
                            y = 1;


                        }
                          if (y == 0) {
                              AsyncTaskManager.inetAddress = InetAddress.getByName("192.168.43." + i );

                              break;
                          }

                        y=0;
                    }


                }
               // AsyncTaskManager.inetAddress=InetAddress.getByName("192.168.43.34");


            }
            catch(Exception e)
            {

            }

             multicastSocket = new MulticastSocket(new InetSocketAddress(inetAddress,3843));

            inetAddress = multicastSocket.getLocalAddress();


            name = param[1].trim();
            if (name == "")
                name = "anonymous";
            s1 = param[0].split("\\.");
            if (s1[0].equals("224")) {
                publishProgress(("1Welcome " + name + " ,connection established with default group:").getBytes());
                state = "Welcome " + name + ",connection established with default group:";



            } else {
                publishProgress(("1Welcome " + name + " ,connection established with group id: " + AsyncTaskManager.id+ " ").getBytes());
                state = "Welcome " + name + ",connection established with group id: " +  AsyncTaskManager.id + " ";


            }

        } catch (IOException io) {

            publishProgress(("2error in connection(wifi/hotspot enabled or not check group id range ), try again" + io).getBytes());
            state = "error in connection(check group id range), try again" + io;
            Chatting.check = false;

            multicastSocket = null;
        }



            while (true) {

                DatagramPacket datagramPacket = new DatagramPacket(b, b.length);

                try {


                    if (multicastSocket != null) {

                        AsyncTaskManager.multicastSocket.receive(datagramPacket);

                        publishProgress(b);

                        b=new byte[500];


                    } else
                        break;
                } catch (IOException e) {
                    //file or message to long
                }


            }

            return null;


        }

        @Override
        protected void onProgressUpdate ( byte[]...values){
            super.onProgressUpdate(values);

            if (Chatting.check == false) {
                if (values[0][0] == '1') {
                    t1.setText(new String(values[0], 1, values[0].length - 1));
                    b1.setText("DISCONNECT");
                    b1.setClickable(true);
                    Chatting.status = true;
                    Chatting.check = true;


                }
                if (values[0][0] == '2') {
                    t1.setText(new String(values[0], 1, values[0].length - 1));
                    b1.setText("CONNECT");
                    b1.setClickable(true);
                    Chatting.check = false;


                }


            } else {

                String []s=(new String(values[0],0,values[0].length)).split("\\#");
                String ss="";
                if(AsyncTaskManager.id.trim().equals(s[0].trim()));
                {
                    for(int i=1;i<s.length-1;i++)
                    {
                      ss=ss+s[i]+"#";
                    }
                    ss=ss+s[s.length-1];
                    ListAdapter.chat.add(new String(ss.getBytes(), 0, ss.length()));
                    Chatting.listView.invalidate();
                    ((BaseAdapter) Chatting.listView.getAdapter()).notifyDataSetChanged();
                }




            }


        }


    }


