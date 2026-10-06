# Minpro 3 PBO Sistem Informasi Ekspedisi Antariksa

Mini Project 3 - Praktikum Pemrograman Berorientasi Objek.
Program ini melanjutkan Mini Project 2 (Sistem Pengelolaan Ekspedisi Antariksa) dengan tetap mempertahankan struktur MVC, encapsulation, inheritance, dan polymorphism yang sudah ada, serta menambahkan **interface** sebagai nilai tambah.

## 1. Deskripsi Singkat Program

Program ini adalah aplikasi konsol (CLI) berbasis Java untuk mengelola data operasional sebuah lembaga eksplorasi antariksa, yang terdiri dari tiga entitas utama:

- **Ekspedisi** - misi eksplorasi antariksa (nama, tujuan, durasi, status).
- **Kru** - anggota kru ekspedisi, terbagi menjadi dua peran: **Astronot** dan **Teknisi**.
- **Pesawat Antariksa** - armada pesawat yang digunakan untuk ekspedisi (nama, jenis, kapasitas, status).

Setiap entitas mendukung operasi CRUD penuh (Tambah, Tampilkan, Ubah, Hapus), ditambah satu fitur baru di Mini Project 3: **Ringkasan Semua Data**, yang menampilkan Ekspedisi, Kru, dan Pesawat sekaligus dalam satu daftar gabungan.

## 2. Struktur Package (MVC)

```
src/
├── mini/project/pkg1/
│   └── Main.java              # Entry point, hanya menampilkan menu & mendelegasikan ke Controller
├── model/                      # (Model) Representasi data & aturan dasar tiap entitas
│   ├── EntitasAntariksa.java   # interface - kontrak umum lintas entitas (nilai tambah)
│   ├── Ekspedisi.java
│   ├── Kru.java                # superclass abstrak
│   ├── Astronot.java           # subclass Kru
│   ├── Teknisi.java            # subclass Kru
│   └── PesawatAntariksa.java
├── view/                        # (View) Seluruh tampilan teks & pembacaan input + validasi
│   └── View.java
└── controller/                  # (Controller) Logika bisnis: CRUD, validasi, pencarian, ringkasan
    ├── EkspedisiController.java
    ├── KruController.java
    ├── PesawatController.java
    └── RingkasanController.java # controller baru untuk fitur Ringkasan Semua Data
```

- **Model** hanya berisi atribut (`private`), constructor, getter/setter, dan method tampilan data miliknya sendiri. Tidak ada logika menu atau `Scanner` di sini.
- **View** hanya berisi method untuk mencetak teks ke layar dan membaca + memvalidasi input dari `Scanner`. View tidak menyimpan data aplikasi apa pun.
- **Controller** menyimpan `ArrayList` data, berisi seluruh logika CRUD dan validasi (misalnya cek ID duplikat), lalu memanggil `View` untuk berinteraksi dengan pengguna dan `Model` untuk membuat/mengubah objek data.
- **Main** hanya menampilkan struktur menu dan meneruskan pilihan pengguna ke method Controller yang sesuai.

## 3. Penjelasan Alur Program

1. Program dimulai dari `Main.java`, menampilkan menu utama dengan 4 modul (Ekspedisi, Kru, Pesawat, Ringkasan) dan 1 pilihan keluar.
2. Saat program dijalankan, `EkspedisiController`, `KruController`, dan `PesawatController` masing-masing langsung mengisi **dummy data awal** ke `ArrayList`-nya, sehingga menu "Tampilkan Data" langsung berisi data tanpa harus input manual dulu.
3. Setiap modul punya sub-menu (Tambah, Tampilkan, Ubah, Hapus, Kembali) yang memanggil method pada Controller terkait.
4. Menu baru **"Ringkasan Semua Data"** memanggil `RingkasanController`, yang mengambil data dari ketiga controller lain lewat method `getSemuaEntitas()`, menggabungkannya menjadi satu `List<EntitasAntariksa>`, lalu menampilkan identitas singkat setiap data secara seragam -- walau tipe aslinya berbeda-beda (Ekspedisi, Astronot, Teknisi, atau PesawatAntariksa).
5. Semua pembacaan input dari keyboard (`Scanner`) beserta validasinya dipusatkan di `View`, dipakai ulang oleh seluruh Controller.
6. Program terus berputar dalam menu (`do-while`) sampai pengguna memilih "Keluar"/"Kembali".

## 4. Penerapan Encapsulation dan Inheritance

### Encapsulation
Seluruh atribut pada `Ekspedisi`, `Kru` (beserta subclass-nya), dan `PesawatAntariksa` dideklarasikan `private`, dan hanya bisa diakses/diubah lewat getter dan setter publik. Contoh: `idKru`, `nama`, `usia` pada `Kru.java` bersifat `private`, hanya bisa dibaca lewat `getIdKru()`, `getNama()`, `getUsia()`, atau diubah lewat setter-nya.

### Inheritance
Superclass abstrak `Kru` punya dua subclass:
- `Astronot extends Kru` - menambahkan atribut `spesialisasi` dan `jamTerbang`.
- `Teknisi extends Kru` - menambahkan atribut `bidangKeahlian` dan `sertifikasi`.

Keduanya mewarisi atribut umum (`idKru`, `nama`, `usia`) beserta getter/setter dari `Kru`, dan memanggil constructor superclass lewat `super(...)`. `KruController` menyimpan data dalam satu `ArrayList<Kru>` berisi campuran objek `Astronot` dan `Teknisi`.

## 5. Penerapan Polymorphism dan Abstraction

### Abstraction
`Kru` dideklarasikan sebagai **abstract class** (`public abstract class Kru`), tidak bisa di-instantiate langsung. Di dalamnya ada dua **abstract method**:
```java
public abstract String getPeran();
public abstract String getDetailTugas();
```
Kedua method ini tidak punya isi di `Kru`, dan **wajib** diimplementasikan oleh setiap subclass (`Astronot`, `Teknisi`) sesuai perannya masing-masing. Ini memaksa setiap jenis kru baru di masa depan untuk mendefinisikan perilakunya sendiri, sekaligus menyembunyikan detail implementasi di balik kontrak umum `Kru`.

### Polymorphism
Diterapkan dalam dua bentuk, ditambah satu bentuk baru lewat interface:

1. **Method Overriding** (di dalam hierarki `Kru`) - `getPeran()` dan `getDetailTugas()` yang abstrak di `Kru` diimplementasikan berbeda oleh `Astronot` dan `Teknisi`. Saat `tampilkanData()` (didefinisikan sekali di `Kru`) dipanggil untuk objek apa pun dalam `ArrayList<Kru>`, hasilnya otomatis menyesuaikan tipe objek aslinya.
2. **Method Overloading** - setiap Controller punya dua method pencarian dengan nama sama tapi parameter beda, misalnya `cariKru(int id)` vs `cariKru(String nama)`. Java memilih method yang sesuai berdasarkan tipe parameter saat dipanggil.
3. **Polymorphism lewat Interface** (baru di Mini Project 3) - `RingkasanController` menyimpan data dalam `List<EntitasAntariksa>` berisi campuran `Ekspedisi`, `Astronot`, `Teknisi`, dan `PesawatAntariksa` -- empat class yang **tidak** berada dalam satu hierarki pewarisan yang sama. Saat method `getIdentitas()` dipanggil pada tiap elemen list, Java tetap menjalankan implementasi sesuai tipe objek aslinya. Ini membuktikan polymorphism tidak hanya berlaku lewat inheritance (subclass-superclass), tapi juga lewat interface (unrelated classes).

## 6. Penerapan Nilai Tambah: Interface

File: `model/EntitasAntariksa.java`

```java
public interface EntitasAntariksa {
    String getIdentitas();
    void tampilkanData();
}
```

Diimplementasikan oleh **tiga class yang berbeda hierarki**:
- `Ekspedisi implements EntitasAntariksa`
- `Kru implements EntitasAntariksa` (lalu otomatis diwarisi `Astronot` dan `Teknisi`)
- `PesawatAntariksa implements EntitasAntariksa`

Interface ini dipakai secara konkret di `controller/RingkasanController.java`: method `tampilkanRingkasanSemua()` mengambil data dari ketiga controller lain lewat method `getSemuaEntitas()` (yang masing-masing mengembalikan `List<EntitasAntariksa>`), menggabungkannya jadi satu list, lalu mencetak `getIdentitas()` tiap elemen. Fitur ini bisa diakses lewat menu utama **"4. Ringkasan Semua Data"**.

Manfaat nyatanya: `RingkasanController` tidak perlu tahu atau peduli apakah suatu data itu `Ekspedisi`, `Astronot`, `Teknisi`, atau `PesawatAntariksa` -- selama class itu `implements EntitasAntariksa`, ia bisa diperlakukan secara seragam.

## 7. Validasi Input yang Diterapkan

- Input angka wajib berupa angka valid (`inputInt`), diminta ulang jika tidak valid.
- Input angka tertentu wajib lebih dari 0, misalnya ID, durasi, kapasitas, usia (`inputIntPositif`).
- Input angka tertentu tidak boleh negatif, misalnya jam terbang (`inputIntNonNegatif`).
- Input teks wajib diisi, tidak boleh kosong (`inputString`).
- ID pada setiap entitas divalidasi agar tidak duplikat sebelum data baru ditambahkan.
- Pilihan menu dan pilihan status divalidasi terhadap rentang pilihan yang tersedia.

## 8. Dummy Data Awal

- **Ekspedisi**: "Galang Dana BEM KM UNMUL" (tujuan Mars, status Berlangsung).
- **Kru**: 1 data Astronot ("Dylan Al Furqon") dan 1 data Teknisi ("Tony Stark").
- **Pesawat Antariksa**: "Taufan BAEK" (jenis Roket Orbital, status Siap).

## Penulis

Muhammad Dylan Al Furqon
