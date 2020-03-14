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
    echo "Total: " $runtime

    start=`date +%s.%N`
    cat $FILE | java quicklinks.QuickLinks -bt > trash.out
    cat $FILE | java quicklinks.QuickLinks -bt > trash.out
    cat $FILE | java quicklinks.QuickLinks -bt > trash.out
    cat $FILE | java quicklinks.QuickLinks -bt > trash.out
    cat $FILE | java quicklinks.QuickLinks -bt > trash.out
    end=`date +%s.%N`
    runtime2=$(python -c "print((${end} - ${start})/5.0)")
    echo "Pre:   " $runtime2

    runtime3=$(python -c "print((${runtime} - ${runtime2}))")
    #display the difference in runtimes    
    echo "Post:  " $runtime3

    #now we want to see if the answer is valid
    filename=$(basename -- "$FILE")
    extension="${filename##*.}"
    filename="${filename%.*}"
    filename="data2/$filename.ans"
    diff $filename tmp_out.out > differences
    wc -l differences
    rm differences

    
    echo
done
