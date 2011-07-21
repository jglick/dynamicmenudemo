package demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionID;

@ActionID(id="demo.Win1Action", category="Demo")
@ActionRegistration(displayName="Action for Window #1", iconInMenu=false)
@ActionReference(path="win1/Menu/First")
public final class Win1Action implements ActionListener {

    public void actionPerformed(ActionEvent e) {
    }
}
