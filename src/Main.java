import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import janelas.TelaInicial;;

public class Main {

    public static void main(String[] args) throws IOException {
        String url = "jdbc:postgresql://localhost:5432/";
        String usuario = "postgres";
        String senha = "admin";

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            if (conexao != null) {
                System.out.println("Conexão estabelecida com PostgreSQL");
                Statement statement = conexao.createStatement();

                // Verificação do banco de dados //
                String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'classortbd';";
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    System.out.println("O banco de dados existe");                    
                } else {
                    System.out.println("Criando banco de dados...");
                    File arquivoScript = new File("src/bancodedados/scriptbd.txt");
                    sql = new String(Files.readAllBytes(arquivoScript.toPath()));
                    statement.execute(sql);
                    System.out.println("Banco de dados criado com sucesso");
                }
                // Inicia a aplicação //
                System.out.println("Iniciando o programa");
                TelaInicial telainicial = new TelaInicial(statement);
                telainicial.setVisible(true);
            }
        } catch (SQLException e) {
            System.out.println("Falha na conexão com PostgreSQL!");
            e.printStackTrace();
        }
    }


}