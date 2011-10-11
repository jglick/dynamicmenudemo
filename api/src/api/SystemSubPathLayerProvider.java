package api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import org.openide.filesystems.AbstractFileSystem;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.util.Enumerations;

/**
 * Layer provider based on a subdirectory of the system filesystem.
 */
public final class SystemSubPathLayerProvider implements LayerProvider {
    
    private final String path;

    public SystemSubPathLayerProvider(String path) {
        this.path = path;
    }
    
    public @Override FileSystem layer() throws Exception {
        return new Layer();
    }

    private class Layer extends AbstractFileSystem implements AbstractFileSystem.Info, AbstractFileSystem.List, AbstractFileSystem.Attr {

        Layer() {
            info = this;
            list = this;
            attr = this;
        }

        @Override public String getDisplayName() {
            return "SFS/" + path;
        }

        @Override public boolean isReadOnly() {
            return true;
        }

        private FileObject delegate(String name) {
            if (name.equals(path) || name.startsWith(path + '/')) {
                return null;
            }
            return FileUtil.getConfigFile(name.isEmpty() ? path : path + '/' + name);
        }

        @Override public Date lastModified(String name) {
            FileObject d = delegate(name);
            return d != null ? d.lastModified() : new Date();
        }

        @Override public boolean folder(String name) {
            FileObject d = delegate(name);
            return d != null && d.isFolder();
        }

        @Override public boolean readOnly(String name) {
            return true;
        }

        @Override public String mimeType(String name) {
            FileObject d = delegate(name);
            return d != null ? d.getMIMEType() : null;
        }

        @Override public long size(String name) {
            FileObject d = delegate(name);
            return d != null ? d.getSize() : 0;
        }

        @Override public InputStream inputStream(String name) throws FileNotFoundException {
            FileObject d = delegate(name);
            if (d == null) {
                throw new FileNotFoundException();
            } else {
                return d.getInputStream();
            }
        }

        @Override public String[] children(String name) {
            FileObject d = delegate(name);
            if (d == null) {
                return new String[0];
            }
            FileObject[] kids = d.getChildren();
            String[] names = new String[kids.length];
            for (int i = 0; i < kids.length; i++) {
                names[i] = kids[i].getNameExt().replace(".hidden", "_hidden");
            }
            return names;
        }

        @Override public Object readAttribute(String name, String attrName) {
            if (name.isEmpty()) {
                return null; // otherwise a stack overflow
            }
            FileObject d = delegate(name);
            return d != null ? d.getAttribute(attrName) : null;
            }

        @Override public Enumeration<String> attributes(String name) {
            if (name.isEmpty()) {
                // MultiFileObject.getAttributes is weird, causes stack overflows
                return Enumerations.empty();
            }
            FileObject d = delegate(name);
            if (d == null) {
                return Enumerations.empty();
            }
            return d.getAttributes();
        }

        @Override public OutputStream outputStream(String name) throws IOException {
            throw new IOException();
        }

        @Override public void lock(String name) throws IOException {
            throw new IOException();
        }

        @Override public void unlock(String name) {}

        @Override public void markUnimportant(String name) {}

        @Override public void writeAttribute(String name, String attrName, Object value) throws IOException {
            throw new IOException();
        }

        @Override public void renameAttributes(String oldName, String newName) {}

        @Override public void deleteAttributes(String name) {}

    }
    
}
