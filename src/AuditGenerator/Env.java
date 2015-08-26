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
public class Env {
    String name;
    String db_url;
    String db_user;
    String db_pw;    
    Org[] orgs;    
    DataSet [] dataSets;
    
    public Env(String name, String db_url, String db_user, String db_pw, Org[] orgs){
        this.name = name;
        this.db_url = db_url;
        this.db_pw = db_pw;
        this.db_user = db_user;
        this.orgs = orgs;       
    }
    
    public Env(String name, String db_url, String db_user, String db_pw){
        this.name = name;
        this.db_url = db_url;
        this.db_pw = db_pw;
        this.db_user = db_user;        
    }
    
       
    public Env(String name, String db_url, String db_user, String db_pw, Org[] orgs, DataSet [] dataSets){
        this.name = name;
        this.db_url = db_url;
        this.db_pw = db_pw;
        this.db_user = db_user;
        this.orgs = orgs;
        this.dataSets = dataSets;
    }
    
    public void setOrgs(Org[] orgs){
        this.orgs = orgs;
    }
}
