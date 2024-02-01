package janelas;


import java.awt.Toolkit;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jxl.write.*;
import entidades.TabelaDia;
import entidades.TabelaDisciplina;
import entidades.TabelaTurma;
import entidades.Tabelas;
import entidades.Turno;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class GerarPlanilhas {
	private ArrayList<TabelaTurma> tts;
	private ArrayList<TabelaDisciplina> tds;
	private ArrayList<TabelaDia> tdias;

	public GerarPlanilhas(Turno turno, Tabelas tabela, ArrayList<TabelaDia> tabeladias) {
		if (tabela != null && tabeladias != null) {
			this.tts = tabela.tabelaturmas;
			this.tds = tabela.tabeladisciplinas;
			this.tdias = tabeladias;

			LookAndFeel lookAndFeelOriginal = UIManager.getLookAndFeel();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				JFrame frame = new JFrame();
				frame.setIconImage(
						Toolkit.getDefaultToolkit().getImage(TelaHorariosGerados.class.getResource("/imgs/icon.png")));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Escolha um diretório");
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int escolha = fileChooser.showOpenDialog(frame);
				if (escolha == JFileChooser.APPROVE_OPTION) {
					File diretorioSelecionado = fileChooser.getSelectedFile();
					String nomeNovaPasta = turno.getNomeTurno() + " "
							+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
					File novaPasta = new File(diretorioSelecionado, nomeNovaPasta);
					if (!novaPasta.exists()) {
						novaPasta.mkdir();
					}
					try {	
						
						WritableWorkbook workbookTurmas = Workbook
								.createWorkbook(new File(novaPasta, "Turmas.xls"));
						for (TabelaTurma tabelaTurma : tts) {
							sheetsTurmas(workbookTurmas, tabelaTurma);
						}
						workbookTurmas.write();
						workbookTurmas.close();
						
						WritableWorkbook workbookDisciplinas = Workbook.createWorkbook(new File(novaPasta, "Disciplinas.xls"));
						for (TabelaDisciplina tabeladisciplina : tds) {
							sheetsDisciplinas(workbookDisciplinas, tabeladisciplina);
						}
						workbookDisciplinas.write();
						workbookDisciplinas.close();
						
						WritableWorkbook workbookDias = Workbook.createWorkbook(new File(novaPasta, "AulasPorDia.xls"));
						for (TabelaDia tabeladia : tdias) {
							sheetsDias(workbookDias, tabeladia);
						}
						workbookDias.write();
						workbookDias.close();
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				frame.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					UIManager.setLookAndFeel(lookAndFeelOriginal);
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void sheetsTurmas(WritableWorkbook workbookTurmas, TabelaTurma tabelaTurma) {
		try {
			WritableSheet sheet = workbookTurmas.createSheet(tabelaTurma.getTurma().getNomeTurma(), 0);
			String[][] matriz = tabelaTurma.getMatriz();
			
			WritableCellFormat estiloPrimeiraLinha = new WritableCellFormat();
			estiloPrimeiraLinha.setAlignment(Alignment.CENTRE);
			estiloPrimeiraLinha.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloPrimeiraLinha.setFont(new WritableFont(WritableFont.ARIAL, 16, WritableFont.NO_BOLD, true,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			estiloPrimeiraLinha.setBackground(Colour.GRAY_50);
			estiloPrimeiraLinha.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			WritableCellFormat estiloNome = new WritableCellFormat();
			estiloNome.setAlignment(Alignment.CENTRE);
			estiloNome.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloNome.setFont(new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.RED));
			estiloNome.setBackground(Colour.GRAY_25);
			estiloNome.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat estiloTitulos = new WritableCellFormat();
			estiloTitulos.setAlignment(Alignment.CENTRE);
			estiloTitulos.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloTitulos.setFont(new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.PALETTE_BLACK));
			estiloTitulos.setBackground(Colour.GRAY_25);
			estiloTitulos.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat estiloGeral = new WritableCellFormat();
			estiloGeral.setAlignment(Alignment.CENTRE);
			estiloGeral.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloGeral.setFont(new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			estiloGeral.setBorder(Border.ALL, BorderLineStyle.THIN);

			for (int i = 0; i < matriz.length; i++) {
				for (int j = 0; j < matriz[i].length; j++) {
					Label label = null;

					sheet.setColumnView(j, 25);
					sheet.setRowView(i, 600);

					if (i == 0 && j == 0) {
						label = new Label(j, i, matriz[i][j], estiloNome);
					} else if (i == 0 || j == 0) {
						label = new Label(j, i, matriz[i][j], estiloTitulos);
					} else if (i != 0 || j != 0) {
						label = new Label(j, i, matriz[i][j], estiloGeral);
					}

					sheet.addCell(label);
				}
			}
			sheet.insertRow(0);
			sheet.setRowView(0, 1200);
			int colunasMatriz = matriz.length > 0 ? matriz[0].length : 0;
			sheet.mergeCells(0, 0, colunasMatriz - 1, 0);
			Label labelCelulaUnificada = new Label(0, 0, "Classort - Lia Therezinha M. Rocha", estiloPrimeiraLinha);
			sheet.addCell(labelCelulaUnificada);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void sheetsDisciplinas(WritableWorkbook workbookDisciplinas, TabelaDisciplina tabeladisciplina) {
		try {
			WritableSheet sheet = workbookDisciplinas.createSheet(tabeladisciplina.getDisciplina().getNomeCompleto(), 0);
			String[][] matriz = tabeladisciplina.getMatriz();
			
			WritableCellFormat estiloPrimeiraLinha = new WritableCellFormat();
			estiloPrimeiraLinha.setAlignment(Alignment.CENTRE);
			estiloPrimeiraLinha.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloPrimeiraLinha.setFont(new WritableFont(WritableFont.ARIAL, 16, WritableFont.NO_BOLD, true,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			estiloPrimeiraLinha.setBackground(Colour.GRAY_50);
			estiloPrimeiraLinha.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			WritableCellFormat estiloNome = new WritableCellFormat();
			estiloNome.setAlignment(Alignment.CENTRE);
			estiloNome.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloNome.setFont(new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.RED));
			estiloNome.setBackground(Colour.GRAY_25);
			estiloNome.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat estiloTitulos = new WritableCellFormat();
			estiloTitulos.setAlignment(Alignment.CENTRE);
			estiloTitulos.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloTitulos.setFont(new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.PALETTE_BLACK));
			estiloTitulos.setBackground(Colour.GRAY_25);
			estiloTitulos.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat estiloGeral = new WritableCellFormat();
			estiloGeral.setAlignment(Alignment.CENTRE);
			estiloGeral.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloGeral.setFont(new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			estiloGeral.setBorder(Border.ALL, BorderLineStyle.THIN);

			for (int i = 0; i < matriz.length; i++) {
				for (int j = 0; j < matriz[i].length; j++) {
					Label label = null;

					sheet.setColumnView(j, 25);
					sheet.setRowView(i, 600);

					if (i == 0 && j == 0) {
						label = new Label(j, i, matriz[i][j], estiloNome);
					} else if (i == 0 || j == 0) {
						label = new Label(j, i, matriz[i][j], estiloTitulos);
					} else if (i != 0 || j != 0) {
						label = new Label(j, i, matriz[i][j], estiloGeral);
					}

					sheet.addCell(label);
				}
			}
			sheet.insertRow(0);
			sheet.setRowView(0, 1200);
			int colunasMatriz = matriz.length > 0 ? matriz[0].length : 0;
			sheet.mergeCells(0, 0, colunasMatriz - 1, 0);
			Label labelCelulaUnificada = new Label(0, 0, "Classort - Lia Therezinha M. Rocha", estiloPrimeiraLinha);
			sheet.addCell(labelCelulaUnificada);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private static void sheetsDias(WritableWorkbook workbookDias, TabelaDia tabelaDia) {
		try {
			WritableSheet sheet = workbookDias.createSheet(tabelaDia.getNomeDia(), 0);
			String[][] matriz = tabelaDia.getMatriz();
			
			WritableCellFormat estiloPrimeiraLinha = new WritableCellFormat();
			estiloPrimeiraLinha.setAlignment(Alignment.CENTRE);
			estiloPrimeiraLinha.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloPrimeiraLinha.setFont(new WritableFont(WritableFont.ARIAL, 16, WritableFont.NO_BOLD, true,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			estiloPrimeiraLinha.setBackground(Colour.GRAY_50);
			estiloPrimeiraLinha.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			WritableCellFormat estiloNome = new WritableCellFormat();
			estiloNome.setAlignment(Alignment.CENTRE);
			estiloNome.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloNome.setFont(new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.RED));
			estiloNome.setBackground(Colour.GRAY_25);
			estiloNome.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat estiloTitulos = new WritableCellFormat();
			estiloTitulos.setAlignment(Alignment.CENTRE);
			estiloTitulos.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloTitulos.setFont(new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.PALETTE_BLACK));
			estiloTitulos.setBackground(Colour.GRAY_25);
			estiloTitulos.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat estiloGeral = new WritableCellFormat();
			estiloGeral.setAlignment(Alignment.CENTRE);
			estiloGeral.setVerticalAlignment(VerticalAlignment.CENTRE);
			estiloGeral.setFont(new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			estiloGeral.setBorder(Border.ALL, BorderLineStyle.THIN);

			for (int i = 0; i < matriz.length; i++) {
				for (int j = 0; j < matriz[i].length; j++) {
					Label label = null;

					sheet.setColumnView(j, 25);
					sheet.setRowView(i, 600);

					if (i == 0 && j == 0) {
						label = new Label(j, i, matriz[i][j], estiloNome);
					} else if (i == 0 || j == 0) {
						label = new Label(j, i, matriz[i][j], estiloTitulos);
					} else if (i != 0 || j != 0) {
						label = new Label(j, i, matriz[i][j], estiloGeral);
					}

					sheet.addCell(label);
				}
			}
			sheet.insertRow(0);
			sheet.setRowView(0, 1200);
			int colunasMatriz = matriz.length > 0 ? matriz[0].length : 0;
			sheet.mergeCells(0, 0, colunasMatriz - 1, 0);
			Label labelCelulaUnificada = new Label(0, 0, "Classort - Lia Therezinha M. Rocha", estiloPrimeiraLinha);
			sheet.addCell(labelCelulaUnificada);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
