/*
 *
 *  *    Copyright 2017. iota9star
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package star.iota.kisssub.ui.rss

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.fragment_default.*
import star.iota.kisssub.R
import star.iota.kisssub.base.BaseFragment
import star.iota.kisssub.room.Record
import star.iota.kisssub.widget.MessageBar


class RssFragment : BaseFragment(), RssContract.View {
    override fun success(items: ArrayList<Record>) {
        end()
        adapter.addAll(items)
    }

    override fun error(e: String?) {
        end()
        MessageBar.create(context!!, e)
    }

    override fun noData() {
        end()
        MessageBar.create(context!!, "没有获得数据")
    }

    companion object {
        val TITLE = "title"
        val URL = "url"
        val SUFFIX = "suffix"
        fun newInstance(title: String, url: String): RssFragment {
            val fragment = RssFragment()
            val bundle = Bundle()
            bundle.putString(URL, url.replace(".xml", ""))
            bundle.putString(SUFFIX, ".xml")
            bundle.putString(TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun end() {
        isLoading = false
        refreshLayout.finishRefreshing()
    }

    override fun getBackgroundView(): ImageView = imageViewContentBackground
    override fun getMaskView(): View = viewMask

    override fun getContainerViewId(): Int = R.layout.fragment_default

    override fun doSome() {
        initBase()
        initPresenter()
        initRecyclerView()
        initRefreshLayout()
    }

    private lateinit var url: String
    private lateinit var suffix: String

    private fun initBase() {
        setToolbarTitle(arguments!!.getString(TITLE, getString(R.string.app_name)))
        url = arguments!!.getString(URL)
        suffix = arguments!!.getString(SUFFIX, "")
    }

    private lateinit var presenter: RssPresenter
    private fun initPresenter() {
        presenter = RssPresenter(this)
    }

    private var isLoading = false
    private fun initRefreshLayout() {
        refreshLayout.startRefresh()
        refreshLayout.setEnableLoadmore(false)
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                if (!isLoading()) {
                    isLoading = true
                    adapter.clear()
                    presenter.get(url + suffix)
                }
            }
        })
    }

    private fun isLoading(): Boolean = if (isLoading) {
        MessageBar.create(context!!, "数据正在加载中，请等待...")
        true
    } else {
        false
    }

    private lateinit var adapter: RssAdapter
    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = LandingAnimator()
        adapter = RssAdapter()
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

}