package com.example.flashcart.Filter;

import android.widget.Filter;

import com.example.flashcart.Adaptor.AdaptorProductSeller;
import com.example.flashcart.Adaptor.AdaptorUserShopProduct;
import com.example.flashcart.Model.ModelProduct;

import java.util.ArrayList;

public class FilterProductUser extends Filter {

    private AdaptorUserShopProduct adaptor;
    private ArrayList<ModelProduct> filterList;

    public FilterProductUser(AdaptorUserShopProduct adaptor, ArrayList<ModelProduct> filterList) {
        this.adaptor = adaptor;
        this.filterList = filterList;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();
        //here we will validate the data for reasearch query
        if(constraint != null && constraint.length()>0){

            //chnage to uppercase, to make case sensitive

            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelProduct> filteredModels = new ArrayList<>();
            for(int i=0;i<filterList.size();i++){
                //here we will check by title and category

                if(filterList.get(i).getProductTitle().toUpperCase().contains(constraint) ||
                        filterList.get(i).getProductCategory().toUpperCase().contains(constraint)){

                    //if upper case will run then we will add final filtered data to list

                    filteredModels.add(filterList.get(i));

                }

            }

            results.count = filteredModels.size();
            results.values = filteredModels;

        }else {

            //if not found then we will show orignal list --> check on "AdaptorProductSeller" in --> AdaptorProductSeller this.filterList = productList;
            results.count = filterList.size();
            results.values = filterList;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {

        adaptor.productsList = (ArrayList<ModelProduct>) results.values;
        adaptor.notifyDataSetChanged();

    }


}
