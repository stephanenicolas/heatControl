package com.github.stephanenicolas.heatcontrol.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import butterknife.BindView
import com.github.stephanenicolas.heatcontrol.R
import toothpick.Scope
import toothpick.Toothpick

class HeatControlActivity : AppCompatActivity() {

    @BindView(R.id.content)
    lateinit var contentView: FrameLayout

    lateinit var activityScope: Scope

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showControlHeatView()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                showSettingsView()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun showSettingsView(): View {
        contentView.removeAllViews()
        val settingsView = SettingsView(this)
        Toothpick.inject(settingsView, activityScope)
        contentView.addView(settingsView)
        return settingsView
    }

    private fun showControlHeatView(): View {
        contentView.removeAllViews()
        val controlHeatView = ControlHeatView(this)
        Toothpick.inject(controlHeatView, activityScope)
        contentView.addView(controlHeatView)
        return controlHeatView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityScope = Toothpick.openScopes(getApplication(), this)
        setContentView(R.layout.activity_heat_control)

        contentView = findViewById(R.id.content) as FrameLayout
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        showControlHeatView()
    }
}
