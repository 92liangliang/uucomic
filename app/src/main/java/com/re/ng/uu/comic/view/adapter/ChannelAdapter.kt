package com.re.ng.uu.comic.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.http.bean.ChannelBeen
import kotlinx.android.synthetic.main.cell_chanel_bean.view.*

class ChannelAdapter(val context: Context, val dataList: List<ChannelBeen>) :
    RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    lateinit var selected: ChannelBeen
    private var viewList = ArrayList<View>()

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_chanel_bean, parent, false)
        viewList.add(view.findViewById<RelativeLayout>(R.id.ll_alipay))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(dataList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(data: ChannelBeen) {
            itemView.text_name.text = "${data.title}"
//            if (data.switch == 1) {
//                itemView.visibility = View.VISIBLE
                if (data.click) {
                    selected = data
                    itemView.ll_alipay.isSelected = true
                } else {
                    itemView.ll_alipay.isSelected = false
                }
                itemView.ll_alipay.setOnClickListener {
                    if (!itemView.ll_alipay.isSelected) {
                        clearAll()
                        itemView.ll_alipay.isSelected = true
                        data.click = true
                        selected = data
                    }
                }
//            } else {
//                itemView.visibility = View.GONE
//            }
        }
    }

    private fun clearAll() {
        viewList.forEach {
            it.isSelected = false
        }
        dataList.forEach {
            it.click = false
        }
    }

}
