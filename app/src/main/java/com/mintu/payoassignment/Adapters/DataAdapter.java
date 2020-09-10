package com.mintu.payoassignment.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mintu.payoassignment.Models.ItemModel;
import com.mintu.payoassignment.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created By Mintu Giri on 9/10/2020.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> implements View.OnLongClickListener {

    private List<ItemModel> list;
    private Context context;

    public DataAdapter(List<ItemModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout
                , parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, final int position) {
        final ItemModel itemModel = list.get(position);

        holder.name.setText(itemModel.getFname() + " " + itemModel.getLname());
        holder.email.setText(itemModel.getEmail());
        Picasso.get().load(itemModel.getImageUrl()).into(holder.imageView);

        holder.itemView.setOnLongClickListener(this);

        holder.itemView.setTag(holder);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onLongClick(final View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Delete!!!")
                .setMessage("Are you sure to delete this item?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ViewHolder holder = (ViewHolder) view.getTag();
                        list.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
        return true;
    }

    public class MyViewHolder extends ViewHolder {

        CircularImageView imageView;
        TextView name, email;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
