package devbrat.anand;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ObserveTheCall extends AsyncTask<Void,Integer,Void> {

   static  int whatrecieved,inside=0;
   //1 for pick and 2 for end
   static int fragButtonChoosen=0;
  incomingcall incomingcall;
  static int removerecieveaudio,removesendaudio;
    @Override
    protected Void doInBackground(Void... voids) {
        DatagramPacket pkt,pkt1;
        while (true) {
            if (AudioChat.observeothersidesocket != null) {
                byte[] b1;
                b1 = new byte[1];
                pkt = new DatagramPacket(b1, 0, 1);

                try {




                    AudioChat.observeothersidesocket.receive(pkt);
                    publishProgress((Integer)(int)b1[0]);
                    if (!pkt.getAddress().getHostName().equals(AudioChat.inetAddress.getHostName())) {
                        if (b1[0] == 1 && inside == 0) {

                            AudioChat.mediaPlayer=MediaPlayer.create(AudioChat.context,R.raw.googleduo);
                            AudioChat.mediaPlayer.setLooping(true);
                            AudioChat.mediaPlayer.start();
                            whatrecieved = 1;
                            inside = 1;
                            AudioChat.putip1 = pkt.getAddress().getHostName();
                            publishProgress(11);

                            byte[] b12 = {1};
                            DatagramPacket pkt2 = new DatagramPacket(b12, 0, 1, InetAddress.getByName(AudioChat.putip1), 4512);
                            AudioChat.observeothersidesocket.send(pkt2);

                        }
                        if (b1[0] == 3) {
                            if(AudioChat.mediaPlayer!=null)
                                AudioChat.mediaPlayer.release();
                            whatrecieved = 3;
                            removerecieveaudio=1;
                            removesendaudio=1;

                            inside = 0;


                            publishProgress(10);


                        }

                    }


                } catch (Exception e) {

                    //publishProgress(e);

                }
            }
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Toast.makeText(AudioChat.context, "values: "+values[0], Toast.LENGTH_SHORT).show();
        if(values[0]==10)
        {
            if(incomingcall!=null)
                incomingcall.dismiss();
        }
        if(values[0]==11)
        {
            incomingcall = new incomingcall();
            FragmentTransaction fragmentTransaction = ((AudioChat) (AudioChat.context)).getSupportFragmentManager().beginTransaction();

            fragmentTransaction.add(incomingcall, "null");
          //  fragmentTransaction.addToBackStack(null);
            incomingcall.setCancelable(false);
            fragmentTransaction.commit();
        }
    }

    public static class incomingcall extends DialogFragment
    {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate(R.layout.incomingcallbox,container,false);
        }
        Button b2;
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button b1=view.findViewById(R.id.end1);
            b2=view.findViewById(R.id.pick1);
            TextView t1=view.findViewById(R.id.putip1);
            t1.setText(AudioChat.putip1);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try{

                    fragButtonChoosen=2;
                        removerecieveaudio=1;
                        removesendaudio=1;
                        sendtocaller sendtocaller=new sendtocaller();
                        sendtocaller.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    inside=0;
                    dismiss();
                    }
                    catch (Exception e)
                    {

                    }


                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

            fragButtonChoosen=1;
                    removerecieveaudio=0;
                    removesendaudio=0;
            sendtocaller sendtocaller=new sendtocaller();
            sendtocaller.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            recievecallonotherside recievecallonotherside=new recievecallonotherside();
            recievecallonotherside.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            sendcallonotherside sendcallonotherside=new sendcallonotherside();
            sendcallonotherside.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
            });


        }
    }

        static class sendtocaller extends AsyncTask<Void,Object,Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                try {


                    if (fragButtonChoosen == 1) {
                       AudioChat.mediaPlayer.release();
                        byte[] b11 = {2};
                        DatagramPacket pkt1 = new DatagramPacket(b11, 0, 1, InetAddress.getByName(AudioChat.putip1), 4512);
                        AudioChat.observeothersidesocket.send(pkt1);
                        removerecieveaudio=0;
                        removesendaudio=0;
                        publishProgress(2);




                        fragButtonChoosen = 0;
                    }
                    if (fragButtonChoosen == 2) {
                        byte[] b11 = {3};
                        if(AudioChat.mediaPlayer!=null)
                            AudioChat.mediaPlayer.release();
                        removerecieveaudio=1;
                        removesendaudio=1;

                        DatagramPacket pkt1 = new DatagramPacket(b11, 0, 1, InetAddress.getByName(AudioChat.putip1), 4512);
                        AudioChat.observeothersidesocket.send(pkt1);
                        publishProgress(3);
                        fragButtonChoosen = 0;
                    }
                }
                catch (Exception e){}


                return null;
            }

            @Override
            protected void onProgressUpdate(Object... values) {
                super.onProgressUpdate(values);
                Toast.makeText(AudioChat.context, "sent :"+values[0], Toast.LENGTH_SHORT).show();


            }
        }
        //capture audio on other side

        static class recievecallonotherside extends AsyncTask<Void,Void,Void>
        {
            @Override
            protected Void doInBackground(Void... voids) {
                AudioChat.audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 8000, 1, AudioFormat.ENCODING_PCM_16BIT, 256, AudioTrack.MODE_STREAM);
                   if(AudioChat.audioTrack!=null)
                    AudioChat.audioTrack.play();
                    DatagramPacket datagramPacket;

                    byte[] bytes = new byte[256];

                    datagramPacket = new DatagramPacket(bytes, 0, 256);

                    while (true) {

                        try {

                            if(AudioChat.audioTrack!=null)
                                AudioChat.datagramSocket.receive(datagramPacket);
                       //    if (datagramPacket.getAddress().getHostName().equals(AudioChat.putip1))
                            {

                                if (removerecieveaudio == 1) {

                                    AudioChat.audioTrack.stop();
                                    AudioChat.audioTrack.release();
                                return null;
                                }

                                if (removerecieveaudio == 1) {
                                    AudioChat.audioTrack.stop();
                                    AudioChat.audioTrack.release();
                             return null;
                                }
                                if(AudioChat.audioTrack!=null)
                                AudioChat.audioTrack.write(bytes, 0, 256);


                            }

                        } catch (Exception e) {

                        }

                    }

                }


        }
        static  class sendcallonotherside extends AsyncTask<Void,Void,Void>
        {
            @Override
            protected Void doInBackground(Void... voids) {
                AudioChat.audioRecord=new AudioRecord(MediaRecorder.AudioSource.MIC,8000,1, AudioFormat.ENCODING_PCM_16BIT,AudioRecord.getMinBufferSize(8000, 1, AudioFormat
                        .ENCODING_PCM_16BIT)*8);
                if(AudioChat.audioRecord!=null)
                AudioChat.audioRecord.startRecording();
                DatagramPacket datagramPacket;

                byte[] bytes = new byte[256];



                while (true) {

                    try {


                        if (AudioChat.audioRecord != null) {
                            AudioChat.audioRecord.read(bytes, 0, 256);
                            datagramPacket = new DatagramPacket(bytes, 0, 256, InetAddress.getByName(AudioChat.putip1), 4278);
                            AudioChat.datagramSocket.send(datagramPacket);
                            if (removerecieveaudio == 1) {
                                if(AudioChat.audioRecord!=null){
                            AudioChat.audioRecord.stop();
                            AudioChat.audioRecord.release();}
                           return null;
                            }


                        }
                    }catch(Exception e){

                        }


                }

            }
        }






}
