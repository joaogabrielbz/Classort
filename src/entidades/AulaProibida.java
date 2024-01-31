package entidades;

public class AulaProibida {
	private int idAulaProibida;
	private int disciplinaId;
	private int horaIndex;
	private int diaIndex;
	public int getIdAulaProibida() {
		return idAulaProibida;
	}
	public void setIdAulaProibida(int idAulaProibida) {
		this.idAulaProibida = idAulaProibida;
	}
	public int getDisciplinaId() {
		return disciplinaId;
	}
	public void setDisciplinaId(int disciplinaId) {
		this.disciplinaId = disciplinaId;
	}
	public int getHoraIndex() {
		return horaIndex;
	}
	public void setHoraIndex(int horaIndex) {
		this.horaIndex = horaIndex;
	}
	public int getDiaIndex() {
		return diaIndex;
	}
	public void setDiaIndex(int diaIndex) {
		this.diaIndex = diaIndex;
	}
	public AulaProibida(int idAulaProibida, int disciplinaId, int horaIndex, int diaIndex) {
		super();
		this.idAulaProibida = idAulaProibida;
		this.disciplinaId = disciplinaId;
		this.horaIndex = horaIndex;
		this.diaIndex = diaIndex;
	}
	
	
}
