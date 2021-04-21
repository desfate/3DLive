package com.futrtch.live.mvvm.vm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.beans.AccountInfoBean;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.repository.EditRepository;
import com.futrtch.live.mvvm.repository.LoginRepository;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.login.TCUserMgr;

import java.io.File;

public class EditViewModel extends ViewModel {

    private static final int CROP_CHOOSE = 10;
    private static final int CAPTURE_IMAGE_CAMERA = 100;
    private static final int IMAGE_STORE = 200;

    private Uri mAvatarPicUri, mCropAvatarPicUri; // 头像 Uri 、裁剪头像的 Uri

    Context context;
    EditRepository repository;

    AccountInfoBean accountInfoBean = new AccountInfoBean(); //                 用于请求的数据集
    MutableLiveData<AccountInfoBean> accountInfo = new MutableLiveData<>();  // 同步服务器的数据集



    EditViewModel(Context context){
        this.context = context;
        repository = new EditRepository(((MVVMActivity)context));
        accountInfoBean.setNickname(LoginRepository.getInstance().getLoginInfo().getmUserName());
        accountInfoBean.setUserid(LoginRepository.getInstance().getUserId());
        accountInfo.postValue(accountInfoBean);
    }

    /**
     * 暂时只支持修改名称
     * @param name 用户名
     */
    public void requestEditUserInfoName(String name){
        accountInfoBean.setSex(1);
        accountInfoBean.setAvatar("");
        accountInfoBean.setFrontcover("");
        accountInfoBean.setNickname(name);
        accountInfoBean.setUserid(LoginRepository.getInstance().getUserId());
        repository.editAccountInfo(accountInfoBean);
    }


    public void selectPicture(){
        mAvatarPicUri = createCoverUri("_select_icon");
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        ((MVVMActivity)context).startActivityForResult(intent_album, IMAGE_STORE);
    }

    public void takePicture(){
        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mAvatarPicUri = createCoverUri("_icon");
        if (mAvatarPicUri != null) {
            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, mAvatarPicUri);
        }
        ((MVVMActivity)context).startActivityForResult(intent_photo, CAPTURE_IMAGE_CAMERA);
    }


    /**
     * 创建封面图片的uri
     *
     * @param type 要创建的URI类型
     *             _icon ：通过相机拍摄图片
     *             _select_icon ： 从文件获取图片文件
     * @return 返回uri
     */
    private Uri createCoverUri(String type) {
        String filename = TCUserMgr.getInstance().getUserId() + type + ".jpg";
        File sdcardDir = context.getExternalFilesDir(null);
        if (sdcardDir == null) {
            return null;
        }
        File outputImage = new File(sdcardDir, filename);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((MVVMActivity)context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, TCConstants.WRITE_PERMISSION_REQ_CODE);
            return null;
        }
        if (outputImage.exists()) {
            outputImage.delete();
        }
        return Uri.fromFile(outputImage);
    }

    /**
     * 打开图片裁剪页面
     *
     * @param uri 裁剪图片的URI
     */
    public void startPhotoZoom(Uri uri) {
        mCropAvatarPicUri = createCoverUri("_icon_crop");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 200);
        intent.putExtra("aspectY", 200);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        if (mCropAvatarPicUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropAvatarPicUri);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        ((MVVMActivity)context).startActivityForResult(intent, CROP_CHOOSE);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case CAPTURE_IMAGE_CAMERA:
                if (mAvatarPicUri != null) {
                    startPhotoZoom(mAvatarPicUri);
                }
                break;
            case IMAGE_STORE:
                String path = TCUtils.getPath(context, data.getData());
                if (null != path){
                    File file = new File(path);
                    startPhotoZoom(Uri.fromFile(file));
                }
                break;
            case CROP_CHOOSE:
                if (mCropAvatarPicUri != null) {
//                    mCosUploadHelper.uploadPic(mCropAvatarPicUri.getPath());
                }
                break;
        }
    }

    public MutableLiveData<AccountInfoBean> getAccountInfo() {
        return accountInfo;
    }
}
