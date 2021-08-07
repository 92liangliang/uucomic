package com.re.ng.uu.comic.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.http.bean.RechargeMoney
import kotlinx.android.synthetic.main.list_item_vip_money.view.*

class VipMoneyAdapter(val context: Context, val dataList: List<RechargeMoney>) : RecyclerView.Adapter<VipMoneyAdapter.ViewHolder>() {

    lateinit var selected: RechargeMoney
    private var viewList = ArrayList<View>()

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_vip_money, parent, false)
        viewList.add(view.findViewById<View>(R.id.rl_container))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(dataList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(data: RechargeMoney) {
            itemView.tv_show.text = "${data.show}"
            itemView.tv_value.text = "${data.value}"
            itemView.tv_desc.text = "${data.desc}"
            if(data.isSelected){
                selected = data
                itemView.rl_container.isSelected = true
            }else{
                itemView.rl_container.isSelected = false
            }
            itemView.rl_container.setOnClickListener {
                if(!itemView.rl_container.isSelected){
                    clearAll()
                    itemView.rl_container.isSelected = true
                    data.isSelected = true
                    selected = data
                }
            }
        }
    }

    private fun clearAll(){
        viewList.forEach {
            it.isSelected = false
        }
        dataList.forEach {
            it.isSelected = false
        }
    }

}
