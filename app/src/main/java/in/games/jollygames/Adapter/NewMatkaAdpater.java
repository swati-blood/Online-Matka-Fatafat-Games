package in.games.jollygames.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.jollygames.Activity.GameListActivity;
import in.games.jollygames.Common.Common;
import in.games.jollygames.Model.MatkasObjects;
import in.games.jollygames.R;
import in.games.jollygames.utils.ToastMsg;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 09,September,2020
 */
public class NewMatkaAdpater extends RecyclerView.Adapter<NewMatkaAdpater.ViewHolder> {
    private final String TAG= NewMatkaAdpater.class.getSimpleName();
    private Activity context;
    Common common ;
    private ArrayList<MatkasObjects> list;
    private int flag=0;

    public NewMatkaAdpater(Activity context, ArrayList<MatkasObjects> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(context).inflate(R.layout.row_home_matka,null);
         return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MatkasObjects model=list.get(position);
        holder.txtMatkaName.setText(model.getName());
        String startTime="";
        String endTime="";
        String dy=new SimpleDateFormat("EEEE").format(new Date());
//        String dy="Sunday";
        if(dy.equalsIgnoreCase("Sunday")){
          if(getValidTime(model.getStart_time().toString(),model.getEnd_time().toString())){
              startTime=model.getStart_time();
              endTime=model.getEnd_time();
          }else{
              startTime=model.getBid_start_time();
              endTime=model.getBid_end_time();
          }
        }else if(dy.equalsIgnoreCase("Saturday")){
            if(getValidTime(model.getSat_start_time().toString(),model.getSat_end_time().toString())){
                startTime=model.getSat_start_time();
                endTime=model.getSat_end_time();
            }else{
                startTime=model.getBid_start_time();
                endTime=model.getBid_end_time();
            }
        }else{
            startTime=model.getBid_start_time();
            endTime=model.getBid_end_time();
        }
        Log.e("matka_time", "onBindViewHolder: "+model.getName()+"--"+startTime+"\n "+endTime );
        holder.txtmatkaBid_openTime.setText("OPEN\n"+common.get24To12Format(startTime));
        holder.txtmatkaBid_closeTime.setText("CLOSE \n"+common.get24To12Format(endTime));

        holder.txtMatka_startingNo.setText(getValidNumber(model.getStarting_num(),1)+"-"+getValidNumber(model.getNumber(),2)+"-"+getValidNumber(model.getEnd_num(),3));

        Date date=new Date();
        SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
        String time1 = startTime.toString();
        String time2 = endTime.toString();

        Date cdate=new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date2=null;
        Date date3=null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
            date3=format.parse(time3);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;

        long diff_close=date3.getTime()-date2.getTime();
        long c=(diff_close/1000)/60;
      if(dy.equalsIgnoreCase("Sunday")){
          if(getValidTime(model.getStart_time().toString(),model.getEnd_time().toString())){
              getPlayButton(as,c,holder.tv_status,holder.btnPlay);
          }else
          {
              setInactiveStatus(holder.tv_status,holder.btnPlay);
//              holder.btnPlay.setVisibility(View.GONE);
          }
      }else if(dy.equalsIgnoreCase("Saturday")){
          if(getValidTime(model.getSat_start_time().toString(),model.getSat_end_time().toString())){
              getPlayButton(as,c,holder.tv_status,holder.btnPlay);
          }else
          {
              setInactiveStatus(holder.tv_status,holder.btnPlay);

//              holder.btnPlay.setVisibility(View.GONE);
          }
      }else{

          if(getValidTime(model.getBid_start_time().toString(),model.getBid_end_time().toString())){
              getPlayButton(as,c,holder.tv_status,holder.btnPlay);
          }else
          {
              setInactiveStatus(holder.tv_status,holder.btnPlay);

//              holder.btnPlay.setVisibility(View.GONE);
          }
//          getPlayButton(as,c,holder.tv_status,holder.btnPlay);
      }

        holder.rel_matka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              gotoGames(model);
            }
        });
      holder.btnPlay.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              gotoGames(model);
          }
      });

//        holder.rel_matka.setBackgroundColor(Color.parseColor(list.get(position).getColor_code()));
//
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtmatkaBid_openTime,txtmatkaBid_closeTime,txtMatkaName,txtMatka_startingNo;
        TextView txtMatka_id ,tv_status;

        LinearLayout rel_matka;
       ImageView btnPlay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmatkaBid_openTime=(TextView)itemView.findViewById(R.id.tv_open);
            txtmatkaBid_closeTime=(TextView)itemView.findViewById(R.id.tv_close);
            txtMatkaName=(TextView)itemView.findViewById(R.id.tv_name);
            txtMatka_startingNo=(TextView)itemView.findViewById(R.id.tv_number);
            tv_status=(TextView)itemView.findViewById(R.id.tv_status);
            rel_matka = itemView.findViewById(R.id.lin_matka);
            btnPlay=itemView.findViewById(R.id.iv_play);
         common = new Common(context);


        }
    }

    public boolean getValidTime(String sTime, String eTime){

        if(sTime.equalsIgnoreCase("00:00:00") && eTime.equalsIgnoreCase("00:00:00")){
          return false;
        }else if(sTime.equalsIgnoreCase("00:00:00.000000") && eTime.equalsIgnoreCase("00:00:00.000000")){
         return false;
        }else{
            return true;
        }
    }

    public String getValidNumber(String str, int palace){
        String validStr="";
        if(str ==null || str.isEmpty() || str.equalsIgnoreCase("null")){
            if(palace==1){
                validStr="***";
            }else if(palace==2){
                validStr="**";
            }else{
                validStr="***";
            }
        }else{
            validStr=str;
        }
        return validStr;
    }
public void getPlayButton(long as, long c, TextView tv_status, ImageView btnPlay){
    if(as<0)
    {
        flag=2;
//       tv_status.setVisibility(View.VISIBLE);
       setActiveStatus(tv_status,btnPlay);
//        btnPlay.setVisibility(View.VISIBLE);

    }


    else if(c>0)
    {
        flag=3;
        setInactiveStatus(tv_status,btnPlay);
//        tv_status.setVisibility(View.VISIBLE);
//        btnPlay.setVisibility(View.GONE);
    }
    else
    {
        flag=1;
//        tv_status.setVisibility(View.VISIBLE);
        setActiveStatus(tv_status,btnPlay);
//        btnPlay.setVisibility(View.GONE);
    }


}

public void setActiveStatus(TextView tv, ImageView btn){
        tv.setVisibility(View.GONE);
        tv.setText("BID Is Running For Today");
        tv.setTextColor(context.getResources().getColor(R.color.green));
        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.play));
}

    public void setInactiveStatus(TextView tv, ImageView btn){
        if(tv.getVisibility()== View.GONE){
            tv.setVisibility(View.VISIBLE);
                 }
        tv.setText("BID Is Closed For Today");
        tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.pause));
    }

    public void gotoGames(MatkasObjects model){
        String dyClick=new SimpleDateFormat("EEEE").format(new Date());
//                String dyClick="Sunday";
        Log.e("asdaee",""+dyClick);
        String stime ="";
        String etime ="";
        int err=0;
        boolean is_error=false;
        if(dyClick.equalsIgnoreCase("Sunday"))
        {
            if(getValidTime(model.getStart_time(),model.getEnd_time()))
            {err=1;
                stime=model.getStart_time().toString();
                etime=model.getEnd_time().toString();
                Log.e(TAG, "onClick: "+etime );

            }else{
                err=2;
                is_error=true;
            }


        }
        else if(dyClick.equalsIgnoreCase("Saturday"))
        {
            if(getValidTime(model.getSat_start_time(),model.getSat_end_time()))
            {
                err=3;
                stime=model.getSat_start_time().toString();
                etime=model.getSat_end_time().toString();
            }else{
                err=4;
                is_error=true;
            }
        }
        else
        {
            stime=model.getBid_start_time().toString();
            etime=model.getBid_end_time().toString();
        }

        long endDiff=common.getTimeDifference(etime);
//              common.showToast(""+err);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        if(endDiff<0 || is_error==true)
        {
            new ToastMsg(context).toastIconError("Bid is Closed for today");
        }
        else
        {
            try
            {

                // Toast.makeText(HomeActivity.this,""+Prevalent.Matka_count,Toast.LENGTH_LONG).show();

                // String st=txtStatus.getText().toString();
                String m_id=model.getId().toString().trim();
                Log.e("mat",m_id);
                String matka_name=model.getName().toString().trim();
                String status = model.getStatus();

                if (status.equals( "active" )) {

                    // Toast.makeText(context,"Position"+m_id,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent( context, GameListActivity.class );
                    //    intent.putExtra("tim",position);

                    intent.putExtra( "matkaName", matka_name );
                    intent.putExtra( "m_id", m_id );
                    //  intent.putExtra("bet","cb");
                    context.startActivity( intent );
                }
                else
                {
//                    common.errorMessageDialog("Betting is currently closed" );
                    new ToastMsg(context).toastIconError("Bid Closed");
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(context,""+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
