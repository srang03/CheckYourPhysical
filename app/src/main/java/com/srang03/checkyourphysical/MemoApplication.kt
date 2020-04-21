package com.srang03.checkyourphysical

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import io.realm.Realm

class MemoApplication (): Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("9izt7505i5")
    }
}