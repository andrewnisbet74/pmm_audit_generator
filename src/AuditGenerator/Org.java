/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AuditGenerator;

/**
 *
 * @author dcain
 */
public class Org {
    private String name;
    private int id;
    private int parentId;
    private String [] siteIds;
    private User [] users;

    public Org(String name, int id, int parentId, String[] siteIds, User[] users) {
        this.name = name;
        this.id = id;       
        this.siteIds = siteIds;
        this.users = users;
    }
    
    public Org(String name, int id, int parentId, User[] users) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.users = users;
    }
   
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the siteIds
     */
    public String[] getSiteIds() {
        return siteIds;
    }

    /**
     * @param siteIds the siteIds to set
     */
    public void setSiteIds(String[] siteIds) {
        this.siteIds = siteIds;
    }

    /**
     * @return the users
     */
    public User[] getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(User[] users) {
        this.users = users;
    }

    /**
     * @return the parentId
     */
    public int getParentId() {
        return parentId;
    }
    
    
}
