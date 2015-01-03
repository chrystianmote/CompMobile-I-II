package br.com.alexromanelli.android.experimentodialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * A classe OnUpdateItemListener é o listener que deve ser usado
 * para a confirmação do diálogo de alteração de item.
 * 
 * @author Alexandre Romanelli
 * @since 2013-09-06
 *
 */
public class OnUpdateItemListener implements OnClickListener {
	
	/**
	 * referência para a atividade principal
	 */
	private MainActivity refPai;
	
	/**
	 * posição, na lista exibida na atividade principal, do item
	 * selecionado para ser modificado
	 */
	private int indiceItem;
	
	/**
	 * Construtor da classe. Faz a associação desta instância com
	 * o objeto da atividade principal e retém o índice do item 
	 * selecionado para alteração.
	 * 
	 * @param refPai a referência para a atividade principal.
	 * @param indiceItem a posição do item selecionado para remoção.
	 */
	public OnUpdateItemListener(MainActivity refPai, int indiceItem) {
		this.refPai = refPai;
		this.indiceItem = indiceItem;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		refPai.alterarItem(indiceItem);
	}

}
