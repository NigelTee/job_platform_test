package materialtest.example.user.materialtest.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class L {
    public static void m(String message) {
        Log.d("Nigel",""+message);}

    public static void t(Context context, String message){
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }
}
