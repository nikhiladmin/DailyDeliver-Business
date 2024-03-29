package com.daytoday.business.dailydelivery.MainHomeScreen.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Customers;
import com.daytoday.business.dailydelivery.MainHomeScreen.UI.CalenderActivity;
import com.daytoday.business.dailydelivery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    Context context;
    List<Customers> customersList;
    Bussiness currentBussiness;

    public CustomerAdapter(Context context, List<Customers> customersList, Bussiness currentBussiness) {
        this.context = context;
        this.customersList = customersList;
        this.currentBussiness = currentBussiness;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_card, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        holder.customer_name.setText(customersList.get(position).getName());
        holder.product_name.setText(currentBussiness.getName());
        holder.customer_address.setText(customersList.get(position).getAddress());
        String imgPath = customersList.get(position).getCustProfilepic();
        if (imgPath != null && !imgPath.isEmpty()) {
            Picasso.get()
                    .load(imgPath)
                    .into(holder.custProfilePic);
        }
        holder.customer_status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CalenderActivity.class);
                intent.putExtra(CalenderActivity.PRODUCT_NAME, currentBussiness.getName());
                intent.putExtra(CalenderActivity.UNIQUE_ID, customersList.get(position).getBussCustID());
                intent.putExtra(CalenderActivity.BUSINESS_ID, currentBussiness.getBussid());
                intent.putExtra(CalenderActivity.CUSTOMER_ID, customersList.get(position).getCustId());
                intent.putExtra(CalenderActivity.MOrD, currentBussiness.getdOrM());
                intent.putExtra(CalenderActivity.PRODUCT_PRICE, currentBussiness.getPrice());
                intent.putExtra(CalenderActivity.CUSTOMER_NAME,customersList.get(position).getName());
                intent.putExtra(CalenderActivity.CUSTOMER_TOKEN,customersList.get(position).getCustToken());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customersList.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView call_customer_btn;
        TextView customer_name, customer_address, product_name;
        CircleImageView custProfilePic;
        Button customer_status_btn;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_address = itemView.findViewById(R.id.customer_address);
            customer_name = itemView.findViewById(R.id.customer_name);
            call_customer_btn = itemView.findViewById(R.id.call_customer_btn);
            custProfilePic = itemView.findViewById(R.id.custprofilepic);
            product_name = itemView.findViewById(R.id.product_name);
            customer_status_btn = itemView.findViewById(R.id.customer_status_btn);
        }
    }
}
