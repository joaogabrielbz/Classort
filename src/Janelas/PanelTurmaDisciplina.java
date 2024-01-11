package janelas;

import javax.swing.JPanel;
import java.awt.Color;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import entidades.Disciplina;
import entidades.Turma;
import entidades.Turno;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;

public class PanelTurmaDisciplina extends JPanel {
	
	private TelaInicial janela;
	private Turno turno;
	private ArrayList<Turma> turmas;
	private ArrayList<Disciplina> disciplians;
	

	private static final long serialVersionUID = 1L;

	public PanelTurmaDisciplina(Statement statement, TelaInicial janela, Turno turno, ArrayList<Turma> turmas, ArrayList<Disciplina> disciplians) {
		this.janela = janela;
		this.turno = turno;
		this.turmas = turmas;
		this.disciplians = disciplians;
		
		
		
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
				janela.setContentPane(janela.paneldisciplina);
				janela.revalidate();
				janela.repaint();
			}
		});
		lblVoltar.setHorizontalAlignment(SwingConstants.LEFT);
		lblVoltar.setForeground(new Color(136, 136, 136));
		lblVoltar.setFont(new Font("Noto Sans Light", Font.PLAIN, 16));
		lblVoltar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(30, 30, 30)));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(1, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addComponent(lblVoltar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
}
