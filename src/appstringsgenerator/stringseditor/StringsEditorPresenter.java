package appstringsgenerator.stringseditor;

import appstringsgenerator.models.StringModel;
import java.util.ArrayList;

/**
 *
 * @author Giovanni
 */
public class StringsEditorPresenter implements StringsEditorContract.Presenter {
    private final StringsEditorContract.View view;
    private final ArrayList<StringModel> stringModelList = new ArrayList<>();
    private int actualStringModel = 0;

    public StringsEditorPresenter(StringsEditorContract.View view) {
        this.view = view;
    }

    @Override
    public void onMenuNewClicked() {
        
    }

    @Override
    public void onMenuOpenClicked() {
        
    }

    @Override
    public void onMenuSaveClicked() {
        
    }

    @Override
    public void onMenuExportToAndroidClicked() {
        
    }

    @Override
    public void onMenuExportToSwiftClicked() {
        
    }

    @Override
    public void onMenuExitClicked() {
        System.exit(0);
    }

    @Override
    public void onNewStringClicked() {
        String key = view.onShowInputDialog("Inserisci la chiave della stringa:");
        if(key != null && !key.isEmpty()) {
            String value = view.onShowInputDialog("Inserisci il valore della stringa \"" + key + "\":");
            if(value != null && !value.isEmpty()){
                updateStringsData(key, value);
            }
        }
    }

    @Override
    public void onNewDetailClicked() {
        String pathFile = view.onSelectFileWithFileChooser();
        if(pathFile != null){
            updateDetailsData(pathFile);
        }
    }

    @Override
    public void onStringClicked(StringModel stringModel) {
        boolean done = false;
        System.out.println(stringModel.getKey() + " = " + stringModel.getValue());
        
        for(int i = 0; i < stringModelList.size() && !done; i++){
            if(stringModelList.get(i).equals(stringModel)){
                actualStringModel = i;
                done = true;
            }
        }
    }

    @Override
    public void onOpenFileClicked(String pathFile) {
        
    }
    
    private void updateStringsData(String key, String value) {
        boolean isPresent = false;
        StringModel newStringModel = new StringModel(key, value);
        
        for(int i = 0; i < stringModelList.size() && !isPresent; i++){
            if(stringModelList.get(i).equals(newStringModel)){
                isPresent = true;
            }
        }
        
        if(isPresent){
            //Already exist, ask to add a new detail
        }else{
            stringModelList.add(newStringModel);
            view.onStringAdded(newStringModel);
        }
    }
    
    private void updateDetailsData(String pathFile) {
        stringModelList.get(actualStringModel).addDetail(pathFile);
        view.onDetailAdded(pathFile, (stringModelList.get(actualStringModel).getDetailList().size() % 3 == 0));   
    }
}
