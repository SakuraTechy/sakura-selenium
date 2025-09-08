#!/bin/bash

echo '修改环境变量'
sed -i "s#^Environment_Type=.*#Environment_Type=Windows#g"  src/main/java/common.properties
sed -i "s#^Product_Name=.*#Product_Name=${Abbreviate}#g"  src/main/java/common.properties
sed -i "s#^Product_Version=.*#Product_Version=${Version}#g"  src/main/java/common.properties
sed -i "s#^Product_Version_Description=.*#Product_Version_Description=${Description}#g"  src/main/java/common.properties
sed -i "s#^testPlanId=.*#testPlanId=${testPlanId}#g"  src/main/java/common.properties
sed -i "s#^testReportId=.*#testReportId=${testReportId}#g"  src/main/java/common.properties
sed -i "s#^buildNumber=.*#buildNumber=${BUILD_NUMBER}#g"  src/main/java/common.properties
#sed -i "s#^ExtentReport_URL=.*#ExtentReport_URL=${BUILD_URL}#g"  src/main/java/common.properties
sed -i "s#^ExtentReport_Type=.*#ExtentReport_Type=artifact#g"  src/main/java/common.properties
sed -i "s#^ExtentReport_URL=.*#ExtentReport_URL=${jenkinsUrl}#g"  src/main/java/common.properties
sed -i "s#^ExtentReport_BUILD_NUMBER=.*#ExtentReport_BUILD_NUMBER=${BUILD_NUMBER}#g"  src/main/java/common.properties
#if [[ "${Branch}" == "prod" ]]; then
  #sed -i 's|ExtentReport_URL=http://172.19.5.222:8080|ExtentReport_URL=https://www.ankki.hk.cn:38937|g' src/main/java/common.properties
#fi

#ServerPassWord='@1fw#2soc$3vpn'
escaped_PassWord=$(echo "$ServerPassWord" | sed 's/[!@#$%^&*()]/\\&/g')
if [[ "$ServerPassWord" == "$escaped_PassWord" ]]; then
  echo "字符串不包含特殊字符"
else
  echo "字符串包含特殊字符，并已转义: $escaped_PassWord"
fi
sed -i "s#^${Abbreviate}_Login_IP=.*#${Abbreviate}_Login_IP=${IP}#g"  src/main/java/common.properties
sed -i "s#^${Abbreviate}_Login_Port=.*#${Abbreviate}_Login_Port=${Port}#g"  src/main/java/common.properties
sed -i "s#^${Abbreviate}_LinuxShell_IP=.*#${Abbreviate}_LinuxShell_IP=${IP}#g"  src/main/java/common.properties
sed -i "s#^${Abbreviate}_LinuxShell_Port=.*#${Abbreviate}_LinuxShell_Port=${ServerPort}#g"  src/main/java/common.properties
sed -i "s#^${Abbreviate}_LinuxShell_UserName=.*#${Abbreviate}_LinuxShell_UserName=${ServerUserName}#g"  src/main/java/common.properties
sed -i "s#^${Abbreviate}_LinuxShell_PassWord=.*#${Abbreviate}_LinuxShell_PassWord=${escaped_PassWord}#g"  src/main/java/common.properties

sed -i "s#^${Abbreviate}_Oracle.jdbc.Turl=.*#${Abbreviate}_Oracle.jdbc.Turl=${IP}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_Oracle.jdbc.Tport=.*#${Abbreviate}_Oracle.jdbc.Tport=${DataBasePort}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_Oracle.jdbc.Tusername=.*#${Abbreviate}_Oracle.jdbc.Tusername=${DataBaseName}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_Oracle.jdbc.Tpassword=.*#${Abbreviate}_Oracle.jdbc.Tpassword=${DataBasePassWord}#g"  src/main/java/jdbc.properties

sed -i "s#^${Abbreviate}_MySql.jdbc.Turl=.*#${Abbreviate}_MySql.jdbc.Turl=${IP}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_MySql.jdbc.Tport=.*#${Abbreviate}_MySql.jdbc.Tport=${DataBasePort}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_MySql.jdbc.Tusername=.*#${Abbreviate}_MySql.jdbc.Tusername=${DataBaseName}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_MySql.jdbc.Tpassword=.*#${Abbreviate}_MySql.jdbc.Tpassword=${DataBasePassWord}#g"  src/main/java/jdbc.properties

sed -i "s#^${Abbreviate}_DM.jdbc.Turl=.*#${Abbreviate}_DM.jdbc.Turl=${IP}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_DM.jdbc.Tport=.*#${Abbreviate}_DM.jdbc.Tport=${DataBasePort}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_DM.jdbc.Tusername=.*#${Abbreviate}_DM.jdbc.Tusername=${DataBaseName}#g"  src/main/java/jdbc.properties
sed -i "s#^${Abbreviate}_DM.jdbc.Tpassword=.*#${Abbreviate}_DM.jdbc.Tpassword=${DataBasePassWord}#g"  src/main/java/jdbc.properties

sed -i "s#^${Abbreviate}_Object1_MySql.jdbc.Turl=.*#${Abbreviate}_Object1_MySql.jdbc.Turl=${Run}#g"  src/main/java/jdbc.properties