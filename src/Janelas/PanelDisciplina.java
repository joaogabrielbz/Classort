package janelas;

// joaogabrielbz //

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import entidades.Disciplina;
import entidades.Turma;
import entidades.Turno;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.MatteBorder;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.BevelBorder;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelDisciplina extends JPanel {

	public TelaInicial janela;
	public Turno turno;

	private ArrayList<Integer> idsDisciplinas = new ArrayList<Integer>();
	private int idDisciplinaSelecionada;

	private JTextField txtNomeDisciplina;
	private JTextField txtProfessorDisciplina;
	private JList<String> listDisciplinas;
	private JButton btNovaDisciplina;
	private JButton btRemoverDisciplina;

	private static final long serialVersionUID = 1L;

	public PanelDisciplina(Statement statement, TelaInicial janela, Turno turno) throws SQLException {
		setBackground(new Color(30, 30, 30));
		this.janela = janela;
		this.turno = turno;

		JLabel lblVoltar = new JLabel("< Voltar ");
		lblVoltar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblVoltar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(45, 45, 45)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblVoltar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(30, 30, 30)));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				janela.setContentPane(janela.panelturma);
				janela.revalidate();
				janela.repaint();
			}
		});
		lblVoltar.setHorizontalAlignment(SwingConstants.LEFT);
		lblVoltar.setForeground(new Color(136, 136, 136));
		lblVoltar.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		lblVoltar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(30, 30, 30)));

		JLabel lblTitulo = new JLabel("Professores do turno " + turno.getNomeTurno() + ":");
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(new Color(136, 136, 136));
		lblTitulo.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(30, 30, 30));

		JLabel lblNomeDisciplina = new JLabel("Nome da disciplina:");
		lblNomeDisciplina.setHorizontalAlignment(SwingConstants.LEFT);
		lblNomeDisciplina.setForeground(new Color(136, 136, 136));
		lblNomeDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));

		txtNomeDisciplina = new JTextField();
		txtNomeDisciplina.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String nomeDisciplina = txtNomeDisciplina.getText();
					String professorDisciplina = txtProfessorDisciplina.getText();

					if (!nomeDisciplina.isEmpty() && !professorDisciplina.isEmpty()) {
						Disciplina novaDisciplina = new Disciplina(0, nomeDisciplina, professorDisciplina,
								turno.getIdTurno());
						if (btNovaDisciplina.getText().equals("Nova disciplina")) {
							insertDisciplina(statement, turno, novaDisciplina);
						} else {
							alterarDisciplina(statement, novaDisciplina);
						}
						reiniciarLayout();
					}
					txtNomeDisciplina.requestFocusInWindow();
				}
			}
		});
		txtNomeDisciplina.setForeground(Color.WHITE);
		txtNomeDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		txtNomeDisciplina.setColumns(10);
		txtNomeDisciplina.setBackground(new Color(45, 45, 45));

		JLabel lblProfessorDisciplina = new JLabel("Nome do professor:");
		lblProfessorDisciplina.setHorizontalAlignment(SwingConstants.LEFT);
		lblProfessorDisciplina.setForeground(new Color(136, 136, 136));
		lblProfessorDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));

		txtProfessorDisciplina = new JTextField();
		txtProfessorDisciplina.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String nomeDisciplina = txtNomeDisciplina.getText();
					String professorDisciplina = txtProfessorDisciplina.getText();

					if (!nomeDisciplina.isEmpty() && !professorDisciplina.isEmpty()) {
						Disciplina novaDisciplina = new Disciplina(0, nomeDisciplina, professorDisciplina,
								turno.getIdTurno());
						if (btNovaDisciplina.getText().equals("Nova disciplina")) {
							insertDisciplina(statement, turno, novaDisciplina);
						} else {
							alterarDisciplina(statement, novaDisciplina);
						}
						reiniciarLayout();
					}
					txtNomeDisciplina.requestFocusInWindow();
				}
			}
		});
		txtProfessorDisciplina.setForeground(Color.WHITE);
		txtProfessorDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		txtProfessorDisciplina.setColumns(10);
		txtProfessorDisciplina.setBackground(new Color(45, 45, 45));

		btNovaDisciplina = new JButton("Nova disciplina");
		btNovaDisciplina.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				String nomeDisciplina = txtNomeDisciplina.getText();
				String professorDisciplina = txtProfessorDisciplina.getText();

				if (!nomeDisciplina.isEmpty() && !professorDisciplina.isEmpty()) {
					Disciplina novaDisciplina = new Disciplina(0, nomeDisciplina, professorDisciplina,
							turno.getIdTurno());
					if (btNovaDisciplina.getText().equals("Nova disciplina")) {
						insertDisciplina(statement, turno, novaDisciplina);
					} else {
						alterarDisciplina(statement, novaDisciplina);
					}
					reiniciarLayout();
				}
				txtNomeDisciplina.requestFocusInWindow();
			}

		});
		btNovaDisciplina.setForeground(Color.WHITE);
		btNovaDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btNovaDisciplina.setBackground(new Color(45, 45, 45));

		btRemoverDisciplina = new JButton("Remover disciplina");
		btRemoverDisciplina.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				deleteDisciplina(statement);
			}
		});
		btRemoverDisciplina.setForeground(Color.WHITE);
		btRemoverDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btRemoverDisciplina.setBackground(new Color(172, 0, 9));
		btRemoverDisciplina.setVisible(false);

		JButton btnAvancar = new JButton("Avançar");
		btnAvancar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// Lista todas as disciplinas e turmas de determinado turno//

				ArrayList<Turma> turmas = new ArrayList<Turma>();
				ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();

				String sql = "SELECT * FROM classortbd.turma WHERE turnoid = " + turno.getIdTurno();
				try {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						turmas.add(new Turma(resultset.getInt("idturma"), resultset.getString("nometurma"),
								resultset.getInt("turnoid")));

					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				sql = "SELECT * FROM classortbd.disciplina WHERE turnoid = " + turno.getIdTurno();
				try {
					ResultSet resultset = statement.executeQuery(sql);
					while (resultset.next()) {
						disciplinas.add(
								new Disciplina(resultset.getInt("iddisciplina"), resultset.getString("nomedisciplina"),
										resultset.getString("professordisciplina"), resultset.getInt("turnoid")));

					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				try {
					janela.panelturmadisciplina = new PanelTurmaDisciplina(statement, janela, turno, turmas,
							disciplinas);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				janela.setContentPane(janela.panelturmadisciplina);
				janela.revalidate();
				janela.repaint();
			}
		});
		btnAvancar.setForeground(Color.WHITE);
		btnAvancar.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btnAvancar.setBackground(new Color(45, 45, 45));

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
						.addComponent(lblVoltar, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 60,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTitulo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
						.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btNovaDisciplina, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
								.addComponent(btRemoverDisciplina, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
						.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(lblNomeDisciplina, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
								.addGap(225))
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(txtNomeDisciplina, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												404, Short.MAX_VALUE)
										.addComponent(lblProfessorDisciplina, Alignment.LEADING,
												GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
										.addComponent(txtProfessorDisciplina, Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
								.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(btnAvancar, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()))));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(20)
				.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(18)
				.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout
						.createParallelGroup(
								Alignment.LEADING)
						.addGroup(
								groupLayout.createSequentialGroup()
										.addComponent(lblNomeDisciplina, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtNomeDisciplina, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblProfessorDisciplina, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addGap(6)
										.addComponent(txtProfessorDisciplina, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(btRemoverDisciplina, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btNovaDisciplina, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(btnAvancar, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
				.addGap(25)));

		listDisciplinas = new JList<String>();
		listDisciplinas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteDisciplina(statement);
				}
			}
		});
		listDisciplinas.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (!e.getValueIsAdjusting()) {
					if (listDisciplinas.getSelectedIndex() != -1) {
						String disciplinaProfessor = listDisciplinas.getModel()
								.getElementAt(listDisciplinas.getSelectedIndex());
						String[] disciplinaProfessorSeparados = disciplinaProfessor.split(" - ");
						txtNomeDisciplina.setText(disciplinaProfessorSeparados[0]);
						txtProfessorDisciplina.setText(disciplinaProfessorSeparados[1]);

						idDisciplinaSelecionada = idsDisciplinas.get(listDisciplinas.getSelectedIndex());

						btNovaDisciplina.setText("Salvar");
						btRemoverDisciplina.setVisible(true);
						btRemoverDisciplina.setText("Remover Disciplina: " + disciplinaProfessor);
					}
				}

			}
		});
		listDisciplinas.setVisibleRowCount(10);

		listDisciplinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDisciplinas.setForeground(Color.WHITE);
		listDisciplinas.setFont(new Font("Noto Sans Light", Font.BOLD, 20));
		listDisciplinas.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listDisciplinas.setBackground(new Color(45, 45, 45));
		scrollPane.setViewportView(listDisciplinas);
		setLayout(groupLayout);

		listDisciplinas.setModel(gerarListModelDisciplina(statement));
	}

	protected void alterarDisciplina(Statement statement, Disciplina novaDisciplina) {
		String sql = "UPDATE classortbd.disciplina SET nomedisciplina='" + novaDisciplina.getNomeDisciplina() + "', "
				+ "professordisciplina='" + novaDisciplina.getProfessorDisciplina() + "'" + "	WHERE idDisciplina = "
				+ idDisciplinaSelecionada + ";";
		try {
			statement.execute(sql);
			listDisciplinas.setModel(gerarListModelDisciplina(statement));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	protected void insertDisciplina(Statement statement, Turno turno2, Disciplina novaDisciplina) {
		boolean existe = false;
		for (int i = 0; i != listDisciplinas.getModel().getSize(); i++) {
			if (listDisciplinas.getModel().getElementAt(i)
					.equals(novaDisciplina.getNomeDisciplina() + " - " + novaDisciplina.getProfessorDisciplina())) {
				existe = true;
			}
		}
		if (!existe) {
			String sql = "INSERT INTO classortbd.disciplina(nomedisciplina, professordisciplina, turnoid) "
					+ "VALUES ('" + novaDisciplina.getNomeDisciplina() + "', '"
					+ novaDisciplina.getProfessorDisciplina() + "', " + turno.getIdTurno() + ");";
			try {
				statement.execute(sql);
				listDisciplinas.setModel(gerarListModelDisciplina(statement));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

	public DefaultListModel<String> gerarListModelDisciplina(Statement statement) throws SQLException {

		DefaultListModel<String> modelDisciplina = new DefaultListModel<String>();
		idsDisciplinas = new ArrayList<Integer>();

		String sql = "SELECT * FROM classortbd.disciplina WHERE turnoid = " + turno.getIdTurno() + "";
		ResultSet result = statement.executeQuery(sql);

		while (result.next()) {
			String elemento = result.getString("nomeDisciplina") + " - " + result.getString("professorDisciplina");
			modelDisciplina.addElement(elemento);
			idsDisciplinas.add(result.getInt("idDisciplina"));
		}
		return modelDisciplina;
	}

	private void reiniciarLayout() {
		btRemoverDisciplina.setVisible(false);
		btNovaDisciplina.setText("Nova disciplina");
		txtNomeDisciplina.setText("");
		txtProfessorDisciplina.setText("");
	}

	private void deleteDisciplina(Statement statement) {
		if (idDisciplinaSelecionada > 0) {
			String sql = "DELETE FROM classortbd.turma_disciplina WHERE disciplinaId =" + idDisciplinaSelecionada + ";"
					+ "DELETE FROM classortbd.disciplina WHERE idDisciplina = " + idDisciplinaSelecionada + ";";
			try {
				statement.execute(sql);
				listDisciplinas.setModel(gerarListModelDisciplina(statement));

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			reiniciarLayout();
		}
	}
}