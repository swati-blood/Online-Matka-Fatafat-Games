package in.games.jollygames.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.jollygames.Common.Common;
import in.games.jollygames.Model.AddWithdrawModel;
import in.games.jollygames.R;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
    private Context context;
  Common common ;
    private ArrayList<AddWithdrawModel> list;

    public TransactionHistoryAdapter(Context context, ArrayList<AddWithdrawModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TransactionHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view= LayoutInflater.from(context).inflate(R.layout.row_transaction,null);
        final ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryAdapter.ViewHolder viewHolder, int i) {

        AddWithdrawModel objects=list.get(i);
       if( common.checkNull(objects.getTrans_id()))
        {
            viewHolder.txtId.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.txtId.setText(objects.getTrans_id());
        }
        viewHolder.txtAmount.setText(objects.getRequest_points());

        String st=objects.getType().toString();
//        String day="";
//        try
//        {
//            Date dt=new Date(  );
//            dt=new SimpleDateFormat( "dd/MM/yyyy" ).parse( objects.getTime() );
//            SimpleDateFormat simpleDateFormat=new SimpleDateFormat( "EEEE" );
//             day=simpleDateFormat.format( dt);
//        }
//        catch (Exception ex)
//        {
//         ex.printStackTrace();
//        }

        if(st.equalsIgnoreCase("Withdraw"))
        {
            viewHolder.iv_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icons8_minus_96px));
//            viewHolder.txtDescription.setText("Amount Debited For bidding " + objects.getName() + " "+objects.getBet_type()+" \n Bid Id :#"+objects.getBid_id());
        }
        else
        {
            viewHolder.iv_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icons8_add_96px));
//            viewHolder.txtDescription.setText("Amount Credited For bidding " + objects.getName() + " "+objects.getBet_type()+" \n Bid Id :#"+objects.getBid_id());
        }
        viewHolder.txtDate.setText(objects.getTime());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtId,txtAmount,txtDescription,txtStatus,txtDate;
        RelativeLayout rel_back ;
        ImageView iv_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId=(TextView)itemView.findViewById(R.id.trans_id);
            txtAmount=(TextView)itemView.findViewById(R.id.trans_amount);
            txtDescription=(TextView)itemView.findViewById(R.id.description);
            txtStatus=(TextView)itemView.findViewById(R.id.status);
            txtDate=(TextView)itemView.findViewById(R.id.txtDate);
            iv_icon = itemView.findViewById(R.id.iv_icon);
//            rel_back = itemView.findViewById( R.id.rel_transaction );
            common = new Common(context);

        }
    }
}
