package api;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MultiFileSystem;

/**
 * Layer provider based on a subdirectory of the system filesystem.
 */
public final class SystemSubPathLayerProvider implements LayerProvider {
    
    private final String path;

    public SystemSubPathLayerProvider(String path) {
        this.path = path;
    }
    
    public @Override FileSystem layer() throws Exception {
        return new MultiFileSystem(new FileSystem[] {FileUtil.getConfigRoot().getFileSystem()}) {
            {
                setPropagateMasks(true);
            }
            public @Override FileObject findResource(String name) {
                return super.findResource(path + '/' + name);
            }
            protected @Override FileObject findResourceOn(FileSystem fs, String res) {
                return super.findResourceOn(fs, path + '/' + res);
            }
            public @Override void addNotify() {}
            public @Override void removeNotify() {}
            @Override public String toString() {
                return "SFS/" + path;
            }
        };
    }
    
}
