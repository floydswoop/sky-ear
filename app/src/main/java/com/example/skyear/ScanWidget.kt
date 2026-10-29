package com.example.skyear

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

// Home-screen widget: "Start scan" opens Sky Ear and starts a scan; the name opens the app as usual.
class ScanWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val base = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val open = PendingIntent.getActivity(context, 10, Intent(base), flags)
        val scan = PendingIntent.getActivity(
            context, 11, Intent(base).putExtra(MainActivity.EXTRA_START_SCAN, true), flags
        )
        for (id in ids) {
            val views = RemoteViews(context.packageName, R.layout.widget_scan)
            views.setOnClickPendingIntent(R.id.widget_open, open)
            views.setOnClickPendingIntent(R.id.widget_scan, scan)
            manager.updateAppWidget(id, views)
        }
    }
}
