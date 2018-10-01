package com.szh.wjemspda.buletooth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class ScaleView extends View {
	
	private String sShowName="weight";
	private String sWei="wei";
	private String sUnit="";
	private boolean bZero=false; 
	private boolean bStable=false; 
	private boolean bTare=false; 
	int BackgroundColor= Color.WHITE;
	int FontColor= Color.BLACK;
	
	public ScaleView(Context context, AttributeSet ats, int defStyle) {
		        super(context, ats, defStyle);
		    }
	
	public ScaleView(Context context, AttributeSet ats) {
        super(context, ats);
    }
	public ScaleView(Context context) {
		super(context);
		
		// TODO Auto-generated constructor stub
	}
	public void SetDrawColor(int backgroundColor,int fontColor)
	{
		BackgroundColor=backgroundColor;
		FontColor=fontColor;
		invalidate();
	}
	@Override
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		RectF rect=new RectF();
		rect.left=0;
		rect.top=0;
		rect.right=canvas.getWidth();
		rect.bottom=canvas.getHeight();
		paint.setColor(BackgroundColor);
		canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
		
		
		int fontsize=(int) (canvas.getWidth()*(sUnit.isEmpty()?0.18:0.16));
		
		paint.setColor(FontColor);
		paint.setTextSize((int)(fontsize*0.25));  // 字体大小

		canvas.drawText(sShowName, 5,canvas.getHeight()/4-2, paint);

		if(bZero)canvas.drawText("Zero", 5,canvas.getHeight()/2-2, paint);

		if(bStable)canvas.drawText("Stable", 5,canvas.getHeight()*3/4-2, paint);

		if(bTare)canvas.drawText("Tare", 5,canvas.getHeight()-2, paint);

        paint.setTextSize(fontsize);  // 字体大小
        //paint.setTextScaleX(1); // 字体水平缩放比例
       // paint.setTextSkewX(0);  // 字体倾斜角度
        paint.setTypeface(Typeface.MONOSPACE);
        canvas.drawText(sWei, fontsize*0.8f, canvas.getHeight()/2+fontsize/3, paint);
		
        if(!sUnit.isEmpty())
        {
	        int unitfontsize=(int) (fontsize*1.2/sUnit.length());
	        paint.setTextSize(unitfontsize);
	        paint.setTypeface(Typeface.DEFAULT);
	        int textWidth = getTextWidth(paint,sUnit);
	        if(textWidth>canvas.getWidth()*0.1)unitfontsize=(int) ((canvas.getWidth()*0.10)*unitfontsize/textWidth);
	        paint.setTextSize(unitfontsize);
	        textWidth = getTextWidth(paint,sUnit);
	        canvas.drawText(sUnit, (float) (canvas.getWidth()-textWidth-5), canvas.getHeight()-fontsize/3, paint);
        }
		
	}
	public static int getTextWidth(Paint paint, String str) {
		// TODO Auto-generated method stub
		int iRet = 0;  
        if (str != null && str.length() > 0) {  
            int len = str.length();  
            float[] widths = new float[len];  
            paint.getTextWidths(str, widths);  
            for (int j = 0; j < len; j++) {  
                iRet += (int) Math.ceil(widths[j]);
            }  
        }  
        return iRet;  
	}
	public void setText(String text)
	{
		if(text.length()>8)sWei=text.substring(0,8);
		else sWei=text;
		invalidate();
	}
	public String getText()
	{
		return sWei;
	}

	public void setZero(boolean Zero) {
		bZero = Zero;
	}

	public  void setStable(boolean Stable) {
		bStable = Stable;
	}

	public  void setTare(boolean Tare) {
		bTare = Tare;
	}
	public  void setShowName(String showname) {
		if(showname.length()>8)sShowName = showname.substring(0,8);
		else sShowName = showname;
	}
	public void setUnit(String Unit) {
		if(Unit.length()>4)sUnit = Unit.substring(0,4);
		else sUnit=Unit;
	}
	
}
