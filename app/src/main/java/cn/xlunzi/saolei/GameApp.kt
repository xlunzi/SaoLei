package cn.xlunzi.saolei

import android.app.Application
import android.content.Context

/**
 * Created by SunLW on 2018-04-05.
 */
class GameApp : Application() {

    override fun onCreate() {
        super.onCreate()

        context = this
    }

    companion object {
        private lateinit var context: GameApp
        fun getCtx(): GameApp = context
    }
}