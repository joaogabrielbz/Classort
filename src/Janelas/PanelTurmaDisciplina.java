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
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.ListSelectionModel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class PanelTurmaDisciplina extends JPanel {

	private TelaInicial janela;
	private Turno turno;
	private ArrayList<Turma> turmas;
	private ArrayList<Disciplina> disciplians;
	
	private int indexTurma = 0;
		
	private JTable tableDisciplinasSelecionadas;
	private JLabel lblTitulo;
	private JButton btAvancarTurma;
	private JButton btVoltarTurma;

	private static final long serialVersionUID = 1L;
	

	public PanelTurmaDisciplina(Statement statement, TelaInicial janela, Turno turno, ArrayList<Turma> turmas,
			ArrayList<Disciplina> disciplians) {
		this.janela = janela;
		this.turno = turno;
		this.turmas = turmas;
		this.disciplians = disciplians;

		setBackground(new Color(30, 30, 30));
		setForeground(new Color(255, 255, 255));

		JLabel lblVoltar = new JLabel("< Voltar ");
		lblVoltar.setBounds(10, 20, 60, 32);
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

		JScrollPane scrollPaneSelecionadas = new JScrollPane();
		scrollPaneSelecionadas.setBounds(67, 93, 325, 246);

		JPanel panelSelecionadas = new JPanel();
		panelSelecionadas.setBackground(new Color(45, 45, 45));
		scrollPaneSelecionadas.setViewportView(panelSelecionadas);
		panelSelecionadas.setLayout(new BorderLayout(0, 0));

		tableDisciplinasSelecionadas = new JTable();
		tableDisciplinasSelecionadas.setBackground(new Color(45, 45, 45));
		panelSelecionadas.add(tableDisciplinasSelecionadas, BorderLayout.CENTER);

		JScrollPane scrollPaneNaoSelecionadas = new JScrollPane();
		scrollPaneNaoSelecionadas.setBounds(410, 93, 321, 246);

		JList<String> listSelecionarDisciplinas = new JList<String>();
		listSelecionarDisciplinas.setVisibleRowCount(10);
		listSelecionarDisciplinas.setToolTipText("");
		listSelecionarDisciplinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSelecionarDisciplinas.setForeground(Color.WHITE);
		listSelecionarDisciplinas.setFont(new Font("Noto Sans Light", Font.BOLD, 20));
		listSelecionarDisciplinas.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listSelecionarDisciplinas.setBackground(new Color(45, 45, 45));
		scrollPaneNaoSelecionadas.setViewportView(listSelecionarDisciplinas);

		lblTitulo = new JLabel("Disciplinas da turma:");
		lblTitulo.setBounds(67, 52, 321, 41);
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(new Color(136, 136, 136));
		lblTitulo.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		JLabel lblInstrucao = new JLabel("Clique para adicionar:");
		lblInstrucao.setBounds(410, 52, 321, 41);
		lblInstrucao.setHorizontalAlignment(SwingConstants.LEFT);
		lblInstrucao.setForeground(new Color(136, 136, 136));
		lblInstrucao.setFont(new Font("Noto Sans Light", Font.PLAIN, 18));

		btAvancarTurma = new JButton("Avançar");
		btAvancarTurma.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(indexTurma != turmas.size()-1) {
					indexTurma++;
					carregarDisciplinas(statement);
				}
			}
		});
		btAvancarTurma.setHorizontalAlignment(SwingConstants.RIGHT);
		btAvancarTurma.setBounds(639, 374, 137, 25);
		btAvancarTurma.setForeground(Color.WHITE);
		btAvancarTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btAvancarTurma.setBackground(new Color(45, 45, 45));

		btVoltarTurma = new JButton("Retornar");
		btVoltarTurma.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(indexTurma != 0) {
					indexTurma--;
					carregarDisciplinas(statement);
				}				
			}
		});
		btVoltarTurma.setHorizontalAlignment(SwingConstants.LEFT);
		btVoltarTurma.setBounds(10, 374, 137, 25);
		btVoltarTurma.setForeground(Color.WHITE);
		btVoltarTurma.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btVoltarTurma.setBackground(new Color(45, 45, 45));
		setLayout(null);

		JButton btRemover = new JButton("Remover");
		btRemover.setVisible(false);
		btRemover.setBounds(67, 340, 325, 25);
		btRemover.setForeground(Color.WHITE);
		btRemover.setFont(new Font("Noto Sans Light", Font.PLAIN, 12));
		btRemover.setBackground(new Color(172, 0, 9));
		add(btRemover);
		add(lblVoltar);
		add(lblTitulo);
		add(lblInstrucao);
		add(scrollPaneSelecionadas);
		add(scrollPaneNaoSelecionadas);
		add(btVoltarTurma);
		add(btAvancarTurma);

		carregarDisciplinas(statement);
	}

	private void carregarDisciplinas(Statement statement) {
		lblTitulo.setText("Disciplinas da turma "+turmas.get(indexTurma).getNomeTurma()+":");
		
		if(indexTurma == 0) {
			btVoltarTurma.setVisible(false);
		}
		else {
			if(indexTurma == 1) {
				btVoltarTurma.setVisible(true);
			}
			btVoltarTurma.setText("< "+turmas.get(indexTurma-1).getNomeTurma());
		}
		
		if(indexTurma == turmas.size()-1) {
			btAvancarTurma.setVisible(false);
		}
		else {
			if(indexTurma == turmas.size()-2) {
				btAvancarTurma.setVisible(true);
			}
			btAvancarTurma.setText(turmas.get(indexTurma+1).getNomeTurma()+" >");
		}
		
	}
}
