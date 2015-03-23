#!/usr/bin/env bash

javac QuickSummary.java
mkdir -p output

for file in ../../test-logs/original-logs/*; do
  logfile=output/${file##*/}
  java QuickSummary $file > $logfile
  pngfile=${logfile%.log}.png
  gnuplot -e "logfile='${logfile}'; pngfile='${pngfile}'" plot.plg
done
