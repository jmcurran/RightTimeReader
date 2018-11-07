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
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
class ToolbarPanel extends JPanel{

    public ToolbarPanel() {
        this.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 40);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(800, 40);
    }
    
    
    
    
    
    
}
