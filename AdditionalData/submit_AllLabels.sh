#!/bin/bash
rows=1000
maxjobs=10
bash_pid=$$
for label in {1..11}
do
    for iteration in {1..112}
    do
        executed=0
        while ((executed==0))
        do
            num_children=$(ps -eo ppid | grep -w $bash_pid | wc -w)
            if ((num_children < maxjobs+1));then
                srun --nodes=1 --exclusive java -Xmx28g -jar CellTreeEditDistance.jar -c=12 -b=ProgramData -f=swcFile_All.json -l=$label -i=$iteration -r=$rows > slurm_lab-${label}_it-${iteration}.txt &
                executed=1
            fi
            sleep 1
        done
    done
done
wait
