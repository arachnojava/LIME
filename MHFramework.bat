rem ===============================
rem Grab all the framework stuff.
rem ===============================

md bin

md bin\mhframework
md bin\mhframework\gui
md bin\mhframework\event
md bin\mhframework\images
md bin\mhframework\media
md bin\mhframework\net
md bin\mhframework\net\client
md bin\mhframework\net\common
md bin\mhframework\net\server
md bin\mhframework\tilemap

copy ..\MHFramework\bin\mhframework\*.class            .\bin\mhframework
copy ..\MHFramework\bin\mhframework\gui\*.class        .\bin\mhframework\gui
copy ..\MHFramework\bin\mhframework\event\*.class      .\bin\mhframework\event
copy ..\MHFramework\bin\mhframework\media\*.class      .\bin\mhframework\media
copy ..\MHFramework\bin\mhframework\images\*.*         .\bin\mhframework\images
copy ..\MHFramework\bin\mhframework\net\*.class        .\bin\mhframework\net
copy ..\MHFramework\bin\mhframework\net\client\*.class .\bin\mhframework\net\client
copy ..\MHFramework\bin\mhframework\net\common\*.class .\bin\mhframework\net\common
copy ..\MHFramework\bin\mhframework\net\server\*.class .\bin\mhframework\net\server
copy ..\MHFramework\bin\mhframework\tilemap\*.class    .\bin\mhframework\tilemap

cd bin
rem C:\"Program Files"\Java\jdk1.6.0_12\bin\jar cvf mhframework.jar mhframework
pause

