
package com.xxyp.xxyp.message.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.PermissionActivity;
import com.xxyp.xxyp.common.bean.PhotoViewBean;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.provider.CommonProvider;
import com.xxyp.xxyp.common.utils.CameraUtils;
import com.xxyp.xxyp.common.utils.DownloadCallback;
import com.xxyp.xxyp.common.utils.DownloadUtils;
import com.xxyp.xxyp.common.utils.FileConfig;
import com.xxyp.xxyp.common.utils.FileUtils;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.common.utils.VoicePlayHelper;
import com.xxyp.xxyp.common.utils.VoiceRecordHelper;
import com.xxyp.xxyp.common.utils.dialog.DialogUtils;
import com.xxyp.xxyp.common.utils.gallery.GalleryActivity;
import com.xxyp.xxyp.common.utils.gallery.GalleryProvider;
import com.xxyp.xxyp.common.utils.permissions.PermissionsConstant;
import com.xxyp.xxyp.common.utils.permissions.PermissionsMgr;
import com.xxyp.xxyp.common.utils.permissions.PermissionsResultAction;
import com.xxyp.xxyp.common.view.dialog.CommonDialogView;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageImageBean;
import com.xxyp.xxyp.message.bean.MessageShotBean;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;
import com.xxyp.xxyp.message.contract.ChatBaseContract;
import com.xxyp.xxyp.message.customsviews.MessageInputBar;
import com.xxyp.xxyp.message.customsviews.panel.PanelConfig;
import com.xxyp.xxyp.message.provider.ChatProvider;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.message.utils.MessageSendUtils;
import com.xxyp.xxyp.message.utils.MessageUtils;
import com.xxyp.xxyp.user.provider.UserProvider;
import com.xxyp.xxyp.user.service.UserServiceManager;
import com.xxyp.xxyp.user.view.MyDatingShotActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 消息基础presenter Created by sunpengfei on 2017/8/16. Person in
 * charge : sunpengfei
 */
public abstract class ChatBasePresenter implements ChatBaseContract.Presenter {

    private static final String TAG = ChatBasePresenter.class.getSimpleName();

    /* 相机 */
    private final int CAMERA_REQUSET = 1000;

    /* 相册 */
    private final int IMAGE_REQUSET = 1001;

    /* 创建约拍 */
    private final int CAREATE_SHOT_REQUSET = 1002;

    /* 选择约拍 */
    private final int CHOOSE_SHOT_REQUSET = 1003;

    /* 拍照权限 */
    private final int TYPE_CAMERA_PERMISSION = 1;

    /* 相册权限 */
    private final int TYPE_PHOTO_PERMISSION = 2;

    /* 语音权限 */
    private final int TYPE_VOICE_PERMISSION = 3;

    protected String mMyUserId;

    protected String mChatId;

    protected int mChatType;

    private MessageSendUtils mSendUtils;

    private ChatBaseContract.View mView;

    private ChatBaseContract.Model mModel;

    /* 录制语音帮助类 */
    private VoiceRecordHelper mVoiceRecordHelper;

    /* 播放语音帮助类 */
    private VoicePlayHelper mVoicePlayHelper;

    /* 语音录制事件 */
    private int mRecordVoiceAction;

    /* 语音录制时间 */
    private long mRecordVoiceTime;

    /* 语音名称 */
    private String mVoiceName;

    /**
     * 语音录制状态 1:未录制状态;2:录制中状态 *
     */
    private int mRecordingStatus = 1;

    /* 照相的相片名字 */
    private String mCameraName;

    /* 相片所在文件夹 */
    private String mCameraDir;

    /* 相片完整路径 包含相片名字 */
    private String mCameraPath;

    ChatBasePresenter() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState != null && !TextUtils.isEmpty(mCameraPath)) {
            outState.putString(MessageConfig.KEY_CAMERA_PATH, mCameraPath);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        if (outState != null) {
            String cameraPath = outState.getString(MessageConfig.KEY_CAMERA_PATH);
            if (!TextUtils.isEmpty(cameraPath))
                mCameraPath = cameraPath;
        }
    }

    @Override
    public void setChatInfo(int chatType, String myUserId, String chatId) {
        mChatType = chatType;
        mMyUserId = myUserId;
        mChatId = chatId;
        mView.setChatIds(chatType, myUserId, chatId);
        mSendUtils = new MessageSendUtils();
        mSendUtils.setChatInfo(chatType, myUserId, chatId);
    }

    @Override
    public void setChatBaseView(ChatBaseContract.View view, ChatBaseContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void onSendTextRequest(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mView.sendChatMessage(mSendUtils.sendText(text));
    }

    @Override
    public void onFunctionRequest(int functionType) {
        switch (functionType) {
            case PanelConfig.PANEL_CAMERA:
                // 拍照 需要SD卡读写 拍照权限
                if (hasPermission(PermissionsConstant.CAMERA, PermissionsConstant.READ_STORAGE,
                        PermissionsConstant.WRITE_STORAGE)) {
                    takePic();
                } else {
                    requestPermissions(TYPE_CAMERA_PERMISSION, PermissionsConstant.READ_STORAGE);
                }
                break;
            case PanelConfig.PANEL_IMAGE:
                // 选择相册 需要SD卡读写权限
                if (hasPermission(PermissionsConstant.READ_STORAGE,
                        PermissionsConstant.WRITE_STORAGE)) {
                    GalleryProvider.openGalley((Activity)mView.getContext(), 9, IMAGE_REQUSET);
                } else {
                    requestPermissions(TYPE_CAMERA_PERMISSION, PermissionsConstant.READ_STORAGE);
                }
                break;
            case PanelConfig.PANEL_CREATE_SHOT:
                break;
            case PanelConfig.PANEL_CHOOSE_SHOT:
                // 选择约拍
                Intent intent = new Intent(mView.getContext(), MyDatingShotActivity.class);
                intent.putExtra(MyDatingShotActivity.IS_CHOOSE, true);
                ((Activity)mView.getContext()).startActivityForResult(intent, CHOOSE_SHOT_REQUSET);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSendVoiceRequest(int voiceAction, long voiceTime) {
        // 录音开始则不可以进行切换
        mRecordVoiceAction = voiceAction;
        mRecordVoiceTime = voiceTime;
        // 用户开始录音需要判断，其他不需要
        try {
            if (voiceAction == MessageInputBar.VOICE_START) {
                if (!FileUtils.checkSDCard()) {
                    ToastUtil.showTextViewPrompt("SD 不存在！");
                } else {
                    // 语音需要SD卡读写 麦克风权限
                    if (hasPermission(PermissionsConstant.RECORD_AUDIO,
                            PermissionsConstant.READ_STORAGE, PermissionsConstant.WRITE_STORAGE)) {
                        recordMedia(voiceAction, voiceTime);
                    } else {
                        // 如果没有权限 首先请求SD读权限
                        requestPermissions(TYPE_VOICE_PERMISSION, PermissionsConstant.READ_STORAGE);
                    }
                }
            } else {
                recordMedia(voiceAction, voiceTime);
            }
        } catch (IOException e) {
            XXLog.log_e("ChatBasePresenter", "recordMedia is failed:" + e);
        }
    }

    @Override
    public void stopAudio() {
        stopVoicePlay();
    }

    @Override
    public void onPause() {
        stopAudio();
    }

    /**
     * 如果有权限，则进行录音任务
     */
    private void recordMedia(int action, long time) throws IOException {
        // 停止其他占用音频的地方
        stopAudio();
        if (mVoiceRecordHelper == null) {
            mVoiceRecordHelper = new VoiceRecordHelper((Activity)mView.getContext());
            mVoiceRecordHelper.setCallBackSoundDecibel(new VoiceRecordHelper.OnCallBackSoundDecibel() {
                @Override
                public void callBackSoundDecibel(float decibel) {
                    //音量
                    if(mView != null){
                        mView.showRecordMicView((int) decibel);
                    }
                }
            });
        }
        if (action == MessageInputBar.VOICE_START && mRecordingStatus == 1) {
            // 展示语音录制view
            mView.showRecordView();
            // 改变录音状态
            mRecordingStatus = 2;
            // 开始时间
            long mStartVoiceTime = System.currentTimeMillis();
            if (mRecordingStatus == 2) {
                mVoiceName = FileConfig.DIR_APP_CACHE_VOICE + "/" + mStartVoiceTime
                        + MessageConfig.MediaFormat.VOICE_FORMAT;
                File file = new File(mVoiceName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                mVoiceRecordHelper.startVoiceRecord(mVoiceName);
            }
            // 手势抬起
        } else if (action == MessageInputBar.VOICE_FINISH && mRecordingStatus == 2) {// 松开手势时执行录制完成
            restoreRecording();
            if (time < 1) {
                mVoiceRecordHelper.stopVoiceRecord(true, mVoiceName);
                return;
            }
            mVoiceRecordHelper.stopVoiceRecord(false, null);
            if (new File(mVoiceName).exists() && new File(mVoiceName).length() > 0) {
                // 文件长度大于0才发送 否则就是没有录制成功
                mView.sendChatMessage(mSendUtils.sendVoice(mVoiceName, "", (int)time));
            } else {
                // 录制失败 停止录音并删除文件
                mVoiceRecordHelper.stopVoiceRecord(true, mVoiceName);
                ToastUtil.showTextViewPrompt(
                        mView.getContext().getString(R.string.record_fail_check_permission));
            }
            // 如果时间超过一分钟，则直接发送，复原初始状态
        } else if (action == MessageInputBar.VOICE_TIME_OUT && mRecordingStatus == 2) {
            mVoiceRecordHelper.stopVoiceRecord(false, null);
            restoreRecording();
            long fileLength = 0;
            File file = new File(mVoiceName);
            // 如果语音长度小于一分钟，则录音失败
            if (file.isFile() && file.exists()) {
                fileLength = file.length();
            }
            if (fileLength >= 60) {
                mView.sendChatMessage(mSendUtils.sendVoice(mVoiceName, "", 60));
            }
        } else if (action == MessageInputBar.VOICE_OVER && mRecordingStatus == 2) {
            // 手指划出语音录制区域

        } else if (action == MessageInputBar.VOICE_NORMAL && mRecordingStatus == 2) {
            // 手指重新划入语音录制区域

        } else if (action == MessageInputBar.VOICE_CANCEL && mRecordingStatus == 2) {// 锁屏
            // 接电话等触摸失去焦点时
            restoreRecording();
            mVoiceRecordHelper.stopVoiceRecord(true, mVoiceName);
        }
    }

    @Override
    public void onGoToUserDetail(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            UserProvider.openFrame((Activity)mView.getContext(), userId);
        }
    }

    @Override
    public void onPlayVoiceListener(final ChatMessageBean chatBean) {
        if (mView == null || chatBean == null || chatBean.getVoiceBean() == null
                || chatBean.getVoiceBean().getVoiceLen() <= 0) {
            return;
        }
        if (mVoicePlayHelper == null) {
            mVoicePlayHelper = new VoicePlayHelper((Activity)mView.getContext());
            // registerSensor();
        }
        final MessageVoiceBean voiceBean = chatBean.getVoiceBean();
        switch (voiceBean.getStatus()) {
            case MessageConfig.VoiceStatus.VOICE_PLAY:
                // 正在播放
                voiceBean.setStatus(MessageConfig.VoiceStatus.VOICE_READED);
                mView.updateChatMessage(chatBean);
                stopVoicePlay();
                break;
            case MessageConfig.VoiceStatus.VOICE_UNREAD:
                // 未读
                voiceBean.setStatus(MessageConfig.VoiceStatus.VOICE_READED);
                mModel.updateVoiceMessageStatus(MessageConfig.VoiceStatus.VOICE_READED,
                        voiceBean.getVoiceId());
            case MessageConfig.VoiceStatus.VOICE_READED:
                // 已读
                if (TextUtils.isEmpty(voiceBean.getVoiceLocalPath())
                        || !new File(voiceBean.getVoiceLocalPath()).exists()) {
                    XXLog.log_i(TAG, "local voiceUrl is null");
                }
                if (TextUtils.isEmpty(voiceBean.getVoiceUrl())) {
                    XXLog.log_i(TAG, "http voiceUrl is null");
                }
                String voiceUrl = null;
                if (!TextUtils.isEmpty(voiceBean.getVoiceLocalPath())
                        && new File(voiceBean.getVoiceLocalPath()).exists()) {
                    voiceUrl = voiceBean.getVoiceLocalPath();
                }
                stopVoicePlay();
                if (!TextUtils.isEmpty(voiceUrl)) {
                    playVoice(voiceUrl, chatBean);
                } else if (!TextUtils.isEmpty(voiceBean.getVoiceUrl())) {
                    String url = voiceBean.getVoiceUrl();
                    DownloadUtils.getInstance().downloadAsyn(url, FileConfig.DIR_APP_CACHE_VOICE,
                            MessageConfig.MediaFormat.VOICE_FORMAT, new DownloadCallback() {
                                @Override
                                public void postDownloadProgress(long totalSize, long currentSize) {

                                }

                                @Override
                                public void postFail(File file, int errorCode) {

                                }

                                @Override
                                public void postSuccess(File localFile) {
                                    String filePath = localFile.getAbsolutePath();
                                    if (!TextUtils.isEmpty(filePath)) {
                                        playVoice(filePath, chatBean);
                                        voiceBean.setVoiceLocalPath(filePath);
                                        mModel.updateMessageVoice(voiceBean);
                                    }
                                }

                                @Override
                                public void postCancel(File file) {

                                }
                            });
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开始播放语音
     *
     * @param chatBean 语音消息体
     */
    private void playVoice(String voicePath, final ChatMessageBean chatBean) {
        if (TextUtils.isEmpty(voicePath) || chatBean == null || chatBean.getVoiceBean() == null
                || mView == null || mVoicePlayHelper == null) {
            return;
        }
        chatBean.getVoiceBean().setStatus(MessageConfig.VoiceStatus.VOICE_PLAY);
        mView.updateChatMessage(chatBean);
        mVoicePlayHelper.setOnVoiceFinishListener(new VoicePlayHelper.OnVoiceFinishListener() {
            @Override
            public void onFinish() {
                chatBean.getVoiceBean().setStatus(MessageConfig.VoiceStatus.VOICE_READED);
                if (mView == null) {
                    return;
                }
                mView.updateChatMessage(chatBean);
            }
        });
        mVoicePlayHelper.startVoice(voicePath);
    }

    /**
     * 拍照
     */
    private void takePic() {
        // 拍照
        if (!TextUtils.isEmpty(mCameraName)) {
            mCameraName = null;
        }
        if (!TextUtils.isEmpty(mCameraDir)) {
            mCameraDir = null;
        }
        if (!TextUtils.isEmpty(mCameraPath)) {
            mCameraPath = null;
        }
        mCameraName = CameraUtils.getInstance().getCameraName();
        mCameraDir = FileConfig.DIR_APP_CACHE_CAMERA + "/";
        mCameraPath = FileConfig.DIR_APP_CACHE_CAMERA + "/" + mCameraName + ".jpg";
        CameraUtils.getInstance().takePhoto(mCameraDir, mCameraPath, (Activity)mView.getContext(),
                CAMERA_REQUSET);
    }

    @Override
    public void onClickToBigImgListener(View view, ChatMessageBean chatBean) {
        // 跳转图片查看
        List<MessageImageBean> imageBeans = mModel.getMessageImages(mChatId);
        if (imageBeans == null || imageBeans.isEmpty()) {
            return;
        }
        List<PhotoViewBean> photoViewBeans = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < imageBeans.size(); i++) {
            PhotoViewBean photoViewBean = new PhotoViewBean();
            MessageImageBean bean = imageBeans.get(i);
            if (!TextUtils.isEmpty(bean.getBigImagePath())
                    && new File(bean.getBigImagePath()).exists()) {
                // 首先将压缩的过得图设置
                photoViewBean.setLocalPath(bean.getBigImagePath());
            } else {
                // 否则显示原图
                photoViewBean.setLocalPath(bean.getLocalImagePath());
            }
            if (!TextUtils.isEmpty(bean.getImageUrl())) {
                photoViewBean.setHttpUrl(bean.getImageUrl());
            }
            if (!TextUtils.isEmpty(bean.getThumbImageUrl())) {
                photoViewBean.setThumbHttpUrl(bean.getThumbImageUrl());
            }
            if (bean.getImgId() == chatBean.getRelationSourceId()) {
                index = i;
            }
            photoViewBeans.add(photoViewBean);
        }
        ChatProvider.openPhotoPreView((Activity)mView.getContext(), index, photoViewBeans);
    }

    @Override
    public void onGoToShotDetail(ChatMessageBean chatBean) {
        if (chatBean == null || chatBean.getMsgType() != MessageConfig.MessageType.MSG_APPOINTMENT || chatBean.getShotBean() == null) {
            return;
        }
        String userId = SharePreferenceUtils.getInstance().getUserId();
        FindProvider.openShot((Activity) mView.getContext(), userId,
                String.valueOf(chatBean.getShotBean().getDatingShotId()));
    }

    @Override
    public void onUpdateShot(final ChatMessageBean chatBean, final int targetStatus) {
        if (chatBean == null || chatBean.getMsgType() != MessageConfig.MessageType.MSG_APPOINTMENT || chatBean.getShotBean() == null) {
            return;
        }
        mView.showChatLoading(true);
        final MessageShotBean messageShotBean = chatBean.getShotBean();

        ShotBean shotBean = new ShotBean();
        shotBean.setStatus(targetStatus);
        shotBean.setDatingShotId(String.valueOf(messageShotBean.getDatingShotId()));
        shotBean.setDatingUserId(SharePreferenceUtils.getInstance().getUserId());
        UserServiceManager.updateDatingShot(shotBean).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (mView != null) {
                    mView.cancelChatLoading();
                    ToastUtil.showTextViewPrompt("处理约拍失败");
                }
            }

            @Override
            public void onNext(Object o) {
                messageShotBean.setStatus(targetStatus);
                //更新消息
                mModel.updateMessageShot(messageShotBean);
                if (mView != null) {
                    mView.updateChatMessage(chatBean);
                    mView.cancelChatLoading();
                }
                if (targetStatus == MessageConfig.ShotStatus.SHOT_DONE) {
                    //完成之后发送notice信息
                    mView.sendChatMessage(mSendUtils.sendNotice("对方确认了[拍摄完成]"));
                }else{
                    // 需要发送修改约拍状态
                    MessageUtils.buildSendMessage(chatBean);
                    String content = chatBean.getContent();
                    mSendUtils.sendOperate(chatBean.getMsgId(), MessageConfig.OperateType.TYPE_UPDATE, content);
                }
            }
        });
    }

    @Override
    public void onReSendMessageListener(ChatMessageBean chatBean) {

    }

    @Override
    public void onMessageLongClick(final ChatMessageBean chatBean) {
        if(chatBean == null){
            return;
        }
        List<String> operates = new ArrayList<>();
        operates.add("删除");
        DialogUtils.getInstance().showOperateDialog(mView.getContext(), operates, null, null, 0, false, new CommonDialogView.DialogViews_ask.DialogViews_askImpl() {
            @Override
            public void doOk(String text) {
                if (TextUtils.equals("删除", text)) {
                    mModel.deleteMessage(chatBean);
                    if (mView != null) {
                        mView.deleteMessage(chatBean);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_REQUSET:
                // 相册
                if (resultCode == Activity.RESULT_OK && data != null) {
                    List<String> picPaths = data.getStringArrayListExtra(GalleryActivity.PHOTOS);
                    if (picPaths == null || picPaths.size() == 0) {
                        return;
                    }
                    mView.sendChatMessages(mSendUtils.sendImages(picPaths));
                }
                break;
            case CAMERA_REQUSET:
                // 拍照
                if (resultCode == Activity.RESULT_OK) {
                    if (!TextUtils.isEmpty(mCameraPath) && new File(mCameraPath).exists()) {
                        File f = new File(mCameraPath);
                        mView.getContext().sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f)));
                        mView.sendChatMessage(mSendUtils.sendImage(mCameraPath));
                    }
                }
                break;
            case CHOOSE_SHOT_REQUSET:
                // 选择约拍
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ShotBean shotBean = (ShotBean)data
                            .getSerializableExtra(MyDatingShotActivity.MY_SHOT);
                    mView.sendChatMessage(mSendUtils.sendShot(shotBean));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 取消播放的语音
     */
    private void stopVoicePlay() {
        // if (mView != null) {
        // mView.cancelVoicePlay();
        // }
        if (mVoicePlayHelper != null) {
            mVoicePlayHelper.stopVoice();
        }
    }

    /**
     * 是否有权限
     *
     * @param permissions 权限列表
     * @return 是否具有所有权限
     */
    private boolean hasPermission(String... permissions) {
        if (mView.getContext() instanceof PermissionActivity) {
            PermissionActivity activity = (PermissionActivity)mView.getContext();
            return activity.hasPermission(permissions);
        } else {
            throw new IllegalArgumentException("is not allow request permission");
        }
    }

    /**
     * 请求权限
     *
     * @param requestType 权限请求类型
     * @param permissions 权限列表
     * @return 是否具有所有权限
     */
    private void requestPermissions(final int requestType, final String... permissions) {
        if (!(mView.getContext() instanceof PermissionActivity)) {
            XXLog.log_d(TAG, "activity is not PermissionActivity");
            return;
        }
        PermissionsMgr.getInstance().requestPermissionsIfNecessaryForResult(
                (Activity)mView.getContext(), permissions, new PermissionsResultAction() {

                    @Override
                    public void onGranted(List<String> perms) {
                        XXLog.log_d(TAG, "Permission is Granted:" + perms);
                        onGrantedPermission(requestType, perms);
                    }

                    @Override
                    public void onDenied(List<String> perms) {
                        XXLog.log_d(TAG, "Permission is Denied" + perms);
                        onDeniedPermission(requestType, perms);
                    }
                });
    }

    /**
     * 权限接受
     * 
     * @param requestType 请求权限类型
     * @param permissions 权限接受的列表
     */
    private void onGrantedPermission(int requestType, List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return;
        }
        for (String permission : permissions) {
            switch (permission) {
                case PermissionsConstant.READ_STORAGE:
                    // 读SD权限
                    if (hasPermission(PermissionsConstant.WRITE_STORAGE)) {
                        // 如果有读写权限都有 则直接操作
                        hasPermissionHandler(requestType);
                    } else {
                        requestPermissions(requestType, PermissionsConstant.WRITE_STORAGE);
                    }
                    break;
                case PermissionsConstant.WRITE_STORAGE:
                    // 写SD权限 如果有读写权限都有 则直接操作
                    hasPermissionHandler(requestType);
                    break;
                case PermissionsConstant.CAMERA:
                    if (requestType == TYPE_CAMERA_PERMISSION) {
                        // 拍照
                        takePic();
                    }
                    break;
                case PermissionsConstant.RECORD_AUDIO:
                    try {
                        if (requestType == TYPE_VOICE_PERMISSION) {
                            recordMedia(mRecordVoiceAction, mRecordVoiceTime);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取到权限之后的操作
     * 
     * @param requestType 请求的权限类型
     */
    private void hasPermissionHandler(int requestType) {
        switch (requestType) {
            case TYPE_CAMERA_PERMISSION:
                // 拍照
                if (hasPermission(PermissionsConstant.CAMERA)) {
                    takePic();
                } else {
                    requestPermissions(requestType, PermissionsConstant.CAMERA);
                }
                break;
            case TYPE_PHOTO_PERMISSION:
                // 相册
                GalleryProvider.openGalley((Activity)mView.getContext(), 9, IMAGE_REQUSET);
                break;
            case TYPE_VOICE_PERMISSION:
                // 语音权限
                try {
                    if (hasPermission(PermissionsConstant.RECORD_AUDIO)) {
                        recordMedia(mRecordVoiceAction, mRecordVoiceTime);
                    } else {
                        requestPermissions(requestType, PermissionsConstant.RECORD_AUDIO);
                    }
                } catch (IOException e) {
                    XXLog.log_e(TAG, "recordMedia is failed:" + e);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 权限拒绝
     * 
     * @param requestType 请求权限类型
     * @param permissions 被拒绝的权限
     */
    private void onDeniedPermission(int requestType, List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return;
        }
        for (String permission : permissions) {
            switch (permission) {
                case PermissionsConstant.READ_STORAGE:
                case PermissionsConstant.WRITE_STORAGE:
                    // 读写SD权限拒绝
                    CommonProvider.openAppPermission((Activity)mView.getContext());
                    break;
                case PermissionsConstant.CAMERA:
                    if (requestType == TYPE_CAMERA_PERMISSION) {
                        // 拍照权限拒绝
                        ToastUtil.showTextViewPrompt(mView.getContext()
                                .getString(R.string.camera_fail_check_permission));
                    }
                    break;
                case PermissionsConstant.RECORD_AUDIO:
                    if (requestType == TYPE_VOICE_PERMISSION) {
                        // 语音权限拒绝
                        ToastUtil.showTextViewPrompt(mView.getContext()
                                .getString(R.string.record_fail_check_permission));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 复原录音初始状态
     */
    private void restoreRecording() {
        mRecordingStatus = 1;
        // 隐藏语音录制view
        mView.hideRecordView();
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        mModel = null;
        mSendUtils = null;
    }

}
