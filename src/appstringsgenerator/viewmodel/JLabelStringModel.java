package appstringsgenerator.viewmodel;

import appstringsgenerator.models.StringModel;
import java.awt.Font;
import javax.swing.JLabel;

/**
 *
 * @author Giovanni
 */
public class JLabelStringModel extends JLabel {
    private final StringModel stringModel;
    
    public JLabelStringModel(StringModel stringModel){
        super.setText(stringModel.getKey() + " -> " + stringModel.getValue());
        super.setFont(new Font(super.getFont().getName(), Font.PLAIN, 16));
        this.stringModel = stringModel;
    }
    
    public void setText(String key, String value){
        super.setText(key + " -> " + value);
        stringModel.setKey(key);
        stringModel.setValue(value);
    }
    
    public StringModel getStringModel(){
        return stringModel;
    }
    
}
