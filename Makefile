all: project.jar final_output.txt

project.jar: code/src/Main.java
	ant -f code/build.xml
	cp code/out/artifacts/DmProject_jar/DmProject.jar project.jar

intermediate_files/r_dataset.csv: project.jar
		java -cp project.jar Main 0

prediction.csv: intermediate_files intermediate_files/r_dataset.csv code/rscripts/main.R
	Rscript code/rscripts/main.R

final_output.txt: prediction.csv project.jar
		java -cp project.jar Main 1

intermediate_files:
	mkdir intermediate_files

clean:
	rm -rf code/out project.jar final_output.txt intermediate_files Rplots.pdf prediction.csv
