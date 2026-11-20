@echo off
rem Chay toan bo thi nghiem tren Windows (can JDK va Python)
if not exist bin mkdir bin
if not exist results mkdir results
if not exist data mkdir data
call javac -d bin src\*.java || goto :err
call java -cp bin Test || goto :err
call java -cp bin DataGen data || goto :err
call java -cp bin Experiment results\results.csv || goto :err
call python analyze.py || goto :err
call python plots.py || goto :err
echo XONG. Xem thu muc results\
goto :eof
:err
echo Co loi o buoc tren, xem thong bao phia tren.
