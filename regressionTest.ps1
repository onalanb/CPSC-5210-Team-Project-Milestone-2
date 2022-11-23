$username = "team8cpsc5210@gmail.com"
$password = ConvertTo-SecureString "oxatsnjlkmwqnokf" -AsPlainText -Force
$creds = New-Object System.Management.Automation.PSCredential -ArgumentList ($username, $password)

$body = Get-Content -Path ./report.txt | Out-String
 
Send-MailMessage -SmtpServer smtp.gmail.com -Port 587 -Credential $creds -From team8cpsc5210@gmail.com -To onalanbaran@seattleu.edu,jho4@seattleu.edu,mammenjulie@seattleu.edu,lzhang@seattleu.edu -Subject "Regression Test Results" -Body $body -UseSsl
