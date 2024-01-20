package janelas;

// joaogabrielbz //

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import entidades.Disciplina;
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

public class TelaRevisao extends JDialog {

	private JPanel contentPane;
	private TelaRevisao telaRevisao = this;
	private static final long serialVersionUID = 1L;
	private JTable tableDisciplinas;

	public TelaRevisao(TelaInicial janela, PanelTurmaDisciplina panelturmadisciplina, ArrayList<Disciplina> disciplinas,
			int maxAulas) {
		setModal(true);

		setTitle("Classort");
		setIconImage(Toolkit.getDefaultToolkit().getImage(TelaRevisao.class.getResource("/imgs/icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 410);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 30));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblHProfessoresCom = new JLabel("Revise a quantidade de aulas dos professores:");
		lblHProfessoresCom.setHorizontalAlignment(SwingConstants.LEFT);
		lblHProfessoresCom.setForeground(new Color(136, 136, 136));
		lblHProfessoresCom.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		lblHProfessoresCom.setBounds(10, 32, 416, 41);
		contentPane.add(lblHProfessoresCom);

		JScrollPane scrollDisciplinas = new JScrollPane();
		scrollDisciplinas.setBounds(10, 66, 416, 246);
		contentPane.add(scrollDisciplinas);

		JPanel panelDisciplinas = new JPanel();
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

		JButton btAvancar = new JButton("Voltar e editar");
		btAvancar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (btAvancar.getText() == "Avançar") {
					System.out.println("GERANDO HARIRIOS");
				}
				telaRevisao.dispose();
			}
		});
		btAvancar.setForeground(Color.WHITE);
		btAvancar.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btAvancar.setBackground(new Color(45, 45, 45));
		btAvancar.setBounds(265, 323, 161, 25);
		contentPane.add(btAvancar);

		Collections.sort(disciplinas, new Comparator<Disciplina>() {
			@Override
			public int compare(Disciplina d1, Disciplina d2) {
				return Integer.compare(d2.getAulasTotais(), d1.getAulasTotais());
			}
		});

		DefaultTableModel modelDisciplinas = new DefaultTableModel();
		modelDisciplinas.addColumn("Disciplinas");
		modelDisciplinas.addColumn("Aulas");

		boolean haAulasAMais = false;

		for (Disciplina d : disciplinas) {
			int aulasTotais = d.getAulasTotais();

			String disicplina = d.getNomeCompleto();
			String aulas = aulasTotais + "/" + maxAulas;

			if (aulasTotais > maxAulas) {
				haAulasAMais = true;
			}

			modelDisciplinas.addRow(new Object[] { disicplina, aulas });
		}

		if (!haAulasAMais) {
			btAvancar.setText("Avançar");
		}

		tableDisciplinas.setModel(modelDisciplinas);
		tableDisciplinas.getColumnModel().getColumn(0).setPreferredWidth(240);
		tableDisciplinas.setDefaultRenderer(Object.class, new CustomRenderer());
	}

	private class CustomRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

			String aulas = (String) table.getValueAt(row, 1);
			String[] partes = aulas.split("/");
			int aulasTotais = Integer.parseInt(partes[0]);
			int maxAulas = Integer.parseInt(partes[1]);

			if (aulasTotais > maxAulas) {

				rendererComponent.setForeground(Color.red);
			} else {
				rendererComponent.setBackground(table.getBackground());
				rendererComponent.setForeground(table.getForeground());
			}

			return rendererComponent;
		}
	}

}