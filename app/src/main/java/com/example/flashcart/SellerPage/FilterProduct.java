package com.example.flashcart.SellerPage;

import android.widget.Filter;

import com.example.flashcart.Adaptor.AdaptorProductSeller;

import java.util.ArrayList;

public class FilterProduct extends Filter {

    private AdaptorProductSeller adaptor;
    private ArrayList<ModelProduct> filterList;

    public FilterProduct(AdaptorProductSeller adaptor, ArrayList<ModelProduct> filterList) {
        this.adaptor = adaptor;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
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
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adaptor.productList = (ArrayList<ModelProduct>) results.values;
        adaptor.notifyDataSetChanged();

    }
}
