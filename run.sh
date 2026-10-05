#!/usr/bin/env sh
# Kompilasi lalu jalankan. Pemakaian: ./run.sh [cli|gui]
set -e
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name '*.java')
java -cp out com.kalkulator.Main "$@"
