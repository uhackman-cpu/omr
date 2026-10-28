package com.dev.omr

import android.app.Application
import com.dev.omr.util.LocaleHelper

class OmrApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LocaleHelper.set(this, LocaleHelper.current(this))
    }
}
