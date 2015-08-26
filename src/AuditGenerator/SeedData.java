/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuditGenerator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author dcain
 */
public class SeedData {
    static final String dbHost = "jdbc:mysql://localhost/";
    static final String dbName = "content_compliance";
    static final String dbURL = dbHost + dbName;
    static final String dbUser = "devuser";
    static final String dbPW = "d3vpass";
    static final int contentId = 4;
    static Random randomGenerator = new Random();
    
    public static void main (String[] args) throws SQLException, ClassNotFoundException, InterruptedException{       
        sqlHandler.setConnection(dbURL, dbUser, dbPW);
        truncateEverythingWithinReason();
        generateURLS(500);
        generateMerchants();
        generateProjects();
        generateProjectURLS();
        generateAudits();
        generateMultipleProjects("227");    
        buildScreenshotContentType();
        sqlHandler.closeConnection();
        
    } 

    
    
    private static void truncateEverythingWithinReason(){
        String [] tables = {"audit_client_content_type_severities", "audit_client_merchant_resolutions", "audit_content_types", 
        "audits", "merchant_urls", "merchants", "notes", "project_urls", "projects", "sites", "urls","record_changes","screenshot_content_types"};
        
    
        ArrayList<String> tablesToTruncate = new ArrayList<String>(Arrays.asList(tables));
        Iterator it = tablesToTruncate.iterator();
        while (it.hasNext()){
            String thisTable = it.next().toString();
            String  sql = "TRUNCATE " + dbName + "." + thisTable;
            sqlHandler.executeStatement(sql);
        }
    }
    
    
    private static void generateURLS(int numToGen) {
        for (int i = 1; i <= numToGen; i ++){            
            String rootDomain = "fakeURL_" + i + ".com";
            String protocol = "http";
            String tld = "com";
            String path = "/fake";
            String thisURL = protocol + "://" + rootDomain + path;
            String urlSql = "INSERT INTO " + dbName + ".urls (id, url, root_domain, protocol, host, path, effective_tld, port, created_at, updated_at) VALUES (" 
                    + i + ",'" + thisURL + "', '" + rootDomain + "', '" + protocol + "', '" + rootDomain + "','" + path + "', '" + tld + 
                    "', 80, '1971-05-12 11:54:52','1971-05-12 11:54:52')";            
            sqlHandler.executeStatement(urlSql);
            String sitesSql = "INSERT INTO " + dbName + ".sites (id, url_id, boundary, created_at, updated_at) VALUES (" + i + "," + i + 
                    ", 'host', '1971-05-12 11:54:52','1971-05-12 11:54:52')";
            sqlHandler.executeStatement(sitesSql);            
        }
    }
    
    
    public static void buildScreenshotContentType()
    {        
      int[] contentTypeIds = {105,107,115,118,120,130};
      for(int i=0; i < contentTypeIds.length;i++)
      {
          generateScreenshotContentTypes(contentTypeIds[i]);
      }    
            
    }        
    
    
    
    /**************************generateScreenshotContentTypes******************************
     * @author:  anisbet<anisbet@g2ll.com> 
     * @param:  contentTypeId(int), screenshotId(int)
     * @return: void
     * @discription: Creates a screenshotContentType record based on the provided contentType parameter 
     */
    private static void generateScreenshotContentTypes(int contentTypeId)
    {
        String[] screenShotsIds = {"3113dd65-7185-4213-86b3-b4d325acc999","7e297483-4e68-49c5-bedc-9733047e52d8","467bccce-7d90-492e-9e52-dfdfb0731cd6","c7d8519a-6798-4a17-9dc0-1efc235e6b38"};
        
        for (int i = 0; i< screenShotsIds.length; i++)
        {
          String queryString = "INSERT INTO " + dbName + ".screenshot_content_types(screenshot_id,content_type_id,created_at,updated_at)" +
                  "values('"+ screenShotsIds[i] + "',"+ contentTypeId+ ",'2015-07-19 14:36:53','2015-07-19 14:36:53')";  
           sqlHandler.executeStatement(queryString);      
           System.out.println(queryString);
        } 
    }
    
    
    /**************************generateAuditChange******************************
     * @author:  anisbet<anisbet@g2ll.com> 
     * @param:  auditId(int)
     * @return: void
     * @discription: Creates an audit change record for the specified audit. 
     *               At present this is a pretty static exercise. Should 
     *               be randomized to a greater degree in subsequent iterations. 
     */
    private static void generateAuditChange(int auditId)
    {
        int authorId = 56;
        int organizationId = 229; 
        String model_attribute = "client_merchant_resolution";
        String actionType = "add";
        String description = "---\n" +":value: Cleared";
        String changeSQL = "INSERT INTO " + dbName +".record_changes (user_id,model_name,record_id,model_attribute,action_type,additional_data" +
        ", created_at,updated_at,organization_id) VALUES(" + authorId + ","+ "'audit'," + auditId + ",'" + model_attribute + "','" + actionType +
        "','"+ description + "','2015-07-19 14:36:53','2015-07-19 14:36:53',"+ organizationId +")";      
        sqlHandler.executeStatement(changeSQL);        
        System.out.println(changeSQL);
        
    }        
    
    /**************************generateNotes************************************
     * @author: anisbet<anisbet@g2llc.com> 
     * @param: auditId(int), noteCount(int)
     * @return: void
     * @description: Creates a note for the specified audit.  The noteCount 
     *               value is included in the text of the note. 
     */
    private static void generateNotes(int auditId, int noteCount)
    {
        int authorId = 56; // Test Analyst 
        int organizationId = 229; // test org
        String  body = "This is a test Note provided by a test user. " + noteCount ;
        String noteSQL = "INSERT INTO " + dbName + ".notes (audit_id,author_user_id,body,created_at,updated_at,author_organization_id,last_updated_by_user_id)"
                + "VALUES(" + auditId + ","+ authorId + ",'" + body + "', '2015-07-19 14:36:53','2015-07-19 14:36:53',"+ organizationId + "," + authorId + ")" ;
        sqlHandler.executeStatement(noteSQL);
    }

    private static void generateMerchants() {
        String orgId = "229";
        //Org VCM 1
        //generate merchant data "Merchant_1" for url_id 1-200
        for (int j = 1; j < 201; j++){  
            String merSql = buildMerchantSqlString(1,orgId, j);
            sqlHandler.executeStatement(merSql);
            String merUrlSql = buildMerchantUrlSqlString("1",""+j);
            sqlHandler.executeStatement(merUrlSql);
            System.out.println(merSql);            
        }
        //generate merchant data Merchant_2 - 10 url_id 1
        for (int i = 2; i < 100; i ++){
           int baseId = 200;      
           int uid = baseId + i;
            String merSql1 = buildMerchantSqlString(i,orgId, uid);
            sqlHandler.executeStatement(merSql1);
            String merUrlSq2 = buildMerchantUrlSqlString(baseId + i - 1 + "",String.valueOf(uid));
            sqlHandler.executeStatement(merUrlSq2);             
        }
        
        orgId = "227";
        //Org Acq 1
        //generate merchant data "Merchant_100" for url_id 201
        String merSqlA = buildMerchantSqlString(100,orgId, 201);
        sqlHandler.executeStatement(merSqlA);
        String merUrlSq3 = buildMerchantUrlSqlString("100","201");
        sqlHandler.executeStatement(merUrlSq3);        
        
        //Org solo_org
        //generate merchant data "Merchant_200" for url_id 203
        orgId = "230";
        String merSqlS = buildMerchantSqlString(200,orgId,203);
        sqlHandler.executeStatement(merSqlS);
        String merUrlSq4 = buildMerchantUrlSqlString("200","203");
        sqlHandler.executeStatement(merUrlSq4);
        System.out.println(merSqlS);
    }
    
    private static String buildMerchantSqlString(int mNum, String orgId, int url_id){
        String sqlString = "INSERT INTO " + dbName + ".merchants (organization_id,id_1,mcc,name,created_at,updated_at,url_id) VALUES (" 
                    + orgId + ",'MID_" + mNum + "'," + mNum + ",'Merchant_" + mNum + "','1971-05-12 11:54:52','1971-05-12 11:54:52'," + url_id+ ")";        
        return sqlString;
    }

    private static String buildMerchantUrlSqlString(String mId, String uId) {
        String sqlString = "INSERT INTO " + dbName + ".merchant_urls (merchant_id, url_id, created_at, updated_at) VALUES (" + mId + "," + uId + ",'1971-05-12 11:54:52','1971-05-12 11:54:52')";
        return sqlString;
    }

    private static void generateProjects() {
        int baseId = 200;
        
        //create 1 project for each vcm
        for(int i = 1; i < 150; i++){
            int orgId = baseId + i;
            String sqlString = buildProjectSqlString(""+orgId,""+i);
            sqlHandler.executeStatement(sqlString);
            System.out.println(sqlString);
            
        } 
    }
    private static void generateMultipleProjects(String orgID){
        //create 50 more projects for vcm1
        for(int i = 1; i < 150; i++){            
            String projID = 54 + i + "";             
            String sqlString = buildProjectSqlString(orgID, "VCM1_" + i);
            sqlHandler.executeStatement(sqlString);           
            String sql = buildProjUrlSqlString(projID, "1");
            sqlHandler.executeStatement(sql);
        }
        String aSqlString = buildProjectSqlString("227", "101");      
        sqlHandler.executeStatement(aSqlString);
        String a2SqlString = buildProjectSqlString("228", "102");       
        sqlHandler.executeStatement(a2SqlString);
        String soloSqlString = buildProjectSqlString("230", "103");       
        sqlHandler.executeStatement(soloSqlString);   
    }
    
    private static String buildProjectSqlString(String orgId, String proNum) {
        String sqlString = "INSERT INTO " + dbName + ".projects (organization_id, name, created_at, updated_at) VALUES (" + orgId + ",'Project_" + proNum + 
                "','1971-05-12 11:54:52','1971-05-12 11:54:52')";
        return sqlString;
    }
    
    private static String buildProjUrlSqlString(String proId, String urlId){
        String sqlString = "INSERT INTO " + dbName + ".project_urls (project_id, url_id, created_at, updated_at) VALUES (" + proId + "," + urlId + 
                ",'1971-05-12 11:54:52','1971-05-12 11:54:52')";
        return sqlString;
    }

    private static void generateProjectURLS() {
        for (int i = 1;i < 305; i++){
            String sql = buildProjUrlSqlString(""+i, ""+i);
            sqlHandler.executeStatement(sql);
            System.out.println(sql);        
        }
       // for (int i = 202; i < 100; i++){
        //    String sql = buildProjUrlSqlString(""+i, ""+i);
         //   sqlHandler.executeStatement(sql);
        //    System.out.println(sql);        
        //}
        
        String sql1 = buildProjUrlSqlString("101", "201");
        sqlHandler.executeStatement(sql1);
        String sql2 = buildProjUrlSqlString("102", "202");
        sqlHandler.executeStatement(sql2);
        String sql3 = buildProjUrlSqlString("103", "203");
        sqlHandler.executeStatement(sql3);
         System.out.println(sql3);  
         
         
    }
   
     private static void createAudit(String date, String childId, String parentId, int statusIndex, int siteId, int referringUrlId) throws SQLException, InterruptedException {                
                String sql;
                String currentSeverityId = "1";
                DateTime currentTime = DateTime.now();              
                int randomSecond = randomGenerator.nextInt(100000);
                currentTime = currentTime.minusSeconds(randomSecond);
                
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                String due = currentTime.toString(fmt);            

                String thisSiteId = ""+siteId;
                if (referringUrlId == -1 )
                {
                    if (statusIndex == 0 || statusIndex == 1){
                      System.out.println("statusIndex " + statusIndex);
                      sql = "INSERT into " + dbName + ".audits (created_at, updated_at, status, site_id, due_at, published_at,audit_type_id) VALUES ('"                  
                      + date + "', '" + date + "', 'published', " + thisSiteId + ",'" + due + "', '" + date + "',1)";
                  } else {
                      sql = "INSERT into " + dbName + ".audits (created_at, updated_at, status, site_id,referral_url_id, due_at,audit_type_id) VALUES ('"                  
                      + date + "', '" + date + "', 'unaudited', " + thisSiteId + ",'" + due + "',1)";
                  }
                }   
                else 
                {
                  if (statusIndex == 0 || statusIndex == 1){
                      System.out.println("statusIndex " + statusIndex);
                  sql = "INSERT into " + dbName + ".audits (created_at, updated_at, status, site_id, referral_url_id, due_at, published_at,audit_type_id) VALUES ('"                  
                      + date + "', '" + date + "', 'published', " + thisSiteId + ","+ referringUrlId  + ",'" + due + "', '" + date + "',1)";
                  } else {
                      sql = "INSERT into " + dbName + ".audits (created_at, updated_at, status, site_id,referral_url_id, due_at,audit_type_id) VALUES ('"                  
                      + date + "', '" + date + "', 'unaudited', " + thisSiteId + "," + referringUrlId  +  ",'" + due + "',1)";
                  }
                }
    
                //Grab audit ID from insert                 
                int auditId = sqlHandler.getSQLResponse(sql);              

                String typePMMSQL = "INSERT INTO " + dbName + ".audit_audit_types (audit_id, audit_type_id, created_at, updated_at) VALUES (" + auditId + 
                        ", 1, '" + date + "','" + date + "')";                
                sqlHandler.executeStatement(typePMMSQL);
                ArrayList addContent = new ArrayList<String>();
                addContent.add(4);
                Iterator it = addContent.iterator();
                while (it.hasNext()){
                    int nextContent = (int)it.next();                                    
                    String sql3 = "INSERT INTO " + dbName + ".audit_content_types (audit_id, content_type_id, created_at, updated_at) VALUES (" + auditId + ", " + nextContent + ", '" + date + "', '" + date + "')";
                    System.out.println(sql3);
                    sqlHandler.executeStatement(sql3);
                    //severities are related to content type
                    
                    String sql4 = "INSERT INTO " + dbName + ".audit_client_content_type_severities (audit_id, severity_id, content_type_id, client_organization_id, created_at, updated_at)"
                            + "VALUES (" + auditId + ", " + currentSeverityId + "," + nextContent + ", " + childId + ", '" + date + "', '" + date + "')";
                    System.out.println(sql4);
                    sqlHandler.executeStatement(sql4);
                    if (parentId != null && statusIndex == 1){
                        String parentContentSQL = "INSERT INTO " + dbName + ".audit_client_content_type_severities (audit_id, severity_id, content_type_id, client_organization_id, created_at, updated_at)"
                                + "VALUES (" + auditId + ", " + currentSeverityId + "," + nextContent + ", " + parentId + ", '" + date + "', '" + date + "')";
                        sqlHandler.executeStatement(parentContentSQL);
                    }
                }
                if(statusIndex == 1){
                        String thisResolution = "cleared";
                        String sql5;
                        if ("Under Review".equals(thisResolution)){
                            sql5 = "INSERT INTO " + dbName + ".audit_client_merchant_resolutions "
                            + "(audit_id, severity_id, status, author_organization_id, created_at, updated_at)"
                            + "VALUES (" + auditId + ", " + currentSeverityId + ", '" + thisResolution + "', " + childId + ", '" + date + "', '" + date + "')";
                        } else {
                            sql5 = "INSERT INTO " + dbName + ".audit_client_merchant_resolutions "
                            + "(audit_id, severity_id, status, author_organization_id, recipient_organization_id, published_at, created_at, updated_at)"
                            + "VALUES (" + auditId + ", " + currentSeverityId + ", '" + thisResolution + "', " + childId + ", " + parentId + ", '" + date + "', '" + date + "', '" + date + "')";
                        }
                        System.out.println("merchant res sql " + sql5);
                        sqlHandler.executeStatement(sql5);
                    }
               // create one or more notes  
               Random r = new Random();
               int noteCount = r.nextInt(6 - 1) + 1;
               for (int i = 0; i  < noteCount; i++)
               {    
                 generateNotes(auditId,i);    
               }  
               generateAuditChange(auditId);
            }

    private static void generateAudits() throws SQLException, InterruptedException {
        //generate 10 audits for Acq1
        DateTime thisDateTime = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String sqlDate = thisDateTime.toString(fmt);       
        
        String orgId = "227";
        String parentId = "229";
        Integer siteId = 201;
        for (int i = 1; i < 50; i ++){
            siteId++;
            createAudit(sqlDate, orgId, parentId, 1, siteId, i+300);
        }
        String a2Id = "228";
        //generate 10 audits for acq 2
        for (int i = 1; i < 50; i ++){
            siteId++;
            createAudit(sqlDate, a2Id, parentId, 1, siteId,-1);
        }
        //generate 10 audits for solo org
        String soloId = "230";
        for (int i = 1; i < 50; i ++){
            siteId++;
            createAudit(sqlDate, soloId, null, 1, siteId,-1);
        }
        //generate 19 audits for vcm 1
        String vcmId = "231";
        for (int i = 1; i < 60; i ++){ 
            createMultiAudit(sqlDate, vcmId, "227", "228",1, i);  
            
        }
        //generate 10 non-published audits for vcm1
        String vcmId1 = "231";
        for (int i = 1; i < 11; i ++){
            createAudit(sqlDate, vcmId1, "0", 0, i,-1);
        }
        //generate violations for audit 31 (site 1) for vcms 2-51
        int baseVCM = 231;        
        
        for (int i = 0; i < 50; i ++){
            int vcm = baseVCM + i;
            generateContent(vcm+"");
        }
    }
    private static void generateContent(String orgId){
        String sql = "INSERT INTO audit_client_content_type_severities (audit_id, severity_id, content_type_id, client_organization_id, created_at, updated_at) VALUES (31,1," + contentId + ","+orgId+",'2015-03-19 11:48:40','2015-03-19 11:48:40')";
        sqlHandler.executeStatement(sql);
        String resSql = "INSERT INTO audit_client_merchant_resolutions (audit_id, severity_id, status, author_organization_id, recipient_organization_id, published_at, created_at, updated_at) "
                + "VALUES (31, 1, 'cleared', " + orgId + ",5001, '2015-03-19 14:36:53', '2015-03-19 14:36:53', '2015-03-19 14:36:53')";
        System.out.println(resSql);
        sqlHandler.executeStatement(resSql);
    }

    private static void createMultiAudit(String date, String childId, String parentId1, String parentId2,int statusIndex, int siteId) {
        String sql;
                String currentSeverityId = "1";
                DateTime currentTime = DateTime.now();
              
                int randomSecond = randomGenerator.nextInt(100000);
                currentTime = currentTime.minusSeconds(randomSecond);
                
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                String due = currentTime.toString(fmt);
                String thisSiteId = ""+siteId;
                if (statusIndex == 0 || statusIndex == 1){
                    System.out.println("statusIndex " + statusIndex);
                sql = "INSERT into " + dbName + ".audits (created_at, updated_at, status, site_id, due_at, published_at) VALUES ('"                  
                    + date + "', '" + date + "', 'published', " + thisSiteId + ", '" + due + "', '" + date + "')";
                } else {
                    sql = "INSERT into " + dbName + ".audits (created_at, updated_at, status, site_id, due_at) VALUES ('"                  
                    + date + "', '" + date + "', 'unaudited', " + thisSiteId + ", '" + due + "')";
                }
 
                //Grab audit ID from insert                 
                int auditId = sqlHandler.getSQLResponse(sql);
                String typePMMSQL = "INSERT INTO " + dbName + ".audit_audit_types (audit_id, audit_type_id, created_at, updated_at) VALUES (" + auditId + ", 1, '" + date + "','"
                                        + date + "')"; 
               
                sqlHandler.executeStatement(typePMMSQL);
                ArrayList addContent = new ArrayList<String>();
                addContent.add(4);
                Iterator it = addContent.iterator();
                while (it.hasNext()){
                    int nextContent = (int)it.next();                                    
                    String sql3 = "INSERT INTO " + dbName + ".audit_content_types (audit_id, content_type_id, created_at, updated_at) VALUES (" + auditId + ", " + nextContent + ", '" + date + "', '" + date + "')";
                    System.out.println(sql3);
                    sqlHandler.executeStatement(sql3);
                    //severities are related to content type
                    
                    String sql4 = "INSERT INTO " + dbName + ".audit_client_content_type_severities (audit_id, severity_id, content_type_id, client_organization_id, created_at, updated_at)"
                            + "VALUES (" + auditId + ", " + currentSeverityId + "," + nextContent + ", " + childId + ", '" + date + "', '" + date + "')";
                    System.out.println(sql4);
                    sqlHandler.executeStatement(sql4);
                    if (parentId1 != null && statusIndex == 1){
                        String parentContentSQL = "INSERT INTO " + dbName + ".audit_client_content_type_severities (audit_id, severity_id, content_type_id, client_organization_id, created_at, updated_at)"
                                + "VALUES (" + auditId + ", " + currentSeverityId + "," + nextContent + ", " + parentId1 + ", '" + date + "', '" + date + "')";
                        String parentContent2SQL = "INSERT INTO " + dbName + ".audit_client_content_type_severities (audit_id, severity_id, content_type_id, client_organization_id, created_at, updated_at)"
                                + "VALUES (" + auditId + ", " + currentSeverityId + "," + nextContent + ", " + parentId2 + ", '" + date + "', '" + date + "')";
                                               
                        sqlHandler.executeStatement(parentContentSQL);
                        sqlHandler.executeStatement(parentContent2SQL);
                    }
                }
                if(statusIndex == 1){
                        String thisResolution = "cleared";
                        String sql5, sql6;
                        if ("Under Review".equals(thisResolution)){
                            sql5 = "INSERT INTO " + dbName + ".audit_client_merchant_resolutions "
                            + "(audit_id, severity_id, status, author_organization_id, created_at, updated_at)"
                            + "VALUES (" + auditId + ", " + currentSeverityId + ", '" + thisResolution + "', " + childId + ", '" + date + "', '" + date + "')";
                            sql6="";
                        
                        } else {
                            sql5 = "INSERT INTO " + dbName + ".audit_client_merchant_resolutions "
                            + "(audit_id, severity_id, status, author_organization_id, recipient_organization_id, published_at, created_at, updated_at)"
                            + "VALUES (" + auditId + ", " + currentSeverityId + ", '" + thisResolution + "', " + childId + ", " + parentId1 + ", '" + date + "', '" + date + "', '" + date + "')";
                        
                            sql6 = "INSERT INTO " + dbName + ".audit_client_merchant_resolutions "
                            + "(audit_id, severity_id, status, author_organization_id, recipient_organization_id, published_at, created_at, updated_at)"
                            + "VALUES (" + auditId + ", " + currentSeverityId + ", '" + thisResolution + "', " + childId + ", " + parentId2 + ", '" + date + "', '" + date + "', '" + date + "')";
                        }
                        System.out.println("merchant res sql " + sql5);
                        sqlHandler.executeStatement(sql5);
                        sqlHandler.executeStatement(sql6);
                    }
    }
}
