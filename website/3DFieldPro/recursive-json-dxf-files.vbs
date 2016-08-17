'Recursive search example
'------------------------

'The directory to search.
Const startDir = "C:\Users\ipower\Documents\bubble\"

'Variables
Dim objFSO, outFile, objFile, strFolder, strFile, subFolder

'Use the FileSystemObject to search directories and create the text file.
Set objFSO = CreateObject("Scripting.FileSystemObject")

'Create a text file.
Set outFile = objFSO.CreateTextFile("C:\Users\ipower\Documents\DirectoryList.txt",True)

'Write a 'heading' to the text file.
outFile.WriteLine "Directory Listing of " & startDir
outFile.WriteLine String(25,"-")
outFile.WriteBlankLines 1

'Call the Search subroutine to start the recursive search.
Search objFSO.GetFolder(startDir)

Sub Search(sPath)
	
	'Assign the value of sPath to the strFolder variable.
	strFolder = sPath
	
	'Loop through each file in the sPath folder.
	For Each objFile In sPath.Files
		outFile.WriteLine objFile.Name & "\"
		'Assign each file name to the strFile variable.
		strFile = strFile & objFile.Name & vbCrLf
		searchStr = ".csv"
		If InStr(objFile.Name, searchStr) > 0 Then
			outFile.WriteLine "substring .csv found within filename"
			'outFile.WriteLine objFile.Name & "\"
			'outFile.WriteLine "FieldPro64.exe /n grid.net /m contours /v " & objFile.ParentFolder & "\ /h " & objFile.ParentFolder & "\" & objFile.name
			Dim params
			' /va test.dxf /m contours /h points.dat
			params = """C:\3DFieldPro64\FieldPro64.exe"" -nocheckgrid /m contours /va " & objFile.ParentFolder & "\" & "values.dxf /h " & objFile.ParentFolder & "\" & objFile.name
			Set objShell = CreateObject("Wscript.Shell")
			objShell.Run params,0,true
			Set objShell = Nothing
			'start "" "C:\3DFieldPro64\FieldPro64.exe" params
			'c:\3Dfield\field.exe /n grid.net /m contours /v values.txt /h your_data.csv
		End If
	Next
	
	'Write the folder and all file names to the text file.
	'outFile.Write strFolder & vbCrLf & strFile
	'outFile.WriteBlankLines 1
	
	strFolder = ""
	strFile = ""
	
	'Find EACH SUBFOLDER.
	For Each subFolder In sPath.SubFolders
	
		'Call the Search subroutine to start the recursive search on EACH SUBFOLDER.
		Search objFSO.GetFolder(subFolder.Path)
	Next
End Sub

'Close the text file write stream.
outFile.Close

'Notify the user that the script is done.
WScript.Echo "Done."