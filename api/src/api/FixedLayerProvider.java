package api;

import java.net.URL;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.XMLFileSystem;
import org.xml.sax.SAXException;

/**
 * Layer provider based on an XML fragment.
 */
public final class FixedLayerProvider implements LayerProvider {
    
    private final FileSystem fs;

    public FixedLayerProvider(URL layerXML) throws SAXException {
        fs = new XMLFileSystem(layerXML);
    }

    public @Override FileSystem layer() {
        return fs;
    }
    
}
