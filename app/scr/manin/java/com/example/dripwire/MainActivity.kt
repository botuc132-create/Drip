package com.example.dripwire

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 14, 24)
    private val panel = Color.rgb(17, 29, 43)
    private val cyan = Color.rgb(43, 220, 245)
    private val muted = Color.rgb(153, 174, 193)
    private var connected = false
    private lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        showLogin()
    }

    private fun shape(color: Int, stroke: Int = Color.TRANSPARENT): GradientDrawable = GradientDrawable().apply {
        setColor(color); cornerRadius = dp(18).toFloat()
        if (stroke != Color.TRANSPARENT) setStroke(dp(1), stroke)
    }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun text(value: String, size: Float, color: Int = Color.WHITE, bold: Boolean = false): TextView = TextView(this).apply {
        text = value; textSize = size; setTextColor(color); if (bold) setTypeface(null, Typeface.BOLD)
    }
    private fun base(title: String, subtitle: String? = null): LinearLayout {
        root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(22), dp(24), dp(22), dp(24)); setBackgroundColor(bg) }
        val scroll = ScrollView(this).apply { isFillViewport = true; addView(root) }
        setContentView(scroll)
        root.addView(text("DRIPWIRE", 13f, cyan, true), LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(16) })
        root.addView(text(title, 29f, Color.WHITE, true), LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(7) })
        if (subtitle != null) root.addView(text(subtitle, 14f, muted), LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(22) })
        return root
    }
    private fun button(label: String, primary: Boolean = false, action: () -> Unit): Button = Button(this).apply {
        text = label; isAllCaps = false; textSize = 15f; setTextColor(if (primary) bg else Color.WHITE)
        background = shape(if (primary) cyan else panel, if (primary) cyan else Color.rgb(45, 70, 91))
        setOnClickListener { action() }
    }
    private fun addButton(parent: LinearLayout, label: String, primary: Boolean = false, action: () -> Unit) {
        parent.addView(button(label, primary, action), LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(12) })
    }
    private fun field(hintText: String, secret: Boolean = false): EditText = EditText(this).apply {
        hint = hintText; setHintTextColor(muted); setTextColor(Color.WHITE); textSize = 15f
        setPadding(dp(16), 0, dp(16), 0); background = shape(panel, Color.rgb(45, 70, 91))
        if (secret) inputType = 129
    }
    private fun showLogin() {
        base("Welcome back", "Sign in to the local demo interface")
        val card = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(18), dp(16), dp(18)); background = shape(panel) }
        root.addView(card, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(20) })
        card.addView(text("ACCESS KEY", 12f, cyan, true), LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10) })
        val key = field("Enter demo access key")
        card.addView(key, LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(14) })
        card.addView(text("Demo build: any non-empty key opens the interface. No external account or key server is connected.", 12f, muted))
        root.addView(Space(this), LinearLayout.LayoutParams(1, 0, 1f))
        addButton(root, "CONTINUE", true) { if (key.text.isNotBlank()) showHome() else Toast.makeText(this, "Enter a demo key", Toast.LENGTH_SHORT).show() }
        root.addView(text("Independent UI recreation · local demo", 11f, muted).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, -2))
    }
    private fun showHome() {
        base("Dashboard", "Proxy control panel · interface demo")
        val status = text(if (connected) "●  CONNECTED" else "●  DISCONNECTED", 13f, if (connected) cyan else muted, true)
        val statusCard = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(18), dp(18), dp(18), dp(18)); background = shape(panel) }
        statusCard.addView(text("CONNECTION STATUS", 11f, muted, true))
        statusCard.addView(status, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(10) })
        statusCard.addView(text("This rebuild does not establish a VPN/proxy tunnel.", 12f, muted).apply { setPadding(0, dp(9), 0, 0) })
        root.addView(statusCard, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(18) })
        addButton(root, if (connected) "STOP DEMO" else "START DEMO", true) {
            connected = !connected
            Toast.makeText(this, if (connected) "Demo state switched on" else "Demo state switched off", Toast.LENGTH_SHORT).show()
            showHome()
        }
        val settings = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(12), dp(16), dp(12)); background = shape(panel) }
        settings.addView(text("QUICK SETTINGS", 12f, cyan, true), LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(8) })
        listOf("Auto reconnect", "Show floating control", "Start on launch").forEach { label ->
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
            row.addView(text(label, 14f, Color.WHITE), LinearLayout.LayoutParams(0, dp(50), 1f))
            row.addView(Switch(this).apply { buttonTintList = android.content.res.ColorStateList.valueOf(cyan) })
            settings.addView(row)
        }
        root.addView(settings, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(18) })
        addButton(root, "ABOUT", false) { showAbout() }
        addButton(root, "LOG OUT", false) { connected = false; showLogin() }
    }
    private fun showAbout() {
        base("About", "DripWire-style independent rebuild")
        root.addView(text("This sample recreates a basic dark dashboard and login flow based on visible APK resources. The original app's protected native components, server configuration, VPN engine, licensing and floating overlay are not included.", 15f, muted), LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(24) })
        addButton(root, "BACK", true) { showHome() }
    }
}
