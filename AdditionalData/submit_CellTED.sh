#!/bin/bash
#
#SBATCH --job-name=TED
#SBATCH --nodes=1
#SBATCH --ntasks-per-node=1
#SBATCH --mail-type=END
#SBATCH --mail-user=s4372430@stud.uni-frankfurt.de
#SBATCH --array=1-20
#SBATCH --exclusive

i=${SLURM_ARRAY_TASK_ID}

srun java -Xmx28g -jar CellTreeEditDistanceMemory.jar -c=12 -b=ProgramData -f=swcFile_All.json -i=${i} -r=5577
