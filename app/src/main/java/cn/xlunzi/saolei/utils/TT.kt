package cn.xlunzi.saolei.utils

import android.widget.Toast
import cn.xlunzi.saolei.GameApp

/**
 * 单例 Toast
 * Created by SunLW on 2018-04-05.
 */
object TT {

    fun show(s: String) {
        Inner.toast.setText(s)
        Inner.toast.show()
    }

    object Inner {
        val toast: Toast = Toast.makeText(GameApp.getCtx(), "", Toast.LENGTH_SHORT)
    }
}