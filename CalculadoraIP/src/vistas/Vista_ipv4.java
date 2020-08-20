package vistas;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.border.*;

import baseDatos.Base;
import direcciones.IPV4;

public class Vista_ipv4 {
	JFrame vista;
	JMenu opcion;
	JMenuBar barra;
	JButton B1,B2;
	JPanel auxP;
	Base db= Base.getInstances();
	Vista_ipv4(){
		vista=new JFrame("Calculadora IP");
		auxP=new JPanel(new BorderLayout());
		barra=new JMenuBar();
		opcion= new JMenu("Estilo");
		barra.add(opcion);
		B1=new JButton("registro");
		B2=new JButton("IPV6");
		B1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				registro nuevo=new registro(vista,auxP,db);
				vista.setContentPane(nuevo.getRegistro());
				vista.invalidate();
				vista.validate();
			}	
		});
		B2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vista_ipv6 nuevo = new Vista_ipv6(vista,auxP,db);
				vista.setContentPane(nuevo.getIPV6());
				vista.invalidate();
				vista.validate();
			}	
		});
		opcion.add(B1);
		opcion.add(B2);
		auxP.add(cabecera(),BorderLayout.NORTH);
		vista.add(auxP);
		vista.setSize(500, 150);
		vista.setVisible(true);
	}
	//cabecera
	private JPanel cabecera() {
		JPanel panel=new JPanel(new BorderLayout());
		JPanel aux=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel aux2=new JPanel(new BorderLayout());
		JButton aceptar=new JButton("Aceptar");
		JTextField buscador=new JTextField();
		aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//crear objeto ipv4 y llamar al menu central
				//verificar que sea una ip bien dada...
				auxP.removeAll();
				auxP.repaint();
				if(buscador.getText().indexOf("/")>0) {
					auxP.add(central(creacion(buscador.getText())));
				}
				else {
					auxP.add(CentroHost(Integer.parseInt(buscador.getText())));
				}
				buscador.setText(null);
				auxP.add(cabecera(),BorderLayout.NORTH);
				auxP.validate();
				vista.setSize(500,600);
				vista.validate();
			}	
		});
		aux.add(barra);
		aux2.add(new JLabel("Ingrse IP o N. Host: "),BorderLayout.WEST);
		aux2.add(buscador);
		aux2.add(aceptar,BorderLayout.EAST);
		panel.add(aux,BorderLayout.NORTH);
		panel.add(aux2);
		return panel;
	}
	//centro
	private JPanel central(IPV4 RED) {
		JPanel panel=new JPanel(null);
		String [] categoria= {"Clase","Mascara","tipo","Transmision","Reservada","Red",
				"Broadcast","Gateway","Rango"};
		String caracteristica[]= {Character.toString(RED.getClase()),
				this.escribir(RED.getMascara()),RED.getTIPO(),RED.Transmision(),RED.reserva(),
				this.escribir(RED.getDic_red()),this.escribir(RED.getBroadcast()),RED.gateway(),RED.rango()
		};
		String datos[]= {escribir(RED.getRed()),this.escribir(RED.getMascara()),
				RED.getTIPO(),Character.toString(RED.getClase()),escribir(RED.getDic_red()),
				RED.gateway(),escribir(RED.getBroadcast()),RED.rango()
		};
		for(int i=0;i<5;i++) {
			for(int j=i*2;j<i*2+2;j++) {
				JLabel categorias=new JLabel(categoria[j]);
				JTextField algo=new JTextField();
				algo.setText(caracteristica[j]);
				categorias.setBounds(30 + 230*(j-i*2), 20 + 100*i, 100, 50);
				if(i!=4)
					algo.setBounds(110+ 230*(j-i*2),30 + 100*i, 120, 30);
				else {
					algo.setBounds(110+ 230*(j-i*2),30 + 100*i, 200, 30);
					j++;
				}
				panel.add(categorias);
				panel.add(algo);
			}
		}
		db.IngresoDB("insert into ipv4 (direccion,mascara,tipo,clase,red,gateway,broadcast,rango) values(?,?,?,?,?,?,?,?)", datos);
		return panel;
	}
	//crear panel central para el host...
	private JPanel CentroHost(int host) {
		JPanel aux=new JPanel(null);
		String ip=this.transformar(IPV4.host(host));
		int  bitmascara=(int) (32-Math.log10(host+2)/Math.log10(2));
		int bithost=32-bitmascara;
		String categoria[]= {"Resultado","Bit de Host","Bit de mask"};
		String result[]= {ip+"/"+bitmascara, Integer.toString(bithost),
				Integer.toString(bitmascara)};
		String datos[]= {Integer.toString(host),ip+"/"+bitmascara, Integer.toString(bithost),
				Integer.toString(bitmascara)};
		for(int i=0;i<3;i++) {
			JLabel titulo=new JLabel(categoria[i]);
			JTextField respuesta=new JTextField(result[i]);
			if(i==0) {
				titulo.setBounds(130, 20, 100, 50);
				respuesta.setBounds(200, 30, 100, 30);
			}
			else {
				titulo.setBounds(10+230*(i-1), 120, 100, 50);
				respuesta.setBounds(110+230*(i-1), 130, 100, 30);
			}
			aux.add(titulo);
			aux.add(respuesta);
		}
		db.IngresoDB("insert into host (cantidad,redrequerida,bithost,bitmask) values  (?,?,?,?)",datos );
		return aux;
	}
	//Crear red...
	private IPV4 creacion(String txt) {
		IPV4 RED;
		String []division=new String[2];
		String []particion=new String[4];
		int [] opcion=new int[4];
		int aux=0;
		division=txt.split("[/]");
		particion=division[0].split("[.]");
		//ip
		for(int i=0;i<particion.length;i++) {
			opcion[i]=Integer.parseInt(particion[i]);
		}
		//mascara
		aux=Integer.parseInt(division[1]);
		RED=new IPV4(opcion,aux);
		return RED;
	}
	//escribir algunas direcciones
	private String escribir(int[] grupo) {
		StringBuffer escrito = new StringBuffer();
		for(int i=0;i<grupo.length;i++)
			escrito.append(grupo[i]).append((i<grupo.length-1)?'.':"");
		return escrito.toString();
	}
	private String transformar(char a) {
		String ip=null;
		switch(a) {
			case 'A':ip="10.0.0.0";
			break;
			case 'B':ip="172.16.0.0";
			break;
			case 'C':ip="192.168.0.0";
			break;
		}
		return ip;
	}
	public JPanel getIPv4() {
		return auxP;
	}
}
