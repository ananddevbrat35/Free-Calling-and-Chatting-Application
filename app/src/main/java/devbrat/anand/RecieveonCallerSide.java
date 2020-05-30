package devbrat.anand;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class RecieveonCallerSide extends AsyncTask<Void,Object,Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        DatagramPacket pkt;
        while (true) {
            byte[] b1;
            b1 = new byte[1];
            pkt = new DatagramPacket(b1, 0, 1);
            try {
                if (isCancelled() || AudioChat.callcut == 1) {

                    publishProgress(AudioChat.othersidesent);
                    AudioChat.recieveonCallerSide.cancel(true);
                    return null;
                }

                AudioChat.estabsct.receive(pkt);
                AudioChat.othersidesent=b1[0];
                if (isCancelled() || AudioChat.callcut == 1) {
                    publishProgress(AudioChat.othersidesent);
                    AudioChat.recieveonCallerSide.cancel(true);
                    return null;
                }
                if (pkt.getAddress().getHostName().equals(AudioChat.ipaddr))
                {
                    if (b1[0] == 2) {
                        AudioChat.othersidesent = 2;
                        publishProgress(2);
                    }
                    if (b1[0] == 3) {
                        AudioChat.othersidesent = 3;
                        publishProgress(3);
                        AudioChat.recieveonCallerSide.cancel(true);
                        return null;
                    }

                }


            } catch (Exception e) {
            }
        }

    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);

        Toast.makeText(AudioChat.context, "" + values[0] + "  recieved/exiting", Toast.LENGTH_SHORT).show();

    }

    //recieve audio here
    static class recieveaudio extends AsyncTask<Void, Object, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            AudioChat.audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 8000, 1, AudioFormat.ENCODING_PCM_16BIT, 256, AudioTrack.MODE_STREAM);
            publishProgress(31);

            DatagramPacket datagramPacket;

            byte[] bytes = new byte[256];

            datagramPacket = new DatagramPacket(bytes, 0, 256);
            AudioChat.audioTrack.play();
            try {
                Thread.sleep(100);
            }catch (Exception e){}
            while (true) {

                try {

                    if(sendcall.removerecieveaudio==1)
                    {
                        if(AudioChat.audioTrack!=null){
                        AudioChat.audioTrack.stop();
                        AudioChat.audioTrack.release();}
                        publishProgress(30);
                        return null;
                    }
                    AudioChat.datagramSocket.receive(datagramPacket);
                    if(sendcall.removerecieveaudio==1)
                    {
                        if(AudioChat.audioTrack!=null){
                            AudioChat.audioTrack.stop();
                            AudioChat.audioTrack.release();}
                        publishProgress(30);
                        return null;
                    }
                   //if (datagramPacket.getAddress().getHostName().equals(AudioChat.ipaddr))
                    {


                        AudioChat.audioTrack.write(bytes, 0, 256);


                    }

                } catch (Exception e) {
                    publishProgress(e);
                    return null;
                }

            }

        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            if(values.length>0)
            Toast.makeText(AudioChat.context, "not working"+" "+values[0], Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(AudioChat.context, "not working,len=0", Toast.LENGTH_SHORT).show();
            if((int)values[0]==30)
            {
                Toast.makeText(AudioChat.context, ""+"recieveaudio exiting", Toast.LENGTH_LONG).show();
            }
            if((int)values[0]==31)
            {
                Toast.makeText(AudioChat.context, ""+"entering recieveaudio", Toast.LENGTH_LONG).show();
            }


        }
    }
}
