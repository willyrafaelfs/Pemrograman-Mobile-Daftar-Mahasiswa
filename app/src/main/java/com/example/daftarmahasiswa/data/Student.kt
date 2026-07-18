package com.example.daftarmahasiswa.data

/**
 * ============================================================
 * DATA CLASS: Student (Model Data Mahasiswa)
 * ============================================================
 *
 * Data class di Kotlin otomatis menghasilkan:
 * - equals() dan hashCode() → untuk perbandingan objek
 * - toString() → untuk debug/print
 * - copy() → untuk membuat salinan dengan modifikasi
 *
 * Ini adalah "blueprint" untuk data mahasiswa yang akan
 * digunakan di seluruh aplikasi.
 */
data class Student(
    val id: Int,              // ID unik setiap mahasiswa
    val name: String,         // Nama lengkap
    val nim: String,          // Nomor Induk Mahasiswa
    val jurusan: String,      // Program studi
    val email: String = "",   // Email (default kosong)
    val phone: String = "",   // Telepon (default kosong)
    val semester: Int = 6     // Semester saat ini (default 6)
)

/**
 * ============================================================
 * DATA DUMMY: Daftar mahasiswa untuk contoh awal
 * ============================================================
 *
 * Di aplikasi nyata, data ini akan datang dari:
 * - Database lokal (Room)
 * - API server (Retrofit)
 * - Firebase
 *
 * Untuk Week 3, kita gunakan data dummy dulu.
 */
object StudentData {

    // mutableListOf → list yang bisa ditambah/hapus isinya
    private val students = mutableListOf(
        Student(
            id = 1,
            name = "Ahmad Fauzi Rahman",
            nim = "20210001",
            jurusan = "Teknik Informatika",
            email = "ahmad.fauzi@student.ac.id",
            phone = "+62 812-3456-7890",
            semester = 6
        ),
        Student(
            id = 2,
            name = "Siti Aisyah Putri",
            nim = "20210002",
            jurusan = "Sistem Informasi",
            email = "siti.aisyah@student.ac.id",
            phone = "+62 813-2345-6789",
            semester = 6
        ),
        Student(
            id = 3,
            name = "Budi Santoso",
            nim = "20210003",
            jurusan = "Teknik Informatika",
            email = "budi.santoso@student.ac.id",
            phone = "+62 857-1234-5678",
            semester = 6
        ),
        Student(
            id = 4,
            name = "Dewi Lestari",
            nim = "20210004",
            jurusan = "Teknik Komputer",
            email = "dewi.lestari@student.ac.id",
            phone = "+62 878-9876-5432",
            semester = 4
        ),
        Student(
            id = 5,
            name = "Rizky Pratama",
            nim = "20210005",
            jurusan = "Sistem Informasi",
            email = "rizky.pratama@student.ac.id",
            phone = "+62 821-5678-1234",
            semester = 6
        ),
        Student(
            id = 6,
            name = "Duta Pratama",
            nim = "20210006",
            jurusan = "Sistem Informasi",
            email = "duta.pratama@student.ac.id",
            phone = "+62 821-5678-1234",
            semester = 6
        )

    )

    /** Mendapatkan semua mahasiswa (salinan/copy agar aman) */
    fun getAll(): List<Student> = students.toList()

    /** Mencari mahasiswa berdasarkan ID */
    fun getById(id: Int): Student? = students.find { it.id == id }

    /** Menambah mahasiswa baru */
    fun add(student: Student) {
        students.add(student)
    }

    /** Memperbarui data mahasiswa */
    fun update(student: Student) {
        val index = students.indexOfFirst { it.id == student.id }
        if (index != -1) {
            students[index] = student
        }
    }

    /** Menghapus mahasiswa */
    fun delete(id: Int) {
        students.removeAll { it.id == id }
    }

    /** Mendapatkan ID baru (auto increment sederhana) */
    fun getNextId(): Int = (students.maxOfOrNull { it.id } ?: 0) + 1
}
