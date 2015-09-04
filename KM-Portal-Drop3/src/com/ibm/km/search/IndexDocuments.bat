title Calling Blocking
color B

SET APP_HOME=C:\KM_Workspace\KM\WebContent\WEB-INF
#SET APP_HOME=C:\Program Files\IBM\WebSphere\AppServer\profiles\default\installedApps\smstestNode01Cell\NMSTESTGSDEAR.ear\centralnms.war\WEB-INF
#SET APP_HOME=D:\IBM\WebSphere\DeploymentManager\profiles\AppSrv02\installedApps\nmsCell02\DefaultEAR.ear\centralnms.war\WEB-INF

SET JAVA_HOME=C:\Program Files\Java\jdk1.6.0

SET PATH=C:\Program Files\Java\jdk1.6.0\bin

SET ORA_HOME=c:\Oracle\Ora81
#SET ORA_HOME=D:\oracle\ora92

#SET CLASSPATH=.;%JAVA_HOME%\lib\dt.jar;%APP_HOME%\classes;%APP_HOME%\lib\classes12.jar;%APP_HOME%\classes\com\ibm\km\common
SET CLASSPATH=%APP_HOME%\classes;%APP_HOME%\lib\lucene-core-2.0.0.jar;%APP_HOME%\lib\lucene-demos-2.0.0.jar;%APP_HOME%\lib\PDFBox-0.7.3.jar;%APP_HOME%\lib\FontBox-0.1.0-dev.jar

#java com.ibm.km.search.IndexFiles "C:\KM_Workspace\KM\WebContent\Docs\10 Office Inspirations_20080612_09-02-32.doc"
#java com.ibm.km.search.IndexHTML -index HTMLIndex  %APP_HOME%\..\Docs
java com.ibm.km.search.SearchFiles -index Index123456789 -keyword Rome -repeat 500 -raw
#java com.ibm.km.search.SearchFiles -index HTMLIndex 



pause









