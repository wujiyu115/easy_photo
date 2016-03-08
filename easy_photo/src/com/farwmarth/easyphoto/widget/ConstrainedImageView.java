package com.farwmarth.easyphoto.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ConstrainedImageView extends ImageView {
	public ConstrainedImageView(Context paramContext) {
		super(paramContext);
	}

	public ConstrainedImageView(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public ConstrainedImageView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	protected void onMeasure(int paramInt1, int paramInt2) {
		super.onMeasure(paramInt1, paramInt2);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
	}
}