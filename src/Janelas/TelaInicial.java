package janelas;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entidades.Turno;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.border.SoftBevelBorder;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TelaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	private TelaInicial janela = this;
	public PanelTurma panelturma;
	public PanelDisciplina paneldisciplina;
	public PanelTurmaDisciplina panelturmadisciplina;

	public TelaInicial(Statement statement) throws SQLException {

		setBackground(new Color(30, 30, 30));
		setIconImage(Toolkit.getDefaultToolkit().getImage(TelaInicial.class.getResource("/imgs/icon.png")));
		setFont(new Font("SansSerif", Font.PLAIN, 16));
		setTitle("Classort");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 450);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 30));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JLabel lblBemVindo = new JLabel("Bem vindo ao Classort");
		lblBemVindo.setForeground(new Color(136, 136, 136));
		lblBemVindo.setFont(new Font("Noto Sans Light", Font.PLAIN, 25));
		lblBemVindo.setHorizontalAlignment(SwingConstants.CENTER);

		JList<String> listTurnos = new JList<String>();
		
		
		listTurnos.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SelecionarTurno(statement, listTurnos);
			}

		});
		
		listTurnos.setToolTipText("");
		listTurnos.setVisibleRowCount(10);
		listTurnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTurnos.setModel(new AbstractListModel() {
			String[] values = new String[] {};

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listTurnos.setForeground(new Color(255, 255, 255));
		listTurnos.setFont(new Font("Noto Sans Light", Font.BOLD, 20));
		listTurnos.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listTurnos.setBackground(new Color(45, 45, 45));

		JLabel lblSelecioneOTurno = new JLabel("Selecione o turno desejado:");
		lblSelecioneOTurno.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelecioneOTurno.setForeground(new Color(136, 136, 136));
		lblSelecioneOTurno.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addComponent(lblBemVindo,
						GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(174)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(listTurnos, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
								.addComponent(lblSelecioneOTurno, GroupLayout.PREFERRED_SIZE, 267,
										GroupLayout.PREFERRED_SIZE))
						.addGap(190)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(lblBemVindo, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblSelecioneOTurno, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(listTurnos, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(155, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);

		// Listar turnos do banco de dados //
		listTurnos.setModel(gerarListModelTurno(statement));
	}

	public DefaultListModel gerarListModelTurno(Statement statement) throws SQLException {

		String sql = "SELECT * FROM classortbd.turno";
		ResultSet result = statement.executeQuery(sql);

		DefaultListModel modelTurno = new DefaultListModel();
		while (result.next()) {
			modelTurno.addElement(result.getString("nomeTurno"));
		}

		return modelTurno;

	}

	private void SelecionarTurno(Statement statement, JList<String> listTurnos) {
		// coletando o id do turno clicado //
		String nomeTurnoSelecionado = (String) listTurnos.getSelectedValue();
		String sql = "SELECT idturno FROM classortbd.turno WHERE nometurno = '" + nomeTurnoSelecionado + "'";
		ResultSet result = null;
		try {
			result = statement.executeQuery(sql);
			if (result.next()) {

				// trocando painel para PanelTurma injetando o turno selecionado//
				Turno turnoSelecionado = new Turno(result.getInt("idturno"), nomeTurnoSelecionado);
				panelturma = new PanelTurma(statement, janela, turnoSelecionado);
				janela.setContentPane(panelturma);
				janela.revalidate();
				janela.repaint();

			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
