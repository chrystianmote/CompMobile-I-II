package br.com.alexromanelli.android.experimentodatetime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		inicializarTextoData();
		
		Button btAtualizar = (Button)findViewById(R.id.btAtualizar);
		btAtualizar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				atualizarData();
			}
		});
	}
	
	protected void atualizarData() {
		DatePicker dpData = (DatePicker)findViewById(R.id.dpData);
		exibirTextoData(new Date(dpData.getCalendarView().getDate()));
	}

	private void inicializarTextoData() {
		Calendar cal = Calendar.getInstance();
		exibirTextoData(cal.getTime());
	}
	
	private void exibirTextoData(Date data) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		EditText edData = (EditText)findViewById(R.id.edData);
		edData.setText(df.format(data));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
