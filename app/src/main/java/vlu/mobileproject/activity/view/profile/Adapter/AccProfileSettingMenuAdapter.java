package vlu.mobileproject.activity.view.profile.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import io.paperdb.Paper;
import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.profile.Model.AccProfileSettingMenuModel;
import vlu.mobileproject.login.LoginActivity;
import vlu.mobileproject.login.StartupActivity;

public class AccProfileSettingMenuAdapter extends RecyclerView.Adapter<AccProfileSettingMenuAdapter.ViewHolder> {

    Activity activity;
    ArrayList<AccProfileSettingMenuModel> accProfileSettingMenuModel;

    boolean check = true;
    int row_index = -1;

    public AccProfileSettingMenuAdapter(Activity activity, ArrayList<AccProfileSettingMenuModel> homeHorModels) {
        this.activity = activity;
        this.accProfileSettingMenuModel = homeHorModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.acc_vertical_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int AdapterPosition = holder.getAdapterPosition();

        holder.imageView.setImageResource(accProfileSettingMenuModel.get(AdapterPosition).getImage());
        holder.name.setText(accProfileSettingMenuModel.get(AdapterPosition).getSettingName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedSettingItem = accProfileSettingMenuModel.get(AdapterPosition).getSettingCode();

                if (selectedSettingItem.equals("logout")) {
                    FirebaseAuth.getInstance().signOut();
                    Paper.init(activity);
                    Paper.book().destroy();

                    Intent intent = new Intent(activity, StartupActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return accProfileSettingMenuModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ver_img);
            name = itemView.findViewById(R.id.ver_text);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
