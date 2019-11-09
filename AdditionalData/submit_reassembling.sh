#!/bin/bash
#
#SBATCH --job-name=Reassembling
#SBATCH --nodes=1
#SBATCH --ntasks-per-node=1
#SBATCH --mail-type=END
#SBATCH --mail-user=s4372430@stud.uni-frankfurt.de
#SBATCH --exclusive

srun java -jar CellTreeEditDistance.jar -c=13 -b=ProgramData -f=swcFile_HQ_MQ_shuffled.json -d=ClusterresultShuffled/Matrices
