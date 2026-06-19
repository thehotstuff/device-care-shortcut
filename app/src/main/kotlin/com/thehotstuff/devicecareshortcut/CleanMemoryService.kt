package com.thehotstuff.devicecareshortcut

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class CleanMemoryService : AccessibilityService() {

    private var cleaningInProgress = false

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != "com.samsung.android.lool") return

        if (cleaningInProgress) return
        cleaningInProgress = true

        Handler(Looper.getMainLooper()).postDelayed({
            tryClickMemory()
        }, 1000)
    }

    private fun tryClickMemory() {
        val root = rootInActiveWindow ?: run {
            cleaningInProgress = false
            return
        }

        // Cerca il nodo "Memoria" o "Memory"
        val memoryNode = findNodeByText(root, listOf("Memoria", "Memory", "RAM"))
        if (memoryNode != null) {
            memoryNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            Handler(Looper.getMainLooper()).postDelayed({
                tryClickClean()
            }, 1500)
        } else {
            cleaningInProgress = false
        }
    }

    private fun tryClickClean() {
        val root = rootInActiveWindow ?: run {
            cleaningInProgress = false
            return
        }

        // Cerca il pulsante "Pulisci ora" o "Clean now"
        val cleanNode = findNodeByText(root, listOf("Pulisci ora", "Clean now", "Pulisci", "Clean"))
        if (cleanNode != null) {
            cleanNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
        cleaningInProgress = false
    }

    private fun findNodeByText(root: AccessibilityNodeInfo, texts: List<String>): AccessibilityNodeInfo? {
        for (text in texts) {
            val nodes = root.findAccessibilityNodeInfosByText(text)
            if (nodes.isNotEmpty()) return nodes[0]
        }
        return null
    }

    override fun onInterrupt() {
        cleaningInProgress = false
    }
}
