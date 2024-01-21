package janelas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import entidades.Disciplina;
import entidades.Horario;
import entidades.Semana;
import entidades.TabelaDisciplina;
import entidades.TabelaTurma;
import entidades.Turma;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;


public class TelaHorariosGerados extends JDialog {

	private ArrayList<TabelaTurma> tabelaturmas;
	private ArrayList<TabelaDisciplina> tabeladisciplinas;

	private int aulasPorDia = 0;
	private int aulasPorSemana = 0;

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTabbedPane tpTurmaOuDisciplina;
	private JPanel panelTurmas;
	private JPanel panelDisciplina;
	private JTabbedPane tpTurmas;
	private JTabbedPane tpDisciplinas;
	
	static {
		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
	}

	public TelaHorariosGerados(Statement statement, ArrayList<Disciplina> disciplinas, ArrayList<Turma> turmas,
			ArrayList<Horario> horarios, Semana semana) throws SQLException {
		this.aulasPorDia = horarios.size();
		this.aulasPorSemana = semana.getQtdDias();

		setTitle("Classort");
		setIconImage(Toolkit.getDefaultToolkit().getImage(TelaHorariosGerados.class.getResource("/imgs/icon.png")));
		setBackground(new Color(30, 30, 30));
		setModal(true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 510);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 30));
		contentPane.setForeground(new Color(30, 30, 30));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
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
		tpTurmaOuDisciplina.addTab("Horários por disciplina", null, panelDisciplina, null);
		panelDisciplina.setLayout(new BorderLayout(0, 0));

		tpDisciplinas = new JTabbedPane(JTabbedPane.LEFT);
		tpDisciplinas.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tpDisciplinas.setForeground(Color.WHITE);
		tpDisciplinas.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		tpDisciplinas.setBackground(new Color(30, 30, 30));
		panelDisciplina.add(tpDisciplinas, BorderLayout.NORTH);

		tpTurmaOuDisciplina.setUI(new CustomTabbedPaneUI());
		tpTurmas.setUI(new CustomTabbedPaneUI());
		tpDisciplinas.setUI(new CustomTabbedPaneUI());

		// Criando tabelas vazias por turmas //
		tabelaturmas = new ArrayList<TabelaTurma>();
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

		// Criando tabelas vazias por disciplinas //
		tabeladisciplinas = new ArrayList<TabelaDisciplina>();
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
			tabeladisciplinas.add(new TabelaDisciplina(d, matriz));
		}

		// Sorteando horarios //
		String sql = "SELECT * FROM classortbd.turma_disciplina";
		ResultSet r = statement.executeQuery(sql);
		
		
		

		// Criando JTables e exibindo turmas //
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
			table.setRowHeight(60);
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
			table.setRowHeight(60);
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