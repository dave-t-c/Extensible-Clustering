# An extendible clustering package for bioinformatics
Aims: To design and implement an application for clustering different biological data sets with different algorithms using the Java plug-in technology.

## Background
Bioinformatics is currently a quickly growing field within the Computer Science ecosystem. The advent of new sequencing technologies along with the high amount of funding devoted to research in life sciences, have produced a huge amount of biological data which waits to be analysed. Clustering is a technique which is often used for such analysis: given a set of elements (protein sequences, gene expression profiles, etc), performing a clustering amounts to finding meaningful groups in the data set.

There are different clustering algorithms, each one with different features: hierarchical/non-hierarchical, overlapping/non-overlapping, graph-based, etc. Also, for different biological datasets, some clustering algorithms are more appropriate than others. 

Finally, once a clustering has been obtained there are different techniques to assess the validity of the groupings.

In this project a clustering environment for bioinformatics should be developed using the Java language. The environment should be as generic as possible, with a very important feature: it should allow for future extensions. Namely, it should be extensible in three ways:
 - New biological data types
 - New clustering algorithms
 - New techniques for the visualisation/assessment of the clusters
 
In order to do that, the Java plugin architecture should be used. This will allow other people to write future extensions to this system, without the need of rewriting its kernel. Thus, the project should be carefully designed and should contain enough generality to prevent further modifications and allow any future plugins to be added to it.

The final aim of the project is to produce a solid piece of software which will be made available to the scientific community. It is possible that this work will lead to a publication in a bioinformatics journal.

Prerequisites: Students attempting this project should be good at programming in Java. The student should be interested in Java software development, and know basic software engineering techniques. Also, interest in biological problems is desirable.

## Early Deliverables

Proof of concept programs: 
 - Parsers for biological databases,
 - Implementation of one clustering algorithm,
 - Design of an interface.
 
Reports: 
 - Biological databases.
 - Clustering algorithms.
 - Design Patterns in Java (mainly plugin architecture).
 
## Final Deliverables
 - The software should be able to work with at least two kind of biological data (microarray data and sequence data) and two clustering algorithms (for instance k-means and hierarchical clustering).
 - At least one visualisation/assessment technique should be added (for example, over-representation analysis).
 - The report should include a biological overview of the problem,
 - The report should include a detailed description of the implementation of the plugin-architecture,
 - The report should include a comprehensive documentation on how to extend the program.

## Suggested Extensions
 - Adding the possibility of processing more biological data (PPI data, for example, CSV, etc)
 - Adding more clustering algorithms, possibly using the Façade pattern with existing implementations (SCSP, Cluster ONE, ...).
 - Developing cluster visualisation techniques: MDS visualisation, dendrograms, etc.
