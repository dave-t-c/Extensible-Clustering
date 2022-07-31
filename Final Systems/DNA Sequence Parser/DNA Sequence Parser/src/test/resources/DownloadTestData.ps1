Function DeGZip-File{
    Param (
        $infile,
        $outfile = ($infile -replace '\.gz$','')
    )

    $input = New-Object System.IO.FileStream $inFile, ([IO.FileMode]::Open), ([IO.FileAccess]::Read), ([IO.FIleShare]::Read)
    $output = New-Object System.IO.FileStream $outFile, ([IO.FileMode]::Create), ([IO.FileAccess]::Write), ([IO.FIleShare]::None)
    $gzipStream = New-Object System.IO.Compression.GZipStream $input, ([IO.Compression.CompressionMode]::Decompress)

    $buffer = New-Object byte[] (1024)

    while($true){
        $read = $gzipStream.Read($buffer, 0, 1024)
        if ($read -le 0) {break}
        $output.Write($buffer, 0, $read)
    }

    $gzipStream.Close()
    $output.Close()
    $input.Close()

}


$list = Get-Content src\test\resources\test_files.txt

$clnt = New-Object System.Net.WebClient

foreach($url in $list)
{
    $filename = [System.IO.Path]::GetFileName($url)

    $file = [System.IO.Path]::Combine($pwd.Path, $filename)

    Write-Host -NoNewline "Getting ""$url""...."

    $clnt.DownloadFile($url, $file)

    DeGZip-File $file ([System.IO.Path]::Combine($pwd.Path, ([System.IO.Path]::GetFileNameWithoutExtension($filename))))

    Remove-Item $file

    Write-Host "done."

}

Move-Item -Path ".\gbbct400.seq" -Destination ".\src\test\resources"
Move-Item -Path ".\gbbct455.seq" -Destination ".\src\test\resources"