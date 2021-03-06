package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import model.Carona;

/**
 * Classe responsável por gerenciar as operações da tabela carona do banco de dados.
 * 
 * @author Yorras Gomes, Fábio Dantas
 *
 */
public class CaronaDAO {
	
	private static final Logger logger = LogManager.getLogger(CaronaDAO.class);
	
	private static CaronaDAO instanciaUnica = null;	
	
	/**
	 * Contrutor padrão.
	 */
	private CaronaDAO(){
		
	}
	
	/**
	 * Abre uma instância única da base de dados.
	 * 
	 * @return CaronaDAO
	 */
	public static CaronaDAO getInstance(){
		logger.info("Abrindo conexão com a base de dados de caronas");
		
		if(instanciaUnica == null){
			instanciaUnica = new CaronaDAO();			
		}		
		return instanciaUnica;
	}
	
	/**
	 * Recebe uma carona e a armazena no banco de dados do sistema.
	 * 
	 * @param carona objeto carona
	 * @return id da carona
	 * @throws SQLException 
	 */
	public String cadastrarCarona(Carona carona) throws SQLException{
		logger.info("Cadastrando carona");
		
		String idCarona = "";		
		Connection conexao = new ConnectionFactory().getConnection();		
		String sql = "INSERT INTO carona " + "(origem,destino,data,hora,vagas,idUsuario) "
				+ "values (?,?,?,?,?,?)";		
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu");		
		PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, carona.getOrigem());
		stmt.setString(2, carona.getDestino());
		stmt.setString(3, LocalDate.parse(carona.getData(), formato).toString());
		stmt.setString(4, carona.getHora());
		stmt.setInt(5, carona.getVagas());
		stmt.setString(6, carona.getIdUsuario());		
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();		
		while(rs.next()){
			idCarona = rs.getString(1);
		}
		stmt.close();		
		conexao.close();		
		return idCarona;
	}
	
	/**
	 * Recebe a origem e o destino. Retorna a carona e sua id. Tanto a origem quanto o destino podem ser
	 * deixados em branco. origem = "" retorna todas as caronas com o destino não em branco. destino = ""
	 * retorna todas as caronas com a origem não em branco. origem = "" e destino = "" retorna todas as caronas
	 * do sistema.
	 * 
	 * @param origem origem da carona
	 * @param destino destino da carona
	 * @return um map contendo o id e a carona
	 * @throws SQLException 
	 */
	public Map<String, Carona> localizarCarona(String origem, String destino) throws SQLException{
		logger.info("Localizando carona - origem: " + origem + " - destino: " + destino);
		
		Map<String, Carona> caronasLocalizadas = new LinkedHashMap<String, Carona>();		
		String sql = "";		
		if(!origem.equals("")){
			if(!destino.equals("")){
				sql = "SELECT * FROM carona WHERE origem = '" + origem +"' AND destino = '" + destino + "'";
			}
			else{
				sql = "SELECT * FROM carona WHERE origem = '" + origem +"'";
			}
		}
		else{
			if(!destino.equals("")){
				sql = "SELECT * FROM carona WHERE destino = '" + destino + "'";
			}
			else{
				sql = "SELECT * FROM carona";
			}
		}		
		Connection conexao = new ConnectionFactory().getConnection();
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		while (rs.next()) {
			LocalDate data = LocalDate.parse(rs.getString("data"));
			LocalTime hora = LocalTime.parse(rs.getString("hora"));
			Carona carona = new Carona(rs.getString("origem"), rs.getString("destino"), data, hora, rs.getInt("vagas"));
			carona.setIdUsuario(rs.getString("idUsuario"));
			caronasLocalizadas.put(rs.getString("id"), carona);
		}
		stmt.execute();
		stmt.close();
		conexao.close();								
		return caronasLocalizadas;
	}	
	
	/**
	 * Recebe o id da carona e retorna a sua origem.
	 * 
	 * @param idCarona id da carona
	 * @return origem da carona
	 * @throws SQLException 
	 */
	public String origemCarona(String idCarona) throws SQLException{
		logger.info("Retornando origem da carona - id da carona: " + idCarona);
		
		return retornaInformacaoCarona(idCarona, "origem");
	}	
	
	/**
	 * Recebe o id da carona e retorna o seu destino.
	 * 
	 * @param idCarona id da carona
	 * @return destino da carona
	 * @throws SQLException 
	 */
	public String destinoCarona(String idCarona) throws SQLException{
		logger.info("Retornando destino da carona - id da carona: " + idCarona);
		
		return retornaInformacaoCarona(idCarona, "destino");
	}
	
	/**
	 * Recebe o id da carona e retorna a sua data.
	 * 
	 * @param idCarona id da carona
	 * @return data da carona
	 * @throws SQLException 
	 */
	public String dataCarona(String idCarona) throws SQLException{
		logger.info("Retornando data da carona - id da carona: " + idCarona);
		
		String data = retornaInformacaoCarona(idCarona, "data");		
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu");		
		return LocalDate.parse(data).format(formato);
	}
	
	/**
	 * Recebe o id da carona e retorna a sua hora.
	 * 
	 * @param idCarona id da carona
	 * @return hora da carona
	 * @throws SQLException
	 */
	public String horaCarona(String idCarona) throws SQLException{
		logger.info("Retornando hora da carona - id da carona: " + idCarona);
		
		String hora = retornaInformacaoCarona(idCarona, "hora");		
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");	
		return LocalTime.parse(hora).format(formato);
	}
	
	/**
	 * Recebe o id da carona e nome da coluna da tabela carona onde a informação está armazenada no banco de
	 * dados. Por fim, retorna a informação dessa coluna.
	 * 
	 * @param idCarona id da carona
	 * @param coluna uma coluna equivalente da tabela carona no banco de dados. Exemplo: origem, destino, data, hora.
	 * @return a informação armazenada da coluna.
	 * @throws SQLException
	 */
	private String retornaInformacaoCarona(String idCarona, String coluna) throws SQLException{
		String informacao = "";		
		Connection conexao = new ConnectionFactory().getConnection();		
		String sql = "SELECT " + coluna + " FROM carona WHERE id = '" + idCarona + "'";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		while (rs.next()) {
			informacao = rs.getString(coluna);
		}
		stmt.execute();
		stmt.close();
		conexao.close();		
		return informacao;
	}
	
	/**
	 * Recebe o id da carona e retorna a quantidade de vagas.
	 * 
	 * @param idCarona id da carona
	 * @return quantidade de vagas da carona
	 * @throws SQLException 
	 */
	public int vagasCarona(String idCarona) throws SQLException{
		logger.info("Retornando vagas da carona - id da carona: " + idCarona);
		
		int vagas = 0;		
		Connection conexao = new ConnectionFactory().getConnection();		
		String sql = "SELECT vagas FROM carona WHERE id = '" + idCarona + "'";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		while (rs.next()) {
			vagas = rs.getInt("vagas");
		}
		stmt.execute();
		stmt.close();
		conexao.close();		
		return vagas;
	}
	
	/**
	 * Recebe o id da carona e retorna o seu trajeto, ou seja, a origem e o destino.
	 * 
	 * @param idCarona id da carona
	 * @return trajeto da carona
	 * @throws SQLException 
	 */
	public String trajetoCarona(String idCarona) throws SQLException{
		logger.info("Retornando trajeto da carona - id da carona: " + idCarona);
		
		return origemCarona(idCarona) + " - " + destinoCarona(idCarona);
	}
	
	/**
	 * Recebe o id da carona e retorna informações como origem, destino, data e hora.
	 * 
	 * @param idCarona id da carona
	 * @return informações da carona
	 * @throws SQLException 
	 */
	public String informacoesCarona(String idCarona) throws SQLException{
		logger.info("Retornando informações da carona - id da carona: " + idCarona);
		
		String origem = origemCarona(idCarona);
		String destino = destinoCarona(idCarona);
		String data = dataCarona(idCarona);
		String hora = horaCarona(idCarona);
		return  origem + " para " + destino + ", no dia " + data + ", as " + hora;
	}
	
	/**
	 * Recebe o id da carona e verifica se ele é válido, ou seja, se o id da carona existe no banco de dados.
	 * 
	 * @param idCarona id da carona
	 * @return true = id da carona válido, false = id da carona inválido
	 * @throws SQLException 
	 */
	public boolean verificaCarona(String idCarona) throws SQLException{
		logger.info("Verificando se o id da carona é válido - id da carona: " + idCarona);
		
		Connection conexao = new ConnectionFactory().getConnection();
		String sql = "SELECT id FROM carona WHERE id = '" + idCarona +"'";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		boolean resultadoId = rs.next();		
		stmt.execute();
		stmt.close();
		conexao.close();		
		return resultadoId;
	}
	
	/**
	 * Recebe o login do usuário e retorna todo o histórico de caronas desse usuário.
	 * 
	 * @param login login do usuário
	 * @return histórico de caronas do usuário
	 * @throws SQLException
	 */
	public List<String> historicoCaronas(String login) throws SQLException{
		logger.info("Retornando o histórico de caronas do usuário - login: " + login);
		
		List<String> id = new ArrayList<String>();		
		Connection conexao = new ConnectionFactory().getConnection();
		String sql = "SELECT carona.id FROM carona, usuario WHERE carona.idUsuario = usuario.id"
				+ " AND usuario.login = '" + login + "'";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		while (rs.next()) {
			id.add(rs.getString("id"));
		}		
		stmt.execute();
		stmt.close();
		conexao.close();		
		return id;
	}
	
	/**
	 * Recebe o id da sessão do usuário e o índice da posição da carona e retorna o id da carona.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @param indexCarona índice da posição da carona que define a ordem com que a carona foi cadastrada
	 * @return id da carona
	 * @throws SQLException
	 */
	public String getCaronaUsuario(String idSessao, int indexCarona) throws SQLException{
		logger.info("Retornando uma carona pela posição de cadastro - id da sessão do usuário: " + idSessao);
		
		List<String> id  = new ArrayList<String>();
		Connection conexao = new ConnectionFactory().getConnection();
		String sql = "SELECT id FROM carona WHERE idUsuario = '" + idSessao +"'";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		while(rs.next()){
			id.add(rs.getString("id"));
		}		
		stmt.execute();
		stmt.close();
		conexao.close();		
		return id.get(indexCarona-1);
	}
	
	/**
	 * Recebe o id da sessão do usuário e retorna todas as caronas desse usuário.
	 * 
	 * @param idSessao id da sessão do usuário
	 * @return todas as caronas do usuário
	 * @throws SQLException
	 */
	public Map<String, Carona> getTodasCaronasUsuario(String idSessao) throws SQLException{
		Map<String, Carona> caronasLocalizadas = new LinkedHashMap<String, Carona>();				
		
		Connection conexao = new ConnectionFactory().getConnection();
		
		String sql = "SELECT * FROM carona WHERE idUsuario = '" + idSessao +"'";

		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next()) {
			LocalDate data = LocalDate.parse(rs.getString("data"));
			LocalTime hora = LocalTime.parse(rs.getString("hora"));
			Carona carona = new Carona(rs.getString("origem"), rs.getString("destino"), data, hora, rs.getInt("vagas"));
			carona.setIdUsuario(rs.getString("idUsuario"));
			caronasLocalizadas.put(rs.getString("id"), carona);
		}

		stmt.execute();
		stmt.close();

		conexao.close();		
								
		return caronasLocalizadas;
	}
	
	/**
	 * Recebe o login do usuário e retorna o total de caronas desse usuário marcadas como segura e tranquila.
	 * 
	 * @param login login do usuário
	 * @return total de caronas do usuário marcada como segura e tranquila
	 * @throws SQLException
	 */
	public int caronasSeguras(String login) throws SQLException{
		logger.info("Retornando total de caronas marcadas como segura e tranquila - login: " + login);
		
		int resultado = 0;		
		Connection conexao = new ConnectionFactory().getConnection();
		String sql = "SELECT count(*) FROM carona, usuario WHERE usuario.login = '" + login +"'"
				+ " AND carona.idUsuario = usuario.id AND carona.segura = true";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		while(rs.next()){
			resultado = rs.getInt("count(*)");
		}		
		stmt.execute();
		stmt.close();
		conexao.close();		
		return resultado;
	}
	
	/**
	 * Recebe o login do usuário e retorna o total de caronas que não funcionaram.
	 * 
	 * @param login login da carona
	 * @return total de caronas que não funcionaram
	 * @throws SQLException
	 */
	public int caronasQueNaoFuncionou(String login) throws SQLException{
		logger.info("Retornando total de caronas que não funcionaram - login: " + login);
		
		int resultado = 0;		
		Connection conexao = new ConnectionFactory().getConnection();
		String sql = "SELECT count(*) FROM carona, usuario WHERE usuario.login = '" + login +"'"
				+ " AND carona.idUsuario = usuario.id AND carona.funcionou = false";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();		
		while(rs.next()){
			resultado = rs.getInt("count(*)");
		}		
		stmt.execute();
		stmt.close();
		conexao.close();		
		return resultado;
	}
	
	/**
	 * Apaga todas as caronas que estão armazenadas no banco de dados.
	 * 
	 * @throws SQLException 
	 */
	public void apagarCaronas() throws SQLException{
		Connection conexao1 = new ConnectionFactory().getConnection();
		String sql1 = "DELETE FROM carona WHERE id > 0";
		PreparedStatement stmt1 = conexao1.prepareStatement(sql1);
		stmt1.execute();
		stmt1.close();		
		conexao1.close();
		
		Connection conexao2 = new ConnectionFactory().getConnection();
		String sql2 = "ALTER TABLE carona AUTO_INCREMENT = 1";
		PreparedStatement stmt2 = conexao2.prepareStatement(sql2);
		stmt2.execute();
		stmt2.close();		
		conexao2.close();
	}

}
