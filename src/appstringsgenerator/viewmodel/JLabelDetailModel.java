package appstringsgenerator.viewmodel;

import appstringsgenerator.stringseditor.StringsEditorContract;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Giovanni
 */
public class JLabelDetailModel extends JLabel {
    private String pathFile;
    
    public JLabelDetailModel(String pathFile, StringsEditorContract.Presenter presenter) {
        this.pathFile = pathFile;
        super.setIcon(new ImageIcon(getScaledImage(new ImageIcon(pathFile).getImage(), 100, 100)));
        super.setSize(100, 100);
        super.setOpaque(true);
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Open file
                presenter.onOpenFileClicked(pathFile);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setOpaque(false);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setOpaque(true);
            }
        });
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }
    
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
   
}
