package direcciones;

public abstract class IP {
	protected int red[];
	protected int mascara[];
	//si solo me dan la ip
	public IP(int [] red){
		this.red=red;
	}
	//si me dan ambos valores
	public IP(int [] red, int subred){
		this.red=red;
		this.mascara=this.mascaras(subred);
	}
	protected abstract int[] direccion();
	protected abstract int[] Broadcast();
	protected abstract int[] mascaras(int valor);
	public int[] getMascara() {
		return mascara;
	}
	public void setMascara(int mascara[]) {
		this.mascara = mascara;
	}
	public int[] getRed() {
		return red;
	}
}
