package yuqi.amc;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import yuqi.amc.JsonData.Part;

// Cart object(struct)
public class Cart {


    private HashMap<String, Long> cart;
    private HashMap<String, Integer> selectedItem;

    private HashMap<String, Long[]> cartV2;

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

        selectedItem = new HashMap<>();
        selectedItem.put("Respray", null);
        selectedItem.put("Bumper", null);
        selectedItem.put("Bonnet", null);
        selectedItem.put("Spoiler", null);
        selectedItem.put("Exhaust", null);
        selectedItem.put("Suspension", null);
        selectedItem.put("Rim", null);
        selectedItem.put("Brake", null);
        selectedItem.put("Tyre", null);
        selectedItem.put("Lighting", null);

        cartV2 = new HashMap<>();
        cartV2.put("Respray", null);
        cartV2.put("Respray", null);
        cartV2.put("Bumper", null);
        cartV2.put("Bonnet", null);
        cartV2.put("Spoiler", null);
        cartV2.put("Exhaust", null);
        cartV2.put("Suspension", null);
        cartV2.put("Rim", null);
        cartV2.put("Brake", null);
        cartV2.put("Tyre", null);
        cartV2.put("Lighting", null);

    }

    // Method for adding items to the cart, as well as replace/restore current item
    public void addToCart(Part part, Integer position){
        String type = part.getType();
        long id = part.getId();
        long pos = position;

        String test = part.getName();

        if (cart.get(type)!=null){

            if (cart.get(type) == id){

                cart.put(type, null);
                selectedItem.put(type, null);
                cartV2.put(type, null);
                Log.e("Cart", "Deselect " + test);

            }else {
                Long set[] = new Long[2];
                set[0] = id;
                set[1] = pos;
                cart.put(type,id);
                selectedItem.put(type, position);
                cartV2.put(type, set);
                Log.e("Cart", "Replace with " + test);
            }
        }else {
            Long set[] = new Long[2];
            set[0] = id;
            set[1] = pos;
            cart.put(type, id );
            selectedItem.put(type, position);
            cartV2.put(type, set);
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

    public Integer getPosition(String key) { return selectedItem.get(key); }

    public Long getValueV2(String key, Integer mode) {

        Long value[] = cartV2.get(key);

        if (value!=null){

            return value[mode];

        }else {

            return null;
        }

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
