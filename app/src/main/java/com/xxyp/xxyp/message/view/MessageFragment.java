
package com.xxyp.xxyp.message.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleFragment;
import com.xxyp.xxyp.common.utils.dialog.DialogUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.dialog.CommonDialogView;
import com.xxyp.xxyp.common.view.recyclerView.DividerItemDecoration;
import com.xxyp.xxyp.message.adapter.MessageListAdapter;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.ConversationBean;
import com.xxyp.xxyp.message.contract.MessageContract;
import com.xxyp.xxyp.message.presenter.MessagePresenter;
import com.xxyp.xxyp.message.service.MessageListenerManager;
import com.xxyp.xxyp.message.service.MsgServiceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 消息列表 Created by sunpengfei on 2017/8/5. Person in charge :
 * sunpengfei
 */

public class MessageFragment extends BaseTitleFragment
        implements MessageContract.View, MessageListenerManager.OnMsgReceiveListener {

    private MessageListAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private MessageContract.Presenter mPresenter;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(getActivity(), headerContainer);
        builder.setTitle(R.string.message);
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(getActivity(), R.layout.fragment_message, null);
        mRecyclerView = ((RecyclerView)view.findViewById(R.id.message_recycler));
        mAdapter = new MessageListAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView
                .addItemDecoration(new DividerItemDecoration(getActivity(), R.color.guide_divider));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new MessagePresenter(this);
        MsgServiceManager.getInstance().registerIMListener(this, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getConversationList();
    }

    @Override
    protected void setViewListener() {
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversationBean bean = mAdapter.getItem(position);
                if(bean != null){
                    mPresenter.openChat(bean.getChatId(), bean.getChatType());
                }
            }
        });

        mAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showOperateSession(position);
                return true;
            }
        });
    }

    /**
     * 展示操作消息session
     */
    private void showOperateSession(final int position){
        if(mAdapter == null){
            return;
        }
        final ConversationBean bean = mAdapter.getItem(position);
        if(bean == null){
            return;
        }
        List<String> operates = new ArrayList<>();
        operates.add("删除");
        DialogUtils.getInstance().showOperateDialog(getActivity(), operates, null, null, 0, false, new CommonDialogView.DialogViews_ask.DialogViews_askImpl() {
            @Override
            public void doOk(String text) {
                if (TextUtils.equals("删除", text)) {
                    if (mPresenter != null) {
                        mPresenter.clearConversation(bean.getChatId(), bean.getChatType());
                        mAdapter.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void showConversationList(List<ConversationBean> beans) {
        mAdapter.replaceList(beans);
    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {

    }

    @Override
    public void onReceiveMessage(ChatMessageBean bean) {
        if(mPresenter != null){
            mPresenter.getConversationList();
        }
    }

    @Override
    public void onReceiveMessages(List<ChatMessageBean> beans) {
        if(mPresenter != null){
            mPresenter.getConversationList();
        }
    }

    @Override
    public void onDestroyView() {
        MsgServiceManager.getInstance().removeIMListener(this, null);
        if(mPresenter != null){
            mPresenter.onDestroyPresenter();
            setNull(mPresenter);
        }
        setNull(mAdapter);
        setNull(mRecyclerView);
        super.onDestroyView();
    }

}
