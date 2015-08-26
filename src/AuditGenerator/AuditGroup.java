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
public class AuditGroup {
    int orgIndex;
    int parentId;
    int count;
    int startDate;
    Boolean hasIncrementalDates;
    int statusIndex;    
    
    public AuditGroup(){
    }
    
    public AuditGroup(int orgIndex, int parentId, int count, int startDate, Boolean incDates, int statusIndex){
        this.orgIndex = orgIndex;
        this.parentId = parentId;
        this.count = count;
        this.hasIncrementalDates = incDates;
        this.statusIndex = statusIndex;
        this.startDate = startDate;
    }
}
