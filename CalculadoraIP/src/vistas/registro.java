package vistas;
import baseDatos.Base;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
public class registro {
	private JPanel panel;
	private JMenu opcion;
	private JMenuBar barra;
	private JButton B1,B2;
	private Base bd;
	public registro(JFrame vista,JPanel A, Base db) {
		bd=db;
		panel=new JPanel(new BorderLayout());
		barra=new JMenuBar();
		opcion=new JMenu("estilo");
		B1=new JButton("IPV4");
		B2=new JButton("IPV6");
		B2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vista_ipv6 nuevo=new Vista_ipv6(vista,A,db);
				vista.setContentPane(nuevo.getIPV6());
				vista.invalidate();
				vista.validate();
			}	
		});
		B1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vista.setContentPane(A);
				vista.invalidate();
				vista.validate();
			}	
		});
		opcion.add(B1);
		opcion.add(B2);
		barra.add(opcion);
		panel.add(cabecera(vista),BorderLayout.NORTH);
		vista.setSize(500, 200);
	}
	private JPanel cabecera(JFrame vista) {
		JPanel aux=new JPanel(new BorderLayout());
		JPanel aux2=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel aux3=new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton op1,op2,op3;
		op1=new JButton("IPV4");
		op2=new JButton("IPV6");
		op3=new JButton("HOST");
		op3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.removeAll();
				panel.repaint();
				panel.add(centro("HOST"));
				panel.add(cabecera(vista),BorderLayout.NORTH);
				panel.validate();
				vista.setSize(500, 500);
			}	
		});
		op1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.removeAll();
				panel.repaint();
				panel.add(centro("IPV4"));
				panel.add(cabecera(vista),BorderLayout.NORTH);
				panel.validate();
				vista.setSize(500, 500);
			}	
		});
		op2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.removeAll();
				panel.repaint();
				panel.add(centro("IPV6"));
				panel.add(cabecera(vista),BorderLayout.NORTH);
				panel.validate();
				vista.setSize(500, 500);
			}	
		});
		aux3.add(new JLabel("OPCIONES:"));
		aux3.add(op1);
		aux3.add(op2);
		aux3.add(op3);
		aux2.add(barra);
		aux2.add(new JLabel("Registro"));
		aux.add(aux2,BorderLayout.NORTH);
		aux.add(aux3);
		return aux;
	}
	private JPanel centro(String txt) {
		JPanel p=new JPanel(new BorderLayout());
		DefaultTableModel modeA;
		modeA=new DefaultTableModel();
		String opcion;
		if(txt.equals("HOST")) {
			modeA.addColumn("Cantidad");
			modeA.addColumn("Fecha");	
			opcion="host";
		}
		else if(txt.equals("IPV4")){
			modeA.addColumn("Direccion");
			modeA.addColumn("Fecha");
			opcion="ipv4";
		}
		else {
			modeA.addColumn("Direccion");
			modeA.addColumn("Fecha");
			opcion="ipv6";
		}
		escribir(modeA,opcion,bd);
		JTable tabla=new JTable(modeA);
		JScrollPane scroll= new JScrollPane(tabla);
		p.add(scroll,BorderLayout.CENTER);
		return p;
	}
	private void escribir(DefaultTableModel l,String g, Base baseD) {
		baseD.Imprime("Select * from "+ g,l);
	}
	public JPanel getRegistro() {
		return panel;
	}
}
