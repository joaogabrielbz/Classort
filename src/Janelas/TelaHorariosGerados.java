package janelas;

import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entidades.Disciplina;
import entidades.Horario;
import entidades.Semana;
import entidades.TabelaTurma;
import entidades.Turma;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JLayeredPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.ScrollPane;

public class TelaHorariosGerados extends JDialog {

	private ArrayList<TabelaTurma> tabelaturmas;

	private int aulasPorDia = 0;
	private int aulasPorSemana = 0;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public TelaHorariosGerados(Statement statement, ArrayList<Disciplina> disciplinas, ArrayList<Turma> turmas,
			ArrayList<Horario> horarios, Semana semana) {
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

		JTabbedPane tpTurmas = new JTabbedPane(JTabbedPane.LEFT);
		tpTurmas.setForeground(new Color(255, 255, 255));
		tpTurmas.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		tpTurmas.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tpTurmas.setBackground(new Color(30, 30, 30));
		contentPane.add(tpTurmas, BorderLayout.CENTER);

		String[] dias = { " ", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb",
				"Dom" };
		tabelaturmas = new ArrayList<TabelaTurma>();

		// ignorar sempre o 0 //
		// CRIANDO MATRIZES DEFAULT//
		for (Turma t : turmas) {
			String[][] matriz = new String[aulasPorDia + 1][aulasPorSemana + 1];
			matriz[0][0] = t.getNomeTurma();

			int i = 1;

			for (Horario h : horarios) {
				matriz[i][0] = h.getInicioHorario();
				i++;
			}

			int j = 1;

			while (j != aulasPorSemana + 1) {
				matriz[0][j] = dias[j];
				j++;
			}

			tabelaturmas.add(new TabelaTurma(t, matriz));
		}

		String sql = "SELECT * FROM classortbd.turma_disciplina";
		// TODO ResultSet r = statement.executeQuery(sql);

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
	}
}