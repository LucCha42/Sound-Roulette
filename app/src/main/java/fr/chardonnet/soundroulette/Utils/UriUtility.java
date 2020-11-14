package fr.chardonnet.soundroulette.Utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriUtility {

    public static String getFileName(@NonNull Uri uri, @NonNull ContentResolver contentResolver) {
        String result = null;

        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');

            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }

        // removing the extension from file name
        Pattern p = Pattern.compile("(.*)\\.[^.]+$");
        Matcher m = p.matcher(result);
        if(m.matches()) {
            return m.group(1);
        }

        return result;
    }

}
