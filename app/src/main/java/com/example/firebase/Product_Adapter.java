package com.example.firebase;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class Product_Adapter extends FirebaseRecyclerAdapter<product_data,Product_Adapter.ProductHolder>
{
    Context context;
    List<product_data> productdata;
    TextView uname,uprice,udec;
    Button update;
    ImageView uimage;

    public Product_Adapter(@NonNull FirebaseRecyclerOptions<product_data> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Product_Adapter.ProductHolder holder, int position, @NonNull product_data model) {

        holder.pName.setText(""+model.getName());
        holder.pPrice.setText(""+model.getPrice());
        holder.pDes.setText(""+model.getDec());
        Glide.with(holder.imageView.getContext()).load(model.getImgUrl()).into(holder.imageView);
        Log.d("TTT", "onBindViewHolder: "+model.getName());

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,holder.relativeLayout);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setGravity(Gravity.END);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId()==R.id.update)
                        {
                            Dialog dialog=new Dialog(context);
                            dialog.setContentView(R.layout.update_layout);
                            uname=dialog.findViewById(R.id.update_product_name);
                            uprice=dialog.findViewById(R.id.update_product_price);
                            udec=dialog.findViewById(R.id.update_product_desc);
                            uimage=dialog.findViewById(R.id.update_imageview);
                            update=dialog.findViewById(R.id.update_button);

                        }
                        return false;
                    }
                });
                return true;
            }
        });

    }

    @NonNull
    @Override
    public Product_Adapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_show_item,parent,false);
        ProductHolder holder=new ProductHolder(view);
        return holder;
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView pName,pPrice,pDes;
        ImageView imageView;
        RelativeLayout relativeLayout;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            pName=itemView.findViewById(R.id.product_show_name);
            pPrice=itemView.findViewById(R.id.product_show_price);
            pDes=itemView.findViewById(R.id.product_show_desc);
            imageView=itemView.findViewById(R.id.product_show_image);
            relativeLayout=itemView.findViewById(R.id.Reletiv_show);
        }
    }
}
