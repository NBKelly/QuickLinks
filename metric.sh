#!/bin/bash

FILES=data2/*.in
for FILE in $FILES
do
    echo "$FILE"
    #we want to know the number of nodes and the number of scenarios
    line=$(head -n 1 $FILE)
    echo "Nodes Scenarios"
    echo $line

    #now we want to process this five times and record how long it takes
    start=`date +%s.%N`
    cat $FILE | java quicklinks.QuickLinks > tmp_out.out
    cat $FILE | java quicklinks.QuickLinks > tmp_out.out
    cat $FILE | java quicklinks.QuickLinks > tmp_out.out
    cat $FILE | java quicklinks.QuickLinks > tmp_out.out
    cat $FILE | java quicklinks.QuickLinks > tmp_out.out
    end=`date +%s.%N`
    runtime=$(python -c "print((${end} - ${start})/5.0)")
    echo $runtime
    echo
done
