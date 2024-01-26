package janelas;

//joaogabrielbz//

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.GroupLayout.Alignment;
import entidades.Disciplina;
import entidades.Horario;
import entidades.Semana;
import entidades.Turma;
import entidades.TurmaDisciplina;
import entidades.Turno;
import javax.swing.border.MatteBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class PanelTurmaDisciplina extends JPanel {

	@SuppressWarnings("unused")
	private TelaInicial janela;
	private Turno turno;
	private PanelTurmaDisciplina panelturmadisciplina = this;

	private ArrayList<Turma> turmas;
	private ArrayList<Disciplina> disciplinas;
	private ArrayList<Disciplina> disciplinasNaoSelecionadas;
	private ArrayList<TurmaDisciplina> turmadisciplinas = new ArrayList<TurmaDisciplina>();
	private TelaRevisao telaRevisao;

	private int indexTurma = 0;
	private int idTurmaDisciplinaSelecionada = 0;

	private int maxAulas = 0;
	private int aulasPorDia = 0;

	public boolean telaErroAberta = false;

	private JTable tableDisciplinasSelecionadas;
	private JLabel lblTitulo;
	private JButton btAvancarTurma;
	private JButton btVoltarTurma;
	private JLabel lblVoltar;
	private JScrollPane scrollPaneSelecionadas;
	private JPanel panelSelecionadas;
	private JScrollPane scrollPaneNaoSelecionadas;
	public JButton btGerarHorario = new JButton();

	private JList<String> listSelecionarDisciplinas;

	private ArrayList<Horario> horarios = new ArrayList<Horario>();
	private Semana semana;

	private static final long serialVersionUID = 1L;
	private JTextField txtPesquisa;

	public PanelTurmaDisciplina(Statement statement, TelaInicial janela, Turno turno, ArrayList<Turma> turmas,
			ArrayList<Disciplina> disciplinas) throws SQLException {

		this.janela = janela;
		this.turno = turno;
		this.turmas = turmas;
		this.disciplinas = disciplinas;
		this.maxAulas = calcularMaxAulas(statement);
		this.aulasPorDia = getAulasPorDia(statement);

		setBackground(new Color(30, 30, 30));
		setForeground(new Color(255, 255, 255));

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
				salvarQtdAulas(statement);
				janela.setContentPane(janela.paneldisciplina);
				janela.revalidate();
				janela.repaint();
			}
		});
		lblVoltar.setHorizontalAlignment(SwingConstants.LEFT);
		lblVoltar.setForeground(new Color(136, 136, 136));
		lblVoltar.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		lblVoltar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(30, 30, 30)));

		scrollPaneSelecionadas = new JScrollPane();
		scrollPaneSelecionadas.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int unitsToScroll = 50 * e.getWheelRotation() * -1;
				JScrollBar verticalScrollBar = scrollPaneSelecionadas.getVerticalScrollBar();
				verticalScrollBar.setValue(verticalScrollBar.getValue() - unitsToScroll);
			}
		});

		panelSelecionadas = new JPanel();

		panelSelecionadas.setBackground(new Color(45, 45, 45));
		scrollPaneSelecionadas.setViewportView(panelSelecionadas);
		panelSelecionadas.setLayout(new BorderLayout(0, 0));

		tableDisciplinasSelecionadas = new JTable();
		tableDisciplinasSelecionadas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteTurmaDisciplina(statement);
				}
				if (e.getKeyCode() == KeyEvent.VK_E) {
					tableDisciplinasSelecionadas.editCellAt(tableDisciplinasSelecionadas.getSelectedRow(), 1);
				}
			}
		});

		tableDisciplinasSelecionadas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int indexClicado = tableDisciplinasSelecionadas.getSelectedRow();
				if (indexClicado != -1) {
					TurmaDisciplina turmaDisciplinaClicada = turmadisciplinas.get(indexClicado);
					tableDisciplinasSelecionadas.editCellAt(indexClicado, 1);

					idTurmaDisciplinaSelecionada = turmaDisciplinaClicada.getIdTurmaDisciplina();

					btGerarHorario.setText("Remover");
					btGerarHorario.setBackground(new Color(172, 0, 9));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				salvarQtdAulas(statement);
			}
		});
		tableDisciplinasSelecionadas.setForeground(new Color(255, 255, 255));
		tableDisciplinasSelecionadas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDisciplinasSelecionadas.setRowHeight(35);
		tableDisciplinasSelecionadas.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		tableDisciplinasSelecionadas.setBackground(new Color(45, 45, 45));
		panelSelecionadas.add(tableDisciplinasSelecionadas, BorderLayout.CENTER);

		scrollPaneNaoSelecionadas = new JScrollPane();

		listSelecionarDisciplinas = new JList<String>();
		listSelecionarDisciplinas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (listSelecionarDisciplinas.getSelectedIndex() != -1) {
						int indexClicado = listSelecionarDisciplinas.getSelectedIndex();
						int idDisciplinaClicada = disciplinasNaoSelecionadas.get(indexClicado).getIdDisciplina();

						String sql = "INSERT INTO classortbd.turma_disciplina (qtdAulas, turmaId, disciplinaId, turnoId) VALUES "
								+ "( 1, " + turmas.get(indexTurma).getIdTurma() + ", " + idDisciplinaClicada + ", "
								+ turno.getIdTurno() + ");";
						try {
							statement.execute(sql);
							carregarDisciplinas(statement);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		listSelecionarDisciplinas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btGerarHorario.setText("Gerar horarios");
				btGerarHorario.setBackground(new Color(60, 60, 60));
			}
		});
		listSelecionarDisciplinas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					if (listSelecionarDisciplinas.getSelectedIndex() != -1) {
						int indexClicado = listSelecionarDisciplinas.getSelectedIndex();
						int idDisciplinaClicada = disciplinasNaoSelecionadas.get(indexClicado).getIdDisciplina();

						String sql = "INSERT INTO classortbd.turma_disciplina (qtdAulas, turmaId, disciplinaId, turnoId) VALUES "
								+ "( 1, " + turmas.get(indexTurma).getIdTurma() + ", " + idDisciplinaClicada + ", "
								+ turno.getIdTurno() + ");";
						try {
							statement.execute(sql);
							carregarDisciplinas(statement);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		listSelecionarDisciplinas.setVisibleRowCount(30);
		listSelecionarDisciplinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSelecionarDisciplinas.setForeground(Color.WHITE);
		listSelecionarDisciplinas.setFont(new Font("Noto Sans Light", Font.BOLD, 20));
		listSelecionarDisciplinas.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listSelecionarDisciplinas.setBackground(new Color(45, 45, 45));
		scrollPaneNaoSelecionadas.setViewportView(listSelecionarDisciplinas);

		lblTitulo = new JLabel("Disciplinas da turma:");
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(new Color(136, 136, 136));
		lblTitulo.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		JLabel lblInstrucao = new JLabel("Adicionar:");
		lblInstrucao.setHorizontalAlignment(SwingConstants.LEFT);
		lblInstrucao.setForeground(new Color(136, 136, 136));
		lblInstrucao.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		btAvancarTurma = new JButton("Avançar");
		btAvancarTurma.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				avancarTurma(statement);
			}
		});
		btAvancarTurma.setHorizontalAlignment(SwingConstants.RIGHT);
		btAvancarTurma.setForeground(Color.WHITE);
		btAvancarTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btAvancarTurma.setBackground(new Color(45, 45, 45));

		btVoltarTurma = new JButton("Retornar");
		btVoltarTurma.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				voltarTurma(statement);
			}
		});
		btVoltarTurma.setHorizontalAlignment(SwingConstants.LEFT);
		btVoltarTurma.setForeground(Color.WHITE);
		btVoltarTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btVoltarTurma.setBackground(new Color(45, 45, 45));

		btGerarHorario = new JButton("Gerar horarios");
		btGerarHorario.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (btGerarHorario.getText().equals("Remover")) {
					deleteTurmaDisciplina(statement);
				} else {

					salvarQtdAulas(statement);
					for (Disciplina d : disciplinas) {
						String sql = "SELECT * FROM classortbd.turma_disciplina " + "WHERE disciplinaId = "
								+ d.getIdDisciplina() + ";";
						try {
							ResultSet r = statement.executeQuery(sql);
							int contadorDeAulasTotais = 0;
							while (r.next()) {
								contadorDeAulasTotais = contadorDeAulasTotais + r.getInt("qtdAulas");
							}
							d.setAulasTotais(contadorDeAulasTotais);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}

					for (Turma t : turmas) {
						String sql = "SELECT * FROM classortbd.turma_disciplina " + "WHERE turmaId = " + t.getIdTurma()
								+ ";";
						try {
							ResultSet r = statement.executeQuery(sql);
							int contadorDeAulasTotais = 0;
							while (r.next()) {
								contadorDeAulasTotais = contadorDeAulasTotais + r.getInt("qtdAulas");
							}
							t.setAulasTotais(contadorDeAulasTotais);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}

					telaRevisao = new TelaRevisao(statement, janela, panelturmadisciplina, turmas, disciplinas,
							maxAulas, horarios, semana, turno);
					telaRevisao.setResizable(false);
					telaRevisao.setLocationRelativeTo(janela);
					telaRevisao.setVisible(true);
				}
			}
		});
		btGerarHorario.setForeground(Color.WHITE);
		btGerarHorario.setFont(new Font("Noto Sans Light", Font.PLAIN, 15));
		btGerarHorario.setBackground(new Color(60, 60, 60));

		txtPesquisa = new JTextField();
		txtPesquisa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String pesquisa = txtPesquisa.getText();
				char keyChar = e.getKeyChar();
				if (Character.isLetter(keyChar) || Character.isDigit(keyChar)) {
					pesquisa = pesquisa + keyChar;
				}

				if (pesquisa.isEmpty()) {
					try {
						carregarDisciplinas(statement);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} else {
					try {
						carregarDisciplinas(statement);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					ArrayList<Disciplina> temp = new ArrayList<Disciplina>();

					for (int i = 0; i < disciplinasNaoSelecionadas.size(); i++) {
						if (disciplinasNaoSelecionadas.get(i).getNomeCompleto().toLowerCase()
								.contains(pesquisa.toLowerCase())) {
							temp.add(disciplinasNaoSelecionadas.get(i));
						}
					}
					disciplinasNaoSelecionadas = temp;

					DefaultListModel<String> listModel = new DefaultListModel<String>();
					for (Disciplina d : disciplinasNaoSelecionadas) {
						listModel.addElement(d.getNomeCompleto());
					}
					listSelecionarDisciplinas.setModel(listModel);
				}
			}

		});
		txtPesquisa.setForeground(Color.WHITE);
		txtPesquisa.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		txtPesquisa.setColumns(10);
		txtPesquisa.setBackground(new Color(45, 45, 45));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(lblVoltar,
						GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup().addGap(67)
						.addComponent(lblTitulo, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE).addGap(22)
						.addComponent(lblInstrucao, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE).addGap(69))
				.addGroup(groupLayout.createSequentialGroup().addGap(67)
						.addComponent(scrollPaneSelecionadas, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE).addGap(18)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(txtPesquisa, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE).addComponent(
										scrollPaneNaoSelecionadas, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
						.addGap(69))
				.addGroup(groupLayout.createSequentialGroup().addGap(67)
						.addComponent(btVoltarTurma, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
						.addGap(120).addComponent(btGerarHorario, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
						.addGap(120)
						.addComponent(btAvancarTurma, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
						.addGap(69)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(20)
						.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblInstrucao, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPaneSelecionadas, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
								.addGroup(Alignment.TRAILING,
										groupLayout.createSequentialGroup()
												.addComponent(txtPesquisa, GroupLayout.PREFERRED_SIZE, 25,
														GroupLayout.PREFERRED_SIZE)
												.addGap(2).addComponent(scrollPaneNaoSelecionadas,
														GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)))
						.addGap(35)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addGap(1).addComponent(btVoltarTurma))
								.addComponent(btGerarHorario, GroupLayout.PREFERRED_SIZE, 25,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup().addGap(1).addComponent(btAvancarTurma)))
						.addGap(50)));
		setLayout(groupLayout);

		carregarDisciplinas(statement);
	}

	private int getAulasPorDia(Statement statement) throws SQLException {
		int cont = 0;
		String sql = "";
		ResultSet r = null;

		sql = "SELECT * FROM classortbd.horarios WHERE turnoId = " + turno.getIdTurno() + ";";
		r = statement.executeQuery(sql);
		while (r.next()) {
			cont++;
			Time inicioHorarioT = r.getTime("inicioHorario");
			Time fimHorarioT = r.getTime("fimHorario");

			LocalTime inicioHorarioLT = inicioHorarioT.toLocalTime();
			LocalTime fimHorarioLT = fimHorarioT.toLocalTime();

			String inicioHorario = inicioHorarioLT.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
			String fimHorario = fimHorarioLT.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

			horarios.add(new Horario(inicioHorario, fimHorario));
		}
		return cont;
	}

	protected int calcularMaxAulas(Statement statement) throws SQLException {
		int aulasPorDia = 0;
		int aulasPorSemana = 0;
		String sql = "";
		ResultSet r = null;

		sql = "SELECT * FROM classortbd.horarios WHERE turnoId = " + turno.getIdTurno() + ";";
		r = statement.executeQuery(sql);
		while (r.next()) {
			aulasPorDia++;
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

			semana = new Semana(segunda, terca, quarta, quinta, sexta, sabado, domingo);

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
		return aulasPorSemana * aulasPorDia;
	}

	private void carregarDisciplinas(Statement statement) throws SQLException {
		if (turmas.size() != 0) {

			btGerarHorario.setText("Gerar horarios");
			btGerarHorario.setBackground(new Color(60, 60, 60));

			lblTitulo.setText("Aulas por semana da turma " + turmas.get(indexTurma).getNomeTurma() + ":");

			if (indexTurma == 0) {
				btVoltarTurma.setEnabled(false);
				btVoltarTurma.setText("<");
			} else {
				if (indexTurma == 1) {
					btVoltarTurma.setEnabled(true);
				}
				btVoltarTurma.setText("< " + turmas.get(indexTurma - 1).getNomeTurma());
			}

			if (indexTurma == turmas.size() - 1) {
				btAvancarTurma.setEnabled(false);
				btAvancarTurma.setText(">");
			} else {
				if (indexTurma == turmas.size() - 2) {
					btAvancarTurma.setEnabled(true);
				}
				btAvancarTurma.setText(turmas.get(indexTurma + 1).getNomeTurma() + " >");
			}

			turmadisciplinas = new ArrayList<TurmaDisciplina>();
			DefaultTableModel modelDisciplinasSelecionadas = new DefaultTableModel() {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return column == 1;
				}
			};
			modelDisciplinasSelecionadas.addColumn("Disciplinas");
			modelDisciplinasSelecionadas.addColumn("Aulas");

			String sql = "SELECT turma_disciplina.*, disciplina.* " + "FROM classortbd.turma_disciplina "
					+ "JOIN classortbd.disciplina ON disciplina.idDisciplina = turma_disciplina.disciplinaId "
					+ "WHERE turmaId = " + turmas.get(indexTurma).getIdTurma() + ";";
			ResultSet r = statement.executeQuery(sql);
			while (r.next()) {
				int idTurmaDiscipina = r.getInt("idTurmaDisciplina");
				int qtdAulas = r.getInt("qtdAulas");
				int turmaId = r.getInt("turmaId");
				int disciplinaId = r.getInt("disciplinaId");
				String nomeDisciplina = r.getString("nomeDisciplina");
				String professorDisciplina = r.getString("professorDisciplina");

				TurmaDisciplina novaTurmaDisciplina = new TurmaDisciplina(idTurmaDiscipina, qtdAulas, turmaId,
						disciplinaId);
				novaTurmaDisciplina.setNomeCompletoDisciplina(nomeDisciplina + " - " + professorDisciplina);

				turmadisciplinas.add(novaTurmaDisciplina);
			}
			Collections.sort(turmadisciplinas, Comparator.comparing(TurmaDisciplina::getNomeCompletoDisciplina));

			for (TurmaDisciplina td : turmadisciplinas) {
				modelDisciplinasSelecionadas.addRow(new Object[] { td.getNomeCompletoDisciplina(), td.getQtdAulas() });
			}

			tableDisciplinasSelecionadas.setModel(modelDisciplinasSelecionadas);
			tableDisciplinasSelecionadas.getColumnModel().getColumn(0).setPreferredWidth(215);
			tableDisciplinasSelecionadas.getColumnModel().getColumn(1).setPreferredWidth(25);
			tableDisciplinasSelecionadas.getColumnModel().getColumn(1)
					.setCellEditor(new NumerosCellEditor(aulasPorDia));

			disciplinasNaoSelecionadas = new ArrayList<Disciplina>();
			DefaultListModel<String> modelDisciplinasNaoSelecionadas = new DefaultListModel<String>();

			for (int i = 0; i < disciplinas.size(); i++) {
				int idDisciplina = disciplinas.get(i).getIdDisciplina();
				boolean estaSelecionada = false;

				for (int j = 0; j < turmadisciplinas.size(); j++) {
					if (idDisciplina == turmadisciplinas.get(j).getDisciplinaId()) {
						estaSelecionada = true;
					}
				}
				if (!estaSelecionada) {
					disciplinasNaoSelecionadas.add(disciplinas.get(i));
				}
			}

			Collections.sort(disciplinasNaoSelecionadas, Comparator.comparing(Disciplina::getNomeDisciplina));

			for (Disciplina d : disciplinasNaoSelecionadas) {
				modelDisciplinasNaoSelecionadas.addElement(d.getNomeCompleto());
			}
			listSelecionarDisciplinas.setModel(modelDisciplinasNaoSelecionadas);
		}
	}

	private void deleteTurmaDisciplina(Statement statement) {
		if (idTurmaDisciplinaSelecionada > 0) {
			String sql = "DELETE FROM classortbd.turma_disciplina WHERE idTurmaDisciplina = "
					+ idTurmaDisciplinaSelecionada + ";";
			try {
				statement.execute(sql);
				carregarDisciplinas(statement);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			btGerarHorario.setText("Gerar horarios");
			btGerarHorario.setBackground(new Color(60, 60, 60));
		}
	}

	private void salvarQtdAulas(Statement statement) {
		ArrayList<Object> qtdAulasNovos = new ArrayList<Object>();

		for (int i = 0; i < tableDisciplinasSelecionadas.getRowCount(); i++) {
			Object qtd = tableDisciplinasSelecionadas.getModel().getValueAt(i, 1);
			qtdAulasNovos.add(qtd);
		}
		for (int i = 0; i < turmadisciplinas.size(); i++) {
			String sql = "UPDATE classortbd.turma_disciplina SET qtdaulas=" + qtdAulasNovos.get(i)
					+ " WHERE idTurmaDisciplina= " + turmadisciplinas.get(i).getIdTurmaDisciplina() + ";";
			try {
				statement.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void voltarTurma(Statement statement) {
		if (indexTurma != 0) {
			indexTurma--;
			try {
				salvarQtdAulas(statement);
				carregarDisciplinas(statement);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void avancarTurma(Statement statement) {
		if (indexTurma != turmas.size() - 1) {
			indexTurma++;
			try {
				salvarQtdAulas(statement);
				carregarDisciplinas(statement);

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}

class NumerosCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
	private JTextField textField;

	public NumerosCellEditor(int aulasPorDia) {
		super(new JTextField());

		textField = (JTextField) getComponent();
		textField.setDocument(new NumericDocument(aulasPorDia));
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value == null || value.toString().trim().isEmpty()) {
			value = 1;
		}
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
}

@SuppressWarnings("serial")
class NumericDocument extends PlainDocument {
	private int maxValue;

	public NumericDocument(int maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (str == null) {
			return;
		}

		char[] characters = str.toCharArray();
		StringBuilder sb = new StringBuilder();

		for (char character : characters) {
			if (Character.isDigit(character)) {
				sb.append(character);
			}
		}

		String newValue = getText(0, getLength()) + sb.toString();
		if (!newValue.isEmpty()) {
			int value = Integer.parseInt(newValue);

			if (value <= maxValue) {
				super.insertString(offs, sb.toString(), a);
			}
		}
	}
}