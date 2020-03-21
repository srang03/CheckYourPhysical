package com.srang03.checkyourphysical

import android.app.Application
import io.realm.Realm

class MemoApplication (): Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

    }
}