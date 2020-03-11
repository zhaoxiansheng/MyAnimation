package com.example.zy.myanimation.view.recycler.adapter;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.zy.myanimation.view.recycler.channel.ChannelConfig;
import com.example.zy.myanimation.view.recycler.model.config.AppsConfig;
import com.example.zy.myanimation.view.recycler.model.infos.AppInfo;
import com.example.zy.myanimation.view.recycler.view.CustomLinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by JonLuo on 2019/04/08.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.RvViewHolder> implements View.OnClickListener {

    private static final String TAG = "AppListAdapter";

    private List<AppInfo> appInfo;

    private Context mContext;
    private ContentObserver mContentObserver;
    private ContentObserver mProgressContentObserver;
    private int mUpgrade;
    private CustomLinearLayout mUpgradeView;
    private int upgradeProgress;

    private final Handler mHandler = new Handler(msg -> true);

    public AppListAdapter(Context context) {
        mContext = context;
        setHasStableIds(true);
        registerChangeBadgeView();
        registerProgressChange();
    }

    @Override
    public long getItemId(int position) {
        if (position < appInfo.size()) {
            return appInfo.get(position).componentName.hashCode();
        } else {
            return -1;
        }
//        return position;
    }

    public void setData(List<AppInfo> appInfo) {
        if (appInfo == null) {
            appInfo = new ArrayList<>();
        }
        Log.i(TAG, "setNewData: appInfo.size() = " + appInfo.size());
        this.appInfo = appInfo;
        sort();
        notifyDataSetChanged();
    }

    public void changeIcons(Context context) {
        for (AppInfo app : appInfo) {
            app.updateIcon(context);
        }
        notifyDataSetChanged();
    }

    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = createItemView(parent);
        Log.e(TAG, "onCreateViewHolder: " + view.hashCode());
        return new RvViewHolder(view);
    }

    private View createItemView(ViewGroup parent) {
        CustomLinearLayout layout = new CustomLinearLayout(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ChannelConfig.ITEM_WIDTH, ChannelConfig.ITEM_HEIGHT);
        layout.setLayoutParams(params);
        return layout;
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: int position: " + position);
        AppInfo appInfo = getItem(position);
        holder.customLinearLayout.setTag(appInfo);
        holder.customLinearLayout.setOnClickListener(this);
        holder.customLinearLayout.getTextView().setText(appInfo.title.toString());

/*        LinearLayout.LayoutParams paramsImageView = (LinearLayout.LayoutParams) holder.customLinearLayout.getImageView().getLayoutParams();
        LinearLayout.LayoutParams paramsTextView = (LinearLayout.LayoutParams) holder.customLinearLayout.getTextView().getLayoutParams();
        if (!appInfo.isLocalIcon) {
            paramsImageView.width = ChannelConfig.APP_ICON_SIZE_SMALL;
            paramsImageView.height = ChannelConfig.APP_ICON_SIZE_SMALL;
            paramsImageView.topMargin = ChannelConfig.BUBBLE_TEXTVIEW_PADDING_TOP + ChannelConfig.MARGIN_FOR_DEFFERENT_SIZE;
            paramsTextView.topMargin = ChannelConfig.APP_DRAWABLE_PADDING + ChannelConfig.MARGIN_FOR_DEFFERENT_SIZE;
        }*/

       /* if (appInfo.componentName != null) {
            mUpgradeView = holder.customLinearLayout;
            if (appInfo.componentName.getPackageName().equals(ChannelConfig.APPS_FOTA)) {
                if (isUpgrade()) {
                    mUpgradeView.setShowBadge(true);
                } else {
                    mUpgradeView.setShowBadge(false);
                }
                if (upgradeProgress > 0 && upgradeProgress < 100) {
                    mUpgradeView.setProgressShow(true);
                    mUpgradeView.setProgress(upgradeProgress);
                } else {
                    mUpgradeView.setProgressShow(false);
                }
            } else {
                holder.customLinearLayout.getImageView().setImageDrawable(appInfo.iconDrawable);
                mUpgradeView.setProgressShow(false);
            }
        } else {
            holder.customLinearLayout.getImageView().setImageDrawable(appInfo.iconDrawable);
        }*/
    }

    private AppInfo getItem(int position) {
        AppInfo app = appInfo.get(position);
        Log.i(TAG, "getItem: appInfo : " + app.title + "--" + app.intent);
        return app;
    }

    public void add(List<AppInfo> data) {
        for (AppInfo app : data) {
            if (!appInfo.contains(app)) {
                appInfo.add(app);
            }
        }
        sort();
        notifyDataSetChanged();
    }

    public void update(List<AppInfo> data) {
        for (AppInfo app : data) {
            int index = appInfo.indexOf(app);
            if (index != -1) {
                appInfo.remove(index);
                appInfo.add(index, app);
            }
        }
        sort();
        notifyDataSetChanged();
    }

    public void remove(List<AppInfo> data) {
        List<AppInfo> remove = new ArrayList<>();
        for (AppInfo app : data) {
            if (!appInfo.contains(app)) {
                remove.add(app);
            }
        }
        appInfo.removeAll(remove);
        sort();
        notifyDataSetChanged();
    }

    private void sort() {
        if (AppsConfig.FILTER_APP_LIST) {
            Collections.sort(appInfo, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    return Long.compare(o1.id, o2.id);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return appInfo != null ? appInfo.size() : 0;
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void registerProgressChange() {
        mProgressContentObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                upgradeProgress = Settings.System.getInt(mContext.getContentResolver(), AppsConfig.KEY_UPDATE_PROGRESS, -1);
                if (upgradeProgress % 4 == 0) {
                    notifyDataSetChanged();
                }
            }
        };
        mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(AppsConfig.KEY_UPDATE_PROGRESS),
                true, mProgressContentObserver);
    }

    public void registerChangeBadgeView() {
        mContentObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                int upgrade = Settings.System.getInt(mContext.getContentResolver(), AppsConfig.KEY_BADGE, -1);
                if (mUpgrade != upgrade) {
                    if (mUpgradeView != null) {
                        switch (upgrade) {
                            case 0:
                                mUpgradeView.setShowBadge(false);
                                break;
                            case 1:
                                mUpgradeView.setShowBadge(true);
                                break;
                            default:
                                break;
                        }
                        notifyDataSetChanged();
                    }
                }
            }
        };
        mContext.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mContentObserver);
    }

    private boolean isUpgrade() {
        mUpgrade = Settings.System.getInt(mContext.getContentResolver(), AppsConfig.KEY_BADGE, -1);
        switch (mUpgrade) {
            case 0:
                Log.d(TAG, "isUpgrade: hide badge");
                return false;
            case 1:
                Log.d(TAG, "isUpgrade: show badge");
                return true;
            default:
                Log.e(TAG, "isUpgrade: acquire upgrade is fail");
                break;
        }
        return false;
    }

    class RvViewHolder extends RecyclerView.ViewHolder {
        CustomLinearLayout customLinearLayout;

        public RvViewHolder(View itemView) {
            super(itemView);
            customLinearLayout = (CustomLinearLayout) itemView;
        }
    }

}
