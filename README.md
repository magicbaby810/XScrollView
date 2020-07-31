# XScrollView
### 信息流滑动效果 

如果某天产品要你仿下滴滴首页的滑动布局效果，可是扒遍全网也没有相仿的。
滴滴没开源，自己实现吧。
上手后，才发现，计算老TM复杂了

1. 算准布局初始化后展示的高度
2. 算准布局滑动触发到顶部titlebar的高度
3. 算准数据加载后，布局高度的各种变化
4. 适配各种屏幕
5. 处理touch事件的传递

最后，UI还要来波动效打击，脑细胞都快死光光了
不过我还是挺了过来，让大家不必再关心计算过程，只处理自己的布局和数据展示就行了

是不是你想要的，先看效果  


![image](http://third-eye.cn/1596211212353012-2.gif)

### XScrollView 三部分

1. TopLayout 

	demo中展示左边安全中心和右边定位按钮
	
2. BottomLayout
   
   demo中以用车类型和上下车为例	
   
3. RecyclerView
	
	demo中利用多形态item，展示进行中行程、优惠券套餐、和活动广告信息为例
	
#### 三部分均可自定义布局，XScrollView只负责承载和滑动功能。
<br />


## 使用

#### 必须设置

1. dependencies 下
	
	```java 
	implementation 'com.sk.xscrollview:xscrollview:0.0.4'
	```

2. xml中`layout_width`和`layout_height`必须是`match_parent`，必须撑满全屏。
	尽量不要放在垂直的线性布局中，`titlebar`会使`XScrollView`不能撑满全屏。

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
  
  
3. `xScrollView.setTitleBar`，需要把`TitleBar`传入`XScrollView`中，这样才能触发动效。照demo实现下。

4. 初始化  ```xScrollView.setInitItemViewListener(this);```
	
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
	
	上面5个实现后自行初始化对应的布局
	
	
	`animTopLayoutVisible`和`animTopLayoutGone`控制TopLayout布局在滑动到靠近	`TitleBar`时的显示和隐藏动效
	
	`animTitleBar`控制`TitleBar`布局在`TopLayout`靠近时的动效 
	
	> 动效可自行实现   
	
	<br />
	

### 自行设置 
1. `xScrollView.setItemOffsetValue`可以设置`RecyclerView`预展示部分高度
	
	  
<br /><br />
具体使用可参考demo中的实现方式。




#### 如需更多扩展，请提issues。

