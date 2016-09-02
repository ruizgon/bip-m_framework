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

import entities.EntityEncryption;
import java.util.ArrayList;

/**
 *
 * @author Gonzalo
 */
public abstract class SeekerEncrypt extends Seeker{

    private ArrayList<EntityEncryption> cryptList;
    private EntityEncryption firstCryptLocated;
    private int cryptCount;

    public SeekerEncrypt() {
    }

    /*
    *Constructor para cuando decora a otro Seeker
    */
    public SeekerEncrypt(Seeker s){
        super.setSeeker(s);
    }
    
    public ArrayList<EntityEncryption> getCryptList() {
        return cryptList;
    }

    public void setCryptList(ArrayList<EntityEncryption> cryptList) {
        this.cryptList = cryptList;
    }

    public EntityEncryption getFirstCryptLocated() {
        return firstCryptLocated;
    }

    public void setFirstCryptLocated(EntityEncryption firstCryptLocated) {
        this.firstCryptLocated = firstCryptLocated;
    }

    public int getCryptCount() {
        return cryptCount;
    }

    public void setCryptCount(int cryptCount) {
        this.cryptCount = cryptCount;
    }
    
    /*
    * Template methods
    */

    public ArrayList<EntityEncryption> seekEncryptValuesList(){
        ArrayList<EntityEncryption> cryptList = new ArrayList<EntityEncryption>();
        
        return cryptList;
    }
    
    /*
    * Hook methods
    */
    
    public abstract EntityEncryption getFirstEncryptValue();
    public abstract EntityEncryption getNextEncryptValue(EntityEncryption value);
    public abstract EntityEncryption getPrevEncryptValue(EntityEncryption value);
}
