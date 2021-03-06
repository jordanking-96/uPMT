/*****************************************************************************
 * SchemaTransformations.java
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

package utils;

import java.util.LinkedList;

import controller.controller.TypeController;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import model.DescriptionEntretien;
import model.Schema;
import model.Type;

public abstract class SchemaTransformations {

	public static TreeItem<TypeController> SchemaToTreeView(Schema s){
		TreeItem<TypeController> schema;
		TypeController ss = new TypeController(s, null);
		schema = new TreeItem<TypeController>(ss);
		schema.setExpanded(true);
		
		for (Type c : s.getTypes()) {
			addTypeTreeView(schema, c);			
		}
		return schema;
	}
	
	public static TreeItem<DescriptionEntretien> EntretienToTreeView(LinkedList<DescriptionEntretien> entretiens){
		TreeItem<DescriptionEntretien> entres;
		entres = new TreeItem<DescriptionEntretien>(new DescriptionEntretien(null, "Entretiens"));
		entres.setExpanded(true);
		for (DescriptionEntretien d : entretiens) {
			entres.getChildren().add(new TreeItem<DescriptionEntretien>(d));
		}
		return entres;
	}

	private static void addTypeTreeView(TreeItem<TypeController> tree, Type t){
		if(!t.getTypes().isEmpty()){
			
			TypeController tc = new TypeController(t, tree.getValue().getType());
			TreeItem<TypeController> node = new TreeItem<TypeController>(tc);
			node.setExpanded(true);
			tree.getChildren().add(node);
			
			for (Type s : t.getTypes()) {
				addTypeTreeView(node, s);
			}
		}else{
			TypeController tc = new TypeController(t, tree.getValue().getType());
			TreeItem<TypeController> node = new TreeItem<TypeController>(tc);
			tree.getChildren().add(node);
		}
	}	
}
