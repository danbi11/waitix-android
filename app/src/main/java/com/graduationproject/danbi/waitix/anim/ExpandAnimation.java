/*  Created by Edward Akoto on 12/31/12.
 *  Email akotoe@aua.ac.ke
 * 	Free for modification and distribution
 */

package com.graduationproject.danbi.waitix.anim;

import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

public class ExpandAnimation extends TranslateAnimation implements
		Animation.AnimationListener {

	private LinearLayout slidingLayout;
	int panelWidth;
	String isExpanded;

	public ExpandAnimation(LinearLayout layout, int width, String isExpanded,
						   int fromXType, float fromXValue, int toXType, float toXValue,
						   int fromYType, float fromYValue, int toYType, float toYValue) {

		super(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue,
				toYType, toYValue);
		this.isExpanded = isExpanded;
		// Initialize
		slidingLayout = layout;
		panelWidth = width;
		setDuration(300);
		setFillAfter(false);
		setInterpolator(new AccelerateDecelerateInterpolator());	//감속가속
		setAnimationListener(this);
		slidingLayout.startAnimation(this);
	}

	public void onAnimationEnd(Animation arg0) {

		// Create margin and align left
		LayoutParams params = (LayoutParams) slidingLayout.getLayoutParams();

		if (isExpanded.equals("left")) {
			params.leftMargin = panelWidth;
			params.gravity = Gravity.LEFT;

		} else {

			params.rightMargin = panelWidth;
			params.gravity = Gravity.RIGHT;

		}
		slidingLayout.clearAnimation();
		slidingLayout.setLayoutParams(params);
		slidingLayout.requestLayout();

	}

	public void onAnimationRepeat(Animation arg0) {

	}

	public void onAnimationStart(Animation arg0) {

	}

}
