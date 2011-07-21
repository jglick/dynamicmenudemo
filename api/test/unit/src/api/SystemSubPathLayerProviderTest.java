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
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.MultiFileSystem;
import org.openide.loaders.DataObject;

public class SystemSubPathLayerProviderTest extends TestCase {
    
    public SystemSubPathLayerProviderTest(String testName) {
        super(testName);
    }
    
    public void testProvider() throws Exception {
        FileSystem fs = new SystemSubPathLayerProvider("sub1").layer();
        FileObject r = fs.getRoot();
        final FileObject ref = r.getFileObject("stuff/A.shadow");
        assertNotNull(ref);
        assertEquals(Collections.singletonList("originalFile"), Collections.list(ref.getAttributes()));
        FileSystem mfs = new MultiFileSystem(new FileSystem[] {fs});
        FileObject ref2 = mfs.findResource("stuff/A.shadow");
        assertNotNull(ref2);
        assertEquals(Collections.singletonList("originalFile"), Collections.list(ref2.getAttributes()));
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
