/*
 * Copyright (C) 2018 James M. Curran
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package righttimereader;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
public class PagePanel extends JPanel{
    private BufferedImage img;
    private Sentence sentence;
    
    public PagePanel(){
        img = null;
        sentence = null;
    }
    
    public void setPage(Book.Page page){
        img = page.image;
        sentence = new Sentence(page.caption);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (img != null) {
            Graphics2D g2d = (Graphics2D)g;
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Font font = new Font("Helvetica", Font.PLAIN, 10);
            //Fontmetrics fm = g2d.getFontMetrics(font);
            
            int imageWidth = img.getWidth();
            int imageHeight = img.getHeight();
            
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            
            int newWidth = panelWidth;
            int newHeight = panelHeight - ;
            
            
                
                Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                int x = (panelWidth - newWidth) / 2;
                int y = (panelHeight - newHeight) / 2;
                
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(scaledImage, x, y, this);
                g2d.dispose();
    }
    
    
    
}
