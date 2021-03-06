package model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import easyaccept.EasyAccept;

/**
 * Classe que representa uma fachada para o sistema. As informações que vêm de fora do model ou saem, passam
 * por esssa fachada. Ela faz um controle do sistema.
 * 
 * @author Yorras Gomes, Fábio Dantas
 *
 */
public class ModelFacade {

	private UsuarioControl usuario;
	private Motorista motorista;
	private Caroneiro caroneiro;
	
	/**
	 * Construtor padrão. Inicializa um objeto usuario, motorista e caroneiro.
	 */
	public ModelFacade(){
		usuario = new UsuarioControl();
		motorista = new Motorista();
		caroneiro = new Caroneiro();
	}
	
	/**
	 * Cria um novo usuario.
	 * 
	 * @param login login do usuário
	 * @param senha senha do usuário
	 * @param nome nome do usuário
	 * @param endereco endereço do usuário
	 * @param email email do usuário
	 * @throws Exception
	 */
	public void criarUsuario(String login, String senha, String nome, String endereco, String email) throws Exception{
		usuario.criarUsuario(login, senha, nome, endereco, email);
	}
	
	/**
	 * Abre uma sessão para o usuário.
	 * 
	 * @param login login do usuário
	 * @param senha senha do usuário
	 * @return id da sessão do usuário
	 * @throws Exception
	 */
	public String abrirSessao(String login, String senha) throws Exception{
		return usuario.abrirSessao(login, senha);
	}
	
	/**
	 * Retorna o nome ou o endereço do usuário.
	 * 
	 * @param login login do usuário
	 * @param atributo nome ou endereco
	 * @return nome ou endereço do usuário
	 * @throws Exception
	 */
	public String getAtributoUsuario(String login, String atributo) throws Exception{
		return usuario.getAtributoUsuario(login, atributo);
	}	
	
	/**
	 * Cadastra uma nova carona.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param origem origem da carona
	 * @param destino destino da carona
	 * @param data data da carona
	 * @param hora hora da carona
	 * @param vagas vagas da carona
	 * @return id da carona
	 * @throws Exception
	 */
	public String cadastrarCarona(String idSessao, String origem, String destino, String data, String hora, String vagas) throws Exception{
		return motorista.cadastrarCarona(idSessao, origem, destino, data, hora, vagas);
	}
	
	/**
	 * Retorna a origem, o destino, a data ou quantidade de vagas, dependendo do valor do atributo.
	 * 
	 * @param idCarona id da carona
	 * @param atributo origem, destino, data ou vagas
	 * @return origem, destino, data ou vagas, dependendo do valor do atributo
	 * @throws Exception
	 */
	public String getAtributoCarona(String idCarona, String atributo) throws Exception{
		return caroneiro.getAtributoCarona(idCarona, atributo);
	}
	
	/**
	 * Retorna o trajeto, ou seja, a origem e o destino.
	 * 
	 * @param idCarona id da carona
	 * @return trajeto da carona
	 * @throws Exception
	 */
	public String getTrajeto(String idCarona) throws Exception{
		return caroneiro.getTrajeto(idCarona);
	}
	
	/**
	 * Retorna informações da carona como origem, destino, data e hora.
	 * 
	 * @param idCarona id da carona
	 * @return informações da carona
	 * @throws Exception
	 */
	public String getCarona(String idCarona) throws Exception{
		return caroneiro.getCarona(idCarona);
	}
	
	/**
	 * Retorna um map contendo o id e a carona.
	 * 
	 * @param origem origem da carona
	 * @param destino destino da carona
	 * @return um map contendo o id e a carona
	 * @throws Exception
	 */
	public String localizarCarona(String idSessao, String origem, String destino) throws Exception{
		Map<String, Carona> caronas = caroneiro.localizarCarona(origem, destino);
		String keys = caronas.keySet().toString();
		keys = keys.replaceAll(" ", "");
		keys = "{" + keys.substring(1, keys.length()-1) + "}";
		return keys;
	}
	
	/**
	 * Sugere um ponto de encontro para a carona.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idCarona id da carona
	 * @param pontos pontos de encontro
	 * @return id da solicitação
	 * @throws Exception
	 */
	public String sugerirPontoEncontro(String idSessao, String idCarona, String pontos) throws Exception{
		return caroneiro.sugerirPontoEncontro(idSessao, idCarona, pontos);
	}
	
	/**
	 * Responde a sugestão de um ponto de encontro da carona.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idCarona id da carona
	 * @param idSugestao id da sugestão ou solicitação
	 * @param pontos pontos de encontro
	 * @throws Exception
	 */
	public void responderSugestaoPontoEncontro(String idSessao, String idCarona, String idSugestao, String pontos) throws Exception{
		motorista.responderSugestaoPontoEncontro(idSessao, idCarona, idSugestao, pontos);
	}
	
	/**
	 * Solicita vaga para o ponto de encontro definido.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idCarona id da carona
	 * @param ponto ponto de encontro
	 * @return id da solicitação
	 * @throws SQLException
	 */
	public String solicitarVagaPontoEncontro(String idSessao, String idCarona, String ponto) throws SQLException{
		return caroneiro.solicitarVagaPontoEncontro(idSessao, idCarona, ponto);
	}
	
	/**
	 * Retorna atributos da solicitação.
	 * 
	 * @param idSolicitacao id da solicitação
	 * @param atributo origem, destino, Dono da carona, Dono da solicitacao ou Ponto de encontro da solicitação
	 * @return origem, destino, Dono da carona, Dono da solicitacao ou Ponto de encontro da solicitação
	 * @throws SQLException
	 */
	public String getAtributoSolicitacao(String idSolicitacao, String atributo) throws SQLException{
		return usuario.getAtributoSolicitacao(idSolicitacao, atributo);
	}
	
	/**
	 * Aceita solicitação para o ponto de encontro definido.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idSolicitacao id da solicitação
	 * @throws Exception
	 */
	public void aceitarSolicitacaoPontoEncontro(String idSessao, String idSolicitacao) throws Exception{
		motorista.aceitarSolicitacaoPontoEncontro(idSessao, idSolicitacao);
	}
	
	/**
	 * Desiste de uma solicitação.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idCarona id da carona
	 * @param idSolicitacao id da carona
	 * @throws Exception
	 */
	public void desistirRequisicao(String idSessao, String idCarona, String idSolicitacao) throws Exception{
		usuario.desistirRequisicao(idSessao, idCarona, idSolicitacao);
	}
	
	/**
	 * Solicita vaga na carona.
	 * 
	 * @param idSessao id da sessão do usuário.
	 * @param idCarona id da carona
	 * @return id da solicitação
	 * @throws SQLException
	 */
	public String solicitarVaga(String idSessao, String idCarona) throws SQLException{
		return caroneiro.solicitarVaga(idSessao, idCarona);
	}
	
	/**
	 * Aceita solicitação.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idSolicitacao id da solicitação
	 * @throws Exception
	 */
	public void aceitarSolicitacao(String idSessao, String idSolicitacao) throws Exception{
		motorista.aceitarSolicitacaoPontoEncontro(idSessao, idSolicitacao);
	}
	
	/**
	 * Rejeita solicitação.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idSolicitacao id da solicitação
	 * @throws Exception
	 */
	public void rejeitarSolicitacao(String idSessao, String idSolicitacao) throws Exception{
		motorista.rejeitarSolicitacao(idSessao, idSolicitacao);
	}
	
	/**
	 * Retorna atributos do usuário.
	 * 
	 * @param login login do usuário
	 * @param atributo nome, endereco, email, historico de caronas, historico de vagas em caronas, caronas seguras
	 * e traquilas, caronas que não funcionaram, faltas em vagas de caronas ou presenca em vagas de caronas
	 * @return nome, endereço, email, históricos de caronas, histórico de vagas em caronas, caronas seguras e
	 * tranquilas, caronas que não funcionaram, faltas em vagas de caronas ou presenças em vagas de caronas do usuário
	 * @throws SQLException
	 */
	public String getAtributoPerfil(String login, String atributo) throws SQLException{
		return usuario.getAtributoPerfil(login, atributo);
	}
	
	/**
	 * Retorna toda a informação do perfil do usuário.
	 * 
	 * @param idSessao id sessão do usuário
	 * @param login login do usuário
	 * @return objeto usuário contendo as informações do perfil
	 * @throws Exception
	 */
	public Usuario visualizarPerfil(String idSessao, String login) throws Exception{
		return usuario.visualizarPerfil(idSessao, login);
	}
	
	/**
	 * Retorna o id da carona a partir do índice.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param indexCarona índice da posição da carona que define a ordem com que a carona foi cadastrada
	 * @return id da carona
	 * @throws SQLException
	 */
	public String getCaronaUsuario(String idSessao, int indexCarona) throws SQLException{
		return usuario.getCaronaUsuario(idSessao, indexCarona);
	}
	
	/**
	 * Retorna todas as caronas do usuário.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @return todas as caronas do usuário
	 * @throws SQLException
	 */
	public String getTodasCaronasUsuario(String idSessao) throws SQLException{
		Map<String, Carona> caronas = usuario.getTodasCaronasUsuario(idSessao);
		String keys = caronas.keySet().toString();
		keys = keys.replaceAll(" ", "");
		keys = "{" + keys.substring(1, keys.length()-1) + "}";
		return keys;
	}
	
	/**
	 * Retorna as solicitações confirmadas.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idCarona id da carona
	 * @return solicitações confirmadas do usuário
	 * @throws SQLException
	 */
	public String getSolicitacoesConfirmadas(String idSessao, String idCarona) throws SQLException{
		List<String> solicitacoes = motorista.getSolicitacoesConfirmadas(idSessao, idCarona);
		String keys = solicitacoes.toString();
		keys = keys.replaceAll(" ", "");
		keys = "{" + keys.substring(1, keys.length()-1) + "}";
		return keys;
	}
	
	/**
	 * Retorna as solicitações pendentes.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idCarona id da carona
	 * @return solicitações pendentes do usuário
	 * @throws SQLException
	 */
	public String getSolicitacoesPendentes(String idSessao, String idCarona) throws SQLException{
		List<String> solicitacoes = motorista.getSolicitacoesPendentes(idSessao, idCarona);
		String keys = solicitacoes.toString();
		keys = keys.replaceAll(" ", "");
		keys = "{" + keys.substring(1, keys.length()-1) + "}";
		return keys;
	}
	
	/**
	 * Retorna os pontos de encontro de cada caroneiro da carona.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idCarona id da carona
	 * @return pontos de encontro de cada caroneiro da carona
	 * @throws SQLException
	 */
	public List<String> getPontosEncontro(String idSessao, String idCarona) throws SQLException{
		return motorista.getPontosEncontro(idSessao, idCarona);
	}
	
	/**
	 * Retorna os pontos sugeridos de cada caroneiro da carona.
	 * 
	 * @param idSessao
	 * @param idCarona
	 * @return pontos sugeridos de cada caroneiro da carona
	 * @throws SQLException
	 */
	public List<String> getPontosSugeridos(String idSessao, String idCarona) throws SQLException{
		return motorista.getPontosSugeridos(idSessao, idCarona);
	}
	
	/**
	 * Atribui falta ou presença ao caroneiro.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param idCarona id da carona
	 * @param loginCaroneiro login do caroneiro
	 * @param review falta ou presença do caroneiro. Entradas possíveis: faltou, não faltou
	 * @throws Exception
	 */
	public void reviewVagaEmCarona(String idSessao, String idCarona, String loginCaroneiro, String review) throws Exception{
		motorista.reviewVagaEmCarona(idSessao, idCarona, loginCaroneiro, review);
	}
	
	/**
	 * Reinicia o sistema.
	 */
	public void reiniciarSistema(){
		
	}
	
	/**
	 * Encerra a sessão do usuário.
	 * 
	 * @param login login do usuário
	 */
	public void encerrarSessao(String login){
		
	}
	
	/**
	 * Encerra o sistema.
	 */
	public void encerrarSistema(){
		
	}
	
	/**
	 * Apaga todas as caronas no sistema.
	 * @throws SQLException 
	 */
	public void zerarSistema() throws SQLException{
		ZerarSistema zerarSistema = new ZerarSistema();
		zerarSistema.zerarSistema();
	}	
	
	public static void main(String[] args){
		args = new String[] {"model.ModelFacade", "src/test/resources/US01.txt",
				"src/test/resources/US02.txt", "src/test/resources/US03.txt",
				"src/test/resources/US04.txt", "src/test/resources/US05.txt",
				"src/test/resources/US06.txt", "src/test/resources/US07.txt",
				"src/test/resources/US08.txt"};
		EasyAccept.main(args);
	}
}
