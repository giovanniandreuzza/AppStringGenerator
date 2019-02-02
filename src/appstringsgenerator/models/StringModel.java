package appstringsgenerator.models;

import java.util.ArrayList;

/**
 *
 * @author Giovanni
 */
public class StringModel {
    private String key;
    private String value;
    private ArrayList detailList;
    
    public StringModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList getDetailList() {
        return detailList;
    }

    public void setDetailList(ArrayList detailList) {
        this.detailList = detailList;
    }

    public boolean equals(StringModel stringModel) {
        return this.key.equals(stringModel.getKey()) && this.value.equals(stringModel.getValue());
    }

    @Override
    public String toString() {
        return "StringModel{" + "key=" + key + ", value=" + value + ", detailList=" + detailList + '}';
    }   
    
}
