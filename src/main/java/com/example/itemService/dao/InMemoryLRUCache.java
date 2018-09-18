package com.example.itemService.dao;

import com.example.itemService.models.ItemInfo;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryLRUCache {

    private static final int CAPACITY = 100;

    private Map<String, ItemInfo> itemsMap = new HashMap<>();
    private LinkedList<ItemInfo> itemQueue = new LinkedList<>();

    public ItemInfo set(String key, ItemInfo itemInfo)
    {
        if(itemsMap.containsKey(key))
        {
            ItemInfo itemInfo1 = itemsMap.get(key);
            itemQueue.remove(itemInfo1);
            itemQueue.offer(itemInfo);
            return itemInfo;
        }
        else
        {
            if(itemsMap.size()>=CAPACITY)
            {
                String oldestItem = itemQueue.peek().getItem().getId();
                if(itemsMap.containsKey(oldestItem))
                {
                    itemsMap.remove(oldestItem);
                }
                itemQueue.poll();
                itemQueue.offer(itemInfo);

            }
            else
            {
                itemQueue.offer(itemInfo);
            }
            itemsMap.put(key, itemInfo);
            return itemInfo;
        }
    }

    public Collection<ItemInfo> getAll()
    {
        return itemsMap.values();

    }

}
