package com.srang03.checkyourphysical.data

import androidx.lifecycle.ViewModel
import io.realm.Realm

class ListViewModel: ViewModel(){
    private val realm: Realm by lazy{
        Realm.getDefaultInstance()
    }

     val memoDao: MemoDao by lazy {
        MemoDao(realm)
    }

    val memoLiveData: RealmLiveData<MemoData> by lazy{
        RealmLiveData<MemoData> (memoDao.getAllMemos())
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }

//    private val memos: MutableList<MemoData> = mutableListOf()
//    val memoLiveData: MutableLiveData<MutableList<MemoData>> by lazy{
//        MutableLiveData<MutableList<MemoData>>().apply{
//            value = memos
//        }
//    }
//
//    fun addMemo(data: MemoData){
//        val tempList = memoLiveData.value
//        tempList?.add(data)
//        memoLiveData.value = tempList
//    }
}