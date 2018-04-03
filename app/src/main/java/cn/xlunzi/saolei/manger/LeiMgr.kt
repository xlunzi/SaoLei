package cn.xlunzi.saolei.manger

import cn.xlunzi.saolei.common.Constant.DEFAULT_NUM
import cn.xlunzi.saolei.common.Constant.DOWN
import cn.xlunzi.saolei.common.Constant.IS_LEI
import cn.xlunzi.saolei.common.Constant.LEFT
import cn.xlunzi.saolei.common.Constant.LEFT_DOWN
import cn.xlunzi.saolei.common.Constant.LEFT_UP
import cn.xlunzi.saolei.common.Constant.RIGHT
import cn.xlunzi.saolei.common.Constant.RIGHT_DOWN
import cn.xlunzi.saolei.common.Constant.RIGHT_UP
import cn.xlunzi.saolei.common.Constant.UP
import java.util.*

/**
 * 扫雷Manger
 * Created by SunLW on 2018-04-01.
 */
object LeiMgr {

    /** 行数 */
    const val ROW: Int = 10
    /** 列数 */
    const val COLUMN: Int = 8

    /** 雷的系数*/
    const val KEY: Float = 0.20f
    /** 总雷数 */
    const val TOTAL: Int = (ROW * COLUMN * KEY).toInt()

    private val random: Random = Random()

    fun start(numList: MutableList<Int>) {
        // 初始化
        numList.forEachIndexed { index, it ->
            when (it) {
                DEFAULT_NUM -> {
                }
                else -> numList[index] = DEFAULT_NUM
            }
        }
        // 生成指定数【TOTAL】的雷
        for (i in 1..TOTAL) {
            random(numList)
        }
    }

    private fun random(numList: MutableList<Int>) {
        val position = random.nextInt(numList.size)
        when (numList[position]) {
            IS_LEI -> random(numList) // 如果已经是雷，重新生成一个
            else -> {
                numList[position] = IS_LEI // 标记
                signNearby(position, numList) // 在雷周边记数
            }
        }
    }

    private fun signNearby(position: Int, numList: MutableList<Int>) {
        // position [0，80)
        // 行
        val row = position / COLUMN + 1 // [1,8]
        // 列
        val col = position % COLUMN + 1 // [1,8]

        // 8 --> 为左上、上、右上、右、右下、下、左下、左，八个方向
        val direction = MutableList(8, { true })
        when (row) { // 行
            1 -> { // 第一行
                direction[LEFT_UP] = false // 左上
                direction[UP] = false // 上
                direction[RIGHT_UP] = false // 右上
            }
            ROW -> { // 最后一行
                direction[RIGHT_DOWN] = false // 右下
                direction[DOWN] = false // 下
                direction[LEFT_DOWN] = false // 左下
            }
        }
        when (col) { // 列
            1 -> { // 第一列
                direction[LEFT_UP] = false // 左上
                direction[LEFT_DOWN] = false // 左下
                direction[LEFT] = false // 左
            }
            COLUMN -> { // 最后一列
                direction[RIGHT_UP] = false // 右上
                direction[RIGHT] = false // 右
                direction[RIGHT_DOWN] = false // 右下
            }
        }

        direction.forEachIndexed { index, b ->
            if (b) {
                var pos: Int = -1
                when (index) {
                    LEFT_UP -> pos = position - COLUMN - 1
                    UP -> pos = position - COLUMN
                    RIGHT_UP -> pos = position - COLUMN + 1
                    RIGHT -> pos = position + 1
                    RIGHT_DOWN -> pos = position + COLUMN + 1
                    DOWN -> pos = position + COLUMN
                    LEFT_DOWN -> pos = position + COLUMN - 1
                    LEFT -> pos = position - 1
                }
                if (pos != -1 && numList[pos] != IS_LEI) {
                    numList[pos]++
                }
            }
        }
    }
}