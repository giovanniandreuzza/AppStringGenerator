package appstringsgenerator;

import appstringsgenerator.stringseditor.StringsEditorView;
/**
 *
 * @author Giovanni
 */
public class AppStringsGenerator {

    public static void main(String[] args) {
        StringsEditorView editorStrings = new StringsEditorView();
        
        editorStrings.setTitle("Editor di stringhe");
        editorStrings.setSize(700, 700);
        editorStrings.setVisible(true);
    }
    
}
