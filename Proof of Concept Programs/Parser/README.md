# Proof of Concept Program - Parser

This program takes a gene micro-array file, in the series matrix format, and parses it into positions that can be used for clustering.

An example of this type of file can be found [here](https://ftp.ncbi.nlm.nih.gov/geo/series/GSE4nnn/GSE4014/matrix/GSE4014-GPL32_series_matrix.txt.gz).

This proof of concept program takes the url of one of these files, e.g. /Users/name/Documents/Files/example.txt , and print out how many positions have been taken from the file. It also outputs how many components each of the positions has.

### Requirements

To run this program, you will need maven, and Java >= 1.8 installed.

This program uses maven as the build tool, and targets Java version 1.8


### Running the Program

To run the program:
1. CD into the 'parser' folder.
2. Enter: "mvn clean compile exec:java"

This will start running the program. You then need to enter the location of the file that you want to use.
