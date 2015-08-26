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
public class DataSet {
    String name;
    AuditGroup [] audit_groups;
    
    public DataSet(){        
    }
    
    public DataSet(String name, AuditGroup [] groups){
        this.name = name;
        this.audit_groups = groups;    
    }
}
