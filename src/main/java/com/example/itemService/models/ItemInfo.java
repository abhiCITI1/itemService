package com.example.itemService.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class ItemInfo {

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("item")
    private Item item;



}
