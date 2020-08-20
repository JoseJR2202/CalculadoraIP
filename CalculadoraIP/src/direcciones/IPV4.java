package direcciones;

public class IPV4 extends IP {
	private char clase;
	private int []broadcast;
	private int []dic_red;
	private String TIPO;
	public IPV4(int[] red) {
		super(red);
		clase=this.clasificacion();
		// TODO Auto-generated constructor stub
	}
	public IPV4(int[] red, int subred){
		super(red,subred);
		clase=this.clasificacion();
		dic_red=this.direccion();
		broadcast=this.Broadcast();
		TIPO=this.tipo();
	}
	@Override
	protected int[] direccion() {
		int [] resultado = new int[4];
		for(int i=0;i<4;i++) {
			resultado[i]=red[i]&mascara[i];
		}
		return resultado;
	}

	@Override
	protected int[] Broadcast() {
		int [] resultado = new int[4];
		for(int i=0;i<4;i++) {
			resultado[i]=red[i]|~mascara[i]&0xff;
		}
		return resultado;
	}
	//clase
	private char clasificacion(){
		int []octeto= {128,192,224,240,256};
		for(int i=0;i<octeto.length;i++)
			if(this.red[0]<octeto[i]) 
				return (char) (i+65);
		return ' ';
	}
	//clase dado los host...
	static public char host(int host) {
		int []base= {8,16,24};
		for(int i=0;i<base.length;i++)
			if(host<Math.pow(2,base[i])-2)
				return (char) ( 67-i);
		return 0;
	}

	//tipo por red...
	public String tipo() {
		String tipo="";
		switch(this.clase) {
			case 'A': tipo=(this.red[0]==10)? "Privada":"Publica" ;
				break;
			case 'B': 
				if(this.red[0]==172 && (this.red[1]-16 <15))
					tipo="Privada";
				else if(this.red[0]==169&&this.red[1]==254)
					tipo="APIPA";
				else
					tipo="Publica";
				break;
			case 'C':tipo=(this.red[0]==192 && this.red[1]==168)? "Privada": "Publica";
				break;
			default: tipo="Publica";
		}
		return tipo;
	}
	// tipo de transmision (uni,broad, multi o ninguna)
	public String Transmision() {
		String transmisor="";
	if((this.red[0]+this.red[1]+this.red[2]+this.red[3])==(this.broadcast[0]+this.broadcast[1]+this.broadcast[2]+this.broadcast[3]))
			transmisor="broadcast";
		if(this.TIPO.equals("APIPA"))
			transmisor="Unicast";
		if(this.clase=='D')
			transmisor="Multicast";
		if(transmisor.equals(""))
			transmisor="ninguno";
		return transmisor;
	}
	//RANGO...
	public String rango() {
		StringBuffer rang=new StringBuffer();
		for(int i=0;i<4;i++)
			if(i<3)
				rang.append(this.dic_red[i]).append('.');
			else
				rang.append(this.dic_red[i]+1);
		rang.append(" - ");
		for(int i=0;i<4;i++)
			if(i<3)
				rang.append(this.broadcast[i]).append('.');
			else
				rang.append(this.broadcast[i]-1);
		return rang.toString();
	}
	//GATEWAY
	public String gateway() {
		StringBuffer rang=new StringBuffer();
		for(int i=0;i<4;i++)
			if(i<3)
				rang.append(this.dic_red[i]).append('.');
			else
				rang.append(this.dic_red[i]+1);
		return rang.toString();
	}
	public char getClase() {
		return clase;
	}
	public void setClase(char clase) {
		this.clase = clase;
	}
	public int[] getBroadcast() {
		return broadcast;
	}
	public void setBroadcast(int[] broadcast) {
		this.broadcast = broadcast;
	}
	public int[] getDic_red() {
		return dic_red;
	}
	public void setDic_red(int[] dic_red) {
		this.dic_red = dic_red;
	}
	public String getTIPO() {
		return TIPO;
	}
	public void setTIPO(String tIPO) {
		TIPO = tIPO;
	}
	@Override
	protected int[] mascaras(int valor) {
		int aux=valor;
		int sum=0;
		int mask[]=new int [4];
		for(int i=0;i<4;i++) {
			if(aux>=8)
				mask[i]=255;
			else if(aux>0){
				for(int a=8;a>aux;a--)
					sum+=Math.pow(2, 8-a);
				mask[i]=255-sum;
			}
			else
				mask[i]=0;
			aux-=8;
		}
		return mask;
	}
	public String reserva() {
		String reserva = "ninguno";
		switch(clase){
			case 'A':
				if(red[0]==0)
					reserva="Identificacion local";
				else if((red[0]==100) && (red[1]>63&&red[1]<127))
					reserva="Despliegues Carrier Grade NAT";
				else if(red[0]==127)
					reserva="LOOPBACK";
				break;
			case 'B':
				if(this.TIPO.equals("APIPA"));
					reserva="APIPA";
				break;
			case 'C':
				switch(red[0]) {
					case 192:
						if(red[1]==0)
							reserva=(red[2]==0)? "IETF Protocol":
								(red[2]==2)?"asignado aTEST-NET-1":"" ;
						else if(red[1]==88&&red[2]==99)
							reserva="Internet. Previamente usado para relay IPv6 a IPv4.";
						break;
					case 198:
						if(red[1]>17&&red[1]<20)
							reserva="pruebas de referencia de comunicaciones entre dos subredes separadas";
						else if(red[1]==51)
							reserva="TEST-NET-2";
						break;
					case 203:
						if(red[2]==113)
							reserva="TEST-NET-3";
						break;
				}
				break;
			case 'D':reserva="Multicast";
				break;
			case 'E':
				if(red[0]==255&&red[1]==255&&red[2]==255&&red[3]==255)
					reserva="Broadcast limitado";
				else
					reserva="Investigacion";
				break;
		}
		return reserva;
	}
}
