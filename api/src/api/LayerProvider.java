package api;

import org.openide.filesystems.FileSystem;
import org.openide.util.Utilities;

/**
 * Layer fragment active when in {@link Utilities#actionsGlobalContext}.
 */
public interface LayerProvider {
    FileSystem layer() throws Exception;
}