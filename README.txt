24127147 - Do an #1 Slang Word (CSC13002)
Giai doan 1 - Xu ly tu dien goc

Sua file slang.txt goc cua de thanh file chuan (slang_clean.txt), nap vao cau truc du lieu
(TreeMap + index dao) va luu ra file nhi phan slang_default.dat.

CACH CHAY (can JDK 8 tro len)
  Windows : bam dup run.bat  hoac mo terminal trong thu muc nay roi go:  run.bat
  Linux/Mac: sh run.sh

  Hoac tu go tay:
    javac -encoding UTF-8 -d out src/*.java
    java -cp out ProcessOriginalData

Thu muc data phai nam canh thu muc src (chua san file goc slang.txt).
Cac file slang_clean.txt, slang_default.dat, slang_user.*, history.txt duoc chuong trinh tu tao khi chay.
