package com.activities.koushik.google_pay_demo.model;

import com.activities.koushik.google_pay_demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mindtree on 9/4/2018.
 */
public class ConstractModel {
    private List<ProductModel> mProductList;
    private Integer[] mThumbIds = {
            R.drawable.img_1, R.drawable.img_2,
            R.drawable.img_3, R.drawable.img_4,
            R.drawable.img_5, R.drawable.img_6,
            R.drawable.img_7
    };

    public ConstractModel() {
        mProductList = new ArrayList<>();
    }

    public List<ProductModel> constructDataModel() {
        ProductModel model1 = new ProductModel("Clave", "clave",
                "100g", "10.00", mThumbIds[0]);
        ProductModel model2 = new ProductModel("Knorr soup", "knor_soup",
                "500g", "12.00", mThumbIds[1]);
        ProductModel model3 = new ProductModel("Knorr Tomato soup", "knor_tomato",
                "1kg", "1.00", mThumbIds[2]);
        ProductModel model4 = new ProductModel("Hollandaise sauce", "hollandaise_sauce",
                "1.5kg", "14.00", mThumbIds[3]);
        ProductModel model5 = new ProductModel("Mahluta soup", "mahluta_soup",
                "500g", "11.00", mThumbIds[4]);
        ProductModel model6 = new ProductModel("Lipton tea", "lipton_tea",
                "500g", "8.00", mThumbIds[5]);
        ProductModel model7 = new ProductModel("Lipton yellow label tea", "lipton_yellow_tea",
                "1kg", "25.00", mThumbIds[6]);

        mProductList.add(model1);
        mProductList.add(model2);
        mProductList.add(model3);
        mProductList.add(model4);
        mProductList.add(model5);
        mProductList.add(model6);
        mProductList.add(model7);
        return mProductList;
    }
}
