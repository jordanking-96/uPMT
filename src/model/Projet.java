/*****************************************************************************
 * Projet.java
 *****************************************************************************
 * Copyright � 2017 uPMT
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;

public class Projet implements Serializable {
	
	private String nom;
	private Schema schemaProjet;
	private LinkedList<DescriptionEntretien> entretiens;

	
	public Projet(String n,Schema s){
		this.nom = n;
		this.entretiens = new LinkedList<DescriptionEntretien>();
		this.schemaProjet = s;
	}
	
	public void addEntretiens(DescriptionEntretien d){
		this.entretiens.add(d);
	}
	
	public String toString(){
		String str = "NomProjet = "+this.nom + "\n";
		for(DescriptionEntretien e : entretiens){
			str = str + e.toString() + "\n";
		}
		return str;
	}
	
	public String getName(){
		return this.nom;
	}

	public void save(){
		// save in it's own file
		ObjectOutputStream oos = null;
		try {
			final FileOutputStream fichier = new FileOutputStream("./save/"+this.getName()+".ser");
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(this);
			oos.flush();
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try 
			{
				if (oos != null) {
					oos.flush();
					oos.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}		
	}
	
	public void writeMoment(MomentExperience m, PrintWriter writer){
		
	}	
	
	
	public void remove(){
		File f = new File("./save/"+this.getName()+".ser");
		f.delete();
	}
	
	public static Projet load(String projet){
		ObjectInputStream ois = null;
		
		try {
			final FileInputStream fichier = new FileInputStream("./save/"+projet);
			System.out.println(projet);
			ois = new ObjectInputStream(fichier);
			final Projet p2 = (Projet) ois.readObject();
			return p2;

		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally 
		{
			try {
				if (ois != null) {
					ois.close();
				}
			} 
			catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Schema getSchemaProjet() {
		return schemaProjet;
	}

	public void setSchemaProjet(Schema schemaProjet) {
		this.schemaProjet = schemaProjet;
	}

	public LinkedList<DescriptionEntretien> getEntretiens() {
		return entretiens;
	}

	public void setEntretiens(LinkedList<DescriptionEntretien> entretiens) {
		this.entretiens = entretiens;
	}
	
}
