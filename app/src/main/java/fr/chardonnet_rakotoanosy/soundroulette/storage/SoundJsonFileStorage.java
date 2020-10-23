package fr.chardonnet_rakotoanosy.soundroulette.storage;

import android.content.Context;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import fr.chardonnet_rakotoanosy.soundroulette.Sound;

public class SoundJsonFileStorage extends JsonFileStorage<Sound> {

    private static final String NAME = "sound";
    private static final String SOUND_ID = "id";
    private static final String SOUND_NAME = "name";
    private static final String SOUND_URI = "uri";

    private static SoundJsonFileStorage storage;

    private SoundJsonFileStorage(Context context) {
        super(context, NAME);
    }

    public static SoundJsonFileStorage get(Context context) {
        if (storage == null) {
            storage = new SoundJsonFileStorage(context);
        }
        return storage;
    }

    @Override
    protected Sound jsonObjectToObject(JSONObject jsonObject) {
        try {
            return new Sound(
                    jsonObject.getInt(SOUND_ID),
                    jsonObject.getString(SOUND_NAME),
                    Uri.parse(jsonObject.getString(SOUND_URI))
            );
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected JSONObject objectToJsonObject(int id, Sound object) {
        JSONObject json = new JSONObject();
        try {
            json.put(SOUND_ID, id);
            json.put(SOUND_NAME, object.getName());
            json.put(SOUND_URI, object.getUri());
        }
        catch (JSONException e) {
            e.printStackTrace();
            json = null;
        }
        return json;
    }
}
