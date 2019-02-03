/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appstringsgenerator.models;

import javax.swing.ImageIcon;

/**
 *
 * @author Giovanni
 */
public class DetailModel {
    private String pathFile;
    private ImageIcon imageIcon;

    public DetailModel(String pathFile, ImageIcon imageIcon) {
        this.pathFile = pathFile;
        this.imageIcon = imageIcon;
    }

    public String getPathFile() {
        return pathFile;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
   
}
