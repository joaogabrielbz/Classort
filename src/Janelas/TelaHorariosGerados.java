package janelas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import entidades.Coordenada;
import entidades.Disciplina;
import entidades.Horario;
import entidades.Realocacao;
import entidades.Semana;
import entidades.TabelaDia;
import entidades.TabelaDisciplina;
import entidades.TabelaTurma;
import entidades.Tabelas;
import entidades.Turma;
import entidades.TurmaDisciplina;
import entidades.Turno;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaHorariosGerados extends JDialog {

	private ArrayList<TurmaDisciplina> turmadisciplinas;;
	private ArrayList<Tabelas> listTabelas = new ArrayList<Tabelas>();
	private ArrayList<Realocacao> realocacoes = new ArrayList<Realocacao>();

	private ArrayList<TabelaDia> tabeladias;
	private Tabelas tabela;

	Random rand = new Random();

	private int aulasPorDia = 0;
	private int aulasPorSemana = 0;

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTabbedPane tpTurmaOuDisciplina;
	private JPanel panelTurmas;
	private JPanel panelDisciplina;
	private JPanel panelSemana;
	private JTabbedPane tpTurmas;
	private JTabbedPane tpDisciplinas;
	private JTabbedPane tpSemanas;

	private ArrayList<Turma> turmas;
	private ArrayList<Disciplina> disciplinas;
	private ArrayList<Horario> horarios;
	private TelaErros telaerro;

	private Semana semana;


	static {
		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
	}

	public TelaHorariosGerados(Statement statement, TelaInicial janela, ArrayList<Disciplina> disciplinas,
			ArrayList<Turma> turmas, ArrayList<Horario> horarios, Semana semana, Turno turno) throws SQLException {
		this.aulasPorDia = horarios.size();
		this.aulasPorSemana = semana.getQtdDias();
		this.turmas = turmas;
		this.disciplinas = disciplinas;
		this.horarios = horarios;
		this.semana = semana;

		setTitle("Classort");
		setIconImage(Toolkit.getDefaultToolkit().getImage(TelaHorariosGerados.class.getResource("/imgs/icon.png")));
		setBackground(new Color(30, 30, 30));

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1150, 650);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 30));
		contentPane.setForeground(new Color(30, 30, 30));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (telaerro != null) {
					telaerro.dispose();
				}
			}
		});

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btExportar = new JButton("Exportar planilhas");
		btExportar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new GerarPlanilhas(turno, tabela, tabeladias);
			}
		});
		btExportar.setForeground(Color.WHITE);
		btExportar.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btExportar.setBackground(new Color(45, 45, 45));
		btExportar.setBounds(926, 577, 200, 25);
		contentPane.add(btExportar);
		setModal(true);

		if (gerarTela(statement, disciplinas, turmas, horarios, turno)) {
			if (realocacoes.size() > 0) {
				telaerro = new TelaErros(this, realocacoes);
				telaerro.setVisible(true);
				telaerro.setAlwaysOnTop(true);
			}
		}
	}

	private boolean gerarTela(Statement statement, ArrayList<Disciplina> disciplinas, ArrayList<Turma> turmas,
			ArrayList<Horario> horarios, Turno turno) throws SQLException {

		contentPane.setLayout(new BorderLayout(0, 0));

		tpTurmaOuDisciplina = new JTabbedPane(JTabbedPane.TOP);
		tpTurmaOuDisciplina.setBorder(null);
		tpTurmaOuDisciplina.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tpTurmaOuDisciplina.setForeground(Color.WHITE);
		tpTurmaOuDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		tpTurmaOuDisciplina.setBackground(new Color(30, 30, 30));
		contentPane.add(tpTurmaOuDisciplina);

		panelTurmas = new JPanel();
		panelTurmas.setBorder(null);
		panelTurmas.setBackground(new Color(45, 45, 45));
		tpTurmaOuDisciplina.addTab("Horários por turma", null, panelTurmas, null);
		panelTurmas.setLayout(new BorderLayout(0, 0));

		tpTurmas = new JTabbedPane(JTabbedPane.LEFT);
		tpTurmas.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tpTurmas.setForeground(Color.WHITE);
		tpTurmas.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		tpTurmas.setBackground(new Color(30, 30, 30));
		panelTurmas.add(tpTurmas, BorderLayout.NORTH);

		panelDisciplina = new JPanel();
		panelDisciplina.setBorder(null);
		panelDisciplina.setBackground(new Color(45, 45, 45));
		tpTurmaOuDisciplina.addTab("Horários por professor", null, panelDisciplina, null);
		panelDisciplina.setLayout(new BorderLayout(0, 0));

		tpDisciplinas = new JTabbedPane(JTabbedPane.LEFT);
		tpDisciplinas.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tpDisciplinas.setForeground(Color.WHITE);
		tpDisciplinas.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		tpDisciplinas.setBackground(new Color(30, 30, 30));
		panelDisciplina.add(tpDisciplinas, BorderLayout.NORTH);

		panelSemana = new JPanel();
		panelSemana.setBorder(null);
		panelSemana.setBackground(new Color(45, 45, 45));
		tpTurmaOuDisciplina.addTab("Horários por dia", null, panelSemana, null);
		panelSemana.setLayout(new BorderLayout(0, 0));

		tpSemanas = new JTabbedPane(JTabbedPane.LEFT);
		tpSemanas.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tpSemanas.setForeground(Color.WHITE);
		tpSemanas.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		tpSemanas.setBackground(new Color(30, 30, 30));
		panelSemana.add(tpSemanas, BorderLayout.NORTH);

		tpTurmaOuDisciplina.setUI(new CustomTabbedPaneUI());
		tpTurmas.setUI(new CustomTabbedPaneUI());
		tpDisciplinas.setUI(new CustomTabbedPaneUI());
		tpSemanas.setUI(new CustomTabbedPaneUI());

		// Criando tabelas vazias por turmas //
		ArrayList<TabelaTurma> tabelaturmas = getTabelaTurmaVazia(turmas, horarios);

		// Criando tabelas vazias por disciplinas //
		ArrayList<TabelaDisciplina> tabeladisciplinas = gerarTabelaDisciplinaVazia(statement, disciplinas, horarios);

		// Coletando TurmaDisciplinas//
		String sql = "SELECT * FROM classortbd.turma_disciplina WHERE turnoId = " + turno.getIdTurno() + " ;";
		ResultSet r = statement.executeQuery(sql);

		turmadisciplinas = new ArrayList<TurmaDisciplina>();
		while (r.next()) {
			int idTurmaDisciplina = r.getInt("idTurmaDisciplina");
			int qtdAulas = r.getInt("qtdAulas");
			int TurmaId = r.getInt("TurmaId");
			int DisciplinaId = r.getInt("DisciplinaId");

			turmadisciplinas.add(new TurmaDisciplina(idTurmaDisciplina, qtdAulas, TurmaId, DisciplinaId));
		}

		if (turmadisciplinas.size() != 0) {
			Collections.sort(turmadisciplinas, new Comparator<TurmaDisciplina>() {
				@Override
				public int compare(TurmaDisciplina td1, TurmaDisciplina td2) {
					return Integer.compare(td2.getQtdAulas(), td1.getQtdAulas());
				}
			});

			// Gerando horarios e pegando o com menos realocacoes//
			int max = 15000;
			for (int i = 0; i < max; i++) {
				listTabelas.add(gerarTabelas(statement));
				if (listTabelas.get(listTabelas.size() - 1).realocacoes.size() == 0) {
					break;
				}
			}

			System.out.println(listTabelas.size());

			// Pegando as tabelas que tem menos realocaçoes //
			Collections.sort(listTabelas, Comparator.comparingInt(tabelas -> tabelas.realocacoes.size()));
			tabelaturmas = listTabelas.get(0).tabelaturmas;
			tabeladisciplinas = listTabelas.get(0).tabeladisciplinas;
			this.tabela = listTabelas.get(0);
			ArrayList<Realocacao> temp = listTabelas.get(0).realocacoes;

			// Gerando tabela por dia da semana //
			tabeladias = gerarTabelasDia(tabelaturmas);

			if (listTabelas.size() == max) {
				realocacoes = temp;
			}
			listTabelas.clear();

		}

		// Criando JTables e exibindo turmas //
		exibirTabelas(tabelaturmas, tabeladisciplinas, tabeladias);
		// TODO SALVAR PARA IMPRIMIR//
		return true;

	};

	private Tabelas gerarTabelas(Statement statement) {
		ArrayList<TabelaTurma> tabelaturmas = getTabelaTurmaVazia(turmas, horarios);
		ArrayList<TabelaDisciplina> tabeladisciplinas = gerarTabelaDisciplinaVazia(statement, disciplinas, horarios);
		ArrayList<Realocacao> realocacoes = new ArrayList<Realocacao>();

		for (TurmaDisciplina turmadisciplina : turmadisciplinas) {
			int quantidade = turmadisciplina.getQtdAulas();
			TabelaDisciplina td = null;
			TabelaTurma tt = null;

			// Coletando tabeladisciplina correta e seu index //
			for (TabelaDisciplina temp : tabeladisciplinas) {
				if (temp.getDisciplina().getIdDisciplina() == turmadisciplina.getDisciplinaId()) {
					td = temp;
					break;
				}
			}

			// Coletando tabelaturma correta e seu index //
			for (TabelaTurma temp : tabelaturmas) {
				if (temp.getTurma().getIdTurma() == turmadisciplina.getTurmaId()) {
					tt = temp;
					break;
				}
			}

			// Inserindo de acordo com a quantidade de aulas //
			for (int x = 0; x < quantidade; x++) {
				int i = 1;
				int j = 1;

				// para cada quantidade de aulas desta turmadisciplina //
				ArrayList<Coordenada> tentativas = new ArrayList<Coordenada>();
				while (true) {
					i = rand.nextInt(tt.getMatriz().length - 1) + 1;
					j = rand.nextInt(tt.getMatriz()[0].length - 1) + 1;

					boolean jaTentou = false;

					Coordenada temp = new Coordenada(i, j);
					for (Coordenada c : tentativas) {
						if (c.i == temp.i && c.j == temp.j) {
							jaTentou = true;
							break;
						}
					}

					if (!jaTentou) {
						tentativas.add(temp);
						if (tentativas.size() != aulasPorDia * aulasPorSemana) {
							if (tt.getMatriz()[i][j] == null) {
								if (td.getMatriz()[i][j] == null) {

									int vezesQueApareceuNaLinha = 0;
									for (int y = 0; y < tt.getMatriz().length; y++) {
										if (tt.getMatriz()[y][j] != null) {
											if (tt.getMatriz()[y][j].contains(td.getDisciplina().getNomeDisciplina())) {
												vezesQueApareceuNaLinha++;
											}
										}
									}
									if (vezesQueApareceuNaLinha < 2) {
										// caso PERMITA aulas duplas
										if (td.getDisciplina().isAulasDuplas()) {
											tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
											td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
											break;
										}
										// caso NAO PERMITA aulas duplas
										if (!td.getDisciplina().isAulasDuplas()) {
											// caso esteja na primeira linha
											if (i == 1) {
												if (tt.getMatriz()[i + 1][j] == null) {
													tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
													td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
													break;
												} else {
													if (!tt.getMatriz()[i + 1][j]
															.contains(td.getDisciplina().getNomeDisciplina())) {
														tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
														td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
														break;
													}
												}
											}
											// caso esteja na ultima linha
											else if (i == tt.getMatriz().length - 1) {
												if (tt.getMatriz()[i - 1][j] == null) {
													tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
													td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
													break;
												} else {
													if (!tt.getMatriz()[i - 1][j]
															.contains(td.getDisciplina().getNomeDisciplina())) {
														tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
														td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
														break;
													}
												}

											}
											// caso esteja em outro lugar
											else {
												// se os dois sao nulos
												if (tt.getMatriz()[i - 1][j] == null
														&& tt.getMatriz()[i + 1][j] == null) {
													tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
													td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
													break;
												}
												// se o de cima é nulo (confere o debaixo)
												else if (tt.getMatriz()[i - 1][j] == null) {
													if (!tt.getMatriz()[i + 1][j]
															.contains(td.getDisciplina().getNomeCompleto())) {
														tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
														td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
														break;
													}
												} else if (tt.getMatriz()[i + 1][j] == null) {
													if (!tt.getMatriz()[i - 1][j]
															.contains(td.getDisciplina().getNomeCompleto())) {
														tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
														td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
														break;
													}
												} else {
													if (!tt.getMatriz()[i + 1][j]
															.contains(td.getDisciplina().getNomeCompleto())) {
														if (!tt.getMatriz()[i - 1][j]
																.contains(td.getDisciplina().getNomeCompleto())) {
															tt.getMatriz()[i][j] = td.getDisciplina().getNomeCompleto();
															td.getMatriz()[i][j] = tt.getTurma().getNomeTurma();
															break;
														}

													}

												}
											}
										}
									}
								}
							}
						} else {
							realocacoes.add(new Realocacao(td, tt));
							break;
						}
					}
				}
			}
		}
		return new Tabelas(tabelaturmas, tabeladisciplinas, realocacoes);
	}

	private void exibirTabelas(ArrayList<TabelaTurma> tabelaturmas, ArrayList<TabelaDisciplina> tabeladisciplinas,
			ArrayList<TabelaDia> tabeladias) {
		for (TabelaTurma tt : tabelaturmas) {
			DefaultTableModel model = new DefaultTableModel(tt.getMatriz(), tt.getMatriz()[0]);
			JTable table = new JTable(model) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setForeground(new Color(255, 255, 255));
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setFont(new Font("Noto Sans Light", Font.PLAIN, 20));
			table.setBackground(new Color(45, 45, 45));
			table.getTableHeader().setUI(null);
			table.setRowHeight(80);
			DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
			centralizar.setHorizontalAlignment(JLabel.CENTER);
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(new MultiLineCellRenderer());
			}

			// TODO criar os listeners para as tables

			JPanel panel = new JPanel(new BorderLayout());
			panel.setBackground(new Color(30, 30, 30));
			panel.add(table, BorderLayout.CENTER);

			JScrollPane scrollPane = new JScrollPane(panel);

			String nomeTurma = tt.getTurma().getNomeTurma();
			tpTurmas.addTab(nomeTurma, scrollPane);
		}

		// Criando JTables e exibindo disciplinas //
		for (TabelaDisciplina td : tabeladisciplinas) {
			DefaultTableModel model = new DefaultTableModel(td.getMatriz(), td.getMatriz()[0]);
			JTable table = new JTable(model) {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setForeground(new Color(255, 255, 255));
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setFont(new Font("Noto Sans Light", Font.PLAIN, 20));
			table.setBackground(new Color(45, 45, 45));
			table.getTableHeader().setUI(null);
			table.setRowHeight(80);

			DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
			centralizar.setHorizontalAlignment(JLabel.CENTER);
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(centralizar);
			}

			// TODO criar os listeners para as tables

			JPanel panel = new JPanel(new BorderLayout());
			panel.setBackground(new Color(30, 30, 30));
			panel.add(table, BorderLayout.CENTER);

			JScrollPane scrollPane = new JScrollPane(panel);

			String nomeTurma = td.getDisciplina().getNomeCompleto();
			tpDisciplinas.addTab(nomeTurma, scrollPane);
		}

		if (tabeladias != null)
			for (TabelaDia tdia : tabeladias) {
				DefaultTableModel model = new DefaultTableModel(tdia.getMatriz(), tdia.getMatriz()[0]);
				JTable table = new JTable(model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}

				};
				table.setForeground(new Color(255, 255, 255));
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setFont(new Font("Noto Sans Light", Font.PLAIN, 20));
				table.setBackground(new Color(45, 45, 45));
				table.getTableHeader().setUI(null);
				table.setRowHeight(80);

				DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
				centralizar.setHorizontalAlignment(JLabel.CENTER);
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumnModel().getColumn(i).setCellRenderer(new MultiLineCellRenderer());

					if (i != 0) {
						table.getColumnModel().getColumn(i).setPreferredWidth(170);
					}
				}
				JPanel panel = new JPanel(new BorderLayout());
				panel.setBackground(new Color(30, 30, 30));
				panel.add(table, BorderLayout.CENTER);

				JScrollPane scrollPane = new JScrollPane(panel);
				scrollPane.addMouseWheelListener(new MouseWheelListener() {
					@Override
					public void mouseWheelMoved(MouseWheelEvent e) {
						// Rolar horizontalmente
						int unitsToScroll = 50 * e.getWheelRotation() * -1;
						JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
						horizontalScrollBar.setValue(horizontalScrollBar.getValue() - unitsToScroll);
					}
				});

				String nomeDia = tdia.getNomeDia();
				tpSemanas.addTab(nomeDia, scrollPane);
			}
	}

	private ArrayList<TabelaDisciplina> gerarTabelaDisciplinaVazia(Statement statement,
			ArrayList<Disciplina> disciplinas, ArrayList<Horario> horarios) {
		ArrayList<TabelaDisciplina> tabeladisciplinas = new ArrayList<TabelaDisciplina>();
		for (Disciplina d : disciplinas) {
			String[][] matriz = new String[aulasPorDia + 1][aulasPorSemana + 1];
			matriz[0][0] = d.getProfessorDisciplina();

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

			String sql = "SELECT * FROM classortbd.aula_proibida WHERE disciplinaId = " + d.getIdDisciplina() + ";";
			ResultSet r;
			try {

				r = statement.executeQuery(sql);
				while (r.next()) {
					;
					int horaIndex = r.getInt("horaIndex");
					int diaIndex = r.getInt("diaIndex");

					matriz[horaIndex][diaIndex] = "X";
				}

				d.setAulas(matriz);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			tabeladisciplinas.add(new TabelaDisciplina(d, matriz));
		}
		return tabeladisciplinas;
	}

	private ArrayList<TabelaTurma> getTabelaTurmaVazia(ArrayList<Turma> turmas, ArrayList<Horario> horarios) {
		ArrayList<TabelaTurma> tabelaturmas = new ArrayList<TabelaTurma>();
		for (Turma t : turmas) {
			String[][] matriz = new String[aulasPorDia + 1][aulasPorSemana + 1];
			matriz[0][0] = t.getNomeTurma();

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
			tabelaturmas.add(new TabelaTurma(t, matriz));
		}
		return tabelaturmas;
	}

	private ArrayList<TabelaDia> gerarTabelasDia(ArrayList<TabelaTurma> tabelaturmas) {
		ArrayList<TabelaDia> tabeladias = new ArrayList<TabelaDia>();
		String[] dias = { "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo" };
		String[] diasAbreviados = { "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom" };

		for (int i = 0; i < semana.getQtdDias(); i++) {
			String dia = dias[i];
			String matriz[][] = new String[horarios.size() + 1][tabelaturmas.size() + 1];

			matriz[0][0] = diasAbreviados[i];
			int x = 1;
			for (Horario h : horarios) {
				matriz[x][0] = h.getInicioHorario();
				x++;
			}

			x = 1;
			;
			if (tabelaturmas.size() != 0) {
				for (TabelaTurma tt : tabelaturmas) {

					String[] coluna = getColuna(tt.getMatriz(), i + 1);
					String[] colunaComNomeTurma = adicionarNoComeco(coluna, tt.getTurma().getNomeTurma());

					for (int k = 0; k < horarios.size() + 1; k++) {
						matriz[k][x] = colunaComNomeTurma[k];
					}
					x++;
				}
			}
			tabeladias.add(new TabelaDia(dia, matriz));

		}

		return tabeladias;
	}

	public static String[] adicionarNoComeco(String[] vetor, String item) {
		ArrayList<String> lista = new ArrayList<>(Arrays.asList(vetor));
		lista.add(0, item);
		return lista.toArray(new String[0]);
	}

	private String[] getColuna(String[][] matriz, int coluna) {
		String[] resultado = new String[matriz.length - 1];
		for (int i = 1; i < matriz.length; i++) {
			resultado[i - 1] = matriz[i][coluna];
		}
		return resultado;
	}

	class MultiLineCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public MultiLineCellRenderer() {
			setHorizontalAlignment(SwingConstants.CENTER);
			setVerticalAlignment(SwingConstants.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

			if (value != null) {
				String[] parts = value.toString().split(" - ");
				if (parts.length == 2) {
					label.setText("<html><div style='text-align: center;'><p style='font-size: 19px;'><b>" + parts[0]
							+ "</b><br><span style='font-size: 12px;'><i>" + parts[1] + "</i></span></p></div></html>");
				}
			}

			return label;
		}
	}

	private static class CustomTabbedPaneUI extends BasicTabbedPaneUI {
		@Override
		protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
				boolean isSelected) {
			if (isSelected) {
				g.setColor(new Color(136, 136, 136));
				g.fillRect(x, y, w, h);
			} else {
				super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
			}
		}
	}
}