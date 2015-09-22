package com.zcj.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * 相册中选择图片或上传图片，同时支持剪裁
 * <p>第一步（初始化方法中）：</p>
 * <p>     private PhotoChooseUtils photoChooseUtils;</p>
 * <p>     photoChooseUtils = new PhotoChooseUtils(getActivity(), this, false);</p>
 * <p>第二步（点击按钮触发）：</p>
 * <p>     photoChooseUtils.show();</p>
 * <p>第三步（onActivityResult回调方法中）：</p>
 * <p>     super.onActivityResult(requestCode, resultCode, data);</p>
 * <p>     String imgPath = photoChooseUtils.onActivityResult(requestCode, resultCode, data);</p>
 * <p>     if (UtilString.isNotBlank(imgPath)) {</p>
 * <p>         house_idcardimg.setImageDrawable(UtilImage.getDrawableByFilePath(imgPath));</p>
 * <p>     }</p>
 */
public class PhotoChooseUtils {

    /**
     * 剪裁后返回
     */
    private static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /**
     * 相机拍照后返回
     */
    private static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    /**
     * 相册选图后返回
     */
    private static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

    private Activity activity;
    private Fragment fragment;
    private boolean cut;// 是否支持剪裁后保存

    private String cutBeforePath;// 拍照保存的路径
    private String cutAfterPath;// 剪裁后的路径

    private PhotoChooseUtils() {

    }

    /**
     * 传递参数
     *
     * @param activity
     * @param fragment 如果是fragment内使用，则传递此参数；如果是activity内使用，则只需要传递activity参数即可。
     * @param cut      是否支持剪裁
     */
    public PhotoChooseUtils(Activity activity, Fragment fragment, boolean cut) {
        this.activity = activity;
        this.fragment = fragment;
        this.cut = cut;
    }

    public void show() {
        cutBeforePath = null;
        cutAfterPath = null;
        AlertDialog imageDialog = new AlertDialog.Builder(activity).setItems(new CharSequence[]{"手机相册", "手机拍照", "返回"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // 相册选图
                        if (item == 0) {
                            startImagePick();
                        }
                        // 手机拍照
                        else if (item == 1) {
                            startActionCamera();
                        }
                    }
                }).create();
        imageDialog.show();
    }

    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GETIMAGE_BYCAMERA:// 相机拍照后返回
                    if (cut) {
                        startActionCrop();
                    } else {
                        return cutBeforePath;
                    }
                    break;
                case REQUEST_CODE_GETIMAGE_BYCROP:// 相册选图后返回
                    if (data != null) {
                        cutBeforePath = UtilImage.getFilePathByUri(activity, data.getData());
                        if (cut) {
                            startActionCrop();
                        } else {
                            return cutBeforePath;
                        }
                    }
                    break;
                case REQUEST_CODE_GETIMAGE_BYSDCARD:// 剪裁后返回
                    if (cut) {
                        return cutAfterPath;
                    }
                    break;
            }
        }
        return null;
    }

    private String initImgPath() {
        return UtilAppFile.getExternalFilesDir(activity) + com.zcj.util.UtilString.getSoleCode() + ".jpg";
    }

    /**
     * 相册选图
     */
    private void startImagePick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (fragment != null) {
            fragment.startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GETIMAGE_BYCROP);
        } else if (activity != null) {
            activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    /**
     * 相机拍照
     */
    private void startActionCamera() {
        cutBeforePath = initImgPath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cutBeforePath)));
        if (fragment != null) {
            fragment.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA);
        } else if (activity != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA);
        }
    }

    /**
     * 裁剪
     */
    private void startActionCrop() {
        cutAfterPath = initImgPath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(cutBeforePath)), "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cutAfterPath)));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);// 输出图片大小
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        if (fragment != null) {
            fragment.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);
        } else if (activity != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);
        }
    }
}
