package org.nakedobjects.distribution.example;

import org.nakedobjects.distribution.ActionType;
import org.nakedobjects.distribution.client.reflect.NotExpectedException;
import org.nakedobjects.object.reflect.ActionSpecification;
import org.nakedobjects.object.reflect.ActionSpecification.Type;


public class SimpleActionType implements ActionType {

    private final String type;

    public SimpleActionType(Type type) {
        this.type = type.toString();
    }

    public Type getType() {
        Type[] types = new Type[] { ActionSpecification.DEBUG, ActionSpecification.EXPLORATION, ActionSpecification.USER };
        for (int i = 0; i < types.length; i++) {
            if (type.equals(ActionSpecification.DEBUG.toString())) {
                return ActionSpecification.DEBUG;
            }
        }
        throw new NotExpectedException("Invalid type: " + type);
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2004 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */