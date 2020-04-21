package com.srang03.checkyourphysical.data

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

class MemoDao (private val realm: Realm){
    fun getAllMemos(): RealmResults<MemoData>{
        return realm.where(MemoData::class.java)
            .sort("createdAt", Sort.DESCENDING)
            .findAll()
    }
    fun selectMemo(id: String): MemoData{
        return realm.where(MemoData::class.java)
            .equalTo("id", id)
            .findFirst() as MemoData
    }

    fun getActiveAlarms(): RealmResults<MemoData>{
        return realm.where(MemoData::class.java)
            .greaterThan("alarmTime", Date())
            .findAll()
    }

    fun addOrUpdateMemo(memoData: MemoData){
        realm.executeTransaction {
          memoData.createdAt = Date()

            if(memoData.content.length > 100)
                memoData.summary = memoData.content.substring(0..100)
            else
                memoData.summary = memoData.content
            if(!memoData.isManaged){
                it.copyToRealm(memoData)
            }
        }
    }
}