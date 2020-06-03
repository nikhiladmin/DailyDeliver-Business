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
import com.daytoday.business.dailydelivery.MainHomeScreen.UI.BusinessDetailActivity;
import com.daytoday.business.dailydelivery.R;

import java.util.List;

public class BussinessAdapter extends RecyclerView.Adapter<BussinessAdapter.BussinessViewHolder> {

    Context mCtx;
    List<Bussiness> bussinessList;

    public BussinessAdapter(Context mCtx, List<Bussiness> bussinessList) {
        this.mCtx = mCtx;
        this.bussinessList = bussinessList;
    }

    @NonNull
    @Override
    public BussinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(mCtx).inflate(R.layout.bussiness_card,parent,false);
        //com.daytoday.business.dailydelivery.MainHomeScreen.View view = inflater.inflate(R.layout.bussiness_card, null);
        BussinessViewHolder bussinessViewHolder = new BussinessViewHolder(inflater);
        return bussinessViewHolder;
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull BussinessViewHolder holder, int position) {

        Bussiness bussiness = bussinessList.get(position);

        holder.buss_name.setText(bussiness.getProductName());
        holder.tarrif.setText(bussiness.getTarrif());
        holder.tot_earning.setText(bussiness.getEarning());
        holder.prdct_amt.setText(bussiness.getPrice());
        holder.cust_count.setText("( " +bussiness.getCust_cout() + " Customers )");
        holder.tarrif.setText(bussiness.getPay_mode());

        holder.customers_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),CustomerActivity.class);
                intent.putExtra("buisness-Id", bussiness.getId());
                intent.putExtra("buisness-Name",bussiness.getProductName());
                v.getContext().startActivity(intent);
            }
        });

        holder.prdct_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BusinessDetailActivity.class);
                intent.putExtra("buisness-object", bussiness);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bussinessList.size();
    }

    class BussinessViewHolder extends RecyclerView.ViewHolder
    {
        ImageView buss1_image;
        TextView buss_name, cust_count, earning, tot_earning, prdct_amt, tarrif;
        Button customers_btn, prdct_detail_btn;

        public BussinessViewHolder(@NonNull View itemView) {
            super(itemView);
            buss1_image = itemView.findViewById(R.id.bussl_img);
            buss_name =  itemView.findViewById(R.id.buss_name);
            cust_count = itemView.findViewById(R.id.cust_count);
            earning = itemView.findViewById(R.id.earning);
            tot_earning = itemView.findViewById(R.id.tot_earning);
            prdct_amt = itemView.findViewById(R.id.prdct_amt);
            tarrif = itemView.findViewById(R.id.tarrif);
            customers_btn = itemView.findViewById(R.id.customers_btn);
            prdct_detail_btn = itemView.findViewById(R.id.prdct_detail_btn);
        }
    }
}
