package in.games.jollygames.Adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.games.jollygames.Model.ApiGameModel;
import in.games.jollygames.R;


public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ApiGameModel> game_list;

    public GameAdapter(Activity activity, ArrayList<ApiGameModel> game_list) {
        this.activity = activity;
        this.game_list = game_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(activity).inflate(R.layout.row_games,null);
     ViewHolder holder = new ViewHolder(view);
     return holder;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ApiGameModel model = game_list.get(position);
       holder.game_name.setText(model.getGame_name());
//        holder.game_img.setImageDrawable(activity.getDrawable(model.getImg()));

        Glide.with(activity).load(model.getGame_logo()).asBitmap().into(holder.game_img);


    }

    @Override
    public int getItemCount() {
        return game_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView game_img ;
        TextView game_name ;
        RelativeLayout lin_game;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            game_img = itemView.findViewById(R.id.game_img);
            game_name = itemView.findViewById(R.id.game_name);
            lin_game = itemView.findViewById(R.id.lin_game);



        }
    }
}
