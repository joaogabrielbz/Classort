package janelas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;

import entidades.Turma;
import entidades.Turno;

import javax.swing.border.MatteBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.BevelBorder;
import javax.swing.ListSelectionModel;
import javax.swing.LayoutStyle.ComponentPlacement;

import javax.swing.DefaultListModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelTurma extends JPanel {
	public TelaInicial janela;
	public Turno turno;

	private ArrayList<Integer> idsTurmas = new ArrayList<Integer>();
	private int idTurmaSelecionado;

	private JButton btNovaTurma;
	private JButton btRemoverTurma;
	private JTextField txtNomeTurma;
	private JList<String> listTurmas;
	private JButton btAvancar;

	private static final long serialVersionUID = 1L;

	public PanelTurma(Statement statement, TelaInicial janela, Turno turno) throws SQLException {
		this.janela = janela;
		this.turno = turno;

		setBackground(new Color(30, 30, 30));

		JLabel lblVoltar = new JLabel("< Voltar ");
		lblVoltar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(30, 30, 30)));
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
				janela.setContentPane(janela.contentPane);
				janela.revalidate();
				janela.repaint();
			}
		});
		lblVoltar.setHorizontalAlignment(SwingConstants.LEFT);
		lblVoltar.setForeground(new Color(136, 136, 136));
		lblVoltar.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));

		JLabel lblTitulo = new JLabel("Turmas do turno " + turno.getNomeTurno() + ":");
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(new Color(136, 136, 136));
		lblTitulo.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(30, 30, 30));

		txtNomeTurma = new JTextField();
		txtNomeTurma.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					String nometurma = txtNomeTurma.getText();
					Turma novaTurma = new Turma(0, nometurma, 1);
					if (!nometurma.isEmpty()) {
						if (btNovaTurma.getText().equals("Nova turma")) {
							insertTurma(statement, turno, novaTurma);
						} else {
							updateTurma(statement, novaTurma);
						}
						reiniciarLayout();
					}
				}
			}
		});
		txtNomeTurma.setForeground(new Color(255, 255, 255));
		txtNomeTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		txtNomeTurma.setBackground(new Color(45, 45, 45));
		txtNomeTurma.setColumns(10);

		btNovaTurma = new JButton("Nova turma");
		btNovaTurma.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				String nometurma = txtNomeTurma.getText();
				Turma novaTurma = new Turma(0, nometurma, 1);

				if (!nometurma.isEmpty()) {
					if (btNovaTurma.getText().equals("Nova turma")) {
						insertTurma(statement, turno, novaTurma);
					} else {
						updateTurma(statement, novaTurma);
					}
					reiniciarLayout();
				}
			}

		});
		btNovaTurma.setForeground(new Color(255, 255, 255));
		btNovaTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btNovaTurma.setBackground(new Color(45, 45, 45));

		btRemoverTurma = new JButton("Remover turma:");
		btRemoverTurma.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				deleteTurma(statement);
			}
		});
		btRemoverTurma.setVisible(false);
		btRemoverTurma.setForeground(Color.WHITE);
		btRemoverTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btRemoverTurma.setBackground(new Color(172, 0, 9));

		btAvancar = new JButton("Avançar");
		btAvancar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				try {
					janela.paneldisciplina = new PanelDisciplina(statement, janela, turno);
					janela.setContentPane(janela.paneldisciplina);
					janela.revalidate();
					janela.repaint();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});
		btAvancar.setForeground(Color.WHITE);
		btAvancar.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btAvancar.setBackground(new Color(45, 45, 45));

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btRemoverTurma, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
								.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblVoltar)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(txtNomeTurma, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btNovaTurma, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
						.addGap(262)
						.addComponent(btAvancar, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(20)
								.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(txtNomeTurma, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btNovaTurma))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(btRemoverTurma, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btAvancar, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE))
								.addGap(25)));

		listTurmas = new JList<String>();
		listTurmas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteTurma(statement);
				}
			}
		});
		listTurmas.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (listTurmas.getSelectedIndex() != -1) {
						// Coletando nome e id clicado //
						String nomeTurmaClicada = listTurmas.getModel().getElementAt(listTurmas.getSelectedIndex());
						txtNomeTurma.setText(nomeTurmaClicada);
						idTurmaSelecionado = idsTurmas.get(listTurmas.getSelectedIndex());

						btNovaTurma.setText("Salvar");
						btRemoverTurma.setVisible(true);
						btRemoverTurma.setText("Remover turma: " + nomeTurmaClicada);
					}
				}
			}
		});
		listTurmas.setVisibleRowCount(10);
		
		listTurmas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTurmas.setForeground(Color.WHITE);
		listTurmas.setFont(new Font("Noto Sans Light", Font.BOLD, 20));
		listTurmas.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listTurmas.setBackground(new Color(45, 45, 45));
		scrollPane.setViewportView(listTurmas);
		setLayout(groupLayout);

		listTurmas.setModel(gerarListModelTurma(statement));
	}

	public DefaultListModel<String> gerarListModelTurma(Statement statement) throws SQLException {
		idsTurmas = new ArrayList<Integer>(); // reseta a lista de ids
		String sql = "SELECT * FROM classortbd.turma WHERE turnoid = " + turno.getIdTurno() + "";
		ResultSet result = statement.executeQuery(sql);

		DefaultListModel<String> modelTurma = new DefaultListModel<String>();

		while (result.next()) {
			modelTurma.addElement(result.getString("nomeTurma"));
			idsTurmas.add(result.getInt("idTurma"));

		}

		return modelTurma;

	}

	private void insertTurma(Statement statement, Turno turno, Turma novaTurma) {
		// Verificando se ja existe o mesmo nome na lista //
		boolean existe = false;
		for (int i = 0; i != listTurmas.getModel().getSize(); i++) {
			if (listTurmas.getModel().getElementAt(i).equals(novaTurma.getNomeTurma())) {
				existe = true;
			}
		}
		// Caso não exista o nome ele salva a nova turma no banco de dados //
		if (!existe) {
			String sql = "INSERT INTO classortbd.turma(nometurma, turnoid) VALUES ('" + novaTurma.getNomeTurma() + "', "
					+ turno.getIdTurno() + ");";
			try {
				statement.execute(sql);
				listTurmas.setModel(gerarListModelTurma(statement));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void updateTurma(Statement statement, Turma novaTurma) {
		String sql = "UPDATE classortbd.turma SET  nometurma='" + novaTurma.getNomeTurma() + "'  WHERE idTurma="
				+ idTurmaSelecionado + ";";
		try {
			statement.execute(sql);
			listTurmas.setModel(gerarListModelTurma(statement));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void reiniciarLayout() {
		btRemoverTurma.setVisible(false);
		btNovaTurma.setText("Nova turma");
		txtNomeTurma.setText("");
	}

	private void deleteTurma(Statement statement) {
		if (idTurmaSelecionado > 0) {
			String sql = "DELETE FROM classortbd.turma_disciplina WHERE turmaId = " + idTurmaSelecionado + ";"
					+ "DELETE FROM classortbd.turma WHERE idTurma = " + idTurmaSelecionado + "";
			try {
				statement.execute(sql);
				listTurmas.setModel(gerarListModelTurma(statement));

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			reiniciarLayout();
		}
	}
}
