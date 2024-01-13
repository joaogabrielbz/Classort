package janelas;

import javax.swing.JPanel;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import entidades.Disciplina;
import entidades.Turma;
import entidades.TurmaDisciplina;
import entidades.Turno;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import javax.swing.ListSelectionModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelTurmaDisciplina extends JPanel {

	private TelaInicial janela;
	private Turno turno;
	private ArrayList<Turma> turmas;
	private ArrayList<Disciplina> disciplinas;
	private ArrayList<Disciplina> disciplinasNaoSelecionadas;
	private ArrayList<TurmaDisciplina> turmadisciplinas = new ArrayList<TurmaDisciplina>();

	private int indexTurma = 0;
	private int idTurmaDisciplinaSelecionada = 0;

	private JTable tableDisciplinasSelecionadas;
	private JLabel lblTitulo;
	private JButton btAvancarTurma;
	private JButton btVoltarTurma;
	private JButton btRemover;
	private JList<String> listSelecionarDisciplinas;

	private static final long serialVersionUID = 1L;

	public PanelTurmaDisciplina(Statement statement, TelaInicial janela, Turno turno, ArrayList<Turma> turmas,
			ArrayList<Disciplina> disciplians) throws SQLException {
		this.janela = janela;
		this.turno = turno;
		this.turmas = turmas;
		this.disciplinas = disciplians;

		setBackground(new Color(30, 30, 30));
		setForeground(new Color(255, 255, 255));

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

		JScrollPane scrollPaneSelecionadas = new JScrollPane();

		JPanel panelSelecionadas = new JPanel();
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
			}
		});
		tableDisciplinasSelecionadas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int indexClicado = tableDisciplinasSelecionadas.getSelectedRow();
				TurmaDisciplina turmaDisciplinaClicada = turmadisciplinas.get(indexClicado);
				
				tableDisciplinasSelecionadas.editCellAt(indexClicado, 1);

				idTurmaDisciplinaSelecionada = turmaDisciplinaClicada.getIdTurmaDisciplina();

				String sql = "SELECT * FROM classortbd.disciplina WHERE idDisciplina = "
						+ turmaDisciplinaClicada.getDisciplinaId() + ";";
				try {
					ResultSet r = statement.executeQuery(sql);
					if (r.next()) {
						String nomeDisciplina = r.getString("nomedisciplina");
						String professorDisciplina = r.getString("professordisciplina");

						btRemover.setVisible(true);
						btRemover.setText("Remover: " + nomeDisciplina + " - " + professorDisciplina);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			
		});
		tableDisciplinasSelecionadas.setForeground(new Color(255, 255, 255));
		tableDisciplinasSelecionadas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDisciplinasSelecionadas.setRowHeight(35);
		tableDisciplinasSelecionadas.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		tableDisciplinasSelecionadas.setBackground(new Color(45, 45, 45));
		panelSelecionadas.add(tableDisciplinasSelecionadas, BorderLayout.CENTER);

		JScrollPane scrollPaneNaoSelecionadas = new JScrollPane();

		listSelecionarDisciplinas = new JList<String>();
		listSelecionarDisciplinas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btRemover.setVisible(false);
			}
		});
		listSelecionarDisciplinas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					if (listSelecionarDisciplinas.getSelectedIndex() != -1) {
						int indexClicado = listSelecionarDisciplinas.getSelectedIndex();
						int idDisciplinaClicada = disciplinasNaoSelecionadas.get(indexClicado).getIdDisciplina();

						String sql = "INSERT INTO classortbd.turma_disciplina (qtdAulas, turmaId, disciplinaId) VALUES "
								+ "( 1, " + turmas.get(indexTurma).getIdTurma() + ", " + idDisciplinaClicada + ");";
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
		listSelecionarDisciplinas.setToolTipText("");
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

		JLabel lblInstrucao = new JLabel("Clique para adicionar:");
		lblInstrucao.setHorizontalAlignment(SwingConstants.LEFT);
		lblInstrucao.setForeground(new Color(136, 136, 136));
		lblInstrucao.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		btAvancarTurma = new JButton("Avançar");
		btAvancarTurma.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
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
		});
		btAvancarTurma.setHorizontalAlignment(SwingConstants.RIGHT);
		btAvancarTurma.setForeground(Color.WHITE);
		btAvancarTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btAvancarTurma.setBackground(new Color(45, 45, 45));

		btVoltarTurma = new JButton("Retornar");
		btVoltarTurma.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
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
		});
		btVoltarTurma.setHorizontalAlignment(SwingConstants.LEFT);
		btVoltarTurma.setForeground(Color.WHITE);
		btVoltarTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btVoltarTurma.setBackground(new Color(45, 45, 45));

		btRemover = new JButton("Remover");
		btRemover.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				deleteTurmaDisciplina(statement);
			}
		});
		btRemover.setVisible(false);
		btRemover.setForeground(Color.WHITE);
		btRemover.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btRemover.setBackground(new Color(172, 0, 9));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(lblVoltar,
						GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup().addGap(67)
						.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE).addGap(22)
						.addComponent(lblInstrucao, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE).addGap(69))
				.addGroup(groupLayout.createSequentialGroup().addGap(67)
						.addComponent(scrollPaneSelecionadas, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE).addGap(18)
						.addComponent(scrollPaneNaoSelecionadas, GroupLayout.PREFERRED_SIZE, 321,
								GroupLayout.PREFERRED_SIZE)
						.addGap(69))
				.addGroup(groupLayout.createSequentialGroup().addGap(67)
						.addComponent(btRemover, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE).addGap(408))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup().addGap(10)
						.addComponent(btVoltarTurma, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE).addGap(492)
						.addComponent(btAvancarTurma, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
						.addGap(24)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(20)
						.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblInstrucao, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
						.addGroup(
								groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPaneSelecionadas, GroupLayout.DEFAULT_SIZE, 246,
												Short.MAX_VALUE)
										.addComponent(scrollPaneNaoSelecionadas, GroupLayout.PREFERRED_SIZE, 246,
												GroupLayout.PREFERRED_SIZE))
						.addGap(1).addComponent(btRemover).addGap(9)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btVoltarTurma)
								.addComponent(btAvancarTurma))
						.addGap(51)));
		setLayout(groupLayout);

		carregarDisciplinas(statement);
	}

	private void carregarDisciplinas(Statement statement) throws SQLException {
		if (turmas.size() != 0) {

			// Configurando titulo //
			lblTitulo.setText("Disciplinas da turma " + turmas.get(indexTurma).getNomeTurma() + ":");

			// Configurando botões //
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

			// Configurando table //
			turmadisciplinas = new ArrayList<TurmaDisciplina>();
			DefaultTableModel modelDisciplinasSelecionadas = new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int row, int column) {
					// Tornando todas as células não editáveis
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
				TurmaDisciplina novaTurmaDisciplina = new TurmaDisciplina(idTurmaDiscipina, qtdAulas, turmaId,
						disciplinaId);
				turmadisciplinas.add(novaTurmaDisciplina);

				String disciplinaEProfessor = r.getString("nomeDisciplina") + " - "
						+ r.getString("professorDisciplina");
				modelDisciplinasSelecionadas.addRow(new Object[] { disciplinaEProfessor, qtdAulas });
			}
			tableDisciplinasSelecionadas.setModel(modelDisciplinasSelecionadas);
			tableDisciplinasSelecionadas.getColumnModel().getColumn(0).setPreferredWidth(215);
			tableDisciplinasSelecionadas.getColumnModel().getColumn(1).setPreferredWidth(25);

			// Configurando List //
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
					modelDisciplinasNaoSelecionadas.addElement(disciplinas.get(i).getNomeDisciplina() + " - "
							+ disciplinas.get(i).getProfessorDisciplina());
				}
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
				btRemover.setVisible(false);
				carregarDisciplinas(statement);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void salvarQtdAulas(Statement statement) {
		ArrayList<Object> qtdAulasNovos = new ArrayList<Object>();	

		for (int i = 0; i < tableDisciplinasSelecionadas.getRowCount(); i++) {
			Object qtd = tableDisciplinasSelecionadas.getModel().getValueAt(i, 1);
			qtdAulasNovos.add(qtd);
			System.out.println(qtdAulasNovos.get(i));
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
}
