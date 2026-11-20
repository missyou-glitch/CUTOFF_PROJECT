#!/bin/bash
# Bien dich, kiem tra, chay thi nghiem, phan tich (can JDK 11+ va python3 voi pandas, matplotlib)
set -e
mkdir -p bin results data
javac -d bin src/*.java
java -cp bin Test                      # kiem tra tinh dung
java -cp bin DataGen data              # ghi du lieu mau N=10^4, 10^5
java -cp bin Experiment results/results.csv   # thi nghiem (vai phut)
cd results && python3 ../analyze.py && python3 ../plots.py
