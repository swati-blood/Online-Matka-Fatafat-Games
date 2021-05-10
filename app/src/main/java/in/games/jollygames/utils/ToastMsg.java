package in.games.jollygames.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import in.games.jollygames.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 13,June,2020
 */
public class ToastMsg {
    Context context;
    LayoutInflater layoutInflater;

    public ToastMsg(Context context) {
        this.context = context;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void toastIconError(String s)
    {
        Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        android.view.View view=layoutInflater.inflate(R.layout.toast_icon_text,null);
        ((android.widget.TextView)view.findViewById(R.id.message)).setText(s);
        ((ImageView)view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
        ((CardView) view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        toast.setView(view);
        toast.show();
    }

    public void toastIconSuccess(String s) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        android.view.View custom_view = layoutInflater.inflate(R.layout.toast_icon_text, null);
        ((android.widget.TextView) custom_view.findViewById(R.id.message)).setText(s);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.green));

        toast.setView(custom_view);
        toast.show();
    }
}
