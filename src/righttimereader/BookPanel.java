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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
class BookPanel extends JPanel{
    private Book theBook;
    
    private void nextPage(){
        theBook.nextPage();
        capPanel.setSentence(new Sentence(theBook.getCurrentPageCaption()));
        this.revalidate();
        this.repaint();
    }

    private void nextPageActionPerformed(ActionEvent evt) {
        nextPage();
    }

    private void previousPage(){
        theBook.prevPage();
        capPanel.setSentence(new Sentence(theBook.getCurrentPageCaption()));
        this.revalidate();
        this.repaint();
    }
    
    private void previousPageActionPerformed(ActionEvent evt) {
        previousPage();
    }
    
    private class PicturePanel extends JPanel {

        public PicturePanel() {
           
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

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(720, 400);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1080, 480);
        }
        
        
        
    }
    
    private class CaptionPanel extends JPanel {
        private Sentence sentence;
        
        public CaptionPanel() {
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

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(800, 80);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1200, 60);
        }
        
        public void nextWord(){
            if(this.sentence != null){
                sentence.nextWord();
                this.revalidate();
                this.repaint();
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
            this.repaint();
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
          
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.05;
        c.weighty = 8.0 / 9.0; //480 / 540
        this.add(prevButton, c);
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.90;
        c.weighty = 8.0 / 9.0; //480 / 540
        this.add(picPanel, c);
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.05;
        c.weighty = 8.0 / 9.0; //480 / 540
        this.add(nextButton, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.weighty = 1.0 / 9.0; // 60 / 540 
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
        
        this.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent ke) {
            } 

            @Override
            public void keyPressed(KeyEvent ke) {
                int keyCode = ke.getKeyCode();

                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_UP:
                        previousPage();
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_DOWN:
                        nextPage();
                        break;
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_ENTER:
                        capPanel.nextWord();
                        break;
                    default:
                        break;
                  }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        
        this.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent me) {
                capPanel.nextWord();
            }

            @Override
            public void mousePressed(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
         });
        
        this.setFocusable(true);
        
    }
       
    
    public void setBook(Book newBook){
        theBook = newBook;
        capPanel.setSentence(new Sentence(theBook.getCurrentPageCaption()));
        this.revalidate();
        this.repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
}
