package cn.xlunzi.saolei.activity

import android.app.Activity
import android.os.Bundle
import cn.xlunzi.saolei.R
import cn.xlunzi.saolei.adapter.MyAdapter
import cn.xlunzi.saolei.common.Constant.DEFAULT_NUM
import cn.xlunzi.saolei.common.Constant.IS_LEI
import cn.xlunzi.saolei.common.Constant.TYPE_HIDE
import cn.xlunzi.saolei.common.Constant.TYPE_SHOW
import cn.xlunzi.saolei.common.Constant.TYPE_SIGN
import cn.xlunzi.saolei.manger.LeiMgr
import cn.xlunzi.saolei.manger.LeiMgr.COLUMN
import cn.xlunzi.saolei.manger.LeiMgr.ROW
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var mAdapter: MyAdapter

    private var leiNum: Int = LeiMgr.TOTAL

    private val numList = MutableList(COLUMN * ROW, { DEFAULT_NUM })
    private val leiList = MutableList(COLUMN * ROW, { TYPE_HIDE })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gv_content.numColumns = COLUMN

        mAdapter = MyAdapter(this, numList, leiList)
        gv_content.adapter = mAdapter

        resetGame()

        tv_start.setOnClickListener {
            resetGame()
            mAdapter.notifyDataSetChanged()
        }

        gv_content.setOnItemClickListener { parent, view, position, id ->
            when (leiList[position]) {
                TYPE_HIDE -> {
                    when (numList[position]) {
                        IS_LEI -> gameOver() // 点到雷，OVER
                        else -> leiList[position] = TYPE_SHOW
                    }
                }
                TYPE_SIGN -> {
                }
                TYPE_SHOW -> {
                }
            }
            mAdapter.notifyDataSetChanged()
        }

        gv_content.setOnItemLongClickListener { parent, view, position, id ->
            when (leiList[position]) {
                TYPE_HIDE -> {
                    leiList[position] = TYPE_SIGN
                    setLeiNum(leiNum - 1)
                }
                TYPE_SIGN -> {
                    setLeiNum(leiNum + 1)
                    leiList[position] = TYPE_HIDE
                }
                TYPE_SHOW -> {
                }
            }

            mAdapter.notifyDataSetChanged()
            true
        }

    }

    /** 重置游戏*/
    private fun resetGame() {
        leiList.forEachIndexed { index, it ->
            when (it) {
                TYPE_HIDE -> {
                }
                else -> leiList[index] = TYPE_HIDE
            }
        }
        LeiMgr.start(numList)
        setLeiNum(LeiMgr.TOTAL)
    }

    private fun setLeiNum(num: Int) {
        leiNum = num
        val text = "【还有" + num + "个雷】"
        tv_title.text = text
    }

    private fun gameOver() {
        tv_title.text = "游戏结束"
    }
}
