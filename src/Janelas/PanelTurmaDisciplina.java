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
		lblVoltar.setBounds(10, 20, 60, 32);
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
		scrollPaneSelecionadas.setBounds(67, 93, 325, 246);

		JPanel panelSelecionadas = new JPanel();
		panelSelecionadas.setBackground(new Color(45, 45, 45));
		scrollPaneSelecionadas.setViewportView(panelSelecionadas);
		panelSelecionadas.setLayout(new BorderLayout(0, 0));

		tableDisciplinasSelecionadas = new JTable();
		tableDisciplinasSelecionadas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int indexClicado = tableDisciplinasSelecionadas.getSelectedRow();
				TurmaDisciplina turmaDisciplinaClicada = turmadisciplinas.get(indexClicado);

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
		scrollPaneNaoSelecionadas.setBounds(410, 93, 321, 246);

		listSelecionarDisciplinas = new JList<String>();
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
		lblTitulo.setBounds(67, 52, 321, 41);
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(new Color(136, 136, 136));
		lblTitulo.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		JLabel lblInstrucao = new JLabel("Clique para adicionar:");
		lblInstrucao.setBounds(410, 52, 321, 41);
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
						carregarDisciplinas(statement);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btAvancarTurma.setHorizontalAlignment(SwingConstants.RIGHT);
		btAvancarTurma.setBounds(639, 374, 137, 25);
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
						carregarDisciplinas(statement);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btVoltarTurma.setHorizontalAlignment(SwingConstants.LEFT);
		btVoltarTurma.setBounds(10, 374, 137, 25);
		btVoltarTurma.setForeground(Color.WHITE);
		btVoltarTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btVoltarTurma.setBackground(new Color(45, 45, 45));
		setLayout(null);

		btRemover = new JButton("Remover");
		btRemover.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
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
		});
		btRemover.setVisible(false);
		btRemover.setBounds(67, 340, 325, 25);
		btRemover.setForeground(Color.WHITE);
		btRemover.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btRemover.setBackground(new Color(172, 0, 9));
		add(btRemover);
		add(lblVoltar);
		add(lblTitulo);
		add(lblInstrucao);
		add(scrollPaneSelecionadas);
		add(scrollPaneNaoSelecionadas);
		add(btVoltarTurma);
		add(btAvancarTurma);

		carregarDisciplinas(statement);
	}

	private void carregarDisciplinas(Statement statement) throws SQLException {
		if (turmas.size() != 0) {

			// Configurando titulo //
			lblTitulo.setText("Disciplinas da turma " + turmas.get(indexTurma).getNomeTurma() + ":");

			// Configurando botões //
			if (indexTurma == 0) {
				btVoltarTurma.setVisible(false);
			} else {
				if (indexTurma == 1) {
					btVoltarTurma.setVisible(true);
				}
				btVoltarTurma.setText("< " + turmas.get(indexTurma - 1).getNomeTurma());
			}

			if (indexTurma == turmas.size() - 1) {
				btAvancarTurma.setVisible(false);
			} else {
				if (indexTurma == turmas.size() - 2) {
					btAvancarTurma.setVisible(true);
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
}
