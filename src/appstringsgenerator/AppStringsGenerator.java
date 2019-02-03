package appstringsgenerator;

import appstringsgenerator.stringseditor.StringsEditorView;
import javax.swing.JFrame;
/**
 *
 * @author Giovanni
 */
public class AppStringsGenerator extends JFrame {

    public static void main(String[] args) {
        AppStringsGenerator app = new AppStringsGenerator();
        app.buildUI();
    }
    
    public void buildUI() {
        StringsEditorView editorStrings = new StringsEditorView(this);
        
        setTitle("Editor di stringhe");
        setSize(700, 700);
        setVisible(true);
       
        getContentPane().add(editorStrings);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
