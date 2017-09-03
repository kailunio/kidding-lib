package kidding.lib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.IBinder;
import android.util.ArrayMap;

import org.joor.Reflect;
import org.joor.ReflectException;

import java.lang.reflect.Field;

/**
 * @author xukailun on 2017/7/29.
 */
public class Kid {

    public static Application appliction() {
        return Kidding.instance().getApplication();
    }

    public static Activity activity() {
        Activity activity = null;
        try {
            activity = getRunningActivity();
        } catch (ReflectException e) {
            // do nothing
        }
        return activity;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static Activity getRunningActivity() throws ReflectException {
        ArrayMap<IBinder, ?> activities = Reflect.on("android.app.ActivityThread")
                .call("currentActivityThread")
                .get("mActivities");
        for (Object activityRecord : activities.values()) {
            Reflect reflect = Reflect.on(activityRecord);
            boolean paused = reflect.get("paused");
            if (!paused) {
                return reflect.get("activity");
            }
        }
        return null;
    }
}
