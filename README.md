#CellTreeEditDistance 07.11.2019
This project is a framework for using the tree-edit-distance to calculate distances between neurons from [NeuroMorpho.Org](www.NeuroMorpho.Org). Possible applications could be an automatic clustering of neurons regarding their type and testing of neuron generation tools.
The framework has been created in the bachelor thesis 'Clustering von Nervenzellen der NeuroMorpho.Org Datenbank mit Hilfe der Tree-Edit-Distance' at Goethe-University Frankfurt. It mostly implements the ideas presented in 'The Tree-Edit-Distance, a Measure for Quantifying
Neuronal Morphology'.

## General Usage Notes
- project needs gradle and java 8
- There exists a disk containing further optional data regarding the bachelor thesis in which this program was created (see 'Content of disk with optional data' below). This data can be asked for at Goethe-University in Frankfurt, precisely Goethe Center for Scientific Computing (GCSC), Gabriel Wittum or Michael Hoffer.

## Usage of the Framework

### Preparations
The CellTreeEditDistance-Framework can be used from commandline by running the jar-file with the needed parameters as described in 'Using the commandline' below (here querying neurons with Lucene is not supported at the moment).
If adjustments to the code are needed the project can be downloaded from GitHub and imported with Gradle. As no jar-file is used in this case, the commands described in 'Using the commandline' have to be adjusted.

In both cases the following directory-structure must be created (or unzipped from the optional disk). In GitHub a 'MinimumWorkingExample' is provided.

```
ProgramData
 |
 +-- Data
     |
     +-- Metadata
     +-- SWCFiles
 +-- Output
 +-- WorkingDir
```

'ProgramData' is the so called base directory.  
'Metadata' should contain the json-files with the Metadata of the neurons from NeuroMorpho.Org  
'SWCfiles' should contain the swc-files from NeuroMorpho.Org within folders representing their archives  
'Output' is empty and will contain output to be created with the framework  
'WorkingDir' is used by some functions of the framework. No files are required to be contained.

### Using the commandline
The Framework has 13 functionalities which are controlled by the parameter 'case'. It is the most important parameter. Without this parameter nothing will work. The framework will tell you if parameters needed for the case are missing. It will not complain if too many arguments are passed, they will be ignored. A list of the parameters are given below.
For instance this could be a simple command to run a TED-calculation from the commandline:  
```
java -jar CellTreeEditDistance.jar -c=3 -b=~/ProgramData -f=~/swcfiles.json
```

####--case=0
This case is used to preprocess the SWCfiles directory within the given base directory. Not all swc-files can be used, some are duplicates. The unwanted files are moved to subdirectory 'ProgramData/Data/SWCFiles/00_Ignore/'.

####--case=1
Unfortunately this case does not work when executing the jar as there have been problems regarding Lucene when creating the fat-jar.
This case is used to query Lucene for neurons. Json-files containing neuronnames are very important when using the framework. they are used to tell for which neurons a TED-calculation shall be made. With this case it is possible to search the Metadata for neurons with specific properties. Not all Metadata are searchable only the ones that have a related swc file in the SWCfiles directory outside 00_Ignore.

Here is an example query:  
```
(species: rat AND brainRegion: hippocampus AND brainRegion: dentate\ gyrus) OR (brainRegion: hippocampus AND NOT cellType: interneuron)
```
The gist is: all properties defined in class 'NeuronMetaDataR' can be queried by using its name followed by ':' and the string you want to search for. Spaces must be escaped with ``'\ '``.

Every time a query has more than 0 results you are asked whether you want to add the resulting neurons to a buffer. Thereby the number of neurons can be restricted. They will then be randomly picked and limited.
After that the buffer can be saved to a json-file or further queries can be made.

####--case=2
This case can also be used to query neurons and save them in a json-file. Here the criteria are UniqueMetadata-categories. To use it the source code must be adjusted. Again an example shall be given:  
```java
int noOfNeuronsPerType = 143;
UniqueMetadataContainer predefinedUniqueMetadataContainer = new UniqueMetadataContainer();
Set<UniqueMetadataContainer.UniqueMetadata> predefinedUniqueMetadata = new HashSet<>();
predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject("", "", "rat", new HashSet<>(Arrays.asList("hippocampus")), new HashSet<>(Arrays.asList("principal cell", "pyramidal"))));
predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject("", "", "rat", new HashSet<>(Arrays.asList("dentate gyrus", "hippocampus")), new HashSet<>(Arrays.asList("principal cell", "granule"))));
```
'noOfNeuronsPerType' describes how many neurons per type are added to the json-file at max.  
'predefinedUniqueMetadata' contains the UniqueMetadata information to be searched for.

####--case=3
This case starts a TED-calculation for a given json file and exports the resulting matrix to the Outputdirectory. This is an example call from the commandline:  
```
java -jar CellTreeEditDistance.jar -c=3 -b=~/ProgramData -f=~/swcfiles.json -n=newName -l=12
```
It means the TED-calculation will be executed with the swc-files specified in 'swcfiles.json', which are stored in '~/ProgramData/Data/SWCfiles/'. The outputmatrix will be renamed to 'newName_Matrix' and Label 12 will be used for the calculation.

####--case=4
This case is exactly the same as case 3 but it also calculates a dendrogram. Further optional parameters are 'e' and 's'.  
If '-e' is added to the commandline the dendrogram will not contain filenames but a mix of neuronMetadata of the neuron. eg '1, Amaral, 117' where 1 is its UniqueMetadata-category, Amaral is the archive the neuron belongs to, 117 is the Neuron-ID in NeuroMorpho.Org where it can be searched.  
If -s is added the dendrogram will not be shown but saved to the output-directory. If the layout is not suitable, it can be changed inside the class 'Clustering' in line 67: contentPane.setSize(500, 800);
Furthermore the dendrogram can be colored depending on its leafnames. This is adjustable directly in the class 'Main' with the property 'clusterColorRegexes'.  
  
This will lead to a completely black dendrogram:
```
new ClusterColorRegex(Pattern.compile("^.*"), Color.BLACK)
```
This will color all leafnames starting with 1, 4, 9, 12 or 19:
```
new ClusterColorRegex(Pattern.compile("^([149]|12|19).*"), new Color(35, 106, 185))
```
####--case=5
This does exactly the same as case 4 except the input file must be an already calculated distance matrix.

####--case=6
This has mostly been used for testing issues. In a file dialog multiple json-files can be selected. For each of the files and each implemented label a distance-matrix will be calculated.

####--case=7
This has mostly been used for testing issues. In a file dialog multiple distance-matrices can be selected. For each of the files a dendrogram will be calculated. Parameters '-e' and '-s' can be used as in case 4.

####--case=8
This case is used for TED-calculation and dendrogram creation for SWC-files. They are chosen within a file dialog. This can be used for swc-files that are not from NeuroMorpho.Org.

####--case=9
This case can be used to analyze a distance-matrix. It calculates relative partitioning errors between UniqueMetadata-categories and clusters. This functionality is implemented but has not been tested well. Yet it is believed to work correctly.

####--case=10
This case is used to search Metadata for neurons that are in a json-file. They are exported into a file with character separated values. Multiple json-files can be selected at once in a file dialog.

####--case=11
This case copies swc-files from the SWCFiles directory to the output directory. The files must be defined in a json-file. Multiple files can be selected at once in a file dialog. The swc-files will be put into subfolders named after their json-file.

####--case=12
This case is used to calculate the TED on a Clustercomputer. It basically just calculates parts of the distance matrix. These must be assembled with case 13 afterwards.   
Here is an example: A json-file with 1000 neurons is given. The Clustercomputer has 13 nodes so the calculation should be separated in 13 subproblems. Therefore the following commandline calls are necessary:
```
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=1
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=2
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=3
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=4
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=5
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=6
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=7
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=8
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=9
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=10
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=11
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=12
java -jar CellTreeEditDistance.jar -c=12 -b=~/ProgramData -f=~/swcfiles.json -l=12 -r=77 -i=13
```
The number of iterations must fit to the number of neurons and the row-count. r = RoundUp(1000/13)  
If you are using slurm, there are some shell-scripts which can be used for this.  

####--case=13
This case is used to reassemble a complete distance matrix from the smaller distance matrices created in case 12. Therefore all submatrices must be located in one directory. The directory can be specified with '-d' parameter.

####commandline parameters
One quadrupel describes a commandline parameter. The first entry is the shortcut to use the parameter eg '-c=1'. The second entry is the long version '--case=1'. The third entry defines whether the parameter has arguments. The last entry gives an explanation of the parameter's purpose.
```
CALC_TYPE("c", "calc", true, "defines what program should do."),
BASE_DIRECTORY("b", "base", true, "defines the base-directory of the application."),
FILE_INPUT("f", "fileInput", true, "defines the file used for calculation (can be a json-file containing the swc-files used for TED-calculation or a txt-file containing the distance-matrix for dendrogram-calculation."),
NAME_OUTPUT("n", "nameOutput", true, "defines the basic name used for naming the output. appendices might be added. no fileextension needed."),
LABEL("l", "label", true, "defines the label used for TED-Calculation."),
RENAME_DENDROGRAM("e", "rEnameDendrogram", false, "if used, filenames in dendrogram are replaced by a combination of neuronMetadata."),
SAVE_OUTPUT("s", "saveOutput", false, "if used, results are saved to output-directory (might include distance-matrix, dendrogram, filemapping)."),
ROWS("r", "rowsPerCall", true, "used for TED-Calculation on a Cluster. Defines the number of matrix-rows for which TED is calculated per call. To guarantee division in same problem sizes only about half of the columns are calculated."),
ITERATION("i", "iteration", true, "used for TED-Calculation on a Cluster. Defines the actual iteration of a TED-Calculation divided for multiple cluster-nodes."),
DIRECTORY_INPUT("d", "directoryInput", true, "defines the directory that contains the clustermatrices to be reassembled to complete distance-matrix.");
```

## Content of disk with optional data
This data is optional and relates to the bachelor thesis 'Clustering von Nervenzellen der NeuroMorpho.Org Datenbank mit Hilfe der Tree-Edit-Distance'.

- 00_Laufzeit: contains the runtime analysis and therefore used json-files
- 01_Test: contains test-files used by the tests of the framework
- 02_Effektivitätstests: contains the results of the 'Effektivitätstests'
	+ Every test has a matrix and a dendrogram
	+ in addition the newly created labels 23 and 24 have been calculated (r_sec and R_sec)
	+ used json-file
	+ metadata relating to the json-file
- 03_AnwendungClustercomputer:
	+ results of the application on the clustercomputer (cluster-matrices, reassembled matrix and log-files of slurm)
	+ runtimes of the 13 slurm-jobs
	+ shell-Script for starting the TED-calculation of the clustercomputer
	+ used json-files
- 04_HW_Algo: two small programs used for converting from the program of heumann and wittum and this framework
- 05_Data: raw metadata and raw swc-files from NeuroMorpho.Org as a zip. Date: 19.08.2019
- BA_ClusteringNeuroMorpho_LukasMaurer.pdf: Bachelor thesis 
- CellTreeEditDistance.jar: fat-jar of the framework
- CellTreeEditDistance.zip: Intellij-project from the framework as a zip
- ProgramData.zip: directory-structure used by the framework, containing the actual data, that has been used in the bachelor thesis.