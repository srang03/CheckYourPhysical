package com.srang03.checkyourphysical.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srang03.checkyourphysical.R
import kotlinx.android.synthetic.main.item_memo.view.*
import java.text.SimpleDateFormat

class MemoListAdapter (private val list: MutableList<MemoData>)
    : RecyclerView.Adapter<ItemViewHolder> (){

    lateinit var itemClickListener: (itemId: String) -> Unit

    private val dateFormat = SimpleDateFormat("MM/dd HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false)
        view.setOnClickListener {
            itemClickListener?.run{
                val memoId = it.tag as String
                this(memoId)
            }
        }
        return ItemViewHolder(view)
    }

     fun getItemIdString(position: Int): String {
         Log.d("TAG", "${list[position].title}")
        return list[position].id

    }

    fun getItemTitleString(position: Int): String {
        return list[position].title

    }


    override fun getItemCount(): Int {
        return list.count()
    }

    fun getItem(position: Int): String{
        return list[position].id
}

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if(list[position].title.isNotEmpty()){
            holder.containerView.titleView.visibility = View.VISIBLE
            holder.containerView.titleView.text = list[position].title
        }
        else{
            holder.containerView.titleView.visibility = View.GONE
        }
        holder.containerView.summaryView.text = list[position].summary
        holder.containerView.dateView.text = dateFormat.format(list[position].createdAt)
        holder.containerView.tag = list[position].id
    }




}