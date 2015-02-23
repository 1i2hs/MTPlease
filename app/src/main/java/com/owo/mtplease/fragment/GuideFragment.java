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

	private ViewPager guideImageViewPager;
	private GuideImageAdapter mGuideImageAdapter;

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

		guideImageViewPager = (ViewPager) guideFragmentView.findViewById(R.id.viewpager_image_guide);
		mGuideImageAdapter = new GuideImageAdapter(getActivity());
		guideImageViewPager.setAdapter(mGuideImageAdapter);

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
				R.drawable.scrn_guide_1,
				R.drawable.scrn_guide_2,
				R.drawable.scrn_guide_3,
				R.drawable.scrn_guide_4,
				R.drawable.scrn_guide_5
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
			if(position == 4)
				imageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mOnGuideFragmentListener.popGuideFragment();
					}
				});

			((ViewPager) container).addView(imageView, 0);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

}
