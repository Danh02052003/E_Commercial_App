package vlu.mobileproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vlu.mobileproject.modle.Phone;
import vlu.mobileproject.R;

public class Phone_Adapter_Phone  extends
        RecyclerView.Adapter<Phone_Adapter_Phone.ViewHolder>{
        private Context mContext;
        private ArrayList<Phone> mPhone;

        public void setFilterList(ArrayList<Phone> filterList){
            this.mPhone = filterList;
            notifyDataSetChanged();
        }
    public Phone_Adapter_Phone(Context context, ArrayList<Phone> mPhone) {
        this.mContext = context;
        this.mPhone = mPhone;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.custom_layout_phone_search, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
            return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Phone phone = mPhone.get(position);
        holder.imgPhone.setImageResource(phone.getImagePhone());
        holder.namePhone.setText(phone.getNamePhone());
    }

    @Override
    public int getItemCount() {
        return mPhone.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhone;
        private TextView namePhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhone = itemView.findViewById(R.id.imgPhoneSearch);
            namePhone = itemView.findViewById(R.id.txtNamePhone);

        }
    }
}
