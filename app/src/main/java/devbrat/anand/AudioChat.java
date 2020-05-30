package devbrat.anand;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioChat extends AppCompatActivity {

    Button b1;
    static EditText e1;
    static Context context;
    static int whatirec, callstatus;
    static int check = 0, ipverify = 0, state = 0;
    static DatagramSocket datagramSocket;
    static DatagramSocket estabsct;
    static DatagramSocket observeothersidesocket;
    static InetAddress inetAddress;
    static String ipaddr;
    static int ringclose = 0;
    static MediaPlayer mediaPlayer;
    static String putip1;
    static int phonenotcut = 0;
    static CallFragment callFragment;
    static int donotsend;

    //what the other side replied
    static int othersidesent = 0;

    //call cut by the call initiater caller
    static int callcut = 0;



    static int atoneside = 0;
    static  ObserveTheCall observeTheCall;
    static TextView t1;
    static AudioRecord audioRecord;
    static AudioTrack audioTrack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_chat);
        b1 = findViewById(R.id.call);
        e1 = findViewById(R.id.ip);
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio();
        }

     // audioRecord=new AudioRecord(MediaRecorder.AudioSource.MIC,8000,1, AudioFormat.ENCODING_PCM_16BIT,AudioRecord.getMinBufferSize(8000, 1, AudioFormat
       //               .ENCODING_PCM_16BIT)*8);
      //  audioTrack=new AudioTrack(AudioManager.STREAM_VOICE_CALL,8000,1,AudioFormat.ENCODING_PCM_16BIT,1024,AudioTrack.MODE_STREAM);

        Connection connection = new Connection();
        connection.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        observeTheCall=new ObserveTheCall();
        observeTheCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);






    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    static  sendcall sendcall;
    static RecieveonCallerSide recieveonCallerSide;
    public void call(View view) {
        try {

            callcut=0;
            AudioChat.othersidesent=0;
            sendcall.removerecieveaudio=0;
            String s1;
            othersidesent = 0;
           // observeTheCall.cancel(true);


            s1 = e1.getText().toString().trim();
            if (s1 == null) return;
            InetAddress inetAddress = InetAddress.getByName(s1);
            ipaddr = e1.getText().toString().trim();


            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            callFragment = new CallFragment();
            fragmentTransaction.add(callFragment, "null");
            callFragment.setCancelable(false);
            fragmentTransaction.commit();
            sendcall=new sendcall();
            sendcall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            recieveonCallerSide=new RecieveonCallerSide();
            recieveonCallerSide.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



        } catch (Exception e) {
           // Toast.makeText(context, "wrong ip", Toast.LENGTH_SHORT).show();
        }


    }


    public static class CallFragment extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            return inflater.inflate(R.layout.callbox, container, false);

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button b1 = view.findViewById(R.id.end11);
            t1 = view.findViewById(R.id.putip);
            t1.setText(AudioChat.ipaddr.toString() + "");
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    callcut=1;
                    sendcall.removerecieveaudio=1;

                   // sendcall.cancel(true);
                  //recieveonCallerSide.cancel(true);


                    try {

                    } catch (Exception e) {
                    }
                    //mediaPlayer.stop();
                    // mediaPlayer.release();
                    //callstatus=0;
                    //ring.cancel(true);
                    //recieve.cancel(true);
                    // mediaPlayer=null;

                    // recieveOnOtherSide=new recieveOnOtherSide();
                    //recieveOnOtherSide.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




                    dismiss();



                }
            });


        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return super.onCreateDialog(savedInstanceState);

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }




        private int RECORD_AUDIO_REQUEST_CODE = 123;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void getPermissionToRecordAudio() {
            // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
            // checking the build version since Context.checkSelfPermission(...) is only available
            // in Marshmallow
            // 2) Always check for permission (even if permission has already been granted)
            // since the user can revoke permissions at any time through Settings
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // The permission is NOT already granted.
                // Check if the user has been asked about this permission already and denied
                // it. If so, we want to give more explanation about why the permission is needed.
                // Fire off an async request to actually get the permission
                // This will show the standard permission request dialog UI
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        RECORD_AUDIO_REQUEST_CODE);

            }
        }

        // Callback with the request from calling requestPermissions(...)
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @NonNull String permissions[],
                                               @NonNull int[] grantResults) {
            // Make sure it's our original READ_CONTACTS request
            if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
                if (grantResults.length == 3 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                    finishActivity(12);
                }
            }

        }




}