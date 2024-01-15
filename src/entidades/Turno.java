package entidades;

//joaogabrielbz //

public class Turno {
	private int IdTurno;
	private String NomeTurno;

	public Turno(int idTurno, String nomeTurno) {
		super();
		IdTurno = idTurno;
		NomeTurno = nomeTurno;
	}

	public int getIdTurno() {
		return IdTurno;
	}

	public void setIdTurno(int idTurno) {
		IdTurno = idTurno;
	}

	public String getNomeTurno() {
		return NomeTurno;
	}

	public void setNomeTurno(String nomeTurno) {
		NomeTurno = nomeTurno;
	}

}
