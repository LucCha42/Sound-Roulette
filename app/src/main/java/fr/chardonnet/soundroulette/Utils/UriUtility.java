package fr.chardonnet.soundroulette.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;

import java.io.File;

public class UriUtility {

    @NonNull
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

        return result;
    }

}