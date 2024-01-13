package janelas;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;

import entidades.Disciplina;
import entidades.Turma;
import entidades.TurmaDisciplina;
import entidades.Turno;

import javax.swing.border.MatteBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;

import java.awt.BorderLayout;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import java.awt.*;
import java.awt.event.ActionListener;

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
	private JList<String> listSelecionarDisciplinas;

	private static final long serialVersionUID = 1L;
	private JButton btnGerarHorarios;

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
				if (e.getKeyCode() == KeyEvent.VK_E) {
					tableDisciplinasSelecionadas.editCellAt(tableDisciplinasSelecionadas.getSelectedRow(), 1);
				}
			}
		});
		tableDisciplinasSelecionadas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int indexClicado = tableDisciplinasSelecionadas.getSelectedRow();
				if(indexClicado != -1) {
					TurmaDisciplina turmaDisciplinaClicada = turmadisciplinas.get(indexClicado);

					tableDisciplinasSelecionadas.editCellAt(indexClicado, 1);

					idTurmaDisciplinaSelecionada = turmaDisciplinaClicada.getIdTurmaDisciplina();		
					
					btnGerarHorarios.setText("Remover");
					btnGerarHorarios.setBackground(new Color(172,0,9));
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
				btnGerarHorarios.setText("Gerar horarios");
				btnGerarHorarios.setBackground(new Color(60,60,60));
				
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
		
		btnGerarHorarios = new JButton("Gerar horarios");
		btnGerarHorarios.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(btnGerarHorarios.getText().equals("Remover")) {
					deleteTurmaDisciplina(statement);
				}
				else {
					//TODO continuar codigo
				}
			}
		});
		btnGerarHorarios.setForeground(Color.WHITE);
		btnGerarHorarios.setFont(new Font("Noto Sans Light", Font.PLAIN, 15));
		btnGerarHorarios.setBackground(new Color(60, 60, 60));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addGap(0))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(67)
							.addComponent(lblTitulo, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
							.addGap(22)
							.addComponent(lblInstrucao, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(67)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(scrollPaneSelecionadas, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
									.addGap(18)
									.addComponent(scrollPaneNaoSelecionadas, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btVoltarTurma, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
									.addGap(120)
									.addComponent(btnGerarHorarios, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
									.addGap(120)
									.addComponent(btAvancarTurma, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)))))
					.addGap(69))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblInstrucao, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneSelecionadas, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
						.addComponent(scrollPaneNaoSelecionadas, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btVoltarTurma)
						.addComponent(btAvancarTurma)
						.addComponent(btnGerarHorarios, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addGap(50))
		);
		setLayout(groupLayout);

		

		carregarDisciplinas(statement);
	}

	private void carregarDisciplinas(Statement statement) throws SQLException {
		if (turmas.size() != 0) {
			
			btnGerarHorarios.setText("Gerar horarios");
			btnGerarHorarios.setBackground(new Color(60,60,60));

			// Configurando titulo //
			lblTitulo.setText("Aulas por semana da turma " + turmas.get(indexTurma).getNomeTurma() + ":");

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
			tableDisciplinasSelecionadas.getColumnModel().getColumn(1).setCellEditor(new NumerosCellEditor());

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
				carregarDisciplinas(statement);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			btnGerarHorarios.setText("Gerar horarios");
			btnGerarHorarios.setBackground(new Color(60,60,60));
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
	private JTextField textField;

	public NumerosCellEditor() {
		super(new JTextField());

		textField = (JTextField) getComponent();
		textField.setDocument(new NumericDocument(5)); // Defina o valor máximo desejado
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// Se a célula estiver vazia, preencha com o número 1
		if (value == null || value.toString().trim().isEmpty()) {
			value = 1;
		}
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
}

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
