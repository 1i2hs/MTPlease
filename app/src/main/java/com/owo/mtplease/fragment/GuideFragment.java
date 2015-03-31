package com.owo.mtplease.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.owo.mtplease.Analytics;
import com.owo.mtplease.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GuideFragment.OnGuideFragmentListener} interface
 * to handle interaction events.
 * Use the {@link GuideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideFragment extends Fragment {

	private ViewPager _guideImageViewPager;
	private LinearLayout _guideIndicatorLinearLayout;
	private ImageView _closeButton;
	private GuideImageAdapter _mGuideImageAdapter;
	private OnGuideFragmentListener mOnGuideFragmentListener;

	public static GuideFragment newInstance() {
		GuideFragment fragment = new GuideFragment();
		return fragment;
	}

	public GuideFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View guideFragmentView = inflater.inflate(R.layout.fragment_guide, container, false);

		_guideIndicatorLinearLayout = (LinearLayout) guideFragmentView.findViewById(R.id.layout_indicators_guide);

		_closeButton = (ImageView) guideFragmentView.findViewById(R.id.imageView_close);
		_closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnGuideFragmentListener.popGuideFragment();
			}
		});

		for (int i = 0; i < 8; i++) {
			ImageView guideIndicator = new ImageView(getActivity());
			if (i == 0)
				guideIndicator.setImageResource(R.drawable.ic_indicator_selected);
			else
				guideIndicator.setImageResource(R.drawable.ic_indicator_not_selected);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, convertDpToPx(4, getActivity()), 0);

			guideIndicator.setLayoutParams(params);

			_guideIndicatorLinearLayout.addView(guideIndicator);
		}

		_guideImageViewPager = (ViewPager) guideFragmentView.findViewById(R.id.viewpager_image_guide);
		_mGuideImageAdapter = new GuideImageAdapter(getActivity());
		_guideImageViewPager.setAdapter(_mGuideImageAdapter);
		_guideImageViewPager.setOffscreenPageLimit(3);
		_guideImageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				View currentPictureIndicator = _guideIndicatorLinearLayout.getChildAt(position);
				((ImageView) currentPictureIndicator).setImageResource(R.drawable.ic_indicator_selected);

				if (position - 1 >= 0) {
					View leftPictureIndicator = _guideIndicatorLinearLayout.getChildAt(position - 1);
					((ImageView) leftPictureIndicator).setImageResource(R.drawable.ic_indicator_not_selected);
				}

				if (position + 1 < 8) {
					View leftPictureIndicator = _guideIndicatorLinearLayout.getChildAt(position + 1);
					((ImageView) leftPictureIndicator).setImageResource(R.drawable.ic_indicator_not_selected);
				}

				if(position == 7) {
					_closeButton.setVisibility(View.VISIBLE);
				} else {
					_closeButton.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		if(mOnGuideFragmentListener != null)
			mOnGuideFragmentListener.onCreateGuideFragmentView();

		return guideFragmentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnGuideFragmentListener = (OnGuideFragmentListener) activity;

			Tracker t = ((Analytics) getActivity().getApplication()).getTracker();
			t.setScreenName("Guide Page View");
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnGuideFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnGuideFragmentListener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mOnGuideFragmentListener.onDestroyGuideFragmentView();
	}

	public interface OnGuideFragmentListener {
		public void onCreateGuideFragmentView();
		public void popGuideFragment();
		public void onDestroyGuideFragmentView();
	}

	private class GuideImageAdapter extends PagerAdapter {
		Context mContext;

		private int[] GuideImages = new int[]{
				R.drawable.scrn_tutorial_1,
				R.drawable.scrn_tutorial_2,
				R.drawable.scrn_tutorial_3,
				R.drawable.scrn_tutorial_4,
				R.drawable.scrn_tutorial_5,
				R.drawable.scrn_tutorial_6,
				R.drawable.scrn_tutorial_7,
				R.drawable.scrn_tutorial_8
		};

		public GuideImageAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			return GuideImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView imageView = new ImageView(mContext);
			imageView.setImageResource(GuideImages[position]);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			((ViewPager) container).addView(imageView, 0);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

	private static int convertDpToPx(int dp, Context context) {
		float screenDensity = context.getResources().getDisplayMetrics().density;
		int px = (int)(dp * screenDensity);
		return px;
	}

}
