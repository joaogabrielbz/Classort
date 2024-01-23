package janelas;

//joaogabrielbz//

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import entidades.Disciplina;
import entidades.Horario;
import entidades.Semana;
import entidades.Turma;
import entidades.Turno;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Statement;

public class TelaRevisao extends JDialog {

	private JPanel contentPane;
	private JTable tableTurmas;
	private JLabel AulasPorDisciplinas;
	private JScrollPane scrollDisciplinas;
	private JPanel panelDisciplinas;
	private JTable tableDisciplinas;
	private JButton btAvancar;
	private JLabel lblAulasPorTurmas;
	private JScrollPane scrollTurmas;
	private JPanel panelTurmas;
	private JLabel lblDica;

	private int maxAulasTurmas = 0;
	private int maxAulasDisciplinas = 0;

	private TelaRevisao telaRevisao = this;

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private ArrayList<Horario> horarios;
	@SuppressWarnings("unused")
	private Semana semana;

	public TelaRevisao(Statement statement, TelaInicial janela, PanelTurmaDisciplina panelturmadisciplina,
			ArrayList<Turma> turmas, ArrayList<Disciplina> disciplinas, int maxAulas, ArrayList<Horario> horarios,
			Semana semana, Turno turno) {

		this.horarios = horarios;
		this.semana = semana;
		this.maxAulasTurmas = maxAulas;
		this.maxAulasDisciplinas = maxAulas ;

		setModal(true);

		setTitle("Classort");
		setIconImage(Toolkit.getDefaultToolkit().getImage(TelaRevisao.class.getResource("/imgs/icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 710, 410);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 30));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		AulasPorDisciplinas = new JLabel("Aulas por disciplinas:");
		AulasPorDisciplinas.setHorizontalAlignment(SwingConstants.LEFT);
		AulasPorDisciplinas.setForeground(new Color(136, 136, 136));
		AulasPorDisciplinas.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		AulasPorDisciplinas.setBounds(10, 32, 334, 41);
		contentPane.add(AulasPorDisciplinas);

		scrollDisciplinas = new JScrollPane();
		scrollDisciplinas.setBounds(10, 66, 334, 246);
		contentPane.add(scrollDisciplinas);

		panelDisciplinas = new JPanel();
		panelDisciplinas.setBackground(new Color(45, 45, 45));
		scrollDisciplinas.setViewportView(panelDisciplinas);
		panelDisciplinas.setLayout(new BorderLayout(0, 0));

		tableDisciplinas = new JTable();
		tableDisciplinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDisciplinas.setRowHeight(35);
		tableDisciplinas.setForeground(Color.WHITE);
		tableDisciplinas.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		tableDisciplinas.setBackground(new Color(45, 45, 45));
		panelDisciplinas.add(tableDisciplinas, BorderLayout.CENTER);

		btAvancar = new JButton("Voltar e editar");
		btAvancar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (btAvancar.getText() == "Avançar") {
					TelaHorariosGerados telahorariosgerados;
					try {
						telahorariosgerados = new TelaHorariosGerados(statement, janela, disciplinas, turmas, horarios, semana,
								turno);
						telahorariosgerados.setLocationRelativeTo(janela);
						telaRevisao.dispose();
						telahorariosgerados.setVisible(true);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				telaRevisao.dispose();
			}
		});
		btAvancar.setForeground(Color.WHITE);
		btAvancar.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btAvancar.setBackground(new Color(45, 45, 45));
		btAvancar.setBounds(527, 322, 161, 25);
		contentPane.add(btAvancar);

		Collections.sort(disciplinas, new Comparator<Disciplina>() {
			@Override
			public int compare(Disciplina d1, Disciplina d2) {
				return Integer.compare(d2.getAulasTotais(), d1.getAulasTotais());
			}
		});

		DefaultTableModel modelDisciplinas = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modelDisciplinas.addColumn("Disciplinas");
		modelDisciplinas.addColumn("Aulas");

		boolean haAulasAMais = false;

		for (Disciplina d : disciplinas) {
			int aulasTotais = d.getAulasTotais();

			String disicplina = d.getNomeCompleto();
			String aulas = aulasTotais + "/" + maxAulasDisciplinas;

			if (aulasTotais > maxAulasDisciplinas) {
				haAulasAMais = true;
			}

			modelDisciplinas.addRow(new Object[] { disicplina, aulas });
		}

		tableDisciplinas.setModel(modelDisciplinas);

		lblAulasPorTurmas = new JLabel("Aulas por turmas:");
		lblAulasPorTurmas.setHorizontalAlignment(SwingConstants.LEFT);
		lblAulasPorTurmas.setForeground(new Color(136, 136, 136));
		lblAulasPorTurmas.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		lblAulasPorTurmas.setBounds(354, 32, 334, 41);
		contentPane.add(lblAulasPorTurmas);

		scrollTurmas = new JScrollPane();
		scrollTurmas.setBounds(354, 66, 334, 246);
		contentPane.add(scrollTurmas);

		panelTurmas = new JPanel();
		panelTurmas.setBackground(new Color(45, 45, 45));
		scrollTurmas.setViewportView(panelTurmas);
		panelTurmas.setLayout(new BorderLayout(0, 0));

		tableTurmas = new JTable();
		tableTurmas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableTurmas.setRowHeight(35);
		tableTurmas.setForeground(Color.WHITE);
		tableTurmas.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		tableTurmas.setBackground(new Color(45, 45, 45));
		panelTurmas.add(tableTurmas, BorderLayout.CENTER);
		tableDisciplinas.getColumnModel().getColumn(0).setPreferredWidth(240);
		tableDisciplinas.setDefaultRenderer(Object.class, new CustomRendererDisciplina());

		lblDica = new JLabel("Atenção: o limite de aulas por semana não pode ser ultrapassado");
		lblDica.setHorizontalAlignment(SwingConstants.LEFT);
		lblDica.setForeground(new Color(255, 0, 0));
		lblDica.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		lblDica.setBounds(10, 312, 507, 41);
		lblDica.setVisible(false);
		contentPane.add(lblDica);

		Collections.sort(turmas, new Comparator<Turma>() {
			@Override
			public int compare(Turma t1, Turma t2) {
				return Integer.compare(t2.getAulasTotais(), t1.getAulasTotais());
			}
		});

		DefaultTableModel modelTurmas = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modelTurmas.addColumn("Turmas");
		modelTurmas.addColumn("Aulas");

		boolean haAulasAMenos = false;
		for (Turma t : turmas) {
			int aulasTotais = t.getAulasTotais();

			String turma = t.getNomeTurma();
			String aulas = aulasTotais + "/" + maxAulasTurmas;

			if (aulasTotais > maxAulasTurmas) {
				haAulasAMais = true;
			}
			if (aulasTotais < maxAulasTurmas) {
				haAulasAMenos = true;
			}

			modelTurmas.addRow(new Object[] { turma, aulas });
		}
		tableTurmas.setModel(modelTurmas);

		if (haAulasAMenos) {
			lblDica.setForeground(new Color(255, 255, 51));
			lblDica.setText("Aviso: uma ou mais turmas terão aulas vagas");
			lblDica.setVisible(true);
		}
		if (!haAulasAMais) {
			btAvancar.setText("Avançar");
		} else {
			lblDica.setForeground(Color.red);
			lblDica.setText("Atenção: o limite de aulas por semana não pode ser ultrapassado");
			lblDica.setVisible(true);
		}
		tableTurmas.getColumnModel().getColumn(0).setPreferredWidth(240);
		tableTurmas.setDefaultRenderer(Object.class, new CustomRendererTurma());
	}

	private class CustomRendererDisciplina extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

			String aulas = (String) table.getValueAt(row, 1);
			String[] partes = aulas.split("/");
			int aulasTotais = Integer.parseInt(partes[0]);			

			if (aulasTotais > maxAulasDisciplinas) {
				rendererComponent.setForeground(Color.red);
			} else {
				rendererComponent.setBackground(table.getBackground());
				rendererComponent.setForeground(table.getForeground());
			}
			return rendererComponent;
		}
	}

	private class CustomRendererTurma extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

			String aulas = (String) table.getValueAt(row, 1);
			String[] partes = aulas.split("/");
			int aulasTotais = Integer.parseInt(partes[0]);			

			if (aulasTotais > maxAulasTurmas) {
				rendererComponent.setForeground(Color.red);
			} else if (aulasTotais < maxAulasTurmas) {
				rendererComponent.setForeground(new Color(255, 255, 51));
			} else {
				rendererComponent.setBackground(table.getBackground());
				rendererComponent.setForeground(table.getForeground());
			}
			return rendererComponent;
		}
	}
}