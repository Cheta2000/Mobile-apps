package com.example.arkanoidgame

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random


class GameView(context: Context,attributeSet: AttributeSet): SurfaceView(context,attributeSet),SurfaceHolder.Callback {

    private val BLOCKS=20
    private var level=1
    private var lineX=0.0F
    private var lineY=0.0F
    private val LINE_SIZE=150F
    private var ballX=0F
    private var ballY=0F
    private val BALL_SIZE=40F
    private var dx=5F
    private var dy=5F
    private var vector=sqrt(dx*dx+dy*dy)
    private var rectangles= arrayListOf<Block>()
    private val thread: GameThread
    private lateinit var textView:TextView
    private lateinit var progressDAO: ProgressDAO

    init{
        holder.addCallback(this)
        thread= GameThread(holder,this)
    }

    private fun generate(level:Int) {
        var x=0
        var y=100
        var sizeX=width/10
        var sizeY=sizeX-30
        for (i in 1..level * BLOCKS) {
            if ((i - 3) % 10 != 0 && (i - 7) % 10 != 0) {
                val rectPaint = Paint().apply {
                    setARGB(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
                }
                val rect = Rect(x, y, x + sizeX, y + sizeY)
                val block = Block(rect, rectPaint)
                rectangles.add(block)
            }
            if (i % 10 == 0) {
                x = 0
                y += sizeY
            } else {
                x += sizeX
            }
        }
        ballX=100F+Random.nextInt(width-200)
        ballY=y+100F
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!!.action==MotionEvent.ACTION_MOVE) {
            lineX = event.x
        }
        return true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.running=false
        (context as Activity).finish()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                val db = Room.databaseBuilder(context, MyDatabase::class.java, "Game.db").build()
                progressDAO = db.progressDAO()
            } catch (e: Exception) {
                Log.i("exception", e.toString())
            }
            GlobalScope.launch {
                progressDAO.deleteAll()
            }
            textView = (context as Activity).findViewById(R.id.textView3)
            textView.text = "LEVEL $level"
            generate(level)
            lineX = width / 2F
            lineY = 9 / 10F * height
            thread.start()
            thread.running=true
    }
    
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if(canvas==null){
            return
        }
        val ballPaint= Paint().apply{
            setARGB(255,0,0,255)
        }
        val linePaint=Paint().apply{
            setARGB(255,0,255,0)
            strokeWidth=10F
        }
        var borderPaint=Paint().apply {
            setARGB(255,0,0,0)
            strokeWidth=8F
            style=Paint.Style.STROKE
        }

        canvas.drawOval(ballX,ballY,ballX+BALL_SIZE,ballY+BALL_SIZE,ballPaint)
        canvas.drawLine(lineX-LINE_SIZE/2,lineY,lineX+LINE_SIZE/2,lineY,linePaint)
        for(block in rectangles) {
            canvas.drawRect(block.rect,block.paint)
            canvas.drawRect(block.rect,borderPaint)
        }
    }

    fun update(){
        ballX+=dx
        ballY+=dy
        if(rectangles.isEmpty()){
            if(level==7) {
                val myIntent= Intent()
                myIntent.putExtra("result",level)
                (context as Activity).setResult(RESULT_OK,myIntent)
                (context as Activity).finish()
            }
            else {
                reset()
            }
            return
        }
        if(checkLineCollision()){
            val newX=Random.nextFloat()*1.6*vector-0.8*vector
            val newY=sqrt(vector*vector-newX*newX)
            if(dy>0){
                dy=-newY.toFloat()
            }
            else{
                dy=newY.toFloat()
            }
            dx=newX.toFloat()
        }
        if(ballX<=0 || ballX+BALL_SIZE>=width){
            dx=-dx
        }
        if(ballY<=0){
            dy=-dy
        }
        if(ballY+BALL_SIZE>=height){
            val myIntent= Intent()
            var score:Int
            GlobalScope.launch {
                score=progressDAO.countScore()
                myIntent.putExtra("result",score)
                (context as Activity).setResult(RESULT_OK,myIntent)
                (context as Activity).finish()
            }
        }
        val collision=checkBlockCollisionX()
        if(collision.first!=null){
            val progress=Progress(null,level,collision.first!!.rect.left,collision.first!!.rect.top,collision.first!!.paint.color.toString())
            GlobalScope.launch {
                progressDAO.insertAll(progress)
            }
            rectangles.remove(collision!!.first)
            if(collision.second==0) {
                dy = -dy
            }
            else{
                dx=-dx
            }
        }
    }

    private fun reset() {
        level++
        (context as Activity).runOnUiThread {
            textView.text="LEVEL $level"
        }
        lineX=width/2F
        lineY=9/10F*height
        dx=7F+5*(level-1)
        dx=7F+5*(level-1)
        vector=sqrt(dx*dx+dy*dy)
        generate(level)
    }

    private fun checkBlockCollisionX(): Pair<Block?,Int> {
       for(block in rectangles){
            var distX=abs(ballX+BALL_SIZE/2-block.rect.centerX())-block.rect.width()/2
            var distY=abs(ballY+BALL_SIZE/2-block.rect.centerY())-block.rect.height()/2
            if(distX <= BALL_SIZE/2 && distY <= 0 || distY <= BALL_SIZE/2 && distX <= 0 || distX*distX+distY*distY<=BALL_SIZE*BALL_SIZE/4){
                return if(distX<distY) {
                    Pair(block, 0)
                } else{
                    Pair(block,1)
                }
            }
        }
        return Pair(null,2)
    }

    private fun checkLineCollision() :Boolean {
        var distX=abs(ballX+BALL_SIZE/2-lineX) -LINE_SIZE/2
        var distY=lineY-ballY
        if(dy>0 && distY>=0 && distY<=BALL_SIZE && distX<=BALL_SIZE/2){
            return true
        }
        return false
    }
}