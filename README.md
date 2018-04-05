CS 535/CS435 Spring 2017

Time-series data trend prediction

#### Files required

- Makefile
- code directory
- input-dataset directory

#### Intermediate files

- r_dataset.csv
- prediction.csv
- Rplots.pdf

#### Output files

- final.txt

#### Environment

- Linux

#### Language used

- Java and R

#### Compiling and Execution

All the input files MUST be in the same directory names "input-dataset" and MUST be in the same folder as the Makefile.

Run the Makefile with command `make`.  This will compile all the source code and produce `project.jar` executable and `final.txt` as per the given specifications.

#### Explanation

The function `filterKeyProductData()` from Main.java filters the required data for the prediction and writes it into r_dataset.csv file. Using r_dataset.csv, prediction is done using R language. The result of prediction is written into prediction.csv file (which is more convenient to read). Then the function `createOutputFile()` from Main.java takes the data from prediction.csv file and edits it as per the output file requirements and writes into final.txt file.

When we run the `make` command following things will happen:

- The make file will pass the command line argument as 0. This will run the java code to generate r_dataset.csv. Using this generated file, now the Rscript will be executed to produce prediction.csv.
- The make file will now pass the command line argument as 1. This will run the java code to generate the final.txt file which is the final output file.
- An additional file Rplots.pdf is generated which contains graphical information of all the predictions.
