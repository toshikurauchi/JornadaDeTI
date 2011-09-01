package br.com.caelum.jornadaDeTI;

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
	// Visualização de listas
    private ListView listaDeAmigos;
    // Campos de edição de texto
    private EditText campoNome;
    private EditText campoTelefone;
    private EditText campoEndereco;
    // Botão
    private Button botaoAdicionar;
    
    // Lista na qual guardaremos os amigos adicionados pelo
    // usuário quando o botão for clicado
	private List<Amigo> amigos;
	// Adapter é quem diz como cada elemento da lista (no nosso
	// caso a List<Amigo> amigos) será mostrado na tela (ou seja,
	// no nosso caso, na ListView listaDeAmigos)
	private ArrayAdapter<Amigo> adapter;
	// Aqui guardaremos o amigo que o usuário selecionar na lista
	private Amigo amigoSelecionado;

	// Esse é o método chamado pelo Android quando a nossa Activity
	// (tela) é criada
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
		// Digo para o botão o que fazer quando for clicado. As ações a serem
		// tomadas serão descritas no método onClick
		botaoAdicionar.setOnClickListener(new OnClickListener() {
			// Método chamado quando o usuário clicar no botão
			public void onClick(View v) {
				// Guardo os valores digitados pelo usuário
				String nome = campoNome.getEditableText().toString();
				String telefone = campoTelefone.getEditableText().toString();
				String endereco = campoEndereco.getEditableText().toString();
				
				// Como já peguei os valores não quero que o usuário adicione
				// novamente o mesmo amigo, por isso limpo os campos de edição
				campoNome.setText("");
				campoTelefone.setText("");
				campoEndereco.setText("");
				
				Amigo novoAmigo = new Amigo(nome, telefone, endereco);
				// Adiciono o amigo na minha List<Amigo>
				amigos.add(novoAmigo);
				// Aviso o adapter que a minha List<Amigo> foi alterada
				// o adapter vai então atualizar a ListView com os novos
				// valores da List<Amigo>
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void configuraConteudoEComportamentoDoListView() {
		// Instancio o adapter. O primeiro argumento é um contexto, ou seja, em qual tela estou?
		// No nosso caso é a Activity JornadaDeTI. Como já estamos em uma instância dessa classe
		// passamos this. O segundo argumento diz como devo mostrar na ListView cada elemento da
		// List<Amigo>. Nesse caso usamos uma visualização pronta no Android que simplesmente chama
		// o toString do elemento da List, no caso o toString do Amigo. O último argumento é uma
		// List<Amigo>, no caso o amigos.
		adapter = new ArrayAdapter<Amigo>(this, android.R.layout.simple_list_item_1, amigos);
		listaDeAmigos.setAdapter(adapter);
		
		// Aqui dizemos para o Android que a ListView listaDeAmigos pode criar um ContextMenu
		// quando for clicada.
		registerForContextMenu(listaDeAmigos);
		
		// Dizemos o que fazer quando um elemento da listaDeAmigos for clicado
		listaDeAmigos.setOnItemClickListener(new OnItemClickListener() {
			// Quando um elemento da lista for clicado o Android chamará esse método passando
			// a View mãe, a View clicada, a posição e um id. Desses, o único que importa para o
			// nosso exemplo é a posição
			public void onItemClick(AdapterView<?> mae, View view,
					int posicao, long id) {
				// Guardamos o amigo clicado no atributo amigoSelecionado
				amigoSelecionado = amigos.get(posicao);
				// Pedimos para o Android abrir o ContextMenu da listaDeAmigos
				openContextMenu(listaDeAmigos);
			}
		});
	}
	
	// Esse método é chamado pelo Android quando o ContextMenu é criado. São passados um menu vazio,
	// a view que determina o contexto do menu (no nosso caso sabemos que é a ListView listaDeAmigos),
	// e algumas informações extras passadas no menuInfo
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// Adicionando itens no menu. O primeiro e terceiro parâmetros (grupo e ordem dos itens do menu) 
		// são opcionais. Passamos 0 para dizer que não vamos utilizar. O segundo parâmetro é um id do item
		// e o último é o texto do item.
		menu.add(0, 1, 0, "Ligar");
		menu.add(0, 2, 0, "Enviar SMS");
		menu.add(0, 3, 0, "Ver no mapa");
	}
	
	// Método que o Android chama quando um item do ContextMenu é selecionado pelo usuário. É passado o item
	// selecionado.
	public boolean onContextItemSelected(MenuItem item) {
		// Para descobrir qual é o item selecionado pelo usuário utilizamos os id's atribuídos no 
		// onCreateContextMenu
		if(item.getItemId() == 1) {
			// Criamos uma Intent para dizer para o Android o que queremos fazer. Nesse caso, realizar uma
			// ligação.
			Intent ligar = new Intent(Intent.ACTION_CALL);
			// Precisamos dizer para quem queremos ligar (lembrando que quando o usuário clica em um aluno 
			// guardamos o aluno selecionado em um atributo e depois chamamos o ContextMenu)
			ligar.setData(Uri.parse("tel:" + amigoSelecionado.getTelefone()));
			// Pedimos para o Android criar uma Activity (tela) que realize a nossa intenção. Ele vai 
			// procurar pelos aplicativos instalados no celular algum que saiba realizar essa ação.
			startActivity(ligar);
		}
		else if(item.getItemId() == 2) {
			// Agora queremos visualizar algo
			Intent enviarSMS = new Intent(Intent.ACTION_VIEW);
			// O que queremos visualizar: a tela de criação de SMS
			enviarSMS.setData(Uri.parse("sms:" + amigoSelecionado.getTelefone()));
			// Mais uma vez pedimos para criar uma tela que saiba realizar essa ação.
			startActivity(enviarSMS);
		}
		else if(item.getItemId() == 3) {
			// Mais uma vez queremos visualizar algo
			Intent verNoMapa = new Intent(Intent.ACTION_VIEW);
			// Mas nesse caso queremos visualizar o mapa no endereço do amigoSelecionado
			verNoMapa.setData(Uri.parse("geo:0,0?z=17&q=" + amigoSelecionado.getEndereco()));
			startActivity(verNoMapa);
		}
		
		// Chamada da implementação da classe mãe que vai fazer alguns outros tratamentos.
		return super.onContextItemSelected(item);
	}
}