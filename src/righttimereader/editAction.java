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

/**
 *
 * @author James Curran <james.m.curran@gmail.com>
 */
public class editAction {
    public enum editType {
        ADD,
        IMAGE,
        ORDER,
        REMOVE,
        SENTENCE}
    
    private final editType type;
    private final int pageNumber;
    private final Object from;
    private final Object to;
    private final Object pageData;

    public editAction(editType type, int pageNumber, Object from, Object to) {
        this.type = type;
        this.pageNumber = pageNumber;
        this.from = from;
        this.to = to;
        this.pageData = null;
    }    
    
    public editAction(editType type, int pageNumber, Object pageData) {
        this.type = type;
        this.pageNumber = pageNumber;
        this.pageData = pageData;
        this.from = null;
        this.to = null;
    }    
    
    public editAction(editType type, int pageNumber) {
        this.type = type;
        this.pageNumber = pageNumber;
        this.pageData = null;
        this.from = null;
        this.to = null;
    }    
}
    
