package com.example.daftarmahasiswa.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.daftarmahasiswa.data.StudentData
import com.example.daftarmahasiswa.screens.*

/**
 * ============================================================
 * ROUTES: Alamat setiap halaman di aplikasi
 * ============================================================
 */
object Routes {
    const val HOME = "home"                       // Halaman daftar
    const val DETAIL = "detail/{studentId}"       // Halaman detail (butuh ID)
    const val ADD_STUDENT = "add_student"         // Halaman tambah
    const val EDIT_STUDENT = "edit/{studentId}"   // Halaman edit (butuh ID)
    const val SEARCH = "search"                   // Halaman pencarian
    const val PROFILE = "profile"                 // Halaman profil
    const val DATA_NILAI = "nilai/{studentId}"    // Task: Halaman nilai
    const val PROFILE_PHOTO = "profile_photo"      // Task: Halaman edit foto

    fun detailRoute(studentId: Int) = "detail/$studentId"
    fun editRoute(studentId: Int) = "edit/$studentId"
    fun nilaiRoute(studentId: Int) = "nilai/$studentId"
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Routes.HOME, Icons.Default.Home),
    BottomNavItem("Search", Routes.SEARCH, Icons.Default.Search),
    BottomNavItem("Profile", Routes.PROFILE, Icons.Default.Person)
)

/**
 * ============================================================
 * APP NAVIGATION: Pusat pengaturan navigasi seluruh aplikasi
 * ============================================================
 */
@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    
    // State students di-hoist ke sini agar sinkron antar screen
    var students by remember {
        mutableStateOf(StudentData.getAll())
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Tampilkan bottom bar hanya pada screen utama
    val mainRoutes = listOf(Routes.HOME, Routes.SEARCH, Routes.PROFILE)
    val showBottomBar = currentDestination?.route in mainRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Perbaikan: Hanya gunakan padding bawah (untuk navbar)
        // agar top bar di setiap screen tidak terdorong terlalu jauh ke bawah
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // ── HOME ──
            composable(Routes.HOME) {
                HomeScreen(
                    students = students,
                    onStudentClick = { studentId ->
                        navController.navigate(Routes.detailRoute(studentId))
                    },
                    onAddClick = {
                        navController.navigate(Routes.ADD_STUDENT)
                    }
                )
            }

            // ── SEARCH ──
            composable(Routes.SEARCH) {
                SearchScreen(
                    students = students,
                    onStudentClick = { studentId ->
                        navController.navigate(Routes.detailRoute(studentId))
                    }
                )
            }

            // ── PROFILE ──
            composable(Routes.PROFILE) {
                ProfileScreen(
                    isDarkMode = isDarkMode,
                    onThemeChange = onThemeChange,
                    onEditPhotoClick = {
                        navController.navigate(Routes.PROFILE_PHOTO)
                    }
                )
            }

            composable(Routes.PROFILE_PHOTO) {
                ProfileEditPhotoScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // ── DETAIL ──
            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("studentId") { type = NavType.IntType })
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
                val student = students.find { it.id == studentId }

                DetailScreen(
                    student = student,
                    onBack = { navController.popBackStack() },
                    onEdit = { id ->
                        navController.navigate(Routes.editRoute(id))
                    },
                    onDelete = { id ->
                        StudentData.delete(id)
                        students = StudentData.getAll()
                        navController.popBackStack()
                    },
                    onViewGrades = { id ->
                        navController.navigate(Routes.nilaiRoute(id))
                    }
                )
            }

            composable(
                route = Routes.DATA_NILAI,
                arguments = listOf(navArgument("studentId") { type = NavType.IntType })
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
                val student = students.find { it.id == studentId }

                DataNilaiScreen(
                    student = student,
                    onBack = { navController.popBackStack() }
                )
            }

            // ── ADD ──
            composable(Routes.ADD_STUDENT) {
                AddStudentScreen(
                    onSave = { newStudent ->
                        StudentData.add(newStudent)
                        students = StudentData.getAll()
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() },
                    nextId = StudentData.getNextId()
                )
            }

            // ── EDIT ──
            composable(
                route = Routes.EDIT_STUDENT,
                arguments = listOf(navArgument("studentId") { type = NavType.IntType })
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
                val student = students.find { it.id == studentId }

                EditStudentScreen(
                    student = student,
                    onSave = { updatedStudent ->
                        StudentData.update(updatedStudent)
                        students = StudentData.getAll()
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
