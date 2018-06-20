# CellTreeEditDistance

## Information

This code is the implementation of Tree Edit Distance in neurons. It computes the distance between two or more neurons represented in swc format file. It uses the APTED Algorithm [2] and was first introduced from Heumann and Wittum [1]. 

## Usage

The code is intended to be used as a plugin in VRL-Studio. Therefore you need to have [VRL-Studio](https://vrl-studio.mihosoft.eu/) installed and follow these [instructions](https://vrl-studio.mihosoft.eu/documentation.html). 

Another way to use it is in every IDE. Simply open the Main Class and run the following command with the label of your choice:

```
CellTreeEditDistance matrix=new CellTreeEditDistance(); 
matrix.compareFiles(11); 
```
To see the labels call the static function *showLabels()*
````
CellTreeEditDistance.showLabels();
````

## Input

Multiple swc files or a folder. Supported are only files in swc format. Any digital representation of neuron in other format will not be recognized as input.

## Output

The Output is a distance Matrix printed in a table and can be exported to a txt file. The txt file can be used for custer analysis. It is a *n x (n+1)* matrix for *n* files given as input where each entry is divided by a semikolon and the first row contains the names of the selected files. To analyse the distance matrix with the hierarchical clustering method in RStudio do the following:
``` 
clusterfile <- read.csv2("YOUR_PATH_OF_THE_DISTANCE_MATRIX/EXPORTFILE.txt", header=FALSE, row.names=1)
View(clusterfile)
d=as.dist(clusterfile)
hcs <- hclust(d, method="ward.D")
plot(hcs)
rect.hclust(hcs, k = 2, border = "red")	
````
## License

This code is published under the BSD 2-Clause terms. For more details see the licence file found in the root directory of the project.
 
## Reference
 
 [1] H. Heumann and G. Wittum. The Tree Edit Distance, a Measure for Quantifying Neuronal Morphology. Neuroinform 2009.
 
 [2] M. Pawlik and N. Augsten. Tree edit distance: Robust and memory- efficient. Information Systems 56. 2016.