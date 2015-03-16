package com.owo.mtplease;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.owo.mtplease.view.TypefaceLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by In-Ho on 2015-01-14.
 */
public class SpecificInfoRoomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "SIR_RecyclerViewAdapter";
	private static final int REAL_PICTURE_DOESNT_EXISTS = 0;
	private static final int REAL_PICTURE_EXISTS = 1;

	// Series of cards
	private static final int CARD_IMAGE_VIEW_PAGER = 0;
	private static final int CARD_PRICE_AND_RATE = 1;
	private static final int CARD_PHONE_AND_KAKAOTALK_BUTTONS = 2;
	private static final int CARD_BASIC_INFO = 3;
	private static final int CARD_PENSION_NOTICE = 4;
	private static final int CARD_OPTIONS = 5;
	private static final int CARD_ADDRESS_AND_ROUTE_AND_MAP = 6;
	private static final int CARD_PRICE_AND_DATE_SELECTION = 7;
	private static final int CARD_OWNER_INFO = 8;
	//	private static final int CARD_VISITED_SCHOOLS = 5;
//	private static final int CARD_REVIEWS = 6;
	private static final int CARD_EQUIPMENT = 9;
	private static final int CARD_FACILITY_AND_SERVICE = 10;
	private static final int CARD_CAUTIONS = 11;
	// End of series of cards

	private static View _basicInfoCardView;

	// Model: Data variables for User Interface Views
	private static RoomInfoModelController mRoomInfoModelController;
	// End of the Model

	// others
	private Context mContext;
	private static String imageUrl;

	// Naver Map API Key
	private static final String NAVER_MAP_API_KEY = "d95aa436e270994d04d2fcc5bffdb1cb";
	// Naver related instances
	private NMapView mMapView;
	private NMapController mMapController;
		/*NMapViewerResourceProvider mMapViewerResourceProvider = null;
		NMapOverlayManager mOverlayManager;*/

	public SpecificInfoRoomRecyclerViewAdapter(Context context, RoomInfoModelController roomInfoModelController) {
		Log.d(TAG, TAG);
		mContext = context;
		mRoomInfoModelController = roomInfoModelController;

		preLoadImages();
	}

	private void preLoadImages() {
		for(int i = 1; i <= mRoomInfoModelController.getNum_images(); i++) {
				/*imageUrl = mContext.getResources().getString(R.string.mtplease_url) + "img/pensions/" + mRoomInfoModelController.getPen_id() + "/"
						+ URLEncoder.encode(mRoomInfoModelController.getRoom_name(), "utf-8").replaceAll("\\+", "%20");*/

			imageUrl = mContext.getResources().getString(R.string.mtplease_url_temp) + "pensions/" + mRoomInfoModelController.getPen_id() + "/"
					+ mRoomInfoModelController.getRoom_id();

			if (mRoomInfoModelController.getRoom_picture_flag() == REAL_PICTURE_EXISTS)
				imageUrl += "/real/" + i + ".JPG";
			else
				imageUrl += "/unreal/" + i + ".JPG";

			Log.d(TAG, imageUrl);

			if(!ServerCommunicationManager.getInstance(mContext).containsImage(imageUrl)) {
				ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						ServerCommunicationManager.getInstance(mContext).putBitmap(imageUrl, response);
					}
				}, 0, 0, null, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});

				ServerCommunicationManager.getInstance(mContext).addToRequestQueue(imageRequest);
			}
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int cardViewType) {
		Log.d(TAG, "onCreateViewHolder");

		View cardView;

		switch(cardViewType) {
			case CARD_IMAGE_VIEW_PAGER:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_pager_room_images, parent, false);
				return new RoomImageViewPagerCard(cardView, mContext);
			case CARD_PRICE_AND_RATE:
				if(mRoomInfoModelController.getRoom_cost() == 0) {
					cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_blank, parent, false);
					return new BlankCard(cardView);
				} else {
					cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_price_and_rate, parent, false);
					return new PriceAndRateCard(cardView, mContext);
				}
			case CARD_PHONE_AND_KAKAOTALK_BUTTONS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_phone_and_kakaotalk_buttons, parent, false);
				return new PhoneAndKakaotalkButtonsCard(cardView, mContext);
			case CARD_BASIC_INFO:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basic_info, parent, false);
				_basicInfoCardView = cardView;
				return new BasicInfoCard(cardView, mContext);
			case CARD_PENSION_NOTICE:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pension_notice, parent, false);
				return new PensionNoticeCard(cardView, mContext);
			case CARD_OPTIONS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_options_temp, parent, false);
				return new OptionsCard(cardView, mContext);
			/*case CARD_VISITED_SCHOOLS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_visited_schools, parent, false);
				return new VisitedSchoolsCard(cardView, mContext);
			case CARD_REVIEWS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reviews, parent, false);
				return new ReviewsCard(cardView, mContext);*/
			case CARD_ADDRESS_AND_ROUTE_AND_MAP:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_address_and_route_and_map, parent, false);
				return new AddressAndRouteAndMapCard(cardView, mContext);
			case CARD_PRICE_AND_DATE_SELECTION:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_price_and_period, parent, false);
				return new PriceAndDateSelectionCard(cardView, mContext);
			case CARD_OWNER_INFO:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_owner_info, parent, false);
				return new OwnerInfoCard(cardView, mContext);
			case CARD_EQUIPMENT:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_equipment, parent, false);
				return new EquipmentCard(cardView, mContext);
			case CARD_FACILITY_AND_SERVICE:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_facility_and_service, parent, false);
				return new FacilityAndServiceCard(cardView, mContext);
			case CARD_CAUTIONS:
				cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cautions, parent, false);
				return new CautionsCard(cardView, mContext);
		}

		throw new RuntimeException("there is no type that matches the type " + cardViewType + "make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder cardViewHolder, int position) {
		Log.d(TAG, "onBindViewHolder");
		try {
			switch (position) {
				case CARD_IMAGE_VIEW_PAGER:
					((RoomImageViewPagerCard) cardViewHolder).setRoomImages();
					break;
				case CARD_PRICE_AND_RATE:
					if(mRoomInfoModelController.getRoom_cost() != 0)
						((PriceAndRateCard) cardViewHolder).setComponents();
					break;
				case CARD_PHONE_AND_KAKAOTALK_BUTTONS:
					((PhoneAndKakaotalkButtonsCard) cardViewHolder).setComponents();
					break;
				case CARD_BASIC_INFO:
					((BasicInfoCard) cardViewHolder).setComponents();
					break;
				case CARD_PENSION_NOTICE:
					((PensionNoticeCard) cardViewHolder).setComponents();
					break;
				case CARD_OPTIONS:
					((OptionsCard) cardViewHolder).setComponents();
					break;
				/*case CARD_VISITED_SCHOOLS:
					((VisitedSchoolsCard) cardViewHolder).setComponents();
					break;
				case CARD_REVIEWS:
					((ReviewsCard) cardViewHolder).setComponents();
					break;*/
				case CARD_ADDRESS_AND_ROUTE_AND_MAP:
					((AddressAndRouteAndMapCard) cardViewHolder).setComponents();
					break;
				case CARD_PRICE_AND_DATE_SELECTION:
					((PriceAndDateSelectionCard) cardViewHolder).setComponents();
					break;
				case CARD_OWNER_INFO:
					((OwnerInfoCard) cardViewHolder).setComponents();
					break;
				case CARD_EQUIPMENT:
					((EquipmentCard) cardViewHolder).setComponents();
					break;
				case CARD_FACILITY_AND_SERVICE:
					((FacilityAndServiceCard) cardViewHolder).setComponents();
					break;
				case CARD_CAUTIONS:
					((CautionsCard) cardViewHolder).setComponents();
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return 12;
	}

	@Override
	public int getItemViewType(int position) {
		switch(position) {
			case 0:
				return CARD_IMAGE_VIEW_PAGER;
			case 1:
				return CARD_PRICE_AND_RATE;
			case 2:
				return CARD_PHONE_AND_KAKAOTALK_BUTTONS;
			case 3:
				return CARD_BASIC_INFO;
			case 4:
				return CARD_PENSION_NOTICE;
			case 5:
				return CARD_OPTIONS;
/*			case 5:
				return CARD_VISITED_SCHOOLS;
			case 6:
				return CARD_REVIEWS;*/
			case 6:
				return CARD_ADDRESS_AND_ROUTE_AND_MAP;
			case 7:
				return CARD_PRICE_AND_DATE_SELECTION;
			case 8:
				return CARD_OWNER_INFO;
			case 9:
				return CARD_EQUIPMENT;
			case 10:
				return CARD_FACILITY_AND_SERVICE;
			case 11:
				return CARD_CAUTIONS;
			default:
				return position;
		}
	}

	private static class RoomImageViewPagerCard extends RecyclerView.ViewHolder {
		private ViewPager roomImageViewPager;
		private ImageView realPictureSticker;
		private ImageView prevPictureIndicator;
		private ImageView nextPictureIndicator;
		private LinearLayout roomPictureIndicatorLinearLayout;
		private PictureCarouselAdapter roomImageCarouselAdapter;
		private Context mContext;

		private boolean isRoomImageCarouselVisible = false;

		public RoomImageViewPagerCard(View cardView, Context context) {
			super(cardView);
			roomImageViewPager = (ViewPager) cardView.findViewById(R.id.view_pager_room_images);
			realPictureSticker = (ImageView) cardView.findViewById(R.id.imageView_sticker_real);
			realPictureSticker.setImageResource(R.drawable.ic_real_picture_sticker);
			prevPictureIndicator = (ImageView) cardView.findViewById(R.id.imageView_prev);
			prevPictureIndicator.setAlpha(0.0F);
			nextPictureIndicator = (ImageView) cardView.findViewById(R.id.imageView_next);
			nextPictureIndicator.setAlpha(1.0F);
			roomPictureIndicatorLinearLayout = (LinearLayout) cardView.findViewById(R.id.layout_indicators_picture_room);
			mContext = context;
		}

		public void setRoomImages() {
			if (!isRoomImageCarouselVisible) {
				final int numPicture = mRoomInfoModelController.getNum_images();
				roomImageCarouselAdapter = new PictureCarouselAdapter(mContext, numPicture);
				roomImageViewPager.setAdapter(roomImageCarouselAdapter);
				roomImageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageSelected(int position) {
						View currentPictureIndicator = roomPictureIndicatorLinearLayout.getChildAt(position);
						((ImageView) currentPictureIndicator).setImageResource(R.drawable.ic_indicator_selected);

						if (position - 1 >= 0) {
							View leftPictureIndicator = roomPictureIndicatorLinearLayout.getChildAt(position - 1);
							((ImageView) leftPictureIndicator).setImageResource(R.drawable.ic_indicator_not_selected);
						}

						if (position + 1 < numPicture) {
							View leftPictureIndicator = roomPictureIndicatorLinearLayout.getChildAt(position + 1);
							((ImageView) leftPictureIndicator).setImageResource(R.drawable.ic_indicator_not_selected);
						}

						if (position == 0) {
							prevPictureIndicator.setAlpha(0.0F);
							nextPictureIndicator.setAlpha(1.0F);
						} else if (position == numPicture - 1) {
							prevPictureIndicator.setAlpha(1.0F);
							nextPictureIndicator.setAlpha(0.0F);
						} else {
							prevPictureIndicator.setAlpha(1.0F);
							nextPictureIndicator.setAlpha(1.0F);
						}
						prevPictureIndicator.animate().alpha(0);
						nextPictureIndicator.animate().alpha(0);
					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});

				for (int i = 0; i < numPicture; i++) {
					ImageView pictureIndicator = new ImageView(mContext);
					if (i == 0)
						pictureIndicator.setImageResource(R.drawable.ic_indicator_selected);
					else
						pictureIndicator.setImageResource(R.drawable.ic_indicator_not_selected);

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 0, convertDpToPx(4, mContext), 0);

					pictureIndicator.setLayoutParams(params);

					roomPictureIndicatorLinearLayout.addView(pictureIndicator);
				}
				isRoomImageCarouselVisible = true;
			}
		}
	}

	private static class PictureCarouselAdapter extends PagerAdapter {
		private ImageView roomImage;
		private Context mContext;
		private int pictureCount;

		private PictureCarouselAdapter(Context mContext, int pictureCount) {
			this.mContext = mContext;
			this.pictureCount = pictureCount;
		}

		@Override
		public int getCount() {
			return pictureCount;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

				/*imageUrl = mContext.getResources().getString(R.string.mtplease_url) + "img/pensions/" + mRoomInfoModelController.getPen_id() + "/"
						+ URLEncoder.encode(mRoomInfoModelController.getRoom_name(), "utf-8").replaceAll("\\+", "%20");*/

			imageUrl = mContext.getResources().getString(R.string.mtplease_url_temp) + "pensions/" + mRoomInfoModelController.getPen_id() + "/"
					+ mRoomInfoModelController.getRoom_id();

			if (mRoomInfoModelController.getRoom_picture_flag() == REAL_PICTURE_EXISTS)
				imageUrl += "/real/" + (position + 1) + ".JPG";
			else
				imageUrl += "/unreal/" + (position + 1) + ".JPG";

			Log.d(TAG, imageUrl);

			roomImage = new ImageView(mContext);
			roomImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
			roomImage.setImageResource(R.drawable.scrn_room_img_place_holder);

			ServerCommunicationManager.getInstance(mContext).getImage(imageUrl,
					new ImageLoader.ImageListener() {
						@Override
						public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
							if(response.getBitmap() != null) {
								roomImage.setAlpha(0.0F);
								roomImage.setImageBitmap(response.getBitmap());
								roomImage.animate().alpha(1.0F);
								/*if(mRoomInfoModelController.getRoom_picture_flag() == REAL_PICTURE_EXISTS) {
									roomImage.setImageResource(R.drawable.ic_real_picture_sticker);
									roomImage.animate().alpha(1.0F);
								}*/
							} else {
								roomImage.setImageResource(R.drawable.scrn_room_img_place_holder);
							}
						}

						@Override
						public void onErrorResponse(VolleyError error) {
							roomImage.setImageResource(R.drawable.scrn_room_img_error);
						}
					});

			container.addView(roomImage, 0);
			return roomImage;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((ImageView) object);
		}
	}

	private static class BlankCard extends RecyclerView.ViewHolder {
		private BlankCard(View itemView) {
			super(itemView);
		}
	}

	private static class PriceAndRateCard extends RecyclerView.ViewHolder {
		private TextView roomPriceTextView;
		private Context mContext;

		public void setComponents() {
			Log.d(TAG, mRoomInfoModelController.getRoom_cost() + "원");
			if(mRoomInfoModelController.getRoom_cost() == 0)
				roomPriceTextView.setText(R.string.telephone_inquiry);
			else
				roomPriceTextView.setText(castItemPriceToString(mRoomInfoModelController.getRoom_cost(), mContext));
		}

		public PriceAndRateCard(View cardView, Context context) {
			super(cardView);
			roomPriceTextView = (TextView) cardView.findViewById(R.id.text_price_room);
			roomPriceTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			mContext= context;
		}
	}

	private static class PhoneAndKakaotalkButtonsCard extends RecyclerView.ViewHolder {
		private View phoneAndKakaotalkButtonsCardView;
		private FrameLayout contactButton;
		private FrameLayout kakaoTalkButton;
		private FrameLayout callMtpleaseButton;
		private FrameLayout callPensionButton;

		private Context mContext;

		private boolean isContactButtonClicked = false;

		public void setComponents() {
			contactButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!isContactButtonClicked) {
						expand(phoneAndKakaotalkButtonsCardView, convertDpToPx(72 + 4 + 50 + 4, mContext), contactButton);

						callMtpleaseButton.animate().setListener(null);
						callPensionButton.animate().setListener(null);
						callMtpleaseButton.setAlpha(0.0F);
						callMtpleaseButton.setVisibility(View.VISIBLE);
						callMtpleaseButton.animate().alpha(1.0F);
						callPensionButton.setAlpha(0.0F);
						callPensionButton.setVisibility(View.VISIBLE);
						callPensionButton.animate().alpha(1.0F);
						callPensionButton.animate().translationYBy(convertDpToPx(72 + 4, mContext));

						isContactButtonClicked = true;
					} else {
						callMtpleaseButton.animate().alpha(0.0F).
								setListener(new Animator.AnimatorListener() {
									@Override
									public void onAnimationStart(Animator animation) {

									}

									@Override
									public void onAnimationEnd(Animator animation) {
										callMtpleaseButton.setVisibility(View.GONE);
									}

									@Override
									public void onAnimationCancel(Animator animation) {

									}

									@Override
									public void onAnimationRepeat(Animator animation) {

									}
								});

						callPensionButton.animate().alpha(0.0F);
						callPensionButton.animate().translationYBy(-(convertDpToPx(72 + 4, mContext))).
								setListener(new Animator.AnimatorListener() {
									@Override
									public void onAnimationStart(Animator animation) {

									}

									@Override
									public void onAnimationEnd(Animator animation) {
										callPensionButton.setVisibility(View.GONE);
									}

									@Override
									public void onAnimationCancel(Animator animation) {

									}

									@Override
									public void onAnimationRepeat(Animator animation) {

									}
								});

						collapse(phoneAndKakaotalkButtonsCardView, convertDpToPx(72 + 4 + 50 + 4, mContext), contactButton);

						isContactButtonClicked = false;
					}
				}
			});
			kakaoTalkButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Uri webLink = Uri.parse(mRoomInfoModelController.getPen_homepage());
					Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
					mContext.startActivity(webBrowseIntent);*/
					Uri webLink = Uri.parse("http://goto.kakao.com/@%EC%97%A0%ED%8B%B0%EB%A5%BC%EB%B6%80%ED%83%81%ED%95%B4");
					Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
					mContext.startActivity(webBrowseIntent);
				}
			});

			callMtpleaseButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Uri phoneNumber = Uri.parse("tel:" + "01092055132");
					Intent contactIntent = new Intent(Intent.ACTION_DIAL, phoneNumber);
					mContext.startActivity(contactIntent);
				}
			});

			callPensionButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast toast = Toast.makeText(mContext, R.string.please_say_mtplease, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, convertDpToPx(150, mContext));
					toast.show();

					Uri phoneNumber = Uri.parse("tel:" + mRoomInfoModelController.getPen_phone1());
					Intent contactIntent = new Intent(Intent.ACTION_DIAL, phoneNumber);
					mContext.startActivity(contactIntent);
				}
			});
		}
		public PhoneAndKakaotalkButtonsCard(View cardView, Context context) {
			super(cardView);
			phoneAndKakaotalkButtonsCardView = cardView;
			contactButton = (FrameLayout) cardView.findViewById(R.id.btn_contact);
			kakaoTalkButton = (FrameLayout) cardView.findViewById(R.id.btn_kakaotalk_yellow_id);
			callMtpleaseButton = (FrameLayout) cardView.findViewById(R.id.btn_call_mtplease);
			callPensionButton = (FrameLayout) cardView.findViewById(R.id.btn_call_pension);

			mContext = context;
		}
	}

	public static void expand(final View v, final int targetHeight, final View clickedButton) {
		v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height = initialHeight + (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				clickedButton.setClickable(false);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				clickedButton.setClickable(true);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		a.setDuration(500);
		v.startAnimation(a);
	}

	public static void collapse(final View v, final int heightToBeDecreased, final View clickedButton) {
		v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height =  initialHeight - (int) (heightToBeDecreased * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				clickedButton.setClickable(false);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				clickedButton.setClickable(true);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

		a.setDuration(500);
		v.startAnimation(a);
	}

	private static class BasicInfoCard extends RecyclerView.ViewHolder {
		private TextView basicInfoTitleTextView;
		private TextView checkinTimeTextView;
		private TextView checkoutTimeTextView;
		private TextView numberRoomsTextView;
		private TextView numberToiletsTextView;
		private TextView numberPeopleStdTextView;
		private TextView numberPeopleMaxTextView;
		private TextView structureRoomTextView;
		private TextView sizeRoomTextView;

		private Context mContext;

		public void setComponents() {
			if(!mRoomInfoModelController.getPen_checkin().equals("null"))
				checkinTimeTextView.setText(mContext.getResources().getString(R.string.checkin_time) + "  " + mRoomInfoModelController.getPen_checkin());
			else
				checkinTimeTextView.setText("-");

			if(!mRoomInfoModelController.getPen_checkout().equals("null"))
				checkoutTimeTextView.setText(mContext.getResources().getString(R.string.checkout_time) + "  " + mRoomInfoModelController.getPen_checkout());
			else
				checkoutTimeTextView.setText("-");

			numberRoomsTextView.setText(mContext.getResources().getString(R.string.number_of_rooms) + "  " + mRoomInfoModelController.getNum_rooms());
			numberToiletsTextView.setText(mContext.getResources().getString(R.string.number_of_toilets) + "  " + mRoomInfoModelController.getNum_toilets());
			numberPeopleStdTextView.setText(mContext.getResources().getString(R.string.std_number_of_people) + "  " + mRoomInfoModelController.getRoom_std_people());
			numberPeopleMaxTextView.setText(mContext.getResources().getString(R.string.max_number_of_people) + "  " + mRoomInfoModelController.getRoom_max_people());

			if(!mRoomInfoModelController.getRoom_description().equals("null"))
				structureRoomTextView.setText(mContext.getResources().getString(R.string.structure_of_room) + "  " + mRoomInfoModelController.getRoom_description());
			else
				structureRoomTextView.setText(mContext.getResources().getString(R.string.structure_of_room) + " - ");

			if(!mRoomInfoModelController.getRoom_pyeong().equals("null"))
				sizeRoomTextView.setText(mContext.getResources().getString(R.string.size_of_room) + "  " + mRoomInfoModelController.getRoom_pyeong());
			else
				sizeRoomTextView.setText(mContext.getResources().getString(R.string.size_of_room) + " - ");
		}

		public BasicInfoCard(View cardView, Context context) {
			super(cardView);
			basicInfoTitleTextView = (TextView) cardView.findViewById(R.id.textView_info_basic);
			basicInfoTitleTextView.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			checkinTimeTextView = (TextView) cardView.findViewById(R.id.textView_time_checkin);
			checkinTimeTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			checkoutTimeTextView = (TextView) cardView.findViewById(R.id.textView_time_checkout);
			checkoutTimeTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			numberRoomsTextView = (TextView) cardView.findViewById(R.id.textView_num_rooms);
			numberRoomsTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			numberToiletsTextView = (TextView) cardView.findViewById(R.id.textView_num_toilets);
			numberToiletsTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			numberPeopleStdTextView = (TextView) cardView.findViewById(R.id.textView_people_number_std);
			numberPeopleStdTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			numberPeopleMaxTextView = (TextView) cardView.findViewById(R.id.textView_people_number_max);
			numberPeopleMaxTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			structureRoomTextView = (TextView) cardView.findViewById(R.id.textView_structure_room);
			structureRoomTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			sizeRoomTextView = (TextView) cardView.findViewById(R.id.textView_size_room);
			sizeRoomTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			mContext = context;
		}
	}

	private static class PensionNoticeCard extends RecyclerView.ViewHolder {
		private TextView pensionDescriptionTitleTextView;
		private LinearLayout pensionDescriptionLinearLayout;
		private boolean isComponentsLoaded = false;
		private Context mContext;

		public void setComponents() {
			if(!isComponentsLoaded) {
				if (!mRoomInfoModelController.getPen_description().equals("null")) {
					String pensionNoticeString = mRoomInfoModelController.getPen_description();
					String[] pensionNoticeStringList = pensionNoticeString.split("/");

					for (int i = 0; i < pensionNoticeStringList.length; i++) {
						TextView pensionDescriptionTextView = new TextView(mContext);
						pensionDescriptionTextView.setText("> " + pensionNoticeStringList[i]);

						LinearLayout.LayoutParams params
								= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(0, 0, 0, convertDpToPx(4, mContext));

						pensionDescriptionTextView.setLayoutParams(params);
						pensionDescriptionLinearLayout.addView(pensionDescriptionTextView);
					}
				} else {
					TextView pensionDescriptionTextView = new TextView(mContext);
					pensionDescriptionTextView.setText("-");

					pensionDescriptionLinearLayout.addView(pensionDescriptionTextView);
				}
				isComponentsLoaded = true;
			}
		}

		public PensionNoticeCard(View cardView, Context context) {
			super(cardView);
			pensionDescriptionTitleTextView = (TextView) cardView.findViewById(R.id.textView_title_pension_description);
			pensionDescriptionTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			pensionDescriptionLinearLayout = (LinearLayout) cardView.findViewById(R.id.layout_description_pension);
			mContext = context;
		}

	}

	private static class OptionsCard extends RecyclerView.ViewHolder {
		private TextView optionTitle;
		private ImageView playgroundIconImageView;
		private TextView playgroundTextView;
		private TextView playgroundTypeTextView;
		private TextView playgroundDescriptionTextView;
		private ImageView pickupIconImageView;
		private TextView pickupTextView;
		private TextView pickupPriceTextView;
		private TextView pickupLocationTextView;
		private TextView pickupDescriptionTextView;
		private ImageView barbecueIconImageView;
		private TextView barbecueTextView;
		private TextView barbecuePriceTextView;
		private TextView barbecueComponentTextView;
		private TextView barbecueLocationTextView;
		private TextView barbecueDescriptionTextView;
		private ImageView valleyIconImageView;
		private TextView valleyTextView;
		private TextView valleyDistanceTextView;
		private TextView valleyDepthTextView;
		private TextView valleyDescriptionTextView;


		public void setComponents() {
			if(mRoomInfoModelController.getPen_ground() != 1)
				playgroundIconImageView.setImageResource(R.drawable.ic_no_playground);

			if(mRoomInfoModelController.getPen_pickup() != 1)
				pickupIconImageView.setImageResource(R.drawable.ic_no_pick_up);

			if(mRoomInfoModelController.getPen_barbecue() != 1)
				barbecueIconImageView.setImageResource(R.drawable.ic_no_barbecue);

			if(mRoomInfoModelController.getPen_valley() != 1)
				valleyIconImageView.setImageResource(R.drawable.ic_no_valley);

			if(!mRoomInfoModelController.getPen_ground_type().equals("null"))
				playgroundTypeTextView.setText(mRoomInfoModelController.getPen_ground_type());
			else
				playgroundTypeTextView.setText("-");

			if(!mRoomInfoModelController.getPen_ground_description().equals("null"))
				playgroundDescriptionTextView.setText(mRoomInfoModelController.getPen_ground_description());
			else
				playgroundDescriptionTextView.setText("-");

			if(!mRoomInfoModelController.getPen_pickup_cost().equals("null"))
				pickupPriceTextView.setText(mRoomInfoModelController.getPen_pickup_cost());
			else
				pickupPriceTextView.setText("-");

			if(!mRoomInfoModelController.getPen_pickup_location().equals("null"))
				pickupLocationTextView.setText(mRoomInfoModelController.getPen_pickup_location());
			else
				pickupLocationTextView.setText("-");

			if(!mRoomInfoModelController.getPen_pickup_description().equals("null"))
				pickupDescriptionTextView.setText(mRoomInfoModelController.getPen_pickup_description());
			else
				pickupDescriptionTextView.setText("-");

			if(!mRoomInfoModelController.getPen_barbecue_cost().equals("null"))
				barbecuePriceTextView.setText(mRoomInfoModelController.getPen_barbecue_cost());
			else
				barbecuePriceTextView.setText("-");

			if(!mRoomInfoModelController.getPen_barbecue_component().equals("null"))
				barbecueComponentTextView.setText(mRoomInfoModelController.getPen_barbecue_component());
			else
				barbecueComponentTextView.setText("-");

			if(!mRoomInfoModelController.getPen_barbecue_location().equals("null"))
				barbecueLocationTextView.setText(mRoomInfoModelController.getPen_barbecue_location());
			else
				barbecueLocationTextView.setText("-");

			if(!mRoomInfoModelController.getPen_barbecue_description().equals("null"))
				barbecueDescriptionTextView.setText(mRoomInfoModelController.getPen_barbecue_description());
			else
				barbecueDescriptionTextView.setText("-");

			if(!mRoomInfoModelController.getPen_valley_distance().equals("null"))
				valleyDistanceTextView.setText(mRoomInfoModelController.getPen_valley_distance());
			else
				valleyDistanceTextView.setText("-");

			if(!mRoomInfoModelController.getPen_valley_depth().equals("null"))
				valleyDepthTextView.setText(mRoomInfoModelController.getPen_valley_depth());
			else
				valleyDepthTextView.setText("-");

			if(!mRoomInfoModelController.getPen_valley_description().equals("null"))
				valleyDescriptionTextView.setText(mRoomInfoModelController.getPen_valley_description());
			else
				valleyDescriptionTextView.setText("-");
		}

		public OptionsCard(View cardView, Context context) {
			super(cardView);
			optionTitle = (TextView) cardView.findViewById(R.id.textView_title_option);
			optionTitle.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			playgroundIconImageView = (ImageView) cardView.findViewById(R.id.imageView_option_playground_specific_info);
			playgroundTextView = (TextView) cardView.findViewById(R.id.textView_playground);
			playgroundTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			playgroundTypeTextView = (TextView) cardView.findViewById(R.id.textView_type_playground_specific_info);
			playgroundDescriptionTextView = (TextView) cardView.findViewById(R.id.textView_description_playground_specific_info);
			pickupIconImageView = (ImageView) cardView.findViewById(R.id.imageView_option_pickup_specific_info);
			pickupTextView = (TextView) cardView.findViewById(R.id.textView_pickup);
			pickupTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			pickupPriceTextView = (TextView) cardView.findViewById(R.id.textView_price_pickup_specific_info);
			pickupLocationTextView = (TextView) cardView.findViewById(R.id.textView_location_pickup_specific_info);
			pickupDescriptionTextView = (TextView) cardView.findViewById(R.id.textView_description_pickup_specific_info);
			barbecueIconImageView = (ImageView) cardView.findViewById(R.id.imageView_option_barbecue_specific_info);
			barbecueTextView = (TextView) cardView.findViewById(R.id.textView_barbecue);
			barbecueTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			barbecuePriceTextView = (TextView) cardView.findViewById(R.id.textView_price_barbecue_specific_info);
			barbecueComponentTextView = (TextView) cardView.findViewById(R.id.textView_component_barbecue_specific_info);
			barbecueLocationTextView = (TextView) cardView.findViewById(R.id.textView_location_barbecue_specific_info);
			barbecueDescriptionTextView = (TextView) cardView.findViewById(R.id.textView_description_barbecue_specific_info);
			valleyIconImageView = (ImageView) cardView.findViewById(R.id.imageView_option_valley_specific_info);
			valleyTextView = (TextView) cardView.findViewById(R.id.textView_valley);
			valleyTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			valleyDistanceTextView = (TextView) cardView.findViewById(R.id.textView_distance_valley_specific_info);
			valleyDepthTextView = (TextView) cardView.findViewById(R.id.textView_depth_valley_specific_info);
			valleyDescriptionTextView = (TextView) cardView.findViewById(R.id.textView_description_valley_specific_info);
		}
	}

	private static class VisitedSchoolsCard extends RecyclerView.ViewHolder {

		public void setComponents() {

		}

		public VisitedSchoolsCard(View cardView, Context context) {
			super(cardView);
		}
	}

	private class ReviewsCard extends RecyclerView.ViewHolder {

		public void setComponents() {

		}

		public ReviewsCard(View cardView, Context context) {
			super(cardView);
		}
	}

	private static class AddressAndRouteAndMapCard extends RecyclerView.ViewHolder {
		private TextView routeTitleTextView;
		private Button lookLargerMapButton;
		private TextView pensionRoadAddressTitleTextView;
		private TextView pensionRoadAddress;
		private TextView pensionParcelAddressTitleTextView;
		private TextView pensionParcelAddressTextView;
		private WebView mapWebView;
		private TextView walkFromStationTitleTextView;
		private TextView walkFromStationTimeTextView;
		private TextView walkFromTerminalTitleTextView;
		private TextView walkFromTerminalTimeTextView;
		private Context mContext;

		public void setComponents() {
			final String pensionLocationURL = "http://m.map.naver.com/map.nhn?lng=" + mRoomInfoModelController.getPen_longitude()
					+ "&lat=" + mRoomInfoModelController.getPen_latitude() +"&dlevel=11";

			lookLargerMapButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Uri webLink = Uri.parse(pensionLocationURL);
					Intent webBrowseIntent = new Intent(Intent.ACTION_VIEW, webLink);
					mContext.startActivity(webBrowseIntent);
				}
			});
			if(!mRoomInfoModelController.getPen_road_adr().equals("null"))
				pensionRoadAddress.setText(mRoomInfoModelController.getPen_road_adr());
			else
				pensionRoadAddress.setText("-");
			if(!mRoomInfoModelController.getPen_lot_adr().equals("null"))
				pensionParcelAddressTextView.setText(mRoomInfoModelController.getPen_lot_adr());
			else
				pensionParcelAddressTextView.setText("-");

			WebSettings webSettings = mapWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			mapWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			mapWebView.setWebViewClient(new WebViewClient());
			mapWebView.setWebChromeClient(new WebChromeClient());
			mapWebView.loadUrl(pensionLocationURL);

			if(!mRoomInfoModelController.getPen_walk_station().equals("null"))
				walkFromStationTimeTextView.setText(mRoomInfoModelController.getPen_walk_station());
			else
				walkFromStationTimeTextView.setText("-");

			if(!mRoomInfoModelController.getPen_walk_terminal().equals("null"))
				walkFromTerminalTimeTextView.setText(mRoomInfoModelController.getPen_walk_terminal());
			else
				walkFromTerminalTimeTextView.setText("-");
		}

		/*private String changeTimeStringIntoKoreanTimeFormat(String timeString) {
			String timeStringChanged = "";

			Log.d(TAG, timeString);
			if(timeString.length() > 0) {
				int charCounter = 0;
				for (int i = timeString.length() - 1; i >= 0; i--) {
					if(charCounter == 0)
						timeStringChanged += mContext.getResources().getString(R.string.second) + timeString.charAt(i);
					else if(charCounter == 2)
						timeStringChanged += mContext.getResources().getString(R.string.minute) + timeString.charAt(i);
					else
						timeStringChanged += timeString.charAt(i) ;
					charCounter++;
				}
			}
			return new StringBuffer(timeStringChanged).reverse().toString();
		}*/

		public AddressAndRouteAndMapCard(View cardView, Context context) {
			super(cardView);
			routeTitleTextView = (TextView) cardView.findViewById(R.id.textView_title_route);
			routeTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			lookLargerMapButton = (Button) cardView.findViewById(R.id.btn_look_larger_map);
			lookLargerMapButton.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			pensionRoadAddress = (TextView) cardView.findViewById(R.id.textView_address_road);
			pensionRoadAddress.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			pensionRoadAddressTitleTextView = (TextView) cardView.findViewById(R.id.textView_title_address_road);
			pensionRoadAddressTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			pensionParcelAddressTextView = (TextView) cardView.findViewById(R.id.textView_address_parcel);
			pensionParcelAddressTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			pensionParcelAddressTitleTextView = (TextView) cardView.findViewById(R.id.textView_title_address_parcel);
			pensionParcelAddressTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			mapWebView = (WebView) cardView.findViewById(R.id.webView_map);
			walkFromStationTitleTextView = (TextView) cardView.findViewById(R.id.textView_from_train_station);
			walkFromStationTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			walkFromStationTimeTextView = (TextView) cardView.findViewById(R.id.textView_walk_station);
			walkFromStationTimeTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			walkFromTerminalTitleTextView = (TextView) cardView.findViewById(R.id.textView_from_bus_terminal);
			walkFromTerminalTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			walkFromTerminalTimeTextView = (TextView) cardView.findViewById(R.id.textView_walk_terminal);
			walkFromTerminalTimeTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			mContext = context;
		}
	}

	private static class PriceAndDateSelectionCard extends RecyclerView.ViewHolder {
		private TextView priceInfoTitleTextView;
		private TextView priceCell12;
		private TextView priceCell13;
		private TextView priceCell14;
		private TextView priceCell21;
		private TextView priceCell31;
		private TextView priceCell41;
		private TextView priceCell51;
		private TextView priceCell22;
		private TextView priceCell23;
		private TextView priceCell24;
		private TextView priceCell32;
		private TextView priceCell33;
		private TextView priceCell34;
		private TextView priceCell42;
		private TextView priceCell43;
		private TextView priceCell44;
		private TextView priceCell52;
		private TextView priceCell53;
		private TextView priceCell54;
		private TableLayout periodTable;
		private TextView periodCell2;

		private Context mContext;

		private boolean isPeriodTableVisible = false;

		public void setComponents() {
			try {
				for (int i = 0; i < mRoomInfoModelController.getCost_table().length(); i++) {
					JSONObject costObject = mRoomInfoModelController.getCost_table().getJSONObject(i);
					String pen_period_division = costObject.optString("pen_period_division");
					Log.d(TAG, pen_period_division);
					if (pen_period_division.equals(mContext.getResources().getString(R.string.off_season))) {
						Log.d(TAG, "비수기");
						priceCell22.setText(castRoomPrice(costObject.optInt("weekdays")));
						priceCell23.setText(castRoomPrice(costObject.optInt("friday")));
						priceCell24.setText(castRoomPrice(costObject.optInt("weekends")));
					} else if (pen_period_division.equals(mContext.getResources().getString(R.string.semi_on_season))) {
						priceCell32.setText(castRoomPrice(costObject.optInt("weekdays")));
						priceCell33.setText(castRoomPrice(costObject.optInt("friday")));
						priceCell34.setText(castRoomPrice(costObject.optInt("weekends")));
					} else if (pen_period_division.equals(mContext.getResources().getString(R.string.on_season))) {
						Log.d(TAG, "성수기");
						priceCell42.setText(castRoomPrice(costObject.optInt("weekdays")));
						priceCell43.setText(castRoomPrice(costObject.optInt("friday")));
						priceCell44.setText(castRoomPrice(costObject.optInt("weekends")));
					} else {
						Log.d(TAG, "극성수기");
						priceCell52.setText(castRoomPrice(costObject.optInt("weekdays")));
						priceCell53.setText(castRoomPrice(costObject.optInt("friday")));
						priceCell54.setText(castRoomPrice(costObject.optInt("weekends")));
					}
				}

				if(!isPeriodTableVisible) {
					for (int i = 0; i < mRoomInfoModelController.getPeriod_table().length(); i++) {
						int rowNum = i + 1;

						TableRow periodTableRow = new TableRow(mContext);
						periodTableRow.setId(rowNum); // id is set by the level of the row
						periodTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


						JSONObject periodObject = mRoomInfoModelController.getPeriod_table().getJSONObject(i);
						String pen_period_division = periodObject.optString("pen_period_division");
						String period = periodObject.optString("period_start") + " ~ " + periodObject.optString("period_end");

						for (int j = 0; j < 2; j++) {
							int colNum = j + 1;
							int cellNum = rowNum * 10 + colNum;
							TextView periodCell = new TextView(mContext);
							periodCell.setId(cellNum);
							periodCell.setGravity(Gravity.CENTER);
							periodCell.setPadding(0, (int) mContext.getResources().getDimension(R.dimen.cell_vertical_padding), 0, (int) mContext.getResources().getDimension(R.dimen.cell_vertical_padding));
							periodCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
							periodCell.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());

							if (pen_period_division.equals(mContext.getResources().getString(R.string.off_season))) {
								Log.d(TAG, "비수기");
								setPeriodCell(periodCell, R.string.off_season, period);
							} else if (pen_period_division.equals(mContext.getResources().getString(R.string.semi_on_season))) {
								Log.d(TAG, "준성수기");
								setPeriodCell(periodCell, R.string.semi_on_season, period);
							} else if (pen_period_division.equals(mContext.getResources().getString(R.string.on_season))) {
								Log.d(TAG, "성수기");
								setPeriodCell(periodCell, R.string.on_season, period);
							} else {
								Log.d(TAG, "극성수기");
								setPeriodCell(periodCell, R.string.extreme_on_season, period);
							}

							periodTableRow.addView(periodCell);
						}
						periodTable.addView(periodTableRow);
					}
					isPeriodTableVisible = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void setPeriodCell(TextView periodCell, int periodResId, String period) {
			if(periodCell.getId() % 10 == 1) {
				periodCell.setText(periodResId);
				periodCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				periodCell.setBackgroundResource(R.drawable.shape_background_cell);
			} else {
				periodCell.setText(period);
				periodCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			}
		}


		private String castRoomPrice(int roomPrice) {
			if(roomPrice == 0) {
				return mContext.getResources().getString(R.string.telephone_inquiry);
			} else {
				return castItemPriceToString(roomPrice, mContext);
			}
		}

		public PriceAndDateSelectionCard(View cardView, Context context) {
			super(cardView);
			priceInfoTitleTextView = (TextView) cardView.findViewById(R.id.textView_info_price);
			priceInfoTitleTextView.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell12 = (TextView) cardView.findViewById(R.id.cell12_price);
			priceCell12.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell13 = (TextView) cardView.findViewById(R.id.cell13_price);
			priceCell13.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell14 = (TextView) cardView.findViewById(R.id.cell14_price);
			priceCell14.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell21 = (TextView) cardView.findViewById(R.id.cell21_price);
			priceCell21.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell31 = (TextView) cardView.findViewById(R.id.cell31_price);
			priceCell31.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell41 = (TextView) cardView.findViewById(R.id.cell41_price);
			priceCell41.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell51 = (TextView) cardView.findViewById(R.id.cell51_price);
			priceCell51.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell22 = (TextView) cardView.findViewById(R.id.cell22_price);
			priceCell22.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell23 = (TextView) cardView.findViewById(R.id.cell23_price);
			priceCell23.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell24 = (TextView) cardView.findViewById(R.id.cell24_price);
			priceCell24.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell32 = (TextView) cardView.findViewById(R.id.cell32_price);
			priceCell32.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell33 = (TextView) cardView.findViewById(R.id.cell33_price);
			priceCell33.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell34 = (TextView) cardView.findViewById(R.id.cell34_price);
			priceCell34.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell42 = (TextView) cardView.findViewById(R.id.cell42_price);
			priceCell42.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell43 = (TextView) cardView.findViewById(R.id.cell43_price);
			priceCell43.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell44 = (TextView) cardView.findViewById(R.id.cell44_price);
			priceCell44.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell52 = (TextView) cardView.findViewById(R.id.cell52_price);
			priceCell52.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell53 = (TextView) cardView.findViewById(R.id.cell53_price);
			priceCell53.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			priceCell54 = (TextView) cardView.findViewById(R.id.cell54_price);
			priceCell54.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			periodTable = (TableLayout) cardView.findViewById(R.id.tablelayout_period_room);
			periodCell2 = (TextView) cardView.findViewById(R.id.cell12_period);
			periodCell2.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			mContext = context;
		}
	}

	private static class OwnerInfoCard extends RecyclerView.ViewHolder {
		private TextView ceoInfoTitleTextView;
		private TextView ceoNameTextView;
		private TextView ceoAccountTextView;

		private Context mContext;

		public void setComponents() {
			if(!mRoomInfoModelController.getPen_ceo().equals("null"))
				ceoNameTextView.setText(mContext.getResources().getString(R.string.ceo_name) + "  " + mRoomInfoModelController.getPen_ceo());
			else
				ceoNameTextView.setText("-");

			if(!mRoomInfoModelController.getPen_ceo_account().equals("null"))
				ceoAccountTextView.setText(mContext.getResources().getString(R.string.ceo_account) + "  " + mRoomInfoModelController.getPen_ceo_account());
			else
				ceoAccountTextView.setText(mContext.getResources().getString(R.string.ceo_account) + "  " + "-");
		}

		public OwnerInfoCard(View cardView, Context context) {
			super(cardView);
			ceoInfoTitleTextView = (TextView) cardView.findViewById(R.id.textView_info_ceo);
			ceoInfoTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			ceoNameTextView = (TextView) cardView.findViewById(R.id.textView_name_ceo);
			ceoNameTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			ceoAccountTextView = (TextView) cardView.findViewById(R.id.textView_account_ceo);
			ceoAccountTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			mContext = context;
		}
	}

	private static class EquipmentCard extends RecyclerView.ViewHolder {
		private TextView equipmentTitleTextView;
		private TextView equipmentTextView;

		public void setComponents() {
			if(!mRoomInfoModelController.getRoom_equipment().equals("null"))
				equipmentTextView.setText(mRoomInfoModelController.getRoom_equipment());
			else
				equipmentTextView.setText("-");
		}

		public EquipmentCard(View cardView, Context context) {
			super(cardView);
			equipmentTitleTextView = (TextView) cardView.findViewById(R.id.textView_equipment);
			equipmentTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			equipmentTextView = (TextView) cardView.findViewById(R.id.textView_list_equipment);
			equipmentTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
		}
	}

	private static class FacilityAndServiceCard extends RecyclerView.ViewHolder {
		private TextView facilityAndServiceTitleTextView;
		private TextView facilityTitleTextView;
		private TableLayout facilityTable;
		private TextView serviceTitleTextView;
		private TableLayout serviceTable;
		private boolean isComponentVisible = false;

		private Context mContext;

		public void setComponents() {
			if(!isComponentVisible) {
				JSONArray facilityList = mRoomInfoModelController.getFacility();
				for (int i = 0; i < facilityList.length(); i++) {
					try {

						JSONObject facilityObject = facilityList.getJSONObject(i);
						String facilityName = facilityObject.optString("facility_name");

						String facilityString = facilityObject.optString("facility_description");
						String[] facilityStringList = facilityString.split("/");

						for (int j = 0; j < facilityStringList.length; j++) {
							Log.d(TAG, facilityStringList[j]);

							TableRow facilityTableRow = new TableRow(mContext);
							facilityTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

							for(int k = 0; k < 2; k++) {
								TextView facilityCell = createCell();
								TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);

								if(k == 0) {
									params.setMargins(0, 0, convertDpToPx(8, mContext), 0);
									facilityCell.setLayoutParams(params);
								} else {
									facilityCell.setLayoutParams(params);
								}

								if (j == 0 && k == 0)
									facilityCell.setText(facilityName);
								else if (k == 1) {
									if(!facilityStringList[j].equals("null"))
										facilityCell.setText(facilityStringList[j]);
									else
										facilityCell.setText("-");
								}

								facilityTableRow.addView(facilityCell);
							}
							facilityTable.addView(facilityTableRow);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				JSONArray serviceList = mRoomInfoModelController.getService();
				for (int i = 0; i < serviceList.length(); i++) {
					try {
						JSONObject serviceObject = serviceList.getJSONObject(i);
						String serviceName = serviceObject.optString("service_name");

						String serviceString = serviceObject.optString("service_description");
						String[] serviceStringList = serviceString.split("/");

						for (int j = 0; j < serviceStringList.length; j++) {
							Log.d(TAG, serviceStringList[j]);

							TableRow serviceTableRow = new TableRow(mContext);
							serviceTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

							for(int k = 0; k < 2; k++) {
								TextView serviceCell = createCell();
								TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);

								if(k == 0) {
									params.setMargins(0, 0, convertDpToPx(8, mContext), 0);
									serviceCell.setLayoutParams(params);
								} else {
									serviceCell.setLayoutParams(params);
								}

								if (j == 0 && k == 0)
									serviceCell.setText(serviceName);
								else if (k == 1) {
									if(!serviceStringList[j].equals("null")) {
										Log.d(TAG, serviceStringList[j]);
										serviceCell.setText(serviceStringList[j]);
									}
									else
										serviceCell.setText("-");
								}

								serviceTableRow.addView(serviceCell);
							}
							serviceTable.addView(serviceTableRow);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				isComponentVisible = true;
			}
		}

		private TextView createCell() {
			TextView cell = new TextView(mContext);
			cell.setGravity(Gravity.LEFT);
			cell.setPadding(0, (int) mContext.getResources().getDimension(R.dimen.cell_vertical_padding), 0, (int) mContext.getResources().getDimension(R.dimen.cell_vertical_padding));
			cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
			cell.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
			cell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			return cell;
		}

		public FacilityAndServiceCard(View cardView, Context context) {
			super(cardView);
			facilityAndServiceTitleTextView = (TextView) cardView.findViewById(R.id.textView_facility_and_service);
			facilityAndServiceTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			facilityTitleTextView = (TextView) cardView.findViewById(R.id.textView_facility);
			facilityTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			facilityTable = (TableLayout) cardView.findViewById(R.id.tableLayout_facility);
			serviceTitleTextView = (TextView) cardView.findViewById(R.id.textView_service);
			serviceTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			serviceTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			serviceTable = (TableLayout) cardView.findViewById(R.id.tableLayout_service);
			mContext = context;
		}
	}

	private static class CautionsCard extends RecyclerView.ViewHolder {
		private TextView cautionsTitleTextView;
		private TextView roomUsageTitleTextView;
		private LinearLayout roomUsageLinearLayout;
		private TextView reservationTitleTextView;
		private LinearLayout reservationLinearLayout;
		private TextView stdRefundTitleTextView;
		private TableLayout stdRefundTable;

		private Context mContext;

		private boolean isComponentsLoaded = false;

		public void setComponents() {
			if(!isComponentsLoaded) {
				JSONArray roomUsageCautionList = mRoomInfoModelController.getUsage_caution();
				for (int i = 0; i < roomUsageCautionList.length(); i++) {
					try {
						JSONObject roomUsageCautionObject = roomUsageCautionList.getJSONObject(i);
						TextView roomUsageCautionTextView = new TextView(mContext);
						roomUsageCautionTextView.setText("> " + roomUsageCautionObject.optString("pen_usage_caution"));
						roomUsageCautionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(0, 0, 0, convertDpToPx(4, mContext));

						roomUsageCautionTextView.setLayoutParams(params);
						roomUsageLinearLayout.addView(roomUsageCautionTextView);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				JSONArray reservationCautionList = mRoomInfoModelController.getReserve_caution();
				for (int i = 0; i < reservationCautionList.length(); i++) {
					try {
						JSONObject reservationCautionObject = reservationCautionList.getJSONObject(i);
						TextView reservationCautionTextView = new TextView(mContext);
						reservationCautionTextView.setText("> " + reservationCautionObject.optString("pen_reserve_caution"));
						reservationCautionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(0, 0, 0, convertDpToPx(4, mContext));

						reservationCautionTextView.setLayoutParams(params);
						reservationLinearLayout.addView(reservationCautionTextView);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				JSONArray stdRefundList = mRoomInfoModelController.getRefund_caution();
				for (int i = 0; i < stdRefundList.length(); i++) {
					try {
						int rowNum = i + 1;

						TableRow stdRefundTableRow = new TableRow(mContext);
						stdRefundTableRow.setId(rowNum); // id is set by the level of the row
						TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
						stdRefundTableRow.setLayoutParams(params);

						JSONObject stdRefundObject = stdRefundList.getJSONObject(i);
						String stdRefundString = stdRefundObject.optString("pen_refund_table");
						String[] stdRefundStringList = stdRefundString.split(" : ");

						for (int j = 0; j < 2; j++) {
							int colNum = j + 1;
							int cellNum = rowNum * 10 + colNum;
							TextView stdRefundCell = new TextView(mContext);
							stdRefundCell.setId(cellNum);
							stdRefundCell.setGravity(Gravity.CENTER);
							stdRefundCell.setPadding(0, (int) mContext.getResources().getDimension(R.dimen.cell_vertical_padding), 0, (int) mContext.getResources().getDimension(R.dimen.cell_vertical_padding));
							stdRefundCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
							stdRefundCell.setTypeface(TypefaceLoader.getInstance(mContext).getTypeface());
							stdRefundCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
							stdRefundCell.setText(stdRefundStringList[j]);
							if(j == 0)
								stdRefundCell.setBackgroundResource(R.drawable.shape_background_cell);

							stdRefundTableRow.addView(stdRefundCell);
						}
						stdRefundTable.addView(stdRefundTableRow);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				isComponentsLoaded = true;
			}
		}

		public CautionsCard(View cardView, Context context) {
			super(cardView);
			cautionsTitleTextView = (TextView) cardView.findViewById(R.id.textView_cautions);
			cautionsTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			roomUsageTitleTextView = (TextView) cardView.findViewById(R.id.textView_usage_room);
			roomUsageTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			roomUsageLinearLayout = (LinearLayout) cardView.findViewById(R.id.layout_usage_room);
			reservationTitleTextView = (TextView) cardView.findViewById(R.id.textView_reservation);
			reservationTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			reservationLinearLayout = (LinearLayout) cardView.findViewById(R.id.layout_reservation);
			stdRefundTitleTextView = (TextView) cardView.findViewById(R.id.textView_std_refund);
			stdRefundTitleTextView.setTypeface(TypefaceLoader.getInstance(context).getTypeface());
			stdRefundTable = (TableLayout) cardView.findViewById(R.id.tableLayout_refund_std);

			mContext = context;
		}
	}

	private static String castItemPriceToString(int price, Context context) {
		String totalRoomCostString = String.valueOf(price);
		String totalRoomCostStringChanged = "";

		if(totalRoomCostString.length() > 0) {
			int charCounter = 0;
			for (int i = totalRoomCostString.length() - 1; i >= 0; i--) {
				if(charCounter != 0 && charCounter % 3 == 0)
					totalRoomCostStringChanged += "," + totalRoomCostString.charAt(i);
				else
					totalRoomCostStringChanged += totalRoomCostString.charAt(i) ;
				charCounter++;
			}
		}
		return context.getResources().getString(R.string.currency_unit) +
				new StringBuffer(totalRoomCostStringChanged).reverse().toString();
	}

	private static int convertDpToPx(int dp, Context context) {
		float screenDensity = context.getResources().getDisplayMetrics().density;
		int px = (int)(dp * screenDensity);
		return px;
	}

}
