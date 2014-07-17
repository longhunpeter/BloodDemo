package com.chronocloud.rhythm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author ljw
 * @version CareteTime:2014-4-23 下午2:59:45
 * @description 测量界面
 */
public class MeasureActivity extends Activity implements OnClickListener {

	private Button mBtnStart;
	private Button mBtnStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_measure);

		initView();
	}

	private void initView() {

		mBtnStart = (Button) findViewById(R.id.btn_start);
		mBtnStop = (Button) findViewById(R.id.btn_stop);

		mBtnStart.setOnClickListener(this);
		mBtnStop.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		
		case R.id.btn_start:

			break;
		case R.id.btn_stop:

			break;

		default:
			break;
		}

	}

}
