package yuqi.amc;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yuqi.amc.JsonData.Part;

/**
 * Created by ClayW on 7/05/2017.
 */

// Cart object(struct)
public class Cart {

    private HashMap<String, Long> cart;

    public Cart(){
        // The cart can have one of type of parts
        cart = new HashMap<>();
        cart.put("Respray", null);
        cart.put("Bumper", null);
        cart.put("Bonnet", null);
        cart.put("Spoiler", null);
        cart.put("Exhaust", null);
        cart.put("Suspension", null);
        cart.put("Rim", null);
        cart.put("Brake", null);
        cart.put("Tyre", null);
        cart.put("Lighting", null);
    }

    // Method for adding items to the cart, as well as replace/restore current item
    public void addToCart(Part part){
        String type = part.getType();
        long id = part.getId();

        String test = part.getName();

        if (cart.get(type)!=null){

            if (cart.get(type) == id){

                cart.put(type, null);
                Log.e("Cart", "Deselect " + test);

            }else {
                cart.put(type, id );
                Log.e("Cart", "Replace with " + test);
            }
        }else {
            cart.put(type, id );
            Log.e("Cart", "Select " + test);
        }
    }

    // Method for checking whether the cart is empty or not
    public boolean isCartEmpty(){
        for(Map.Entry<String, Long> entry : cart.entrySet()) {
            //String key = entry.getKey();
            if (entry.getValue()!=null){
                Log.e("Cart","Has item");
                return false;
            }
        }
        return true;
    }

    public HashMap<String, Long> getCart() {
        return cart;
    }

    public Long getValue(String key){
        return cart.get(key);
    }

    // Method for turning cart into a json query for executing on the server side
    public static String getQuery(HashMap<String, Long> cart){

        ArrayList<Long> itemList = new ArrayList<>();

        for(Map.Entry<String, Long> entry : cart.entrySet()) {
            Long id = entry.getValue();
            if (id!=null){
                itemList.add(id);
            }
        }

        String query = "";

        for (int i =0; i < itemList.size(); i ++){

            query += String.valueOf(itemList.get(i));

            if (i + 1 != itemList.size()){
                query += ",";
            }

        }

        Log.e("Query", query);

        return query;
    }
}
