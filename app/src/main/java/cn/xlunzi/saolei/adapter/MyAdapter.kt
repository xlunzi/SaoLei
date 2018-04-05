package cn.xlunzi.saolei.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cn.xlunzi.saolei.R
import cn.xlunzi.saolei.common.Constant.TYPE_HIDE
import cn.xlunzi.saolei.common.Constant.TYPE_SHOW
import cn.xlunzi.saolei.common.Constant.TYPE_SIGN
import cn.xlunzi.saolei.utils.ColorUtil
import kotlinx.android.synthetic.main.item_num.view.*

/**
 * MyAdapter
 * Created by SunLW on 2018/1/30.
 */

class MyAdapter(private var mContext: Context, private var numList: List<Int>, private var leiList: List<Int>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.item_num, null)
            holder = ViewHolder()
            holder.tvNum = view.tv_num_item
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        // 显示
        when (leiList[position]) {
            TYPE_HIDE -> {
                holder.tvNum.text = ""
                holder.tvNum.background = ColorUtil.BLUE_Drawable
            }
            TYPE_SIGN -> {
                holder.tvNum.text = "雷"
                holder.tvNum.background = ColorUtil.RED_Drawable
            }
            TYPE_SHOW -> {
                // 显示
                val num = numList[position]
                if (num == 0) {
                    holder.tvNum.text = ""
                } else {
                    holder.tvNum.text = num.toString()
                }
                holder.tvNum.background = ColorUtil.WHITE_Drawable
            }
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return numList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return numList.size
    }

    class ViewHolder {
        lateinit var tvNum: TextView
    }
}