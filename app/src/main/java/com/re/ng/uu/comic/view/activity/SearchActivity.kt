package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell
import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.config.Constant
import com.re.ng.uu.comic.http.bean.SearchHistoryBean
import com.re.ng.uu.comic.http.bean.rv_cell.SearchHistoryCell
import com.re.ng.uu.comic.util.StartActUtil
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_search.*
import org.litepal.LitePal
import java.util.*

class SearchActivity : BaseActivity() {

    lateinit var mIvBack: ImageView
    lateinit var mIvSearch: ImageView
    lateinit var mEtInput: EditText
    lateinit var mTagFlowLayout: TagFlowLayout
    lateinit var mRvHistory: RecyclerView
    lateinit var mHistoryAdapter: RvSimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        initListener()
        setHistoryList()
    }

    protected fun initView() {
        mIvBack = iv_back_act_search
        mIvSearch = iv_right_act_search
        mEtInput = et_input_act_search
        mTagFlowLayout = tfl_hot_keys_act_search
        mRvHistory = rv_history_act_search
        mHistoryAdapter = RvSimpleAdapter()
        mRvHistory.setLayoutManager(LinearLayoutManager(this))
        mRvHistory.setAdapter(mHistoryAdapter)
    }

    protected fun initListener() {
        mIvBack.setOnClickListener { onBackPressed() }
        mIvSearch.setOnClickListener {
            if (!TextUtils.isEmpty(getString(mEtInput))) {
                StartActUtil.toSearchResultAct(
                    this@SearchActivity,
                    getString(mEtInput),
                    getString(mEtInput),
                    Constant.TYPE_SEARCH
                )
                var p = -1
                for (j in mHistoryAdapter.data.indices) {
                    val searchHistoryCell: SearchHistoryCell = mHistoryAdapter.data[j] as SearchHistoryCell
                    if (searchHistoryCell.mData.getKey().equals(getString(mEtInput))) {
                        p = j
                        break
                    }
                }
                if (p != -1) {
                    mHistoryAdapter.remove(p)
                }
                Log.d("TTT", "add history -> p=$p")
                var historyBean = SearchHistoryBean(getString(mEtInput))
                save(historyBean)
                mHistoryAdapter.add(0, createCell(historyBean))
            }
        }
    }

    private fun save(data: SearchHistoryBean){
        var result = LitePal.where("key = ?",data.key)
            .findFirst(SearchHistoryBean::class.java)
        if(result == null){
            data.save()
        }
    }

    fun setHistoryList() {
        val list = LitePal.findAll(SearchHistoryBean::class.java)
        mHistoryAdapter.clear()
        val cellList: MutableList<BaseRvCell<*>> = ArrayList()
        for (bean in list) {
            val cell: BaseRvCell<*> = createCell(bean)
            cellList.add(cell)
        }
        mHistoryAdapter.addAll(cellList)
//        LitePal.deleteAll(SearchHistoryBean::class.java)
    }

    private fun createCell(bean: SearchHistoryBean): BaseRvCell<*> {
        val cell: BaseRvCell<*> = SearchHistoryCell(bean)
        cell.setListener(object : OnClickViewRvListener {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.iv_delete_cell_search_history -> {
                        (mHistoryAdapter.data[position] as SearchHistoryCell).mData.delete()
                        mHistoryAdapter.remove(position)
                    }
                }
            }

            override fun <C> onClickItem(data: C, position: Int) {
                StartActUtil.toSearchResultAct(
                    this@SearchActivity,
                    bean.key,
                    bean.key,
                    Constant.TYPE_SEARCH
                )
            }
        })
        return cell
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
