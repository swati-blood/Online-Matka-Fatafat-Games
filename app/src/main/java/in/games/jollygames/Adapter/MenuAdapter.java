package in.games.jollygames.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.jollygames.Model.MenuModel;
import in.games.jollygames.R;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    Context context;
    ArrayList<MenuModel> list;
    String page;

    public MenuAdapter(Context context, ArrayList<MenuModel> list , String page) {
        this.context = context;
        this.list = list;
        this.page = page;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =null;
        if (page.equals("wallet"))
        {
            view= LayoutInflater.from(context).inflate(R.layout.row_withdraw_options,null);
        }
        else
        {
            view= LayoutInflater.from(context).inflate(R.layout.row_menu,null);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {


        holder.img_item.setImageDrawable(context.getResources().getDrawable(list.get(position).getIcon()));
        holder.tv_name.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView tv_name;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            img_item=itemView.findViewById (R.id.iv_icon);
            tv_name=itemView.findViewById (R.id.tv_name);

        }
    }
}
