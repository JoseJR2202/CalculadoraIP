package direcciones;

public class IPV6 extends IP {

	public IPV6(int[] red) {
		super(red);
		// TODO Auto-generated constructor stub
	}

	public IPV6(int[] red, int subred) {
		super(red, subred);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int[] direccion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int[] Broadcast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int[] mascaras(int valor) {
		// TODO Auto-generated method stub
		return null;
	}
	public String reservas() {
		String reserva="";
		switch(Integer.toHexString(this.red[0])) {
		case "0":
			if(red[7]==0)
				reserva="Identificacion local, equivalente 0.0.0.0 en ipv4";
			else if(red[7]==1)
				reserva="LOOPBACK, equivalente 0.0.0.1 en ipv4";
			break;
		case "2000":reserva="Global unicast, Anicast";
			break;
		case "2001":
			if(Integer.toHexString(red[1]).equals("db8"))
				reserva="Ejemplos y Documentacion";
			break;
		case "2002":reserva="2002";
			break;
		case "fc00":reserva="Local unicast";
			break;
		case "fe80":reserva="Link-Local unicast";
			break;
		case "ff00":reserva="Multicast";
			break;
		case "3fff":
			if(Integer.toHexString(red[1]).equals("ffff"))
				reserva="Ejemplos y Documentacion";
			break;
		}
		return reserva;
	}
	public String escribir() {
		StringBuffer result=new StringBuffer();
		for(int i=0;i<this.red.length;i++)
			if(i<this.red.length-1)
				result.append(Integer.toHexString(this.red[i])).append(":");
			else
				result.append(Integer.toHexString(this.red[i]));
		return result.toString();
	}
}
