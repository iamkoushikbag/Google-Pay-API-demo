package com.activities.koushik.google_pay_demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.activities.koushik.google_pay_demo.R;
import com.activities.koushik.google_pay_demo.model.ProductModel;

import java.util.List;

/**
 * Created by Mindtree on 9/4/2018.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private Context mContext;
    private List<ProductModel> mItemList;
    private IProductItem mIProductItem;

    public ProductListAdapter(Context mContext,
                              List<ProductModel> mItemList,
                              IProductItem ifc) {
        this.mContext = mContext;
        this.mItemList = mItemList;
        mIProductItem = ifc;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        holder.vIVProductThumbnail.setImageResource(mItemList.get(position).getImageUrl());
        String formattedPrice = String.format("$%s", mItemList.get(position).getPrice());
        holder.vTVPrice.setText(formattedPrice);
        holder.vTVQuantity.setText(mItemList.get(position).getQuantity());
        holder.vTVProductName.setText(mItemList.get(position).getProductName());

        holder.vBtnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIProductItem.onBuyClick(mItemList.get(position).getPrice());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private Button vBtnAddToCart;
        private TextView vTVPrice;
        private TextView vTVQuantity;
        private TextView vTVProductName;
        private ImageView vIVProductThumbnail;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            vBtnAddToCart = itemView.findViewById(R.id.btn_ad_to_cart);
            vTVPrice = itemView.findViewById(R.id.tv_product_price);
            vTVQuantity = itemView.findViewById(R.id.tv_product_qty);
            vTVProductName = itemView.findViewById(R.id.tv_product_name);
            vIVProductThumbnail = itemView.findViewById(R.id.iv_product);
        }
    }

    public interface IProductItem {
        void onBuyClick(String price);
    }
}
