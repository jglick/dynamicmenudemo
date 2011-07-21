package impl;

import api.LayerProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.MultiFileSystem;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=FileSystem.class)
public final class DelegatingFS extends MultiFileSystem implements LookupListener {
    
    private final Lookup.Result<LayerProvider> r = Utilities.actionsGlobalContext().lookupResult(LayerProvider.class);

    @SuppressWarnings("LeakingThisInConstructor")
    public DelegatingFS() {
        setPropagateMasks(true);
        r.addLookupListener(this);
        resultChanged(null);
    }

    public @Override void resultChanged(LookupEvent le) {
        List<FileSystem> fss = new ArrayList<FileSystem>();
        for (LayerProvider p : r.allInstances()) {
            try {
                fss.add(p.layer());
            } catch (Exception x) {
                Logger.getLogger(DelegatingFS.class.getName()).log(Level.INFO, null, x);
            }
        }
        System.err.println("XXX delegating to: " + fss);
        setDelegates(fss.toArray(new FileSystem[fss.size()]));
        System.err.println("XXX contents: " + Collections.list(getRoot().getChildren(true)));
        for (FileSystem fs : fss) {
            System.err.println("  from " + fs + ": " + Collections.list(fs.getRoot().getChildren(true)));
        }
    }
    
}
