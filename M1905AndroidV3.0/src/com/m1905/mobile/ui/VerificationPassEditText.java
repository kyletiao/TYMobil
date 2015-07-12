package com.m1905.mobile.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dianxin.mobilefree.R;


public class VerificationPassEditText extends LinearLayout {

	private EditText edt;
	private ProgressBar pb;
	private ImageView uname_delete;
	private RelativeLayout rtw_uname;

	public VerificationPassEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView(context);
	}

	public VerificationPassEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public VerificationPassEditText(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		View view = View.inflate(context, R.layout.edittext_check_verification, null);
		edt = (EditText) view.findViewById(R.id.edittext_check_edt_name);
		//edt.setTextSize(TypedValue.COMPLEX_UNIT_PX, 28);
		pb = (ProgressBar) view.findViewById(R.id.edittext_check_pb_name);
		uname_delete = (ImageView)view.findViewById(R.id.uname_delete);
		uname_delete.setVisibility(View.INVISIBLE);
		rtw_uname = (RelativeLayout)view.findViewById(R.id.rtw_uname);
		this.addView(view);
	}

	public String getEditTextContent() {
		return edt.getText().toString();
	}

	public void setEditTextHint(String hint) {
		edt.setHint(hint);
	}

	public ProgressBar getPb() {
		return pb;
	}


	public EditText getEdt() {
		return edt;
	}

	public void setEdt(EditText edt) {
		this.edt = edt;
	}
	public ImageView getUNameDel()
	{
		return uname_delete;
	}
	public RelativeLayout getTvwUname()
	{
		return rtw_uname;
	}

}
