package com.aleehatech.digitaldetoxchallange

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aleehatech.digitaldetoxchallange.databinding.ActivityMainBinding
import com.aleehatech.digitaldetoxchallange.ui.auth.SigninActivity
import com.aleehatech.digitaldetoxchallange.utils.Utils.promptUserToEnableAccessibilityService
import com.aleehatech.digitaldetoxchallange.utils.Utils.requestUsageAccess
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)



        // Default setup for drawer system
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_stats, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        // navView.setupWithNavController(navController)
        // This was default I override this feature below

        // Handle navigation item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to the HomeFragment
                    navController.navigate(R.id.nav_home)
                    true
                }
                R.id.nav_stats -> {
                    // Navigate to the StatsFragment
                    navController.navigate(R.id.nav_stats)
                    true
                }
                R.id.nav_logout -> {
                    // Call the logout handler
                    handleLogout()
                    true
                }
                else -> {
                    // Let the default NavigationView behavior handle other items
                    menuItem.onNavDestinationSelected(navController) || false
                }
            }
        }


        // Adding profile image to navigation header on the drawer:
        val headerView: View = binding.navView.getHeaderView(0) // Get the header view
        // val profileNameTextView: TextView = headerView.findViewById(R.id.profileName)
        val profileEmailTextView: TextView = headerView.findViewById(R.id.profileEmail)
        val profileImageView: ImageView = headerView.findViewById(R.id.profile_image)

        // Get current user
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        currentUser?.run {
            // Set user display name (if available) or a placeholder
            // profileNameTextView.text = displayName ?: "User"

            // Set user email
            profileEmailTextView.text = email

            // Load profile image if a URL is available (use Picasso or Glide for loading images)
            //photoUrl?.let { photoUri ->
            //  Picasso.get().load(photoUri).placeholder(R.mipmap.ic_launcher_round).into(profileImageView)
            //}
        }





        // Prompt user to enable the Focus Mode Accessibility Service
        promptUserToEnableAccessibilityService(this, FocusModeAccessibilityService::class.java)
        requestUsageAccess(this)





    }

    override fun onResume() {
        super.onResume()
        promptUserToEnableAccessibilityService(this, FocusModeAccessibilityService::class.java)
        requestUsageAccess(this)
    }

    private fun handleLogout() {
        // Log out from Firebase Auth
        FirebaseAuth.getInstance().signOut()

        // Navigate to the Sign-in screen
        val intent = Intent(this, SigninActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
