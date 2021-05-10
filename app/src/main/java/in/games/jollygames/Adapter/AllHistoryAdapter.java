package in.games.jollygames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.jollygames.Model.HistryModel;
import in.games.jollygames.R;


public class AllHistoryAdapter extends RecyclerView.Adapter<AllHistoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HistryModel> list;

    public AllHistoryAdapter(Context context, ArrayList<HistryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View vi = LayoutInflater.from(context).inflate(R.layout.row_history,null);
       ViewHolder holder = new ViewHolder(vi);
       return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistryModel model = list.get(position);
        String bid_time = model.getTime();
        String d = bid_time.substring(0,10);
        String dd[] =d.split("-");
        holder.txt_play_on.setText(dd[2]+"/"+dd[1]+"/"+dd[0]);
        String t = bid_time.substring(11,bid_time.length());
        SimpleDateFormat tformat = new SimpleDateFormat("hh:mm:ss");
        try {
            Date tt = tformat.parse(t);
            SimpleDateFormat f = new SimpleDateFormat("hh:mm a");
            String ttt = f.format(tt);
            holder.txt_bid_time.setText(ttt);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(model.getBet_type ().equals ("")){
            holder.txt_matka_name.setText (model.getName ());
        }else {
            holder.txt_matka_name.setText(model.getName()+" ( "+model.getBet_type()+" ) ");
        }


        //holder.txt_matka_name.setText(model.getName()+" ( "+model.getBet_type()+" ) ");
        holder.txt_bid_digit.setText("Digit: "+model.getDigits());
        holder.txt_bid_id.setText(model.getId());
        holder.txt_bid_points.setText("Points:"+model.getPoints());
        holder.txt_play_for.setText(model.getDate());


     if (model.getStatus().equals("pending"))
     {
         holder.txt_result.setText("Result not announced");
//         holder.txt_result.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icons8_neutral_16px,0);
     }
     else if(model.getStatus().equals("won")||model.getStatus().equals("win"))
     {
         holder.txt_result.setText("You wont the bid");
//         holder.txt_result.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icons8_happy_16px,0);
     }
     else if(model.getStatus().equals("loss"))
     {  holder.txt_result.setText("You Lost the bid");
//         holder.txt_result.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icons8_sad_16px,0);
     }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_matka_name ,txt_play_on ,txt_play_for,txt_bid_id,txt_bid_digit,txt_bid_points,txt_bid_time,txt_result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_matka_name= itemView.findViewById(R.id.game_name);
            txt_play_for= itemView.findViewById(R.id.game_type);
            txt_play_on= itemView.findViewById(R.id.txtDate);
            txt_bid_digit= itemView.findViewById(R.id.txtDigit);
            txt_bid_id= itemView.findViewById(R.id.bid_id);
            txt_bid_points= itemView.findViewById(R.id.points);
            txt_bid_time= itemView.findViewById(R.id.txtDate);
            txt_result= itemView.findViewById(R.id.status);
        }
    }
}
