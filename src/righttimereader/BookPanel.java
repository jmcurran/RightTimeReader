/*
 * Copyright (C) 2018  James Curran <james.m.curran@gmail.com>
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
class BookPanel extends JPanel{
    private Book theBook;

    private void nextPageActionPerformed(ActionEvent evt) {
        theBook.nextPage();
        capPanel.setSentence(new Sentence(theBook.getCurrentPageCaption()));
        this.revalidate();
    }

    private void previousPageActionPerformed(ActionEvent evt) {
        theBook.prevPage();
        capPanel.setSentence(new Sentence(theBook.getCurrentPageCaption()));
        this.revalidate();
    }
    
    private class PicturePanel extends JPanel {

        public PicturePanel() {
            Dimension d = new Dimension(1080, 420);
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
        private Sentence sentence;
        
        public CaptionPanel() {
            Dimension d = new Dimension(1200, 120);
            this.setPreferredSize(d);
            this.setBorder(BorderFactory.createRaisedBevelBorder());
            sentence = null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
            
            if(sentence != null){
                sentence.drawSentence(g, this.getHeight(), this.getWidth());
            }
        }
        
        public void setSentence(Sentence sentence){
            this.sentence = sentence;
//            this.removeAll();
//            
//            for(String word : sentence.getWords()){
//                WordButton wb = new WordButton(word);
//                wb.addMouseListener(new MouseAdapter(){
//                    public void mouseEntered(MouseEvent e){
//                        wb.setBigFont();
//                    }
//                    
//                    public void mouseExited(MouseEvent e){
//                        wb.setSmallFont();
//                    }
//                });
//                this.add(wb);
//            }
            this.revalidate();
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
        nextButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            nextPageActionPerformed(evt);
        });
        nextButton.setPreferredSize(new Dimension(60, 480));
        //nextButton.setEnabled(false);
        
        prevButton = new JButton("<");
        prevButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            previousPageActionPerformed(evt);
        });
        prevButton.setPreferredSize(new Dimension(60, 480));
        //prevButton.setEnabled(false);
        
        picPanel = new PicturePanel();
        capPanel = new CaptionPanel();
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weightx = 0.05;
        c.weighty = 0.80;
        this.add(prevButton, c);
        
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 36;
        c.gridheight = 1;
        c.weightx = 0.90;
        c.weighty = 0.80;
        this.add(picPanel, c);
        
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 38;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weightx = 0.05;
        this.add(nextButton, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 40;
        c.weightx = 1.0;
        c.weighty = 0.2;
        this.add(capPanel, c);
        
        this.addPropertyChangeListener("isLoaded", new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if((Boolean)evt.getNewValue() == true){
                    nextButton.setEnabled(true);
                    prevButton.setEnabled(true);
                }
            }
            
        });
        
    }
       
    
    public void setBook(Book newBook){
        theBook = newBook;
        capPanel.setSentence(new Sentence(theBook.getCurrentPageCaption()));
        this.revalidate();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
}
