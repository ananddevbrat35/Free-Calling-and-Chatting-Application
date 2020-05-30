package devbrat.anand;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaExtractor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class sendcall extends AsyncTask<Void,Integer,Void> {

    static int removerecieveaudio=0;
    @Override
    protected Void doInBackground(Void... voids) {
        byte[]b1={1};
        try {
            DatagramPacket pkt = new DatagramPacket(b1, 0, 1, InetAddress.getByName(AudioChat.ipaddr), 4632);
            AudioChat.mediaPlayer= MediaPlayer.create(AudioChat.context,R.raw.justtalk);
            AudioChat.mediaPlayer.setLooping(true);
            AudioChat.mediaPlayer.start();
      while(AudioChat.othersidesent==0)
      {
          //if no one is going available then on end call interrupt the thread


          Thread.sleep(100);
          if(isCancelled()||AudioChat.callcut==1)
          {
              byte[]b2={3};
              sendcall.removerecieveaudio=1;
              if(AudioChat.mediaPlayer!=null)
              AudioChat.mediaPlayer.release();
              DatagramPacket pkt1 = new DatagramPacket(b2, 0, 1, InetAddress.getByName(AudioChat.ipaddr), 4632);
              AudioChat.estabsct.send(pkt1);
             // publishProgress(3);
                AudioChat.sendcall.cancel(true);
             return null;
          }
           AudioChat.estabsct.send(pkt);


      }



      // publishProgress(AudioChat.othersidesent);
      //sending audio


           while(true) {

               if(AudioChat.othersidesent==3)
               {
                   if(AudioChat.mediaPlayer!=null)
                   AudioChat.mediaPlayer.release();
                   byte[]b2={3};
                   AudioChat.callcut=1;
                   publishProgress(3);
                   publishProgress(21);
                    removerecieveaudio=1;
                   AudioChat.sendcall.cancel(true);
                   return null;
               }


              if(isCancelled()||AudioChat.callcut==1) {
                  byte[] b2 = {3};

                  removerecieveaudio=1;
                  if(AudioChat.mediaPlayer!=null)
                      AudioChat.mediaPlayer.release();
                  DatagramPacket pkt1 = new DatagramPacket(b2, 0, 1, InetAddress.getByName(AudioChat.ipaddr), 4632);
                  AudioChat.estabsct.send(pkt1);
                  publishProgress(3);
                  AudioChat.sendcall.cancel(true);
                  Intent i1=new Intent(AudioChat.context,AudioChat.class);
                  AudioChat.context.startActivity(i1);

                  Runtime.getRuntime().exit(0);
                  return null;

              }

              if(AudioChat.othersidesent==2)
              {
                 byte bytes[] = new byte[256];
                 if(AudioChat.mediaPlayer!=null)
                  AudioChat.mediaPlayer.release();
                  removerecieveaudio=0;
                  DatagramPacket datagramPacket;
                  publishProgress(20);
               AudioChat.audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, 1, AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(8000, 1, AudioFormat
                         .ENCODING_PCM_16BIT) * 8);


              AudioChat.audioRecord.startRecording();



                  while(true)
                  {
                      if(AudioChat.othersidesent==3)
                      {
                          byte[]b2={3};
                          removerecieveaudio=1;
                          AudioChat.callcut=1;
                      //    AudioChat.audioRecord.stop();
                       //   AudioChat.audioRecord.release();
                        //  AudioChat.audioTrack.stop();
                        //  AudioChat.audioTrack.release();
                          AudioChat.audioRecord.stop();
                          AudioChat.audioRecord.release();

                          publishProgress(3);
                          publishProgress(22);

                          AudioChat.sendcall.cancel(true);
                          return null;
                      }


                      if(isCancelled()||AudioChat.callcut==1) {
                          byte[] b2 = {3};
                          removerecieveaudio=1;

                          DatagramPacket pkt1 = new DatagramPacket(b2, 0, 1, InetAddress.getByName(AudioChat.ipaddr), 4632);
                           AudioChat.estabsct.send(pkt1);
                          AudioChat.audioRecord.stop();
                          AudioChat.audioRecord.release();
                          //AudioChat.audioRecord.stop();
                          //AudioChat.audioRecord.release();
                          //AudioChat.audioTrack.stop();
                          //AudioChat.audioTrack.release();
                          publishProgress(3);
                          if(!AudioChat.sendcall.isCancelled())
                          AudioChat.sendcall.cancel(true);
                          Intent i1=new Intent(AudioChat.context,AudioChat.class);
                          AudioChat.context.startActivity(i1);

                          Runtime.getRuntime().exit(0);
                          return null;
                       }
                      //audio capturing from mic


                    if(AudioChat.audioRecord!=null)
                     AudioChat.audioRecord.read(bytes, 0, 256);
                      datagramPacket = new DatagramPacket(bytes, 0, 256, InetAddress.getByName(AudioChat.ipaddr), 4278);
                      if(AudioChat.audioRecord!=null)
                      AudioChat.datagramSocket.send(datagramPacket);









                  }

              }

           }





        }catch (Exception e){ publishProgress(12);}



        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if(values[0]==20)
        {
           removerecieveaudio=0;
            RecieveonCallerSide.recieveaudio recieveaudio=new RecieveonCallerSide.recieveaudio();
            recieveaudio.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        if(values[0]==21)
        {
            AudioChat.callFragment.dismiss();
        }
        if(values[0]==22)
        {
            AudioChat.callFragment.dismiss();
        }


        if(values[0]==3)
        Toast.makeText(AudioChat.context, "sent from send call: "+values[0], Toast.LENGTH_SHORT).show();



    }
}
