package hu.bme.xj4vjg.petshop.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import hu.bme.xj4vjg.petshop.PetShopApplication;

public abstract class BaseActivity extends AppCompatActivity {
	protected void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	protected void showMessage(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
	}

	protected void setToolbar(Toolbar toolbar, String title) {
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle(title);
		}
	}

	protected void setToolbar(Toolbar toolbar) {
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
	}

	protected void setToolbar(Toolbar toolbar, int titleResId) {
		setToolbar(toolbar, getString(titleResId));
	}

	protected Tracker getTracker() {
		return ((PetShopApplication) getApplication()).getDefaultTracker();
	}
}
