package com.owo.mtplease;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

/**
 * Created by In-Ho on 2015-01-31.
 */
public class UndoToastController {

	public static final int INITIALIZATION_CASE = -1;
	public static final int DELETE_ADDED_ROOM_CASE = 0;
	public static final int DELETE_DIRECTLY_INPUT_ROOM_CASE = 1;
	public static final int DELETE_ADDED_MEAT_CASE = 2;
	public static final int DELETE_ADDED_ALCOHOL_CASE = 3;
	public static final int DELETE_ADDED_OTHERS_CASE = 4;
	public static final int CLEAR_ADDED_ROOMS_CASE = 5;
	public static final int CLEAR_ADDED_MEATS_CASE = 6;
	public static final int CLEAR_ADDED_ALCOHOLS_CASE = 7;
	public static final int CLEAR_ADDED_OTHERS_CASE = 8;

	private View undoToastView;
	private TextView notificationMessageView;
	private TextView undoButton;
	private ViewPropertyAnimator undoToastAnimator;
	private Handler mHideHandler = new Handler();

	private int toastCase;
	private Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			hideUndoBar(true, toastCase);
			mUndoToastListener.onTimePassed(toastCase, viewIndex);
		}
	};
	private UndoToastListener mUndoToastListener;
	private int viewIndex;

	public UndoToastController(View undoToastView, UndoToastListener undoToastButtonListener) {
		this.undoToastView = undoToastView;
		undoToastAnimator = undoToastView.animate();
		mUndoToastListener = undoToastButtonListener;
		toastCase = INITIALIZATION_CASE;

		notificationMessageView = (TextView) undoToastView.findViewById(R.id.textView_message_notification);
		undoButton = (TextView) undoToastView.findViewById(R.id.textView_btn_undo);
		undoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideUndoBar(false, toastCase);
				mUndoToastListener.onClickUndoButton(toastCase, viewIndex);
			}
		});

		hideUndoBar(true, INITIALIZATION_CASE);
	}

	public void showUndoBar(String  notificationMessage, int toastCase, int viewIndex) {
		notificationMessageView.setText(notificationMessage);

		this.toastCase = toastCase;
		this.viewIndex = viewIndex;

		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, 2000);

		undoToastView.setVisibility(View.VISIBLE);

		undoToastAnimator.cancel();
		undoToastAnimator
				.alpha(1)
				.setDuration(500)
				.setListener(null);
	}

	public void hideUndoBar(boolean immediate, int toastCase) {

		this.toastCase = toastCase;

		mHideHandler.removeCallbacks(mHideRunnable);
		if (immediate) {
			undoToastView.setVisibility(View.GONE);
			undoToastView.setAlpha(0);
			notificationMessageView.setText(null);
		} else {
			undoToastAnimator.cancel();
			undoToastAnimator
					.alpha(0)
					.setDuration(1000)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							undoToastView.setVisibility(View.GONE);
							notificationMessageView.setText(null);
						}
					});
		}
	}/*

	public void setRoomViewAndData(View roomPlanView, int penId, String roomName, int roomPrice) {
		this.roomPlanView = roomPlanView;
		this.penId = penId;
		this.roomName = roomName;
		this.roomPrice = roomPrice;
	}

	public void setDirectInputRoomViewAndData(View directInputRoomPlanView, int directInputRoomPrice) {
		this.directInputRoomPlanView = directInputRoomPlanView;
		this.directInputRoomPrice = directInputRoomPrice;
	}

	public void setItemViewAndData(View itemPlanView, int itemType, String itemName, int itemUnitPrice) {
		this.itemPlanView = itemPlanView;
		this.itemType = itemType;
		this.itemName = itemName;
		this.itemUnitPrice = itemUnitPrice;
	}*/

	public interface UndoToastListener {
		void onClickUndoButton(int toastCase, int viewIndex);
		void onTimePassed(int toastCase, int viewIndex);
	}
}
