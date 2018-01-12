package com.serverless;

import com.fasterxml.jackson.databind.util.LRUMap;

public class OffersInMemoryCache <K, T> {

    private static LRUMap OffersInMemoryCacheMap;

    protected  class Offers {
        public T value;
        protected Offers(T value){
            this.value = value;
        }
    }

    public OffersInMemoryCache(int maxItems){
        OffersInMemoryCacheMap = new LRUMap(maxItems, maxItems);

    }

    public void put(K key, T value) {
        synchronized (OffersInMemoryCacheMap) {
            OffersInMemoryCacheMap.put(key, new Offers(value));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (OffersInMemoryCacheMap) {
            Offers c = (Offers) OffersInMemoryCacheMap.get(key);
                return c.value;
        }
    }

    public int size() {
        synchronized (OffersInMemoryCacheMap) {
            return OffersInMemoryCacheMap.size();
        }
    }
}
