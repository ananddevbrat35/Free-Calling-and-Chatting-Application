package devbrat.anand;

import android.content.Context;
import android.support.constraint.ConstraintSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

   static ArrayList<String> chat=new ArrayList<String>();
    static  int ind=0;
    LayoutInflater layoutInflater;
    Context ctx=Chatting.ct;
    TextView t1,t2;
   public ListAdapter()
    {
        layoutInflater=LayoutInflater.from(ctx);
     }
    @Override
    public int getCount() {
       if(chat!=null)
        return chat.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       int j=0;
        for( j=0;j<chat.get(i).length();j++)
        {

            if(chat.get(i).charAt(j)=='#')
            {

                break;
            }
        }
        if(view==null)
        {
            view=layoutInflater.inflate(R.layout.activity_list,viewGroup,false);
        }
        t1=view.findViewById(R.id.name);
        t2=view.findViewById(R.id.message);
        LinearLayout l1=view.findViewById(R.id.setit);


       if(!chat.get(i).substring(0,j).equals(AsyncTaskManager.name+AsyncTaskManager.inetAddress))
       {
           t1.setText(chat.get(i).substring(0,j));

           t2.setText(chat.get(i).substring(j+1));

           l1.setBackgroundColor(ctx.getApplicationContext().getResources().getColor(android.R.color.white));


        return view;
       }
       else
       {
           t1.setText("YOU");

           t2.setText(chat.get(i).substring(j+1));
           l1.setBackgroundColor(ctx.getApplicationContext().getResources().getColor(R.color.chatsend));


           return view;
       }


    }
}
