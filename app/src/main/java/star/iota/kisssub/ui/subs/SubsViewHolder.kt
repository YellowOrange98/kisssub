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

package star.iota.kisssub.ui.subs

import android.view.View
import kotlinx.android.synthetic.main.item_sub.view.*
import star.iota.kisssub.R
import star.iota.kisssub.base.BaseViewHolder
import star.iota.kisssub.ext.addFragmentToActivity
import star.iota.kisssub.helper.SearchHelper
import star.iota.kisssub.ui.item.search.SearchFragment
import star.iota.kisssub.utils.ViewContextUtils

class SubsViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

    override fun bindView(bean: String) {
        itemView?.apply {
            textViewSub?.text = bean
            textViewSub?.setOnClickListener {
                ViewContextUtils.getAppCompatActivity(this)?.addFragmentToActivity(SearchFragment.newInstance(bean, bean, SearchHelper.getParam(context)), R.id.frameLayoutContainer)
            }
        }
    }

}
