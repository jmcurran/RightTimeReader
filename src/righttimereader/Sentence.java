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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author James
 */
public class Sentence {
    private String sentence;
    private ArrayList<String> words;
    private int numWords;
    private int currentWord;

    public Sentence(String sentence) {
        this.sentence = sentence;
        
        words = new ArrayList<>(Arrays.asList(sentence.split(":")));
        numWords = words.size();
        currentWord = -1;
    }
    
    public String getCurrentWord(){
        return words.get(currentWord);
    }
    
    public int getNumberOfWords(){
        return numWords;
    }
    
    
    public void setCurrentWord(){
        
    }
    
    public ArrayList<String> getWords(){
        return words;
    }

    public void drawSentence(Graphics g, int canvasHeight, int canvasWidth){
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font bigFont = new Font("Helvetica", Font.PLAIN, 28);
        Font smallFont = new Font("Helvetica", Font.PLAIN, 20);
        
        // First work out how big the whole sentence is so that the drawing starts
        // in the middle of the panel
        
        FontMetrics fmBig = g2d.getFontMetrics(bigFont);
        FontMetrics fmSmall = g2d.getFontMetrics(smallFont);
        Rectangle2D r = fmBig.getStringBounds(sentence, g);
        
        int y = (int)((canvasHeight + r.getHeight()) * 0.5);
        int x = (int)((canvasWidth - r.getWidth()) * 0.5);
        
        int i = 0;
        for(String word : words){
            if(currentWord == i){
                g2d.setFont(bigFont);
                g2d.drawString(word, x, y);             
            }else{
                g2d.setFont(smallFont);
                int xOffset = (fmBig.stringWidth(word) - fmSmall.stringWidth(word)) / 2;
                g2d.drawString(word, x + xOffset, y);
            }
            i++;
            x += fmBig.stringWidth(word + " ");
        }
        
        g2d.dispose();
        
    }
    
    public void nextWord(){
        currentWord = currentWord < (numWords - 1) ? currentWord + 1 : 0;
    }

    @Override
    public String toString() {
        return this.sentence;
    }
    
    
}
