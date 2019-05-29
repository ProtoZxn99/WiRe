package patrick.pramedia.wire.modul;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by PRA on 4/5/2019.
 */

public class SessionManager {

    public void setPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("WIRE", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }


    public  String getPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("WIRE",	Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }
}
