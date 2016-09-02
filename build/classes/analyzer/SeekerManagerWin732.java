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
package analyzer;

import analyzer.factories.Win732AnalyzerFactory;

/**
 *
 * @author Gonzalo
 */
public class SeekerManagerWin732 extends SeekerManager {

    private SeekerManagerWin732() {
        super();
    }

    private SeekerManagerWin732(Win732AnalyzerFactory _factory) {
        super(_factory);
    }

    public static SeekerManagerWin732 getInstance() {
        if (_instance == null) {
            _instance = new SeekerManagerWin732(new Win732AnalyzerFactory());
        }

        return (SeekerManagerWin732) _instance;
    }

}
