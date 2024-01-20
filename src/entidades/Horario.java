package entidades;

// joaogabrielbz // 

public class Horario {
	private String inicioHorario;
    private String fimHorario;    
    
	public Horario(String inicioHorario, String fimHorario) {
		super();
		this.inicioHorario = inicioHorario;
		this.fimHorario = fimHorario;
	}
	
	public String getInicioHorario() {
		return inicioHorario;
	}
	public void setInicioHorario(String inicioHorario) {
		this.inicioHorario = inicioHorario;
	}
	public String getFimHorario() {
		return fimHorario;
	}
	public void setFimHorario(String fimHorario) {
		this.fimHorario = fimHorario;
	}
    
}
