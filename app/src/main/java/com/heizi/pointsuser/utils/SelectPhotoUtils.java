package com.heizi.pointsuser.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.heizi.mycommon.utils.FileUtils;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mycommon.utils.UtilsLog;
import com.heizi.pointsuser.Constants;
import com.heizi.pointsuser.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;


/**
 * 拍照、选择照片
 *
 * @author admin
 */
public class SelectPhotoUtils extends LinearLayout implements Constants, View.OnClickListener {

    private Context c;
    private Fragment fra;
    private ImageView iv;
    private int PHOTO_PICKED_WITH_DATA;
    private int CAMERA_WITH_DATA;
    private String uploadFileName = "", filePath;
    private Uri imgUri;
    private PopupWindow aPopuwindow;
    private View showView;
    private TextView tv_take_photo, tv_picture, tv_moban;
    private int inSampleSize = 0;//图片压缩大小

    public interface OKClick {
        public void okClick(String path, int type);
    }

    private OKClick okClick;

    /**
     * @param context
     * @param imageview
     * @param PHOTO_PICKED_WITH_DATA 图片选择
     * @param CAMERA_WITH_DATA       相机
     *                               type:0：activity,1:Fragment
     */
    public SelectPhotoUtils(Context context, Fragment fra, ImageView imageview,
                            int PHOTO_PICKED_WITH_DATA, int CAMERA_WITH_DATA) {
        super(context);
        c = context;
        iv = imageview;
        this.PHOTO_PICKED_WITH_DATA = PHOTO_PICKED_WITH_DATA;
        this.CAMERA_WITH_DATA = CAMERA_WITH_DATA;
        this.fra = fra;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        if (arg0 == tv_take_photo) {//拍照
            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) { // 判断是否有SD卡
                if (fra == null)
                    takePhoto();
                else
                    takePhotoF(fra);
            } else
                Utils.toastShow(c, "没有SD卡!");

            aPopuwindow.dismiss();
        } else if (arg0 == tv_picture) {
            try {
                Intent getImage = new Intent(Intent.ACTION_PICK);
                getImage.setType("image/*");
                if (fra == null)
                    ((Activity) c).startActivityForResult(getImage, PHOTO_PICKED_WITH_DATA);
                else
                    fra.startActivityForResult(getImage, PHOTO_PICKED_WITH_DATA);
            } catch (Exception e) {
                Utils.toastShow(c, "找不到手机相册!");
            }
            aPopuwindow.dismiss();
        }
    }


    /**
     * 弹出照片选择框dialog(这个方法放弃，用下面的ShowPop方法)
     */
    public void doPickPhotoAction() {
        Context context = c;
        final Context dialogContext = new ContextThemeWrapper(context,
                android.R.style.Theme_Light);
        String[] choices;
        choices = new String[2];
        choices[0] = c.getString(R.string.take_photo); // 拍照
        choices[1] = c.getString(R.string.pick_photo); // 从相册中选择
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
                android.R.layout.simple_list_item_1, choices);
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                dialogContext);
        builder.setTitle(R.string.attach_to_contact);
        builder.setSingleChoiceItems(adapter, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0: { // 拍照
                                String status = Environment
                                        .getExternalStorageState();
                                if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                                    takePhoto();
                                } else {
                                    Utils.toastShow(c, "没有SD卡!");
                                }
                                break;
                            }
                            case 1: // 从相册中去获取
                                try {
                                    // Intent getImage = new
                                    // Intent(Intent.ACTION_GET_CONTENT);
                                    // getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                    Intent getImage = new Intent(Intent.ACTION_PICK);
                                    // getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                    getImage.setType("image/*");
                                    ((Activity) c).startActivityForResult(getImage,
                                            PHOTO_PICKED_WITH_DATA);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    Utils.toastShow(c, "手机上没有相册!");
                                }
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 弹出照片选择框popupwoindow
     * type:popupwindow背景样式（1箭头靠左0还是靠右2居中）
     */
    public void ShowPop(View v, int type) {
        showView = ((LayoutInflater) c
                .getSystemService(c.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.pw_select_photo, null);
        tv_picture = (TextView) showView.findViewById(R.id.tv_picture);
        tv_picture.setOnClickListener(this);
        tv_take_photo = (TextView) showView.findViewById(R.id.tv_take_photo);
        tv_take_photo.setOnClickListener(this);
        if (type == 0)
            showView.setBackgroundResource(R.mipmap.choosearea_bg_right);
        else if (type == 1)
            showView.setBackgroundResource(R.mipmap.choosearea_bg_left);
        else if (type == 2)
            showView.setBackgroundResource(R.mipmap.choosearea_bg_mid);
        aPopuwindow = new PopupWindow(showView, 390, LayoutParams.WRAP_CONTENT, true);
        aPopuwindow.setBackgroundDrawable(new BitmapDrawable());//背景
        //aPopuwindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_dish_bg));
        aPopuwindow.setFocusable(true);
        aPopuwindow.update();
        aPopuwindow.showAsDropDown(v, 10, 0);// 位置
    }

    //从底部浮出
    public void ShowPop1(View v) {
        showView = ((LayoutInflater) c
                .getSystemService(c.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.pw_select_photo1, null);
        tv_picture = (TextView) showView.findViewById(R.id.tv_picture);
        tv_picture.setOnClickListener(this);
        tv_take_photo = (TextView) showView.findViewById(R.id.tv_take_photo);
        tv_take_photo.setOnClickListener(this);
        TextView btn_cancle = (TextView) showView.findViewById(R.id.tv_cancle);
        btn_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                aPopuwindow.dismiss();
            }
        });
        aPopuwindow = new PopupWindow(showView,
                LayoutParams.FILL_PARENT,
                LayoutParams.MATCH_PARENT, true);
        aPopuwindow.setAnimationStyle(R.style.popwin_anim_style);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        aPopuwindow.setFocusable(true);
        // 设置允许在外点击消失
        aPopuwindow.setOutsideTouchable(false);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        aPopuwindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.prompt_popupwindow_bg));
        // 软键盘不会挡着popupwindow
        aPopuwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置菜单显示的位置
        aPopuwindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }



    public void setOkClick(OKClick click) {
        this.okClick = click;
    }

    /**
     * 拍照
     */
    public void takePhoto() {
        Intent getImageByCamera = new Intent(
                "android.media.action.IMAGE_CAPTURE");
        // 图片存储路径，可自定义
        String pathString = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + "DCIM"
                + File.separator
                + "camera";//
        File dir = new File(pathString);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File tmpFile = null;
        if (Utils.checkSDCard()) {
            uploadFileName = System.currentTimeMillis() + ".jpg";
            tmpFile = new File(pathString, uploadFileName);
            filePath = pathString + "/" + uploadFileName;
            // 获取这个图片的URI
            Uri originalUri = Uri.fromFile(tmpFile);// 这是个实例变量，方便下面获取图片的时候用
            imgUri = originalUri;
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
            ((Activity) c).startActivityForResult(getImageByCamera,
                    CAMERA_WITH_DATA);
        } else {
            Utils.toastShow(c, "没有SD卡!");
        }
    }

    /**
     * fragement 中拍照
     *
     * @param f
     */
    public void takePhotoF(Fragment f) {
        Intent getImageByCamera = new Intent(
                "android.media.action.IMAGE_CAPTURE");
        // 图片存储路径，可自定义
        String pathString = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + "DCIM"
                + File.separator
                + "camera";//
        File dir = new File(pathString);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File tmpFile = null;
        if (Utils.checkSDCard()) {
            uploadFileName = System.currentTimeMillis() + ".jpg";
            tmpFile = new File(pathString, uploadFileName);
            filePath = pathString + "/" + uploadFileName;
            // 获取这个图片的URI
            Uri originalUri = Uri.fromFile(tmpFile);// 这是个实例变量，方便下面获取图片的时候用
            imgUri = originalUri;
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
            f.startActivityForResult(getImageByCamera,
                    CAMERA_WITH_DATA);
        } else {
            Utils.toastShow(c, "没有SD卡!");
        }
    }

    public String getFileName() {
        return uploadFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    /**
     * 删除图片
     *
     * @param img
     */
    public void initCommentImage(ImageView img) {
        img.setTag("0");
        img.setImageResource(R.mipmap.photo);
    }

    /**
     * 拍照或者相册选择的图片显示到界面上
     *
     * @param img
     * @param url
     */
    public void initPhotoImg(Bitmap img, String url) {
        if (img == null) {
            return;
        }
        iv.setTag("1");
        // iv.setScaleType(ScaleType.FIT_XY);
        iv.setImageBitmap(img);
    }
    /**
     * 圆角----内存溢出
     *
     * @param source
     * @param radius
     * @return
     */
//	public Bitmap roundCorners(final Bitmap source, final float radius) {
//		try {
//			int width = source.getWidth();
//			int height = source.getHeight();
//			Paint paint = new Paint();
//			paint.setAntiAlias(true);
//			paint.setColor(android.graphics.Color.WHITE);
//			Bitmap clipped = Bitmap.createBitmap(width, height,
//					Bitmap.Config.ARGB_8888);
//			Canvas canvas = new Canvas(clipped);
//			canvas.drawRoundRect(new RectF(0, 0, width, height), radius,
//					radius, paint);
//			paint.setXfermode(new PorterDuffXfermode(
//					android.graphics.PorterDuff.Mode.SRC_IN));
//			canvas.drawBitmap(source, 0, 0, paint);
//			source.recycle();
//			return clipped;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return source;
//	}
/**
 * activity中用法
 */
    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // // TODO Auto-generated method stub
    // if (resultCode == RESULT_OK) {
    // if (data != null) {
    // Bundle bundle = data.getExtras();
    // if (requestCode == PHOTO_PICKED_WITH_DATA) {// 从相册选择图片
    // try {
    // Uri uri = data.getData();
    // String[] proj = { MediaStore.Images.Media.DATA };
    // Cursor cursor = managedQuery(uri, proj, null, null,
    // null);
    // int column_index = cursor
    // .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    // cursor.moveToFirst();
    // filePath = cursor.getString(column_index);
    // uploadFileName = filePath.substring(
    // filePath.lastIndexOf("/") + 1,
    // filePath.length());
    //
    // double size = new NewUtils().getFileSize(UserAct.this,
    // filePath);
    // int opt = 0;
    // if (size <= 1) {
    // opt = 2;
    // } else {
    // opt = 5;
    // }
    // initPhotoImg(
    // rotaingImageView(readPictureDegree(filePath),
    // BitmapFactory.decodeFile(filePath,
    // getOptions(opt))), filePath);
    // } catch (Exception e) {
    // }
    // }
    // }
    // if (requestCode == CAMERA_WITH_DATA) {// 相机
    // filePath=this.getFilePath();
    // imgUri=this.getImgUri();
    // try {
    // if (filePath != null && imgUri != null) {
    // double size = new NewUtils().getFileSize(UserAct.this,
    // filePath);
    // int opt = 0;
    // if (size <= 1) {
    // opt = 2;
    // } else {
    // opt = 5;
    // }
    // initPhotoImg(
    // rotaingImageView(readPictureDegree(filePath),
    // BitmapFactory.decodeFile(filePath,
    // getOptions(opt))), filePath);
    // }
    // } catch (Exception e) {
    // }
    // }
    // }
    // }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     */

//	public Bitmap rotaingImageView(int angle, Bitmap bitmap) {
//		
//		// 旋转图片 动作
//		Matrix matrix = new Matrix();
//		matrix.postRotate(angle);
//		// 创建新的图片
//		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		return resizedBitmap;
//	}
    public Bitmap rotaingImageView(int degree, Bitmap bm) {
        UtilsLog.d("====", "degree===" + degree + "=height==" + bm.getHeight());
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            UtilsLog.d("====", "degree===null");
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 图片缩小
     *
     * @param sampleSize
     * @return
     */
    public BitmapFactory.Options getOptions(int sampleSize) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        opt.inSampleSize = sampleSize;// 图片大小为原图的sampleSize分之一
        opt.inJustDecodeBounds = false;

        return opt;
    }

    /**
     * 压缩图片
     *
     * @param fromFile 要压缩的图片的地址
     * @param toFile   压缩之后的图片的地址
     * @param quality  压缩的图片的质量 0-100 imgType 1:jpg 2:png 3:gif sampleSize:原图的几分之一
     */
    public void transImage(String fromFile, String toFile, int quality,
                           int imgType, int sampleSize) {
        try {

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // Bitmap bitmap = BitmapFactory.decodeFile(fromFile, options);
            // int bitmapWidth = bitmap.getWidth();
            // int bitmapHeight = bitmap.getHeight();
            // Calculate inSampleSize
            options.inSampleSize = sampleSize;// 这里最好做个判断当图片大于1.5M
            // options.inSampleSize =
            // 5
            // 小于就按照计算方法按照图片来计算options.inSampleSize
            // options.inSampleSize = calculateInSampleSize(options,
            // -1,128*128);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            // 产生缩放后的Bitmap对象
            SoftReference<Bitmap> resizeBitmap = new SoftReference<Bitmap>(
                    BitmapFactory.decodeFile(fromFile, options));
            // save file
            File myCaptureFile = new File(toFile);
            FileOutputStream localFileOutputStream = getFileOutputStream(myCaptureFile);
            BufferedOutputStream out = new BufferedOutputStream(
                    localFileOutputStream);
            if (imgType == 1) {
                if (resizeBitmap.get().compress(Bitmap.CompressFormat.JPEG,
                        quality, out)) {
                    out.flush();
                    out.close();
                }
            } else if (imgType == 2) {
                if (resizeBitmap.get().compress(Bitmap.CompressFormat.PNG,
                        quality, out)) {
                    out.flush();
                    out.close();
                }
            }

            if (resizeBitmap != null) { // 记得释放资源，否则会内存溢出
                if (resizeBitmap.get() != null
                        && !resizeBitmap.get().isRecycled()) {
                    resizeBitmap.get().recycle();
                    resizeBitmap = null;
                }

            }
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public FileOutputStream getFileOutputStream(File paramFile) {
        try {
            return new FileOutputStream(paramFile);
        } catch (FileNotFoundException localFileNotFoundException) {
            // LogUtils.e(localFileNotFoundException);
            return null;
        }
    }

    /**
     * 压缩图片，并把压缩后的图片保存到ALITAIL_FOLDER文件夹里
     *
     * @param systemPath
     * @param imgPath
     * @param fileName
     * @return
     */
    public String getUrl(Context c, String systemPath, String imgPath,
                         String fileName) {
        String imgUrl = "";
        String suffixImg = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length());
        int imgType = 1;
        if (suffixImg.contains("png")) {
            imgType = 2;
        }
        if (isPicOrNot(imgPath)) {
            if (!fileName.contains(ALITAIL_FOLDER)) {
                fileName = ALITAIL_FOLDER + fileName;
                double size = FileUtils.getFileSize(c, imgPath);
                int opt = 0;
                if (size <= 1) {
                    opt = 2;
                } else {
                    opt = 5;
                }
                //d
                transImage(imgPath, systemPath + "/" + fileName, 60, imgType,
                        opt);
            }
            imgUrl = systemPath + "/" + fileName;
            UtilsLog.d("====", "imgurl===" + imgUrl);
            return imgUrl;
        }
        return "";
    }

    /**
     * 判断是否是图片
     *
     * @param fileName
     * @return
     */
    public static boolean isPicOrNot(String fileName) {
        if (fileName.contains(".")) {
            fileName = fileName.substring(fileName.lastIndexOf(".") + 1);// "jpeg",
            // "gif",
            fileName = fileName.toLowerCase(); // "png"
            if (fileName.trim().compareTo("jpg") == 0
                    || fileName.trim().compareTo("jpeg") == 0
                    || fileName.trim().compareTo("png") == 0) {
                return true;
            }
        }
        return false;
    }


}
