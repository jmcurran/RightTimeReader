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
import java.awt.Point;
import java.awt.Polygon;
import javax.swing.JButton;

/**
 *
 * @author James Curran
 */
public class JArrowButton extends JButton {
    /**
     *
     */
    public enum Orientation {
        LEFT, WEST, 
        UP, NORTH, 
        RIGHT, EAST,
        DOWN, SOUTH
    }

    private Orientation orient;
    private Polygon shape;
    private int width, height;

    public JArrowButton(Orientation orient) {
        this.orient = orient;
        this.height = 64;
        this.width = 64;
        Dimension d = new Dimension(this.width, this.height);
        this.setSize(d);
        this.shape = new Polygon();
    }

    public JArrowButton(Orientation orient, int width, int height) {
        this.orient = orient;
        this.width = width;
        this.height = height;
        Dimension d = new Dimension(this.width, this.height);
        this.setSize(d);
        this.shape = new Polygon();
    }
    
    private void initShape(){
       Point p1, p2, p3;
       
       switch(this.orient){
           case LEFT:
           case WEST:
               p1 = new Point(0, this.height / 2);
               p2 = new Point(this.width, this.height);
               p3 = new Point(this.width, 0);
               break;
           case RIGHT:
           case EAST:
               p1 = new Point(this.width, this.height / 2);
               p2 = new Point(0, this.height);
               p3 = new Point(0, 0);
               break;
           case UP:
           case NORTH:
               p1 = new Point(this.width / 2, this.height);
               p2 = new Point(this.width, 0);
               p3 = new Point(0, 0);
               break;
           case DOWN:
           case SOUTH:
               p1 = new Point(this.width / 2, 0);
               p2 = new Point(this.width, this.height);
               p3 = new Point(0, this.height);
               break;
           default:
               p1 = p2 = p3 = new Point(0, 0);
       }
       
       this.shape.addPoint((int)p1.getX(), (int)p1.getY());
       this.shape.addPoint((int)p2.getX(), (int)p2.getY());
       this.shape.addPoint((int)p3.getX(), (int)p3.getY());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.fillPolygon(shape);
    }
    
    
    
    
    
}
