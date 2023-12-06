package com.dicoding.todoapp.setting

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.todoapp.R
import androidx.work.*
import com.dicoding.todoapp.notification.NotificationWorker
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Notifications permission granted")
            } else {
                showToast("Notifications will not show without permission")
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification =
                findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { _, newValue ->
                val channelName = getString(R.string.notify_channel_name)
                val workManager = WorkManager.getInstance(requireContext())

                val NOTIFICATION_CHANNEL_ID = "notify-task"

                //TODO 13 : Schedule and cancel daily reminder using WorkManager with data channelName
                if (newValue as Boolean) {
                    // Schedule daily reminder
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    val data = workDataOf(NOTIFICATION_CHANNEL_ID to channelName)

                    val notificationRequest =
                        PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                            .setConstraints(constraints)
                            .setInputData(data)
                            .build()

                    workManager.enqueueUniquePeriodicWork(
                        "dailyReminder",
                        ExistingPeriodicWorkPolicy.REPLACE,
                        notificationRequest
                    )
                } else {
                    // Cancel the existing reminder
                    workManager.cancelUniqueWork("dailyReminder")
                }
                true
            }

        }
    }
}