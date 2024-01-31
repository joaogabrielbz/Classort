package janelas;

//joaogabrielbz//

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import entidades.AulaProibida;
import entidades.Disciplina;
import entidades.Horario;
import entidades.Turma;
import entidades.Turno;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;

public class PanelDisciplina extends JPanel {

	public TelaInicial janela;
	public Turno turno;

	private ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
	private ArrayList<AulaProibida> aulasProibidas = new ArrayList<AulaProibida>();
	private int idDisciplinaSelecionada;

	private JTextField txtNomeDisciplina;
	private JTextField txtProfessorDisciplina;
	private JList<String> listDisciplinas;
	private JButton btNovaDisciplina;
	private JButton btRemoverDisciplina;
	private JLabel lblVoltar;
	private JLabel lblTitulo;
	private JLabel lblNomeDisciplina;
	private JLabel lblProfessorDisciplina;
	private JButton btAvancar;
	private JTable tableAulasProibidas;
	private JLabel lblAulasDuplas;
	private JRadioButton rdNegarDuplas;
	private JRadioButton rdPermitirDuplas;
	private JPanel panel;
	private ButtonGroup gp;
	private ArrayList<Horario> horarios = new ArrayList<Horario>();
	private int aulasPorDia;
	private int aulasPorSemana;

	String[] dias = { "seg", "ter", "qua", "qui", "sex", "sab", "dom" };
	String[] horas = {};

	private static final long serialVersionUID = 1L;

	public PanelDisciplina(Statement statement, TelaInicial janela, Turno turno) throws SQLException {
		setBackground(new Color(30, 30, 30));
		this.janela = janela;
		this.turno = turno;

		calcularHorariosESemana(statement);
		lblVoltar = new JLabel("< Voltar ");
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

		lblTitulo = new JLabel("Professores do turno " + turno.getNomeTurno() + ":");
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(new Color(136, 136, 136));
		lblTitulo.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(30, 30, 30));

		lblNomeDisciplina = new JLabel("Nome da disciplina:");
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
								rdPermitirDuplas.isSelected(), turno.getIdTurno());
						if (btNovaDisciplina.getText().equals("Nova disciplina")) {
							insertDisciplina(statement, turno, novaDisciplina);
						} else {
							updateDisciplina(statement, novaDisciplina);
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

		lblProfessorDisciplina = new JLabel("Nome do professor:");
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
								rdPermitirDuplas.isSelected(), turno.getIdTurno());
						if (btNovaDisciplina.getText().equals("Nova disciplina")) {
							insertDisciplina(statement, turno, novaDisciplina);
						} else {
							updateDisciplina(statement, novaDisciplina);
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
							rdPermitirDuplas.isSelected(), turno.getIdTurno());
					if (btNovaDisciplina.getText().equals("Nova disciplina")) {
						insertDisciplina(statement, turno, novaDisciplina);
					} else {
						updateDisciplina(statement, novaDisciplina);
					}
					reiniciarLayout();
				}
				txtNomeDisciplina.requestFocusInWindow();
				idDisciplinaSelecionada = 0;
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

		btAvancar = new JButton("Avançar");
		btAvancar.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("resource")
			@Override
			public void mousePressed(MouseEvent e) {

				ArrayList<Turma> turmas = new ArrayList<Turma>();
				ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();

				String sql = "SELECT * FROM classortbd.turma WHERE turnoid = " + turno.getIdTurno();
				try {
					ResultSet r = statement.executeQuery(sql);
					while (r.next()) {
						int idTurma = r.getInt("idTurma");
						String nomeTurma = r.getString("nometurma");
						int turnoId = r.getInt("turnoId");

						turmas.add(new Turma(idTurma, nomeTurma, turnoId));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				sql = "SELECT * FROM classortbd.disciplina WHERE turnoid = " + turno.getIdTurno();
				try {
					ResultSet r = statement.executeQuery(sql);
					while (r.next()) {
						int idDisciplina = r.getInt("idDisciplina");
						String nomeDisciplina = r.getString("nomeDisciplina");
						String professorDisciplina = r.getString("professorDisciplina");
						boolean aulasDuplas = r.getBoolean("aulasDuplas");
						int turnoId = r.getInt("turnoId");

						Disciplina novaDisciplina = new Disciplina(idDisciplina, nomeDisciplina, professorDisciplina,
								aulasDuplas, turnoId);

						disciplinas.add(novaDisciplina);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				Collections.sort(turmas, Comparator.comparing(Turma::getNomeTurma));
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
		btAvancar.setForeground(Color.WHITE);
		btAvancar.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btAvancar.setBackground(new Color(45, 45, 45));

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

						idDisciplinaSelecionada = disciplinas.get(listDisciplinas.getSelectedIndex()).getIdDisciplina();

						if (disciplinas.get(listDisciplinas.getSelectedIndex()).isAulasDuplas()) {
							rdPermitirDuplas.setSelected(true);
							rdNegarDuplas.setSelected(false);
						} else {
							rdPermitirDuplas.setSelected(false);
							rdNegarDuplas.setSelected(true);
						}

						btNovaDisciplina.setText("Salvar");
						btRemoverDisciplina.setVisible(true);
						btRemoverDisciplina.setText("Remover Disciplina: " + disciplinaProfessor);

						try {
							tableAulasProibidas
									.setModel(gerarTableModelAulasProibidas(statement, idDisciplinaSelecionada));
							tableAulasProibidas.setDefaultRenderer(Object.class, new cellRender());
						} catch (SQLException e1) {

							e1.printStackTrace();
						}

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

		panel = new JPanel();
		panel.setBackground(new Color(30, 30, 30));

		lblAulasDuplas = new JLabel("Permitir aulas duplas:");
		lblAulasDuplas.setHorizontalAlignment(SwingConstants.LEFT);
		lblAulasDuplas.setForeground(new Color(136, 136, 136));
		lblAulasDuplas.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));

		rdPermitirDuplas = new JRadioButton("Sim");
		rdPermitirDuplas.setForeground(new Color(255, 255, 255));
		rdPermitirDuplas.setBackground(new Color(30, 30, 30));
		rdPermitirDuplas.setFont(new Font("Noto Sans Light", Font.PLAIN, 15));

		rdNegarDuplas = new JRadioButton("Não");
		rdNegarDuplas.setForeground(new Color(255, 255, 255));
		rdNegarDuplas.setBackground(new Color(30, 30, 30));
		rdNegarDuplas.setFont(new Font("Noto Sans Light", Font.PLAIN, 15));

		gp = new ButtonGroup();
		gp.add(rdPermitirDuplas);
		gp.add(rdNegarDuplas);

		JScrollPane scrollAulasProibidas = new JScrollPane();

		tableAulasProibidas = new JTable();
		tableAulasProibidas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableAulasProibidas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = tableAulasProibidas.rowAtPoint(e.getPoint());
				int col = tableAulasProibidas.columnAtPoint(e.getPoint());
				String value = (String) tableAulasProibidas.getModel().getValueAt(row, col);

				if (idDisciplinaSelecionada != 0) {
					if (row > 0 && col > 0) {
						if (value == "X") {
							String sql = "DELETE FROM classortbd.aula_proibida WHERE disciplinaid = "
									+ idDisciplinaSelecionada + " AND horaindex = " + row + " AND" + "	diaindex = "
									+ col + ";";
							try {
								statement.execute(sql);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						} else {
							String sql = "INSERT INTO classortbd.aula_proibida(disciplinaid, horaindex, diaindex)"
									+ "	VALUES (" + idDisciplinaSelecionada + ", " + row + ", " + col + ");";
							try {
								statement.execute(sql);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
					try {
						tableAulasProibidas.setModel(gerarTableModelAulasProibidas(statement, idDisciplinaSelecionada));
					} catch (SQLException e1) {
						;
						e1.printStackTrace();
					}
				}

			}
		});

		JPanel panelAulasProibidas = new JPanel();
		panelAulasProibidas.setBackground(new Color(45, 45, 45));
		scrollAulasProibidas.setViewportView(panelAulasProibidas);
		panelAulasProibidas.setLayout(new BorderLayout(0, 0));
		panelAulasProibidas.add(tableAulasProibidas, BorderLayout.CENTER);
		tableAulasProibidas.setForeground(new Color(255, 255, 255));
		tableAulasProibidas.setBackground(new Color(45, 45, 45));
		tableAulasProibidas.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		tableAulasProibidas.setRowHeight(40);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addGap(10)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(lblTitulo, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE).addGap(474))
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE).addGap(
										6)
								.addGroup(
										groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(scrollAulasProibidas, GroupLayout.DEFAULT_SIZE, 468,
														Short.MAX_VALUE)
												.addGroup(
														groupLayout.createSequentialGroup()
																.addComponent(btNovaDisciplina,
																		GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
																.addGap(2)
																.addComponent(btRemoverDisciplina,
																		GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
																.addPreferredGap(ComponentPlacement.RELATED))
												.addComponent(panel, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblNomeDisciplina, GroupLayout.PREFERRED_SIZE, 253,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(txtNomeDisciplina, GroupLayout.DEFAULT_SIZE, 468,
														Short.MAX_VALUE)
												.addComponent(lblProfessorDisciplina, GroupLayout.PREFERRED_SIZE, 189,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(txtProfessorDisciplina, GroupLayout.DEFAULT_SIZE, 468,
														Short.MAX_VALUE)
												.addGroup(groupLayout.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED, 330,
																Short.MAX_VALUE)
														.addComponent(btAvancar, GroupLayout.PREFERRED_SIZE, 150,
																GroupLayout.PREFERRED_SIZE)))))
				.addGap(15)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(20)
						.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(18)
						.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE).addGap(6)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblNomeDisciplina, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addGap(6)
										.addComponent(txtNomeDisciplina, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addGap(11)
										.addComponent(lblProfessorDisciplina, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addGap(6)
										.addComponent(txtProfessorDisciplina, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addGap(11)
										.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(
												scrollAulasProibidas, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
												.addComponent(btRemoverDisciplina).addComponent(btNovaDisciplina))
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btAvancar)))
						.addGap(17)));

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAulasDuplas, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
								.addComponent(rdPermitirDuplas, GroupLayout.PREFERRED_SIZE, 53,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(rdNegarDuplas,
										GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)))
						.addGap(34)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(lblAulasDuplas, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addGap(2).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(rdPermitirDuplas).addComponent(rdNegarDuplas))));
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

		listDisciplinas.setModel(gerarListModelDisciplina(statement));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableAulasProibidas.setDefaultRenderer(Object.class, centerRenderer);

	}

	@SuppressWarnings("serial")
	public DefaultListModel<String> gerarListModelDisciplina(Statement statement) throws SQLException {

		DefaultListModel<String> modelDisciplina = new DefaultListModel<String>();
		disciplinas = new ArrayList<Disciplina>();

		String sql = "SELECT * FROM classortbd.disciplina WHERE turnoid = " + turno.getIdTurno() + "";
		ResultSet r = statement.executeQuery(sql);

		ArrayList<Disciplina> temp = new ArrayList<Disciplina>();
		while (r.next()) {
			int idDisciplina = r.getInt("idDisciplina");
			String nomeDisciplina = r.getString("nomeDisciplina");
			String professorDisciplina = r.getString("professorDisciplina");
			boolean aulasDuplas = r.getBoolean("aulasDuplas");

			temp.add(
					new Disciplina(idDisciplina, nomeDisciplina, professorDisciplina, aulasDuplas, turno.getIdTurno()));
		}

		Collections.sort(temp, Comparator.comparing(Disciplina::getNomeDisciplina));

		for (Disciplina d : temp) {
			modelDisciplina.addElement(d.getNomeCompleto());
		}
		disciplinas = temp;
		// Adicionando tabelas vazias de horarios
		String[][] matriz = gerarMatrizVazia();

		tableAulasProibidas.setModel(new DefaultTableModel(matriz, matriz[0]) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		return modelDisciplina;
	}

	protected void insertDisciplina(Statement statement, Turno turno2, Disciplina novaDisciplina) {
		boolean existe = false;
		for (int i = 0; i != listDisciplinas.getModel().getSize(); i++) {
			if (listDisciplinas.getModel().getElementAt(i).equals(novaDisciplina.getNomeCompleto())) {
				existe = true;
			}
		}
		if (!existe) {
			String sql = "INSERT INTO classortbd.disciplina(nomedisciplina, professordisciplina, aulasDuplas, turnoid) "
					+ "VALUES ('" + novaDisciplina.getNomeDisciplina() + "', '"
					+ novaDisciplina.getProfessorDisciplina() + "', " + rdPermitirDuplas.isSelected() + " ,"
					+ turno.getIdTurno() + ");";
			try {
				statement.execute(sql);
				listDisciplinas.setModel(gerarListModelDisciplina(statement));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	protected void updateDisciplina(Statement statement, Disciplina novaDisciplina) {
		String sql = "UPDATE classortbd.disciplina SET aulasDuplas = " + rdPermitirDuplas.isSelected()
				+ ", nomedisciplina='" + novaDisciplina.getNomeDisciplina() + "', " + "professordisciplina='"
				+ novaDisciplina.getProfessorDisciplina() + "'" + "	WHERE idDisciplina = " + idDisciplinaSelecionada
				+ ";";
		try {
			statement.execute(sql);
			listDisciplinas.setModel(gerarListModelDisciplina(statement));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
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

	private DefaultTableModel gerarTableModelAulasProibidas(Statement statement, int idDisciplinaSelecionada)
			throws SQLException {
		String sql = "SELECT * FROM classortbd.aula_proibida WHERE disciplinaId = " + idDisciplinaSelecionada + ";";
		ResultSet r = statement.executeQuery(sql);

		aulasProibidas = new ArrayList<>();

		String[][] matriz = gerarMatrizVazia();

		while (r.next()) {
			int idAulaProibida = r.getInt("idAulaProibida");
			int disciplinaId = r.getInt("disciplinaId");
			int horaIndex = r.getInt("horaIndex");
			int diaIndex = r.getInt("diaIndex");

			matriz[horaIndex][diaIndex] = "X";
			aulasProibidas.add(new AulaProibida(idAulaProibida, disciplinaId, horaIndex, diaIndex));
		}

		@SuppressWarnings("serial")
		DefaultTableModel model = new DefaultTableModel(matriz, matriz[0]) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return model;
	}

	private String[][] gerarMatrizVazia() {
		String[][] matriz = new String[aulasPorDia + 1][aulasPorSemana + 1];

		int i = 1;

		for (Horario h : horarios) {
			matriz[i][0] = h.getInicioHorario();
			i++;
		}

		String[] dias = { " ", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom" };

		int j = 1;

		while (j != aulasPorSemana + 1) {
			matriz[0][j] = dias[j];
			j++;
		}
		return matriz;
	}

	private void calcularHorariosESemana(Statement statement) throws SQLException {
		int aulasPorDia = 0;
		int aulasPorSemana = 0;
		ArrayList<Horario> horarios = new ArrayList<Horario>();

		String sql = "";
		ResultSet r = null;

		sql = "SELECT * FROM classortbd.horarios WHERE turnoId = " + turno.getIdTurno() + ";";
		r = statement.executeQuery(sql);
		while (r.next()) {
			aulasPorDia++;
			Time inicioHorarioT = r.getTime("inicioHorario");
			Time fimHorarioT = r.getTime("fimHorario");

			LocalTime inicioHorarioLT = inicioHorarioT.toLocalTime();
			LocalTime fimHorarioLT = fimHorarioT.toLocalTime();

			String inicioHorario = inicioHorarioLT.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
			String fimHorario = fimHorarioLT.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
			horarios.add(new Horario(inicioHorario, fimHorario));
		}

		sql = "SELECT * FROM classortbd.semana WHERE turnoId = " + turno.getIdTurno() + ";";
		r = statement.executeQuery(sql);
		if (r.next()) {

			boolean segunda = r.getBoolean("segunda");
			boolean terca = r.getBoolean("terca");
			boolean quarta = r.getBoolean("quarta");
			boolean quinta = r.getBoolean("quinta");
			boolean sexta = r.getBoolean("sexta");
			boolean sabado = r.getBoolean("sabado");
			boolean domingo = r.getBoolean("domingo");

			if (segunda)
				aulasPorSemana++;
			if (terca)
				aulasPorSemana++;
			if (quarta)
				aulasPorSemana++;
			if (quinta)
				aulasPorSemana++;
			if (sexta)
				aulasPorSemana++;
			if (sabado)
				aulasPorSemana++;
			if (domingo)
				aulasPorSemana++;
		}
		this.horarios = horarios;
		this.aulasPorDia = aulasPorDia;
		this.aulasPorSemana = aulasPorSemana;

	}

	private void reiniciarLayout() {
		btRemoverDisciplina.setVisible(false);
		btNovaDisciplina.setText("Nova disciplina");
		txtNomeDisciplina.setText("");
		txtProfessorDisciplina.setText("");
		gp.getElements().nextElement().setSelected(false);
		gp.clearSelection();
	}

	class cellRender extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			setHorizontalAlignment(JLabel.CENTER);

			if (row != 0 && column != 0) {
				rendererComponent.setForeground(Color.red);
				rendererComponent.setFont(new Font("Noto Sans Light", Font.PLAIN, 30));
			} else {
				rendererComponent.setForeground(Color.white);
				rendererComponent.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
			}
			return rendererComponent;
		}
	}
}