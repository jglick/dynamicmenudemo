package api;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.swing.Action;
import junit.framework.TestCase;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class SystemSubPathLayerProviderTest extends TestCase {
    
    public SystemSubPathLayerProviderTest(String testName) {
        super(testName);
    }
    
    public void testProvider() throws Exception {
        FileObject r = new SystemSubPathLayerProvider("sub1").layer().getRoot();
        final FileObject ref = r.getFileObject("stuff/A.shadow");
        assertNotNull(ref);
        final DataObject d = DataObject.find(ref);
        InstanceCookie ic = d.getLookup().lookup(InstanceCookie.class);
        assertNotNull(d.toString(), ic); // requires patch in DataShadow
        final Action a = (Action) ic.instanceCreate();
        EventQueue.invokeAndWait(new Runnable() {
            @Override public void run() {
                a.actionPerformed(null);
            }
        });
        assertEquals(1, cnt);
    }
    
    private static int cnt = 0;
    @ActionRegistration(displayName="-")
    @ActionID(category="x", id="A")
    @ActionReference(path="sub1/stuff")
    public static class A implements ActionListener {
        public @Override void actionPerformed(ActionEvent e) {
            cnt++;
        }
    }
    
}
