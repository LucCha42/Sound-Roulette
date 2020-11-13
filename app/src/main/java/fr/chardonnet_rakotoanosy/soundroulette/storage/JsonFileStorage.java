package fr.chardonnet_rakotoanosy.soundroulette.storage;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class JsonFileStorage<T> extends FileStorage<T> {

    private final static String NEXT_ID = "next_id";
    private final static String LIST = "list";
    private final static String EXTENSION = "json";

    protected JSONObject json;

    public JsonFileStorage(Context context, String name) {
        super(context, name, EXTENSION);
    }

    protected abstract T jsonObjectToObject(JSONObject jsonObject);
    protected abstract JSONObject objectToJsonObject(int id, T object);

    @Override
    protected void initialize(String s) {
        try {
            json = new JSONObject(s);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void create() {
        json = new JSONObject();
        try {
            json.put(LIST, new JSONObject());
            json.put(NEXT_ID, 0);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getValue() {
        return json.toString();
    }

    @Override
    public T find(int id) {
        T object = null;
        try {
            object = jsonObjectToObject(json.getJSONObject(LIST).getJSONObject(String.valueOf(id)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public List<T> findAll() {
        ArrayList<T> list = new ArrayList<>();
        try {
            Iterator<String> iterator = json.getJSONObject(LIST).keys();
            while (iterator.hasNext()) {
                list.add(jsonObjectToObject(json.getJSONObject(LIST).getJSONObject(iterator.next())));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    @Override
    public int size() {
        int size = 0;
        try {
            size = json.getJSONObject(LIST).length();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return size;
    }

    @Override
    public boolean insert(T elem) {
        int nextId = json.optInt(NEXT_ID);
        boolean res = false;
        try {
            json.getJSONObject(LIST).put(String.valueOf(nextId), objectToJsonObject(nextId, elem));
            json.put(NEXT_ID, nextId + 1);
            res = true;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        write();
        return res;
    }

    @Override
    public boolean update(int id, T elem) {
        boolean res = false;
        try {
            json.getJSONObject(LIST).put(String.valueOf(id), objectToJsonObject(id, elem));
            res = true;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        write();
        return res;
    }

    @Override
    public boolean delete(int id) {
        boolean res = false;
        try {
            json.getJSONObject(LIST).remove(String.valueOf(id));
            res = true;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        write();
        return res;
    }

    public int getNextId() {
        return json.optInt(NEXT_ID);
    }
}
