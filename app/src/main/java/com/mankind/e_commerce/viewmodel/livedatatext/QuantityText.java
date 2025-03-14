package com.mankind.e_commerce.viewmodel.livedatatext;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class QuantityText {
    private MutableLiveData<Integer> quantityText = new MutableLiveData<>();

    public QuantityText(){

    }

    public void increaseQuantity(){
        int currentValue = quantityText.getValue() != null ? quantityText.getValue() : 0;
        quantityText.setValue(currentValue + 1);
    }
    public void decreaseQuantity(){
        int currentValue = quantityText.getValue() != null ? quantityText.getValue() : 0;
        quantityText.setValue(currentValue - 1);
    }
    public LiveData<Integer> getQuantity(){
        return quantityText;
    }
}
