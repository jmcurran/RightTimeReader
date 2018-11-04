/*
 * Copyright (C) 2018 James
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
class BookPanel extends JPanel{
    private Book theBook;
    
    private class PicturePanel extends JPanel {

        public PicturePanel() {
            Dimension d = new Dimension(1080, 480);
            this.setPreferredSize(d);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(theBook != null){
                BufferedImage img = theBook.getCurrentPagePicture();
                Graphics2D g2d = (Graphics2D) g.create();
                int x = (getWidth() - img.getWidth()) / 2;
                int y = (getHeight() - img.getHeight()) / 2;
                g2d.drawImage(img, x, y, this);
                g2d.dispose();
            }
        }
        
    }
    
    private class CaptionPanel extends JPanel {

        public CaptionPanel() {
            Dimension d = new Dimension(1200, 60);
            this.setPreferredSize(d);
        }
        
    }
    
    private final JButton nextButton;
    private final JButton prevButton;
    private final PicturePanel picPanel;
    private final CaptionPanel capPanel;

    public BookPanel() {
        Dimension d = new Dimension(1200,540);
        this.setPreferredSize(d);
        theBook = null;
        
        nextButton = new JButton(">");
        prevButton = new JButton("<");
        nextButton.setPreferredSize(new Dimension(60, 480));
        prevButton.setPreferredSize(new Dimension(60, 480));
        
        picPanel = new PicturePanel();
        capPanel = new CaptionPanel();
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.05;
        c.weighty = 0.9;
        this.add(prevButton, c);
        
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.9;
        c.weighty = 0.9;
        this.add(picPanel, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.05;
        c.weighty = 0.9;
        this.add(nextButton, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.gridwidth = 3;
        this.add(capPanel, c);
        
    }
    
    public void setBook(Book newBook){
        theBook = newBook;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
}
