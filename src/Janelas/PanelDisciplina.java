package janelas;

import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import entidades.Turno;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

public class PanelDisciplina extends JPanel {

	public TelaInicial janela;
	public Turno turno;
	
	private static final long serialVersionUID = 1L;

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
		
		JLabel lblTitulo = new JLabel("Professores do turno "+turno.getNomeTurno()+":");
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(new Color(136, 136, 136));
		lblTitulo.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(30, 30, 30));
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
						.addComponent(lblVoltar, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTitulo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
					.addGap(422))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblTitulo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
					.addGap(44))
		);
		
		JList<String> listProfessores = new JList<String>();
		listProfessores.setVisibleRowCount(10);
		listProfessores.setToolTipText("");
		listProfessores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listProfessores.setForeground(Color.WHITE);
		listProfessores.setFont(new Font("Noto Sans Light", Font.BOLD, 20));
		listProfessores.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listProfessores.setBackground(new Color(45, 45, 45));
		scrollPane.setViewportView(listProfessores);
		setLayout(groupLayout);
		
		
	}

}
