
package com.xxyp.xxyp.message.customsviews.panel;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.ItemEmoji;
import com.xxyp.xxyp.common.view.EMOJI;
import com.xxyp.xxyp.common.view.recyclerView.BaseRecyclerAdapter;
import com.xxyp.xxyp.common.view.recyclerView.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天输入面板功能
 */
public class PanelEmoji implements IPanel {

    private Context mContext;

    private RecyclerView mEmojiView;

    private OnPanelItemListener mItemListener;

    private EmojiAdapter mEmojiAdapter;

    public PanelEmoji(Context context) {
        mContext = context;
    }

    @Override
    public View obtainView(OnPanelItemListener itemListener) {
        mItemListener = itemListener;
        View view = View.inflate(mContext, R.layout.panel_emoji, null);
        mEmojiView = (RecyclerView) view. findViewById(R.id.recycler_emoji);
        mEmojiView.setLayoutManager(new GridLayoutManager(mContext, 7));
        mEmojiAdapter = new EmojiAdapter(mContext, initEmojiData());
        mEmojiView.setAdapter(mEmojiAdapter);
        setViewListener();
        return view;
    }

    private void setViewListener(){
        mEmojiAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mItemListener != null){
                    mItemListener.onPanelItemClick(PanelFactoryImp.TYPE_EMOJI,
                            mEmojiAdapter.getItem(position));
                }
            }
        });
    }

    private List<ItemEmoji> initEmojiData(){
        int length = EMOJI.XXYP_EMOJI_FACE.length;
        List<ItemEmoji> defaultEmojis = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            ItemEmoji itemNormal = new ItemEmoji();
            itemNormal.setEmojiName(EMOJI.XXYP_EMOJI_CODE[i]);
            itemNormal.setEmojiResId(EMOJI.XXYP_EMOJI_FACE[i]);
            defaultEmojis.add(itemNormal);
        }
        return defaultEmojis;
    }

    private class EmojiAdapter extends BaseRecyclerAdapter<ItemEmoji> {

        public EmojiAdapter(Context context) {
            super(context);
        }

        public EmojiAdapter(Context context, List<ItemEmoji> list) {
            super(context, list);
        }

        @Override
        protected int onCreateViewById(int viewType) {
            return R.layout.item_message_emoji;
        }

        @Override
        protected void onBindHolder(BaseViewHolder holder, int position) {
            ImageView imageView = holder.findViewById(R.id.panel_view);

            ItemEmoji bean = getItem(position);
            // 设置本地资源
            imageView.setImageResource(EMOJI.getInstance().getEmojiRes(bean.getEmojiName()));
        }
    }
}
