package janelas;

// joaogabrielbz // 

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtSenha;
	private Main main = this;

	static String url = "jdbc:postgresql://localhost:5432/";

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					ArrayList<String> dados = new ArrayList<String>();
					String diretorioDoJar = obterDiretorioDoJar();
					String nomeArquivo = "credenciais.txt";
					File arquivo = new File(diretorioDoJar, nomeArquivo);

					if (arquivo.exists()) {
						try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
							String linha;
							while ((linha = reader.readLine()) != null) {
								dados.add(linha);
							}
						}
					}

					Main frame = new Main();
					frame.setLocationRelativeTo(null);

					if (dados.size() == 0) {
						frame.setVisible(true);
					} else {
						conectar(url, dados.get(0), dados.get(1));
						frame.dispose();
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	public Main() {
		setTitle("Conexão Postgres");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(TelaInicial.class.getResource("/imgs/icon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 287, 190);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 30));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btEntrar = new JButton("Conectar & Salvar");
		btEntrar.setFont(new Font("Noto Sans Light", Font.PLAIN, 11));
		btEntrar.setBackground(new Color(45, 45, 45));
		btEntrar.setForeground(new Color(255, 255, 255));
		btEntrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				String usuario = txtUsuario.getText();
				char[] charSenha = txtSenha.getPassword();
				String senha = new String(charSenha);

				if (!usuario.equals("") && !senha.equals("")) {

					if (conectar(url, usuario, senha)) {
						String diretorioDoJar = obterDiretorioDoJar();
						String nomeArquivo = "credenciais.txt";
						File arquivo = new File(diretorioDoJar, nomeArquivo);

						try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
							writer.write(usuario + "\n" + senha);
						} catch (Exception e2) {
							e2.printStackTrace();
						}

						main.dispose();
					}

					else {
						main.setTitle("INCORRETO!");
						main.getContentPane().setBackground(new Color(192, 0, 9));
					}
				}

			}

		});
		btEntrar.setBounds(10, 118, 257, 23);
		contentPane.add(btEntrar);

		txtUsuario = new JTextField();
		txtUsuario.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		txtUsuario.setForeground(new Color(255, 255, 255));
		txtUsuario.setBackground(new Color(45, 45, 45));
		txtUsuario.setBounds(10, 45, 257, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);

		txtSenha = new JPasswordField();
		txtSenha.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		txtSenha.setForeground(new Color(255, 255, 255));
		txtSenha.setBackground(new Color(45, 45, 45));
		txtSenha.setBounds(10, 87, 257, 20);
		contentPane.add(txtSenha);

		JLabel lblUsuario = new JLabel("Usuario Postgres:");
		lblUsuario.setFont(new Font("Noto Sans Light", Font.PLAIN, 15));
		lblUsuario.setForeground(new Color(255, 255, 255));
		lblUsuario.setBackground(new Color(30, 30, 30));
		lblUsuario.setBounds(10, 22, 257, 23);
		contentPane.add(lblUsuario);

		JLabel lblSenha = new JLabel("Senha Postgres:");
		lblSenha.setForeground(Color.WHITE);
		lblSenha.setFont(new Font("Noto Sans Light", Font.PLAIN, 15));
		lblSenha.setBackground(new Color(30, 30, 30));
		lblSenha.setBounds(10, 65, 257, 23);
		contentPane.add(lblSenha);
	}

	private static boolean conectar(String url, String usuario, String senha) {
		try {
			Connection conexao = DriverManager.getConnection(url, usuario, senha);
			if (conexao != null) {
				System.out.println("Conexão estabelecida com PostgreSQL");
				Statement statement = conexao.createStatement();

				try {
					criarBd(statement);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} catch (SQLException e1) {
			System.out.println("Falha na conexão com PostgreSQL!");
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	private static void criarBd(Statement statement) throws SQLException, IOException {
		String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'classortbd';";
		ResultSet resultSet = statement.executeQuery(sql);

		if (resultSet.next()) {
			System.out.println("O banco de dados existe");
		} else {
			System.out.println("Criando banco de dados...");

			ClassLoader classLoader = Main.class.getClassLoader();
			try (InputStream inputStream = classLoader.getResourceAsStream("bancodedados/scriptbd.txt");
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

				sql = bufferedReader.lines().collect(Collectors.joining("\n"));

			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println(sql);
			statement.execute(sql);
			System.out.println("Banco de dados criado com sucesso");
		}

		System.out.println("Iniciando o programa");
		TelaInicial telainicial = new TelaInicial(statement);
		telainicial.setVisible(true);
	}

	private static String obterDiretorioDoJar() {
		try {
			String caminhoDoJar = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			String decodedPath = new File(caminhoDoJar).getParent();
			return decodedPath;
		} catch (URISyntaxException e) {
			throw new RuntimeException("Erro ao obter o diretório do JAR.", e);
		}
	}
}
