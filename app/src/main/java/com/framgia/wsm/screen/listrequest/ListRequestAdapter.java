package com.framgia.wsm.screen.listrequest;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.LeaveRequest;
import com.framgia.wsm.data.model.OffRequest;
import com.framgia.wsm.data.model.RequestOverTime;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.databinding.ItemListRequestLeaveBinding;
import com.framgia.wsm.databinding.ItemListRequestOffBinding;
import com.framgia.wsm.databinding.ItemListRequestOvertimeBinding;
import com.framgia.wsm.screen.BaseRecyclerViewAdapter;
import com.framgia.wsm.utils.RequestType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 12/06/2017.
 */

public class ListRequestAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private OnRecyclerViewItemClickListener<Object> mItemClickListener;
    private List<LeaveRequest> mRequests;
    private List<OffRequest> mRequestOffs;
    private List<RequestOverTime> mRequestOverTimes;
    @RequestType
    private int mRequestType;
    private User mUser;

    ListRequestAdapter(@NonNull Context context, @RequestType int requestType) {
        super(context);
        mRequestType = requestType;
        mRequests = new ArrayList<>();
        mRequestOffs = new ArrayList<>();
        mRequestOverTimes = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RequestType.REQUEST_OVERTIME:
                ItemListRequestOvertimeBinding itemListRequestOvertimeBinding =
                        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_list_request_overtime, parent, false);
                return new RequestOverTimeViewHolder(getContext(), itemListRequestOvertimeBinding,
                        mItemClickListener);
            case RequestType.REQUEST_OFF:
                ItemListRequestOffBinding itemListRequestOffBinding =
                        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_list_request_off, parent, false);
                return new RequestOffViewHolder(getContext(), itemListRequestOffBinding,
                        mItemClickListener);
            case RequestType.REQUEST_LATE_EARLY:
                ItemListRequestLeaveBinding itemListRequestLeaveBinding =
                        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_list_request_leave, parent, false);
                return new RequestLeaveViewHolder(getContext(), itemListRequestLeaveBinding,
                        mItemClickListener);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case RequestType.REQUEST_OVERTIME:
                RequestOverTimeViewHolder requestOverTimeViewHolder =
                        (RequestOverTimeViewHolder) holder;
                requestOverTimeViewHolder.bind(mRequestOverTimes.get(position), mUser);
                break;
            case RequestType.REQUEST_OFF:
                RequestOffViewHolder requestOffViewHolder = (RequestOffViewHolder) holder;
                requestOffViewHolder.bind(mRequestOffs.get(position), mUser);
                break;
            case RequestType.REQUEST_LATE_EARLY:
                RequestLeaveViewHolder requestLeaveViewHolder = (RequestLeaveViewHolder) holder;
                requestLeaveViewHolder.bind(mRequests.get(position), mUser);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mRequestType;
    }

    @Override
    public int getItemCount() {
        if (mRequestType == RequestType.REQUEST_OVERTIME) {
            return mRequestOverTimes.size();
        } else if (mRequestType == RequestType.REQUEST_OFF) {
            return mRequestOffs.size();
        } else {
            return mRequests.size();
        }
    }

    void updateDataRequest(List<LeaveRequest> requests, boolean isLoadMore) {
        if (requests == null) {
            return;
        }
        if (!isLoadMore) {
            mRequests.clear();
        }
        mRequests.addAll(requests);
        notifyDataSetChanged();
    }

    void updateDataRequestOff(List<OffRequest> requestOffs, boolean isLoadMore) {
        if (requestOffs == null) {
            return;
        }
        if (!isLoadMore) {
            mRequestOffs.clear();
        }
        mRequestOffs.addAll(requestOffs);
        notifyDataSetChanged();
    }

    void updateDataRequestOverTime(List<RequestOverTime> requestOverTimes, boolean isLoadMore) {
        if (requestOverTimes == null) {
            return;
        }
        if (!isLoadMore) {
            mRequestOverTimes.clear();
        }
        mRequestOverTimes.addAll(requestOverTimes);
        notifyDataSetChanged();
    }

    void updateUser(User user) {
        mUser = user;
    }

    void setItemClickListener(OnRecyclerViewItemClickListener<Object> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    private static class RequestOverTimeViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private ItemListRequestOvertimeBinding mBinding;
        private OnRecyclerViewItemClickListener<Object> mItemClickListener;

        RequestOverTimeViewHolder(Context context, ItemListRequestOvertimeBinding binding,
                BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Object> listener) {
            super(binding.getRoot());
            mContext = context;
            mBinding = binding;
            mItemClickListener = listener;
        }

        void bind(RequestOverTime requestOverTime, User user) {
            mBinding.setViewModel(
                    new ItemListRequestViewModel(mContext, requestOverTime, mItemClickListener,
                            user));
            mBinding.executePendingBindings();
        }
    }

    private static class RequestOffViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private ItemListRequestOffBinding mBinding;
        private OnRecyclerViewItemClickListener<Object> mItemClickListener;

        RequestOffViewHolder(Context context, ItemListRequestOffBinding binding,
                BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Object> listener) {
            super(binding.getRoot());
            mContext = context;
            mBinding = binding;
            mItemClickListener = listener;
        }

        void bind(OffRequest requestOff, User user) {
            mBinding.setViewModel(
                    new ItemListRequestViewModel(mContext, requestOff, mItemClickListener, user));
            mBinding.executePendingBindings();
        }
    }

    private static class RequestLeaveViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private ItemListRequestLeaveBinding mBinding;
        private OnRecyclerViewItemClickListener<Object> mItemClickListener;

        RequestLeaveViewHolder(Context context, ItemListRequestLeaveBinding binding,
                BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Object> listener) {
            super(binding.getRoot());
            mContext = context;
            mBinding = binding;
            mItemClickListener = listener;
        }

        void bind(LeaveRequest request, User user) {
            mBinding.setViewModel(
                    new ItemListRequestViewModel(mContext, request, mItemClickListener, user));
            mBinding.executePendingBindings();
        }
    }
}
