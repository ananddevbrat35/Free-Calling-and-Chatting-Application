package devbrat.anand;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends AsyncTask<Void,Void,Object[]> {
    @Override
    protected Object[] doInBackground(Void... voids) {
        DatagramSocket datagramSocket;
        InetAddress iad;
        int port=4278;
        int port1=4512;
        int port2=4632;
        try {

            WifiManager wifiManager=(WifiManager)AudioChat.context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo=wifiManager.getConnectionInfo();
            int ip=wifiInfo.getIpAddress();
            String ipaddr= Formatter.formatIpAddress(ip);
            if(wifiManager.isWifiEnabled()) {
                datagramSocket = new DatagramSocket(port, InetAddress.getByName(ipaddr));
               AudioChat.estabsct=new DatagramSocket(port1, InetAddress.getByName(ipaddr));
                AudioChat.observeothersidesocket=new DatagramSocket(port2, InetAddress.getByName(ipaddr));
                AudioChat.inetAddress= InetAddress.getByName(ipaddr);

                AudioChat.datagramSocket = datagramSocket;
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
                       AudioChat.datagramSocket = new DatagramSocket(port,InetAddress.getByName("192.168.43." + i ));
                        AudioChat.inetAddress=InetAddress.getByName("192.168.43." + i );
                       AudioChat.estabsct=new DatagramSocket(port1, AudioChat.inetAddress);
                       AudioChat.observeothersidesocket=new DatagramSocket(port2, AudioChat.inetAddress);
                        break;
                    }

                    y=0;
                }


            }

        }
        catch(Exception e)
        {

        }
     //  AudioRecord audioRecord=new AudioRecord(MediaRecorder.AudioSource.MIC,8000,1, AudioFormat.ENCODING_PCM_16BIT,AudioRecord.getMinBufferSize(8000, 1, AudioFormat
       //       .ENCODING_PCM_16BIT)*8);

         //   audioRecord.startRecording();


        //AudioTrack audioTrack=new AudioTrack(AudioManager.STREAM_VOICE_CALL,8000,1,AudioFormat.ENCODING_PCM_16BIT,1024,AudioTrack.MODE_STREAM);
        //audioTrack.play();
        //byte []bytes=new byte[1024];
       //while(true) {
         //  audioRecord.read(bytes, 0, 1024);
          //  audioTrack.write(bytes,0,1024);
        //}






       return new Object[0];
    }

    @Override
    protected void onPostExecute(Object[] objects) {
        super.onPostExecute(objects);
     //   Toast.makeText(AudioChat.context,AudioChat.inetAddress+"",Toast.LENGTH_SHORT).show();
    }
}
