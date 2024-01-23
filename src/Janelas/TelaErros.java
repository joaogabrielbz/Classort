package janelas;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import entidades.Realocacao;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class TelaErros extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JPanel panelPrincipal;
	private JLabel lblNoFoiPossvel;
	private JScrollPane scrollPane;
	private JPanel panelErros;
	private JTable tableErros;

	public TelaErros(TelaHorariosGerados telahorariosgerados, ArrayList<Realocacao> realocacoes) {
		setResizable(false);
		setTitle("Classort - Geração de horario falhou");
		setIconImage(Toolkit.getDefaultToolkit().getImage(TelaErros.class.getResource("/imgs/icon.png")));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());

		panelPrincipal = new JPanel();
		panelPrincipal.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelPrincipal.setBackground(new Color(30, 30, 30));
		getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(null);

		lblNoFoiPossvel = new JLabel("Não foi possível adicionar:");
		lblNoFoiPossvel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNoFoiPossvel.setForeground(new Color(136, 136, 136));
		lblNoFoiPossvel.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		lblNoFoiPossvel.setBounds(10, 11, 267, 41);
		panelPrincipal.add(lblNoFoiPossvel);

		scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(30, 30, 30));
		scrollPane.setBounds(10, 51, 416, 201);
		panelPrincipal.add(scrollPane);

		panelErros = new JPanel();
		panelErros.setBackground(new Color(45, 45, 45));
		scrollPane.setViewportView(panelErros);
		panelErros.setLayout(new BorderLayout(0, 0));

		tableErros = new JTable();
		tableErros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableErros.setRowHeight(35);
		tableErros.setForeground(Color.WHITE);
		tableErros.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		tableErros.setBackground(new Color(45, 45, 45));
		panelErros.add(tableErros, BorderLayout.CENTER);
		setLocationRelativeTo(telahorariosgerados);
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Turma");
		tableModel.addColumn("Disciplia");

		for (Realocacao r : realocacoes) {
			String nomeTurma = r.tt.getTurma().getNomeTurma();
			String nomeDisciplinaCompleto = r.td.getDisciplina().getNomeCompleto();

			tableModel.addRow(new Object[] { nomeTurma, nomeDisciplinaCompleto });
		}
		tableErros.setModel(tableModel);
	}
}
