package kidding.lib;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xukailun on 2017/7/20.
 */
public class Kidding {

    private static Kidding INSTANCE = new Kidding();

    public static Kidding instance() {
        return INSTANCE;
    }

    private Kidding(){
        // singleton
    }

    public static final String ACTION = "kidding.command";

    private Application app = null;
    private AdbReceiver receiver = null;
    private String cProxy = "";
    private String defaultProxy = "";

    /**
     * 启动Kidding的Receiver
     * @param app application
     */
    public void start(@NotNull Application app) {
        if (receiver != null) {
            return;
        }

        // 启动广播监听器
        AdbReceiver receiver = new AdbReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        app.registerReceiver(receiver, intentFilter);

        // 初始化Preference
        PreferenceHelper.init(app);
        String proxy = PreferenceHelper.instance().getString(PreferenceHelper.KEY_IPV4);
        if (!TextUtils.isEmpty(proxy)) {
            cProxy = proxy;
        } else {
            cProxy = getDefaultProxy();
        }

        this.app = app;
        this.receiver = receiver;

        // Toast提示用户
        if (!TextUtils.isEmpty(cProxy)) {
            Toast.makeText(app, "代理服务器已设置：" + cProxy, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 停止Kidding的Receiver
     */
    public void stop() {
        if (receiver == null) {
            return;
        }

        this.app.unregisterReceiver(receiver);
        this.app = null;
        this.receiver = null;
    }

    /** package */ @Nullable Application getApplication() {
        return app;
    }

    public void setDefaultProxy(@NotNull String proxy) {
        this.defaultProxy = proxy;
    }

    @NotNull
    public String getDefaultProxy() {
        return defaultProxy;
    }

    @NotNull
    public String getCurrentProxy() {
        return cProxy;
    }

    public void modifyCurrentProxy(@NotNull String proxy) {
        cProxy = proxy;
    }

}
