package com.mkrstic.callnotes.activity;



import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.mkrstic.callnotes.R;

import android.os.Bundle;

import android.widget.TextView;

public class MainActivity extends SherlockFragmentActivity {

	private TextView hello;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		hello = ((TextView) findViewById(R.id.hello));
	}

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
