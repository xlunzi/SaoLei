package cn.xlunzi.saolei.activity

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
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
import cn.xlunzi.saolei.utils.ColorUtil.BLUE_Drawable
import cn.xlunzi.saolei.utils.ColorUtil.RED_Drawable
import cn.xlunzi.saolei.utils.ColorUtil.TRANSPARENT_Drawable
import cn.xlunzi.saolei.utils.TT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var mAdapter: MyAdapter

    private var leiNum: Int = LeiMgr.TOTAL

    private val numList = MutableList(COLUMN * ROW, { DEFAULT_NUM })
    private val leiList = MutableList(COLUMN * ROW, { TYPE_HIDE })

    private var stop = false

    private var countDownTimer: CountDownTimer? = null

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
            if (!stop) {
                when (leiList[position]) {
                    TYPE_HIDE -> {
                        when (numList[position]) {
                            DEFAULT_NUM -> {
                                leiList[position] = TYPE_SHOW
                                LeiMgr.showZeroNearby(position, numList, leiList)
                            }
                            IS_LEI -> gameOver() // 点到雷，反转并等待
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
        }

        gv_content.setOnItemLongClickListener { parent, view, position, id ->
            if (!stop) {
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
            }
            true
        }

    }

    /** 重置游戏*/
    private fun resetGame() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        stop = false
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
        stop = true
        tv_title.text = "踩到雷了，请等待"
        numList.reverse()
        leiList.reverse()

        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        countDownTimer = object : CountDownTimer(10 * 1000, 1 * 500) {
            override fun onTick(millisUntilFinished: Long) {
                tv_title.background = when (millisUntilFinished / 500 % 2) {
                    0L -> RED_Drawable
                    else -> BLUE_Drawable
                }
                TT.show("还有 $millisUntilFinished 毫秒")
            }

            override fun onFinish() {
                stop = false
                setLeiNum(leiNum)
                TT.show("请继续")
                tv_title.background = TRANSPARENT_Drawable
            }
        }.start()
    }
}
