package impl;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.lang.reflect.Method;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.openide.awt.MenuBar;
import org.openide.util.RequestProcessor;
import org.openide.windows.WindowManager;

/**
 * Adapted from {@code org.netbeans.core.ui.warmup.MenuWarmUpTask}.
 */
class ReheatMenus implements Runnable {

    static void launch() {
        EventQueue.invokeLater(new ReheatMenus());
    }

    private ReheatMenus() {}

    private static final RequestProcessor RP = new RequestProcessor(ReheatMenus.class);

    private MenuBar bar;
    private Component[] comps;

    @Override public void run() {
        if (EventQueue.isDispatchThread()) {
            if (bar == null) {
                Frame main = WindowManager.getDefault().getMainWindow();
                assert main != null;
                if (main instanceof JFrame) {
                    JMenuBar menuBar = ((JFrame) main).getJMenuBar();
                    if (menuBar instanceof MenuBar) {
                        bar = (MenuBar) menuBar;
                        RP.post(this);
                    }
                }
            } else {
                comps = bar.getComponents();
                RP.post(this);
            }
        } else if (comps != null) {
            walkMenu(comps);
        } else {
            bar.waitFinished();
            EventQueue.invokeLater(this);
        }
    }

    private void walkMenu(Component[] items) {
        for (Component item : items) {
            if (!(item instanceof JMenu)) {
                continue;
            }
            try {
                Class<?> cls = item.getClass();
                Method m = cls.getDeclaredMethod("doInitialize");
                m.setAccessible(true);
                m.invoke(item);
                walkMenu(((JMenu) item).getMenuComponents());
            } catch (Exception x) {
                // most likely a NoSuchMethodException on a dynamic submenu
            }
        }
    }

}
