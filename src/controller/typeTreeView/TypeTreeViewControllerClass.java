/*****************************************************************************
 * TypeTreeViewControllerClass.java
 *****************************************************************************
 * Copyright © 2017 uPMT
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

package controller.typeTreeView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.command.AddPropertyToClassCommand;
import controller.command.ChangeColorClassCommand;
import controller.command.RemoveClassFromParentCommand;
import controller.command.RemovePropertyFromClassCommand;
import controller.command.RenameClassSchemeCommand;
import controller.controller.ChangeColorClassSchemeController;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.RenameClassSchemeController;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.Classe;
import model.Propriete;
import model.Type;
import utils.UndoCollector;
import utils.Utils;

public class TypeTreeViewControllerClass extends TypeTreeViewController implements Observer{
	
	private @FXML ColorPicker couleurType;
	private @FXML Button deleteClass;
	private @FXML Button addProperty;
	private @FXML ImageView classIcon;
	
	public static int propertiesNumber = 1;
	
	public TypeTreeViewControllerClass(TypeController type, TypeTreeView typeTreeView) {
		super(type,typeTreeView);
		type.getClassNameController().addObserver(this);
		type.getClassColorController().addObserver(this);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomType.setText(type.getType().getName());
		couleurType.setStyle("-fx-color-label-visible: false ;");
		setColor();
		
		File image = new File("./img/class.gif");
		Image icon = new Image(image.toURI().toString());
		this.classIcon.setImage(icon);
		
		File imageRename = new File("./img/rename.png");
		Node iconRename = new ImageView(new Image(imageRename.toURI().toString()));
		this.rename.setGraphic(iconRename);
		
		File delete = new File("./img/delete.gif");
		Node iconDelete = new ImageView(new Image(delete.toURI().toString()));
		this.deleteClass.setGraphic(iconDelete);
		
		File addPro = new File("./img/addProperty.gif");
		Node iconAddProp = new ImageView(new Image(addPro.toURI().toString()));
		this.addProperty.setGraphic(iconAddProp);
		
		Tooltip deleteClassTip = new Tooltip("Suppression de la classe");
		deleteClass.setTooltip(deleteClassTip);
		
		Tooltip addPropertyTip = new Tooltip("Ajout d'une proprieté a la classe");
		addProperty.setTooltip(addPropertyTip);
		
		Tooltip renameTip = new Tooltip("Renommer la classe");
		rename.setTooltip(renameTip);
	}
	
	@Override
	public void rename() {
		super.rename();
		ChangeListener<Boolean>	 listener = new ChangeListener<Boolean>() {
			 @Override
			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			    {
			        if (!newPropertyValue)
			        {
			        	if(type.getType().isClass()){
							String oldName = new String(type.getType().getName());
							RenameClassSchemeCommand cmd = new RenameClassSchemeCommand(type.getClassNameController(), oldName, textField.getText());
							cmd.execute();
							UndoCollector.INSTANCE.add(cmd);
						}		        	
						typePane.setLeft(nomType);
						rename.setDisable(false);
						textField.focusedProperty().removeListener(this);
			        }
			    }
		};
		
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER){
					
					if(type.getType().isProperty()){
						textField.setText(textField.getText());
					}
					typePane.setLeft(nomType);
					rename.setDisable(false);
				}
				if(event.getCode() == KeyCode.ESCAPE){
					typePane.setLeft(nomType);
					rename.setDisable(true);
				}
			}
		});
		textField.focusedProperty().addListener(listener);
		typePane.setLeft(textField);
		Platform.runLater(()->textField.requestFocus());
		Platform.runLater(()->textField.selectAll());
	}
	
	@FXML
	public void pickColor(){
		Color colorPicked = couleurType.getValue();
		String colorConverted = Utils.toRGBCode(colorPicked);
		ChangeColorClassCommand cmd = new ChangeColorClassCommand(type.getClassColorController(), this.type.getType().getCouleur(), colorConverted);
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);		
	}
	
	@FXML
	public void addProperty(){
		propertiesNumber++;
		Propriete nt = new Propriete("Propriete " + propertiesNumber);
		AddPropertyToClassCommand cmd = new AddPropertyToClassCommand(type, nt, tree.getTreeItem());
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);
	}
		
	@FXML
	public void deleteClass(){
		RemoveClassFromParentCommand cmd = new RemoveClassFromParentCommand(tree.getTreeItem().getValue(), (Classe) type.getType(),tree.getTreeItem().getParent());
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);
	}
	
	public void setColor(){
		if (type.getType().getCouleur() != null) {
			couleurType.setValue(Color.valueOf(type.getType().getCouleur()));
			this.nomType.setTextFill(Color.web(type.getType().getCouleur()));
		}
	}
	
	@Override
	public void hideButtons(){
		this.couleurType.setVisible(false);
		this.addProperty.setVisible(false);
		this.deleteClass.setVisible(false);
		this.rename.setVisible(false);
	}

	@Override
	public void showButtons() {
		this.couleurType.setVisible(true);
		this.addProperty.setVisible(true);
		this.deleteClass.setVisible(true);
		this.rename.setVisible(true);
	}

	@Override
	public void updateVue(Observable obs, Object value) {
		if(obs.getClass().equals(RenameClassSchemeController.class)) {
			this.nomType.setText((String) value);
		}
		if(obs.getClass().equals(ChangeColorClassSchemeController.class)) {
			this.nomType.setTextFill(Color.web((String) value));
			couleurType.setValue(Color.valueOf((String) value));
		}
	}
}
