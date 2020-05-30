package devbrat.anand;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ConsentFragment extends Fragment implements View.OnClickListener {

    String a,b,c;
    Button b1,b2,b3;
    EditText e1,e2,e3,e4;
    static Context ct;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment,container,false);
       b1=view.findViewById(R.id.button);
       b2=view.findViewById(R.id.button2);
       b3=view.findViewById(R.id.button3);
       b1.setOnClickListener(this);
       b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        e1=view.findViewById(R.id.editText);

        e4=view.findViewById(R.id.name);

        ct=getActivity();
        return view;

    }

    @Override
    public void onClick(View view) {
      if(view==b1)
      {
        String addr="224.0.0.251";

        Button a1=getActivity().findViewById(R.id.connect);
          TextView t1=getActivity().findViewById(R.id.msg);
          AsyncTaskManager asyncTaskManager=new AsyncTaskManager(a1,t1,(ListView)getActivity().findViewById(R.id.list));
        a1.setText("connecting wait a moment");

        t1.setText("");
        a1.setClickable(false);
        asyncTaskManager.execute("224."+e1.getText().toString(),e4.getText().toString());

          getFragmentManager().beginTransaction().remove(this).commit();


      }
      if(view==b2)
      {

          Button a1=getActivity().findViewById(R.id.connect);
          TextView t1=getActivity().findViewById(R.id.msg);
          AsyncTaskManager asyncTaskManager=new AsyncTaskManager(a1,t1,(ListView)getActivity().findViewById(R.id.list));
          AsyncTaskManager.id=e1.getText().toString().trim();
          a1.setText("connecting wait a moment");
          t1.setText("");
          a1.setClickable(false);

          asyncTaskManager.execute(e1.getText().toString(),e4.getText().toString());
          getFragmentManager().beginTransaction().remove(this).commit();
      }
       if(view==b3)
       {
            getFragmentManager().beginTransaction().remove(this).commit();
       }


    }
}
