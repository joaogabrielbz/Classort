package entidades;

//joaogabrielbz//

public class TabelaDia {
	private String nomeDia;
	private String[][] matriz;
	
	public String getNomeDia() {
		return nomeDia;
	}
	public void setNomeDia(String nomeDia) {
		this.nomeDia = nomeDia;
	}
	public String[][] getMatriz() {
		return matriz;
	}
	public void setMatriz(String[][] matriz) {
		this.matriz = matriz;
	}
	public TabelaDia(String nomeDia, String[][] matriz) {
		super();
		this.nomeDia = nomeDia;
		this.matriz = matriz;
	}	
}