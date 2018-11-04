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
import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author James
 */
public class WordButton extends JButton {
    private Font bigFont;
    private Font smallFont;

    public WordButton(String string) {
        super(string);
        
        bigFont = new Font("Verdana", Font.PLAIN, 28);
        smallFont = new Font("Verdana", Font.PLAIN, 20);
        this.setFont(bigFont);
        this.setText(string);
        System.out.println(this.getWidth() + " " + this.getHeight());
        //this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setFont(smallFont);
    }
    
    public void setBigFont(){
        this.setFont(bigFont);
    }
    
    public void setSmallFont(){
        this.setFont(smallFont);
    }
    
    
}
