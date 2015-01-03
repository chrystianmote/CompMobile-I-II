package br.com.alexromanelli.android.experimentodialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * A classe OnInsertItemListener é o listener que deve ser usado
 * para a confirmação do diálogo de inclusão de item.
 * 
 * @author Alexandre Romanelli
 * @since 2013-09-06
 *
 */
public class OnInsertItemListener implements OnClickListener {

	/**
	 * referência para a atividade principal
	 */
	private MainActivity refPai;
	
	/**
	 * Construtor da classe. Faz a associação desta instância com
	 * o objeto da atividade principal.
	 * 
	 * @param refPai a referência para a atividade principal.
	 */
	public OnInsertItemListener(MainActivity refPai) {
		this.refPai = refPai;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		refPai.inserirItem();
	}

}
