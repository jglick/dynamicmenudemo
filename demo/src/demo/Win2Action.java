package demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionID;

@ActionID(id="demo.Win2Action", category="Demo")
@ActionRegistration(displayName="Action for Window #2", iconInMenu=false)
@ActionReference(path="win2/Menu/Second")
public final class Win2Action implements ActionListener {

    public void actionPerformed(ActionEvent e) {
    }
}
