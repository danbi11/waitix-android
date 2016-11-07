/*  Created by Edward Akoto on 12/31/12.
 *  Email akotoe@aua.ac.ke
 * 	Free for modification and distribution
 */

package com.graduationproject.danbi.waitix.anim;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

public class CloseAnimation extends TranslateAnimation implements
		TranslateAnimation.AnimationListener {

	private LinearLayout slidingLayout;
	int panelWidth;

	public CloseAnimation(LinearLayout layout, int width, int fromXType,
						  float fromXValue, int toXType, float toXValue, int fromYType,
						  float fromYValue, int toYType, float toYValue) {

		super(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue,
				toYType, toYValue);

		// Initialize
		slidingLayout = layout;
		panelWidth = width;
		setDuration(400);
		setFillAfter(false);
		setInterpolator(new AccelerateDecelerateInterpolator());
		setAnimationListener(this);

		// Clear left and right margins
		LayoutParams params = (LayoutParams) slidingLayout.getLayoutParams();
		params.rightMargin = 0;
		params.leftMargin = 0;
		slidingLayout.setLayoutParams(params);
		slidingLayout.requestLayout();
		slidingLayout.startAnimation(this);

		slidingLayout.setClickable(true);	//아래 깔린 메뉴 클릭 안되게

	}

	public void onAnimationEnd(Animation animation) {

	}

	public void onAnimationRepeat(Animation animation) {

	}

	public void onAnimationStart(Animation animation) {

	}

}
