package com.douyaim.qsapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.douyaim.qsapp.BaseAdapter2;
import com.douyaim.qsapp.R;
import com.douyaim.qsapp.adapter.holder.ChatListBaseVH;
import com.douyaim.qsapp.adapter.holder.ChatListChargeVH;
import com.douyaim.qsapp.adapter.holder.ChatListHeaderVH2;
import com.douyaim.qsapp.adapter.holder.ChatListOpenRedVH;
import com.douyaim.qsapp.adapter.holder.ChatListRedVH;
import com.douyaim.qsapp.adapter.holder.ChatListTaskVH;
import com.douyaim.qsapp.adapter.holder.ChatListVideoVH;
import com.douyaim.qsapp.chat.ui.entity.UIChatlistInfo;
import com.douyaim.qsapp.model.UnreadInfo;
import com.sankuai.xm.proto.constant.IMMsgType;

import java.util.List;

/**
 * Created by garfield on 8/16/16.
 */
public class ChatListAdapter extends BaseAdapter2<ChatListBaseVH, UIChatlistInfo> {

    private final LayoutInflater mLayoutInflater;

    //fixme 新的ui改为了渐变色
    private int[] colors;

    private int[] drawables = {R.drawable.shape_chat_list_item_bg_cyan, R.drawable.shape_chat_list_item_bg_pink, R.drawable.shape_chat_list_item_bg_violet,
            R.drawable.shape_chat_list_item_bg_yellow, R.drawable.shape_chat_list_item_bg_blue};

    private boolean isTrendSetterMode = true;

    public interface onMoreClickListener {
        void onMoreClick(Bundle args);

        UnreadInfo getUnreadInfo(long chatId);

        void clearUnread(long chatId);

        void removeHeader();

        void warnNoContactPermission();

        void updateCoverByMsgIsPlayed(boolean hasPlayed, int msgType, String coverUrl, ImageView coverView);

        void notifyUserIFMatchContacts();

    }

    public onMoreClickListener listener;

    public void setListener(onMoreClickListener listener) {
        this.listener = listener;
    }


    public ChatListAdapter(Fragment frag, boolean isTrendSetterMode) {
        this.isTrendSetterMode = isTrendSetterMode;
        mLayoutInflater = LayoutInflater.from(frag.getContext());
        //// FIXME: 2016/11/30 采用新的渐变色，去掉了以前的色值
        colors = frag.getContext().getResources().getIntArray(R.array.chatListItmeBg);
        if (frag instanceof onMoreClickListener) {
            this.listener = (onMoreClickListener) frag;
        }
    }

    /**
     * 看来还得根据消息类型生成不同的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ChatListBaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0://消息列表头(提示上传通讯录，添加／邀请好友Header)
                return new ChatListHeaderVH2(mLayoutInflater.inflate(R.layout.header_action_group, parent, false), this);
//                return new ChatListHeaderVH(mLayoutInflater.inflate(R.layout.header_recommend_friends_grid, parent, false), this);
            case IMMsgType.IM_MSG_TYPE_OFFI_TASK://收到系统任务
            case IMMsgType.IM_MSG_TYPE_OFFI_TASK_RESULT://系统任务结果
                return new ChatListTaskVH(mLayoutInflater.inflate(R.layout.item_msg_list_trend_task, parent, false), this);
            case IMMsgType.IM_MSG_TYPE_NYED_CHARGE_NOTIFY:// 好友塞钱了
                return new ChatListChargeVH(mLayoutInflater.inflate(R.layout.item_msg_list_game_video, parent, false), this);
            case IMMsgType.IM_MSG_TYPE_REDPACKET_INFO:// 收到／发送红包消息
                return new ChatListRedVH(mLayoutInflater.inflate(R.layout.item_msg_list_trend_red, parent, false), this);
            case IMMsgType.IM_MSG_TYPE_OPENREDPACKET_INFO://红包被打开的消息
                return new ChatListOpenRedVH(mLayoutInflater.inflate(R.layout.item_msg_list_trend_redopen, parent, false), this);
            default://默认类型
                return new ChatListVideoVH(mLayoutInflater.inflate(R.layout.item_msg_list_trend_video, parent, false), this);

        }
    }

    @Override
    public void onBindViewHolder(ChatListBaseVH holder, int position) {
        int color;
        if (isTrendSetterMode) {
            color = drawables[position % 5];
        } else {
            color = colors[position % 4];
        }
        holder.bindData(getData(position), color);
    }

    @Override
    public void onBindViewHolder(ChatListBaseVH holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        UIChatlistInfo msg = getData(position);
//        if (msg.msgType == IMMsgType.IM_MSG_TYPE_VIDEO_INFO && msg.body instanceof IMVideoInfo) {
//            String type = ((IMVideoInfo) msg.body).type;
//            if (TextUtils.equals(type, "1")) {
//                return IMMsgType.IM_MSG_TYPE_VIDEO; // 阅后即焚类型
//            } else {
//                return IMMsgType.IM_MSG_TYPE_VIDEO_INFO;//普通视频
//            }
//        }
        return msg.msgType;

    }

    public UnreadInfo getUnreadInfo(long chatId) {
        if (listener != null) {
            return listener.getUnreadInfo(chatId);
        }
        return null;
    }

    public void clearUnread(long chatId) {
        if (listener != null) {
            listener.clearUnread(chatId);
        }
    }

    public void removeHeader() {
        if (listener != null) {
            listener.removeHeader();
        }
    }

    public void updateCoverByMsgIsPlayed(boolean hasPlayed,int msgType,String coverUrl, ImageView coverView) {
        if (listener != null) {
            listener.updateCoverByMsgIsPlayed(hasPlayed, msgType, coverUrl, coverView);
        }
    }

    public void warnContactPermission() {
        if (listener != null) {
            listener.warnNoContactPermission();
        }
    }

    public void notifyUserIFMatchContacts() {
        if (listener!=null) {
            listener.notifyUserIFMatchContacts();
        }
    }
}
