package com.example.arkanoidgame

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder,private val gameView: GameView):Thread() {


    private var canvas: Canvas?=null
    private val FPS=100
    var running=false

    override fun run() {
        var startTime:Long
        var timeMillis:Long
        var waitTime:Long
        val targetTime=(1000/FPS).toLong()
        while(running){
            startTime=System.nanoTime()
            canvas=surfaceHolder.lockCanvas()
            if(canvas==null){
                break
            }
            gameView.draw(canvas)
            gameView.update()
            surfaceHolder.unlockCanvasAndPost(canvas)
            timeMillis=(System.nanoTime()-startTime)/1000000
            waitTime=targetTime-timeMillis
            if(waitTime>=0){
                sleep(waitTime)
            }
        }

    }
}
