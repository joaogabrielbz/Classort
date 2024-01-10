package janelas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import entidades.Turno;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.MatteBorder;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class PanelDisciplina extends JPanel {

	public TelaInicial janela;
	public Turno turno;

	private ArrayList<Integer> idsDisciplinas = new ArrayList<Integer>();

	private static final long serialVersionUID = 1L;
	private JTextField txtNomeDisciplina;
	private JTextField txtProfessorDisciplina;

	public PanelDisciplina(Statement statement, TelaInicial janela, Turno turno) throws SQLException {
		setBackground(new Color(30, 30, 30));
		this.janela = janela;
		this.turno = turno;

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
				janela.setContentPane(janela.panelturma);
				janela.revalidate();
				janela.repaint();
			}
		});
		lblVoltar.setHorizontalAlignment(SwingConstants.LEFT);
		lblVoltar.setForeground(new Color(136, 136, 136));
		lblVoltar.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		lblVoltar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(30, 30, 30)));

		JLabel lblTitulo = new JLabel("Professores do turno " + turno.getNomeTurno() + ":");
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(new Color(136, 136, 136));
		lblTitulo.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(30, 30, 30));
		
		JLabel lblNomeDisciplina = new JLabel("Nome da disciplina:");
		lblNomeDisciplina.setHorizontalAlignment(SwingConstants.LEFT);
		lblNomeDisciplina.setForeground(new Color(136, 136, 136));
		lblNomeDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		
		txtNomeDisciplina = new JTextField();
		txtNomeDisciplina.setForeground(Color.WHITE);
		txtNomeDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		txtNomeDisciplina.setColumns(10);
		txtNomeDisciplina.setBackground(new Color(45, 45, 45));
		
		JLabel lblProfessorDisciplina = new JLabel("Nome do professor:");
		lblProfessorDisciplina.setHorizontalAlignment(SwingConstants.LEFT);
		lblProfessorDisciplina.setForeground(new Color(136, 136, 136));
		lblProfessorDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		
		txtProfessorDisciplina = new JTextField();
		txtProfessorDisciplina.setForeground(Color.WHITE);
		txtProfessorDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		txtProfessorDisciplina.setColumns(10);
		txtProfessorDisciplina.setBackground(new Color(45, 45, 45));
		
		JButton btNovaDisciplina = new JButton("Nova disciplina");
		btNovaDisciplina.setForeground(Color.WHITE);
		btNovaDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btNovaDisciplina.setBackground(new Color(45, 45, 45));
		
		JButton btRemoverDisciplina = new JButton("Remover disciplina");
		btRemoverDisciplina.setForeground(Color.WHITE);
		btRemoverDisciplina.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btRemoverDisciplina.setBackground(new Color(172, 0, 9));
		btRemoverDisciplina.setVisible(false);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
						.addComponent(lblVoltar, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTitulo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btNovaDisciplina, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNomeDisciplina, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(txtNomeDisciplina, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblProfessorDisciplina, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtProfessorDisciplina, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
							.addGap(18))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btRemoverDisciplina, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblProfessorDisciplina, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(txtProfessorDisciplina, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblNomeDisciplina, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtNomeDisciplina, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
							.addComponent(btRemoverDisciplina, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btNovaDisciplina, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
					.addGap(34))
		);

		JList<String> listDisciplinas = new JList<String>();
		listDisciplinas.setVisibleRowCount(10);
		listDisciplinas.setToolTipText("");
		listDisciplinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDisciplinas.setForeground(Color.WHITE);
		listDisciplinas.setFont(new Font("Noto Sans Light", Font.BOLD, 20));
		listDisciplinas.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listDisciplinas.setBackground(new Color(45, 45, 45));
		scrollPane.setViewportView(listDisciplinas);
		setLayout(groupLayout);
		
		listDisciplinas.setModel(gerarListModelDisciplina(statement));
	}

	public DefaultListModel<String> gerarListModelDisciplina(Statement statement) throws SQLException {
		idsDisciplinas = new ArrayList<Integer>(); // reseta a lista de ids
		String sql = "SELECT * FROM classortbd.disciplina WHERE turnoid = " + turno.getIdTurno() + "";
		ResultSet result = statement.executeQuery(sql);

		DefaultListModel<String> modelDisciplina = new DefaultListModel<String>();

		while (result.next()) {
			String elemento = result.getString("nomeDisciplina") + " - " + result.getString("professorDisciplina");
			modelDisciplina.addElement(elemento);
			idsDisciplinas.add(result.getInt("idDisciplina"));

		}

		return modelDisciplina;

	}

}
