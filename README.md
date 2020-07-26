# XScrollView
### 信息流滑动效果 


是不是你想要的，先看效果
[![image](https://github.com/magicbaby810/XScrollView/tree/master/app/src/main/res/raw/demo_video.gif)]

### XScrollView 三部分

1. TopLayout 

	demo中展示左边安全中心和右边定位按钮
	
2. BottomLayout
   
   demo中以用车类型和上下车为例	
   
3. RecyclerView
	
	demo中利用多形态item，展示进行中行程、优惠券套餐、和活动广告信息为例
	
#### 三部分均可自定义布局，XScrollView只负责承载和滑动功能。

## 使用
1. dependencies 下
	
	```java 
	implementation 'com.sk.xscrollview:xscrollview:0.0.1'
	```

1. xml中`layout_width`和`layout_height`必须是`match_parent`，请勿修改。

	```
	<com.sk.xscrollview.XScrollView
	        android:id="@+id/scroll_view"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        app:routeLayout="@layout/item_new_land_route"
	        app:couponLayout="@layout/item_new_land_coupon"
	        app:activityLayout="@layout/item_new_land_activities"
	        app:topLayout="@layout/layout_top_layout"
	        app:bottomLayout="@layout/layout_bottom_layout"
	        app:backgroundColor="@android:color/white"/>
	```   
	
	- `app:topLayout`: 自定义顶部布局（不是屏幕的顶部）
	- `app:bottomLayout`: 自定义底部布局（不是屏幕的底部）
	- `app:routeLayout`: 自定义未完成行程布局
	- `app:couponLayout`: 自定义优惠券套餐布局
	- `app:activityLayout`: 自定义活动广告布局
	- `app:backgroundColor`: 自定义从`BottomLayout`往下布局的背景色  
  

2. 初始化  ```
xScrollView.setInitItemViewListener(this);
```
	
	```
	public interface InitItemViewListener {
	
	        void initRouteView(XScrollViewHolder holder, int position);
	        void initCouponView(XScrollViewHolder holder, int position);
	        void initActivityView(XScrollViewHolder holder, int position);
	
	        void initTopLayoutView(View view);
	        void initBottomLayoutView(View view);
	
	        void animTopLayoutVisible(View view);
	        void animTopLayoutGone(View view);
	
	        void animTitleBar(boolean touchMoon);
	    }
	```    
	`InitItemViewListener`接口中，有8个抽象方法
	
	```
	void initRouteView(XScrollViewHolder holder, int position);
	void initCouponView(XScrollViewHolder holder, int position);
	void initActivityView(XScrollViewHolder holder, int position);
	
	void initTopLayoutView(View view);
	void initBottomLayoutView(View view);
	```
	实现后自行初始化对应的布局
	
	`animTopLayoutVisible`和`animTopLayoutGone`控制TopLayout布局在滑动到靠近TitleBar时的显示和隐藏动画
	
	`animTitleBar`控制TitleBar布局在TopLayout
	  
	  

具体使用可参考demo中的实现方式。




#### 如需更多扩展，请提issues。

