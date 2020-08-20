package vistas;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;

import baseDatos.Base;
import direcciones.IPV6;

public class Vista_ipv6 {
	JPanel auxP;
	JMenu opcion;
	JMenuBar barra;
	JButton B1,B2;
	private Base bd;
	public Vista_ipv6(JFrame Panel, JPanel A,Base db){
		bd=db;
		auxP=new JPanel(new BorderLayout());
		barra=new JMenuBar();
		opcion=new JMenu("Estilo");
		barra.add(opcion);
		B1=new JButton("IPV4");
		B2=new JButton("Registro");
		B2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				registro nuevo=new registro(Panel,A,db);
				Panel.setContentPane(nuevo.getRegistro());
				Panel.invalidate();
				Panel.validate();
			}	
		});
		B1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Panel.setContentPane(A);
				Panel.invalidate();
				Panel.validate();
			}	
		});
		opcion.add(B1);
		opcion.add(B2);
		auxP.add(Cabecera(Panel),BorderLayout.NORTH);
		Panel.add(auxP);
		Panel.setSize(500, 350);
		Panel.setVisible(true);
	}
	private JPanel Cabecera(JFrame Panel) {
		JPanel panel= new JPanel(new BorderLayout());
		JPanel auxiliar=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel auxiliar2=new JPanel(new BorderLayout());
		JButton aceptar=new JButton("Aceptar");
		JTextField buscador =new JTextField();
		aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//crear objeto ipv4 y llamar al menu central
				//verificar que sea una ip bien dada...
				auxP.removeAll();
				auxP.repaint();
				String respuesta;
				int op;
				IPV6 direccion;
				String division[]=new String[2];
				if(buscador.getText().indexOf("/")>-1) {
					division=buscador.getText().split("[/]");
					respuesta=Verificacion(division[0]);
					op=Integer.parseInt(division[1]);
				}
				else {
					respuesta=Verificacion(buscador.getText());
					op=1;
				}
				direccion=new IPV6(transformacion2(respuesta),op);
				auxP.add(Cabecera(Panel),BorderLayout.NORTH);
				auxP.add(centro(direccion,op));
				auxP.validate();
				Panel.setSize(500, 450);
				Panel.validate();
			}	
		});
		JLabel titulo=new JLabel("IP:");
		auxiliar.add(barra,BorderLayout.WEST);
		auxiliar.add(new JLabel("REDES IPV6"));
		auxiliar2.add(titulo,BorderLayout.WEST);
		auxiliar2.add(buscador);
		auxiliar2.add(aceptar,BorderLayout.EAST);
		panel.add(auxiliar,BorderLayout.NORTH);
		panel.add(auxiliar2);
		return panel;
	}
	private JPanel centro(IPV6 red, int mascara) {
		JPanel panel=new JPanel(null);
		String []categoria= {"IP","Mascara","Tipo"};
		String []result= {red.escribir(),Integer.toString(mascara),red.reservas()};
		for(int i=0;i<3;i++) {
			JLabel titulo=new JLabel(categoria[i]);
			JTextField respuesta=new JTextField();
			respuesta.setText(result[i]);
			titulo.setBounds(10 , 20 + 100*i, 100, 50);
			respuesta.setBounds(120, 30+100*i, 200, 30);
			panel.add(titulo);
			panel.add(respuesta);
		}
		bd.IngresoDB("insert into ipv6 (direccion,mascara,reservacion) values (?,?,?)",result);
		return panel;
	}
	//verificacion y/o transformacion
	private String Verificacion(String txt) {
		String[] direc = null,direc2;
		String divid,divid2;
		StringBuffer cadena =new StringBuffer();
		int sum;
		//revisando notacion simplificada...
		int num=txt.indexOf("::");
		if(num>-1) {
			divid=txt.substring(0,num);
			divid2=txt.substring(num+2, txt.length());
			direc=(txt.indexOf(".")>-1)?notaMixta(divid2).split("[:]"):transformacion(divid2).split("[:]");
			direc2=(num!=0)?transformacion(divid).split("[:]"):null;
			if(num==0) {
				sum=8-direc.length;
				for(int i=0;i<sum;i++)
					cadena.append("0").append(":");
				for(int i=0;i<direc.length;i++)
					if(i!=direc.length-1)
						cadena.append(direc[i]).append(":");
					else
						cadena.append(direc[i]);
			}
			else if(num==txt.length()-1) {
				sum=8-direc2.length;
				for(int i=0;i<direc2.length;i++)
					cadena.append(direc2[i]).append(":");
				for(int i=0;i<sum;i++)
					if(i<sum-1)
						cadena.append("0").append(":");
					else
						cadena.append("0");
			}
			else {
				sum=8-direc.length-direc2.length;
				for(int i=0;i<direc2.length;i++)
					cadena.append(direc2[i]).append(":");
				for(int i=0;i<sum;i++)
					cadena.append("0").append(":");
				for(int i=0;i<direc.length;i++)
					if(i!=direc.length-1)
						cadena.append(direc[i]).append(":");
					else
						cadena.append(direc[i]);
			}
		}
		//notacion mixta
		else if(txt.indexOf(".")>0) {
			int op=txt.lastIndexOf(":");
			divid=txt.substring(0,op);
			divid2=txt.substring(op+1, txt.length());
			direc2=transformacion(divid).split("[:]");
			direc=notaMixta(divid2).split("[:]");
			if(direc2.length+direc.length!=8)
				return null;
			for(int i=0;i<direc2.length;i++)
				cadena.append(direc2[i]).append(":");
			for(int i=0;i<direc.length;i++)
				if(i!=direc.length-1)
					cadena.append(direc[i]).append(":");
				else
					cadena.append(direc[i]);
		}
		//notacion normal
		else {
			direc=transformacion(txt).split("[:]");
			if(direc.length==8)
				for(int i=0;i<direc.length;i++)
					cadena.append(direc[i]).append(":");
			else
				return null;
		}
		return cadena.toString();
	}
	private String notaMixta(String txt) {;
		String divid[]=new String[2],direc[],direc2[];
		StringBuffer algo = new StringBuffer();
		int op=txt.lastIndexOf(":");
		if(op>-1) {
			divid[0]=txt.substring(0,op+1);
			divid[1]=txt.substring(op+1, txt.length());
			direc2=divid[1].split("[.]");
			algo.append(divid[0]);
			//volver metodo
			if(direc2.length<3)
				return null;
			else {
				int []hex=new int[4];
				for(int i=0;i<4;i++) {
					hex[i]=Integer.parseInt(direc2[i]);
					if(i%2!=0)
						algo.append(Integer.toHexString(hex[i-1]))
						.append(Integer.toHexString(hex[i-1])).append(':');
				}
			}
		}
		//ver si se puede volver funcion...
		else {
			direc2=txt.split("[.]");
			if(direc2.length<3)
				return null;
			else {
				int []hex=new int[4];
				for(int i=0;i<4;i++) {
					hex[i]=Integer.parseInt(direc2[i]);
					if(i%2!=0)
						algo.append(Integer.toHexString(hex[i-1]))
						.append(Integer.toHexString(hex[i])).append(':');
				}
			}
			
		}
		return algo.toString();
	}
	private String transformacion(String dat) {
		StringBuffer red=new StringBuffer();
		if(dat.equals(""))
			return "";
		String [] dato=dat.split(":");
		for(int i=0;i<dato.length;i++) {
			//agregar algo para revisar que no pase de ffff
			if(i<dato.length-1){
				red.append(Integer.toHexString(Integer.parseInt(dato[i], 16))).append(":");
			}
			else
				red.append(Integer.toHexString(Integer.parseInt(dato[i], 16)));
		}
		
		return red.toString();
	}
	private int[] transformacion2(String dat) {
		int[] red = new int [8];
		String [] dato=dat.split(":");
		for(int i=0;i<dato.length;i++)
			if(!dato[i].equals(""))
				red[i]=Integer.parseInt(dato[i],16);
		return red;
	}
	public JPanel getIPV6() {
		return auxP;
	}
}
