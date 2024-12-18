package com.example.weatherwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class rv_adapter extends RecyclerView.Adapter<rv_adapter.ViewHolder>
{
    Context context;
    ArrayList<rv_items> rv_itemsArrayList;

    public rv_adapter(Context context, ArrayList<rv_items> rv_itemsArrayList) {
        this.context = context;
        this.rv_itemsArrayList = rv_itemsArrayList;
    }

    @NonNull
    @Override
    public rv_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rv_adapter.ViewHolder holder, int position)
    {
        rv_items items=rv_itemsArrayList.get(position);

        holder.tvTemp.setText(items.getTemp()+" °C");
        holder.tvWindSpeed.setText(items.getWindSpeed()+" Km/h");
        Picasso.get().load("https:".concat(items.getIcon())).into(holder.ivDayNight);

        SimpleDateFormat input=new SimpleDateFormat("yyyy-mm-dd hh:mm");
        SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");
        try
        {
            Date t=input.parse(items.getTime());
            holder.tvTime.setText(output.format(t));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount()
    {
        return rv_itemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTime,tvTemp,tvWindSpeed;
        ImageView ivDayNight;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvTemp=itemView.findViewById(R.id.tvTemp);
            tvWindSpeed=itemView.findViewById(R.id.tvWindSpeed);
            ivDayNight=itemView.findViewById(R.id.ivDayNight);
        }
    }
}