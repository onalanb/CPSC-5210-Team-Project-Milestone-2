set path="ENTER YOUR JDK PATH HERE, BELOW I PUT AN EXAMPLE OF MY PATH";%path%
REM set path=C:\Users\baran\.jdks\openjdk-19\bin;%path%
set path="ENTER YOUR MAVEN PATH HERE, BELOW I PUT AN EXAMPLE OF MY PATH";%path%
REM set path=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.3\plugins\maven\lib\maven3\bin;%path%
mvn clean test -Dtest=* -DfailIfNoTests=false & mvn clean test -Dtest=* -DfailIfNoTests=false > report.txt & powershell -file regressionTest.ps1