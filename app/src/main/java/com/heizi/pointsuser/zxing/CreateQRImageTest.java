package com.heizi.pointsuser.zxing;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class CreateQRImageTest
{
	private ImageView sweepIV;
	private int QR_WIDTH = 200, QR_HEIGHT = 200;
	/**
	 * @鏂规硶鍔熻兘璇存槑: 鐢熸垚浜岀淮鐮佸浘鐗?瀹為檯浣跨敤鏃惰鍒濆鍖杝weepIV,涓嶇劧浼氭姤绌烘寚閽堥敊璇?
	 * @浣滆?:楗舵鍕?
	 * @鏃堕棿:2013-4-18涓婂崍11:14:16
	 * @鍙傛暟: @param url 瑕佽浆鎹㈢殑鍦板潃鎴栧瓧绗︿覆,鍙互鏄腑鏂?
	 * @return void
	 * @throws
	 */
	
	//瑕佽浆鎹㈢殑鍦板潃鎴栧瓧绗︿覆,鍙互鏄腑鏂?
	public void createQRImage(String url)
	{
		try
		{
			//鍒ゆ柇URL鍚堟硶鎬?
			if (url == null || "".equals(url) || url.length() < 1)
			{
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//鍥惧儚鏁版嵁杞崲锛屼娇鐢ㄤ簡鐭╅樀杞崲
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			//涓嬮潰杩欓噷鎸夌収浜岀淮鐮佺殑绠楁硶锛岄?涓敓鎴愪簩缁寸爜鐨勫浘鐗囷紝
			//涓や釜for寰幆鏄浘鐗囨í鍒楁壂鎻忕殑缁撴灉
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			//鐢熸垚浜岀淮鐮佸浘鐗囩殑鏍煎紡锛屼娇鐢ˋRGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			//鏄剧ず鍒颁竴涓狪mageView涓婇潰
			sweepIV.setImageBitmap(bitmap);
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
	}
}
