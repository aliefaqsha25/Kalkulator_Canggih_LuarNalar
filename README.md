# Kalkulator Java

Kalkulator sederhana dengan dua tampilan — **CLI** (terminal) dan **GUI** (Swing) — yang memakai satu mesin perhitungan yang sama.

## Fitur

Penjumlahan, pengurangan, perkalian, pembagian, persen, desimal, akar (√), pangkat (^), kurung buka/tutup, minus & positif (±), Delete, Clear All, angka 0–9, dan sama dengan.

## Persyaratan

- JDK 11 atau lebih baru
- (Opsional) Maven 3.6+ untuk menjalankan tes

## Menjalankan

**Windows**
```
run.bat          # menu pilih mode
run.bat cli      # langsung CLI
run.bat gui      # langsung GUI
```

**Linux / macOS**
```
./run.sh         # menu pilih mode
./run.sh cli
./run.sh gui
```

**Manual (tanpa skrip)**
```
javac -encoding UTF-8 -d out $(find src/main/java -name '*.java')
java -cp out com.kalkulator.Main [cli|gui]
```

**Dengan Maven**
```
mvn test                    # jalankan tes
mvn package                 # buat target/kalkulator-java-1.0.0.jar
java -jar target/kalkulator-java-1.0.0.jar gui
```

> Di Windows, mode CLI otomatis mengatur code page konsol ke UTF-8. Jika kotak tampil rusak, gunakan **Windows Terminal** atau ganti font CMD ke *Consolas*/*Cascadia Mono*.

## Tombol

| Fungsi | Keyboard |
|---|---|
| Angka | `0`–`9` |
| Tambah / kurang / kali / bagi | `+` `-` `*` (atau `x`) `/` |
| Persen | `%` (x% = x ÷ 100) |
| Desimal | `.` |
| Akar | `r` (menjadi √) |
| Pangkat | `^` |
| Kurung | `(` `)` — kurung yang belum ditutup ditutup otomatis saat `=` |
| Minus / positif | `n` (ubah angka terakhir ke negatif/positif) |
| Delete | `d` (GUI: `Backspace`) |
| Clear All | `c` (GUI: `Esc` / `Delete`) |
| Sama dengan | `=` (GUI: `Enter`) |
| Keluar (CLI) | `q` |

Di **CLI**, ketik tombol (boleh berurutan) lalu tekan Enter, mis. `2+3*(4-1)=`.
Di **GUI**, klik tombol atau ketik langsung dengan keyboard.

## Struktur Proyek

```
src/main/java/com/kalkulator/
├── Main.java                  # titik masuk, memilih mode
├── core/                      # logika murni (tanpa UI, mudah dites)
│   ├── CalculatorEngine.java  # state ekspresi + penanganan tombol
│   ├── ExpressionParser.java  # parser recursive descent
│   ├── ResultFormatter.java   # pembulatan & format hasil
│   └── CalculatorException.java
├── ui/
│   └── Keypad.java            # tata letak tombol (dipakai CLI & GUI)
├── cli/
│   ├── CliApp.java            # loop interaktif + menu mode
│   ├── CliRenderer.java       # menggambar kotak/papan tombol
│   ├── ConsoleSetup.java      # setup UTF-8 konsol
│   └── Ansi.java              # kode warna
└── gui/
    ├── GuiApp.java            # jendela Swing
    └── RoundButton.java       # tombol membulat
src/test/java/com/kalkulator/core/   # tes JUnit 5
```

Aturan dependensi: `core` tidak mengimpor apa pun dari `cli`/`gui`/`ui`; `cli` dan `gui` hanya bergantung pada `core` dan `ui`.

## Lisensi

MIT — lihat [LICENSE](LICENSE).
