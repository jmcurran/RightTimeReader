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

import java.util.ArrayDeque;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author James Curran <james.m.curran@gmail.com>
 */
public class ChangeLog {
    private ArrayDeque<editAction> eventLog;

    public ChangeLog() {
        this.eventLog = new ArrayDeque<>();
    }
    
    public void addEvent(editAction ae){
        eventLog.addLast(ae);
    }
    
    public void removeEvent(){
        if(!eventLog.isEmpty()){
            eventLog.removeLast();
        }
    }
    
    public int size(){
        return eventLog.size();
    }
}
