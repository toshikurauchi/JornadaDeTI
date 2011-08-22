package br.com.caelum;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Código comentado do exemplo de aplicação utilizado na Jornada de 
 * Tecnologia da Informação em 27 de Agosto de 2011 na UNIBAN
 * 
 * @author Andrew Toshiaki Nakayama Kurauchi - @toshikurauchi
 * @see http://www.caelum.com.br
 */
public class JornadaDeTI extends Activity {
	// Elementos da tela
    private ListView listaDeAmigos;
    private EditText campoNome;
    private EditText campoTelefone;
    private EditText campoEndereco;
    private Button botaoAdicionar;
    
	private List<Amigo> amigos;
	private ArrayAdapter<Amigo> adapter;
	protected Amigo amigoSelecionado;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        inicializaAplicacao(savedInstanceState);
        configuraConteudoEComportamentoDoListView();
        configuraComportamentoDoBotao();
    }

	private void inicializaAplicacao(Bundle savedInstanceState) {
		// Chamada obrigatória. Inicialização da janela do Android.
		super.onCreate(savedInstanceState);
		
		// Fala qual layout vamos usar na nossa Activiy (tela).
		// Nesse caso estamos usando o arquivo main.xml no diretório
		// res/layout.
        setContentView(R.layout.main);
        
        // Precisamos de uma referência para o ListView do main.xml
        // para colocarmos os amigos nele.
        listaDeAmigos = (ListView) findViewById(R.id.lista_de_amigos);
        // Precisamos dos campos de texto (EditText) para ler a informação
        // que o usuário digitar.
        campoNome = (EditText) findViewById(R.id.nome);
        campoTelefone = (EditText) findViewById(R.id.telefone);
        campoEndereco = (EditText) findViewById(R.id.endereco);
        // Precisamos do botão (Button) para, quando o usuário clicar nele,
        // adicionarmos o novo amigo.
        botaoAdicionar = (Button) findViewById(R.id.adicionar);
        
        // Inicialmente tenho uma lista vazia de amigos.
        amigos = new ArrayList<Amigo>();
	}
	
	private void configuraComportamentoDoBotao() {
		botaoAdicionar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String nome = campoNome.getEditableText().toString();
				String telefone = campoTelefone.getEditableText().toString();
				String endereco = campoEndereco.getEditableText().toString();
				
				campoNome.setText("");
				campoTelefone.setText("");
				campoEndereco.setText("");
				
				Amigo novoAmigo = new Amigo(nome, telefone, endereco);
				amigos.add(novoAmigo);
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void configuraConteudoEComportamentoDoListView() {
		adapter = new ArrayAdapter<Amigo>(this, android.R.layout.simple_list_item_1, amigos);
		listaDeAmigos.setAdapter(adapter);
		
		
		registerForContextMenu(listaDeAmigos);
		
		listaDeAmigos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				amigoSelecionado = amigos.get(position);
				openContextMenu(listaDeAmigos);
			}
		});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, 1, 0, "Ligar");
		menu.add(0, 2, 0, "Enviar SMS");
		menu.add(0, 3, 0, "Ver no mapa");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == 1) {
			Intent ligar = new Intent(Intent.ACTION_CALL);
			ligar.setData(Uri.parse("tel:" + amigoSelecionado.getTelefone()));
			startActivity(ligar);
		}
		else if(item.getItemId() == 2) {
			Intent enviarSMS = new Intent(Intent.ACTION_VIEW);
			enviarSMS.setData(Uri.parse("sms:" + amigoSelecionado.getTelefone()));
			startActivity(enviarSMS);
		}
		else if(item.getItemId() == 3) {
			Intent verNoMapa = new Intent(Intent.ACTION_VIEW);
			verNoMapa.setData(Uri.parse("geo:0,0?z=17&q=" + amigoSelecionado.getEndereco()));
			startActivity(verNoMapa);
		}
		
		return super.onContextItemSelected(item);
	}
}