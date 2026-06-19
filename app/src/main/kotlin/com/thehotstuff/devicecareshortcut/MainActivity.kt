package com.thehotstuff.devicecareshortcut

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val packages = listOf(
            Pair("com.samsung.android.lool", "com.samsung.android.lool.DeviceCareActivity"),
            Pair("com.samsung.android.sm", "com.samsung.android.sm.ui.cstyleboard.DeviceCareDashBoardActivity")
        )

        var launched = false
        for ((pkg, cls) in packages) {
            try {
                val intent = Intent().apply {
                    component = ComponentName(pkg, cls)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                launched = true
                break
            } catch (e: Exception) {
                continue
            }
        }

        if (!launched) {
            Toast.makeText(this, "Schermata non trovata su questo dispositivo", Toast.LENGTH_LONG).show()
        }

        finish()
    }
}
