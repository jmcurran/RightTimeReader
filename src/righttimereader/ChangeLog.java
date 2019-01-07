/*
 * Copyright (C) 2018 James Curran <james.m.curran@gmail.com>
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

import java.beans.PropertyChangeListener;
import java.util.ArrayDeque;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author James Curran <james.m.curran@gmail.com>
 */
public class ChangeLog {
    SwingPropertyChangeSupport changeSupport = new SwingPropertyChangeSupport(this);
        
    private ArrayDeque<EditAction> eventLog;
    private int size;

    public ChangeLog() {
        this.eventLog = new ArrayDeque<>();
        size = 0;
    }
    
    public void addEvent(EditAction ae){
        eventLog.addLast(ae);
        setSize(size + 1);
    }
    
    public void removeEvent(){
        if(!eventLog.isEmpty()){
            eventLog.removeLast();
            setSize(size - 1);
        }
    }
    
    public void setSize(int size){
        changeSupport.firePropertyChange("size", this.size, size);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
    
}
