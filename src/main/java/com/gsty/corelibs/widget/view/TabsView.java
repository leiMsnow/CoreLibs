package com.gsty.corelibs.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ray.corelibs.R;
import com.gsty.corelibs.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义TabsView
 * Created by zhangleilei on 3/24/16.
 */
public class TabsView extends FrameLayout {

    private Context mContext;
    private LinearLayout rootView;
    private List<TextView> textViews = new ArrayList<>();
    private TabsChildViewClickListener childClickListener;
    private int currentPosition = 0;


    public TabsView(Context context) {
        this(context, null);
    }

    public TabsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_tabs, this);
        rootView = (LinearLayout) findViewById(R.id.ll_root);
    }

    /**
     * 添加子项文本数组
     * 先设置setChildViewClickListener再设置此方法
     *
     * @param childViews tabs显示的文本，数组格式
     * @param listener   tabs点击监听，返回相应的索引号，从0开始
     */
    public void setChildView(String[] childViews, TabsChildViewClickListener listener) {
        if (childViews == null)
            return;
        this.childClickListener = listener;
        textViews.clear();
        rootView.removeAllViews();
        int count = childViews.length;
        for (int i = 0; i < count; i++) {
            final TextView childView = (TextView) LayoutInflater.from(mContext)
                    .inflate(R.layout.item_tabs_view, null);
            childView.setText(childViews[i]);
            if (childClickListener != null) {
                final int temp = i;
                childView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentPosition != temp) {
                            currentPosition = temp;
                            initChildTextColor(temp);
                            childClickListener.onTabsChildViewCLick(temp);
                        }
                    }
                });
            }
            if (i != 0) {
                View line = new View(mContext);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(DensityUtils.dp2px(mContext, 1),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                line.setLayoutParams(lp);
                line.setBackgroundColor(mContext.getResources().getColor(R.color.theme_light_blue));
                rootView.addView(line);
            } else {
                childView.setTextColor(mContext.getResources().getColor(R.color.theme_light_blue));
            }
            textViews.add(childView);
            rootView.addView(childView);
        }
    }


    // 初始化文本颜色
    private void initChildTextColor(int position) {
        if (textViews.size() == 0)
            return;
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(mContext.getResources().getColor(R.color.black));
        }
        textViews.get(position).setTextColor(mContext.getResources().getColor(R.color.theme_light_blue));
    }


    /**
     * 子项点击监听
     */
    public interface TabsChildViewClickListener {
        // 根据点击位置进行不同操作
        void onTabsChildViewCLick(int position);
    }

}
