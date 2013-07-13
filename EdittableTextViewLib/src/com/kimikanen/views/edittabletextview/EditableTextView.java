package com.kimikanen.views.edittabletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class EditableTextView extends FrameLayout {

	private static final String TAG = "EditableTextView";
	private static final float DEFAULT_TEXT_SIZE = 14.0f;

	private final EditText edittext;
	private final TextView textView;

	private boolean editMode;
	private String text;
	private float textSize;
	private String hint;

	private final OnClickListener textViewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			editMode = true;
			init();
			setKeyboardVisibility(true);
		}
	};

	private final OnKeyListener editTextKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (v.equals(edittext)) {
				switch (keyCode) {
				// if it's back don't save the new text
				case KeyEvent.KEYCODE_ENTER:
					setText(edittext.getText().toString());
				case KeyEvent.KEYCODE_BACK:
					editMode = edittext.getText().length() <= 0;
					init();
					setKeyboardVisibility(false);
					return true;
				default:
					break;
				}
			}
			return false;
		}
	};

	public EditableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EdittableTextView, 0, 0);
		text = a.getString(R.styleable.EdittableTextView_text);
		editMode = a.getBoolean(R.styleable.EdittableTextView_defaultEdit,
				false);
		textSize = a.getDimension(R.styleable.EdittableTextView_textSize,
				DEFAULT_TEXT_SIZE);
		hint = a.getString(R.styleable.EdittableTextView_hint);

		a.recycle();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.editable_textview, this, true);

		textView = (TextView) findViewById(R.id.text);
		edittext = (EditText) findViewById(R.id.edit);

		textView.setOnClickListener(textViewClickListener);
		textView.setTextSize(textSize);

		edittext.setOnKeyListener(editTextKeyListener);
		edittext.setTextSize(textSize);
		edittext.setHint(hint);

		init();
	}

	private void init() {
		Log.v(TAG, "Init. editMode = " + editMode);
		textView.setText(getText());
		edittext.setText(getText());
		if (editMode) {
			textView.setVisibility(View.INVISIBLE);
			edittext.setVisibility(View.VISIBLE);
			edittext.requestFocus();
			edittext.selectAll();
		} else {
			textView.setVisibility(View.VISIBLE);
			edittext.setVisibility(View.INVISIBLE);
			edittext.clearFocus();
		}
		invalidate();
		requestLayout();
	}

	private void setKeyboardVisibility(boolean visible) {
		final InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (visible) {
			imm.showSoftInput(this, 0);
		} else {
			imm.hideSoftInputFromWindow(getWindowToken(), 0);
		}
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
		init();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
		init();
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

}
