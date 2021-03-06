/*
 *
 *  *    Copyright 2018. iota9star
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

package star.iota.kisssub.ui.history

import com.lzy.okgo.model.Response
import org.jsoup.Jsoup
import star.iota.kisssub.KisssubUrl
import star.iota.kisssub.base.StringContract
import star.iota.kisssub.base.StringPresenter


class HistoryPresenter(view: StringContract.View<ArrayList<HistoryBean>>) : StringPresenter<ArrayList<HistoryBean>>(view) {
    override fun deal(resp: Response<String>): ArrayList<HistoryBean> {
        val items = Jsoup.parse(resp.body())?.select("#bgm-nav > ul.module > li > a")
        val list = ArrayList<HistoryBean>()
        items?.forEach {
            val bean = HistoryBean()
            bean.url = KisssubUrl.BASE + it?.attr("href")
            bean.title = it?.text()
            list.add(bean)
        }
        return list
    }
}
