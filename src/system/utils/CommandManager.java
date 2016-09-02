/* 
 * Copyright (C) 2016 BIP-M Framework.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package system.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gonzalo
 */
public class CommandManager {

    private static CommandManager _instance;
    private List<String> _commandList;

    private CommandManager() {
        _commandList = new ArrayList<String>();
    }

    public CommandManager getInstance() {
        if (_instance == null) {
            _instance = new CommandManager();
        }
        return _instance;
    }

    public void addCommand(String command) {
        this._commandList.add(command);
    }

    public void removeCommand(String command) {
        Iterator i = this._commandList.iterator();
        while (i.hasNext()) {
            if (i.equals(command)) {
                i.remove();
            }
        }
    }
    
}
