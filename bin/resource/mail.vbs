dim msg
set MyApp = CreateObject("Outlook.Application")
IF MyApp is Nothing Then
MyApp.Session.Logon
End IF
Set MyItem = MyApp.CreateItem(0)
with MyItem

.To = Wscript.Arguments(0)
.Subject = Wscript.Arguments(1)
.HtmlBody ="<pre style='font-family:calibri;color:black;font-size:15px'>" & Wscript.Arguments(2) & "<br/><br/>******************Auto Generated mail********************</pre>" 
If Wscript.Arguments(3) = "empty" then
Else
.Attachments.Add Wscript.Arguments(3)
End If
End With
MyItem.send