package appstringsgenerator.stringseditor;

import appstringsgenerator.models.StringModel;

/**
 *
 * @author Giovanni
 */
public interface StringsEditorContract {
    public interface View {
        public String onShowInputDialog(String message);
        public void onStringAdded(StringModel newStringModel);
        public void onDetailAdded(String pathFile, boolean newRow);
        public String onSelectFileWithFileChooser();
    }
    
    public interface Presenter {
        public void onMenuNewClicked();
        public void onMenuOpenClicked();
        public void onMenuSaveClicked();
        public void onMenuExportToAndroidClicked();
        public void onMenuExportToSwiftClicked();
        public void onMenuExitClicked();
        public void onNewStringClicked();
        public void onNewDetailClicked();
        public void onStringClicked(StringModel stringModel);
        public void onOpenFileClicked(String pathFile);
    }
}
