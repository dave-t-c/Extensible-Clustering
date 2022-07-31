#!/bin/bash
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
  cat src/test/resources/test_files.txt | while read source  
  do
    /usr/bin/wget --compression=gzip "$source"
    filename=$(basename "$source")
    gunzip "$filename"
  done
 else
  cat src/test/resources/test_files.txt | while read source
  do
    /usr/local/bin/wget --compression=gzip "$source"
    filename=$(basename "$source")
    gunzip "$filename"
  done
 fi
mv *.seq src/test/resources