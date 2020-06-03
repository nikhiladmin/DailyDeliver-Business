package com.daytoday.business.dailydelivery.MainHomeScreen.View;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.EmployeeInfo;
import com.daytoday.business.dailydelivery.MainHomeScreen.UI.EmpInfoActivity;
import com.daytoday.business.dailydelivery.R;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    List<EmployeeInfo> employeeInfoList;
    Context context;

    public EmployeeAdapter(List<EmployeeInfo> employeeInfoList, Context context) {
        this.employeeInfoList = employeeInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, final int position) {


        /*-------------------------------          Set Text As Employee Name            --------------------------------------------------*/
        holder.emp_name.setText(employeeInfoList.get(position).getName());



        /*-------------------------------          Set Text As Employee ID              --------------------------------------------------*/
        holder.emp_id.setText("ID : " + employeeInfoList.get(position).getId());



        /*-------------------------------          Click Listener on Details Button     --------------------------------------------------*/
        holder.empl_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EmpInfoActivity.class);
                view.getContext().startActivity(intent);
            }
        });


        /*-------------------------------          Click Listener on Delete Button     --------------------------------------------------*/
        holder.del_emp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        /*-------------------------------          Click Listener on Call Button     --------------------------------------------------*/
        holder.call_emp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+employeeInfoList.get(position).getPhoneNo()));
                view.getContext().startActivity(intent);
            }
        });


        /*-------------------------------          Load the Image Here



             --------------------------------------------------*/
    }

    @Override
    public int getItemCount() {
        return employeeInfoList.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        ImageView empl_img,call_emp_btn;
        TextView emp_name,emp_id;
        Button del_emp_btn,empl_detail_btn;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            emp_id = itemView.findViewById(R.id.emp_id);
            emp_name = itemView.findViewById(R.id.emp_name);
            empl_detail_btn = itemView.findViewById(R.id.empl_detail_btn);
            empl_img = itemView.findViewById(R.id.empl_img);
            call_emp_btn = itemView.findViewById(R.id.call_emp_btn);
            del_emp_btn = itemView.findViewById(R.id.del_empl_btn);
        }
    }
}
