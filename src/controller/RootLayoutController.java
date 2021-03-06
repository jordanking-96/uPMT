/*****************************************************************************
 * RootLayoutController.java
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

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import utils.UndoCollector;

public class RootLayoutController implements Initializable{
		
	private @FXML MenuItem openProject;
	private @FXML MenuItem newInterview;
	private @FXML MenuItem quitterBouton;
	private @FXML MenuItem saveProject;
	private @FXML MenuItem exportProject;
	private @FXML MenuItem undo;
	private @FXML MenuItem redo;
	
	private Main main;
	private Stage window;
	
	public RootLayoutController(Main main,Stage window) {
		this.main = main;
		this.window = window;
	}
	
	@FXML
	public void openProject(){
		main.showLaunchingScreen();
	}
	
	@FXML
	public void saveProject(){
		main.saveCurrentProject();
	}
	
	@FXML
	public void exportProject(){
		main.exportCurrentProject();
	}
	
	@FXML
	public void undo(){
		UndoCollector.INSTANCE.showUndoRedoStack();
		UndoCollector.INSTANCE.undo();
	}
	
	@FXML
	public void redo(){
		UndoCollector.INSTANCE.showUndoRedoStack();
		UndoCollector.INSTANCE.redo();;
	}
	
	
	@FXML
	public void newInterview(){
		Stage promptWindow = new Stage();
		promptWindow.setTitle("Nouvel Entretien");
		promptWindow.setResizable(false);
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/NouveauEntretienDialogLayout.fxml"));
            loader.setController(new NewInterviewDialogController(main,promptWindow));
            loader.setResources(main._langBundle);
            BorderPane layout = (BorderPane) loader.load();
			Scene main = new Scene(layout);
			promptWindow.setScene(main);
			promptWindow.showAndWait();
			
		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}
	}
	
	private void saveRequest(WindowEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle(main._langBundle.getString("quit"));
    	alert.setHeaderText(main._langBundle.getString("quit_alarm"));
    	ButtonType buttonTypeOne = new ButtonType(main._langBundle.getString("ok"));
    	ButtonType buttonTypeTwo = new ButtonType(main._langBundle.getString("no"));
    	ButtonType buttonTypeCancel = new ButtonType(main._langBundle.getString("cancel"));
    	
    	alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo,buttonTypeCancel);

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == buttonTypeOne){
    		saveProject();
    		window.close();
    	} else if (result.get() == buttonTypeTwo) {
    	    window.close();
    	} else if (result.get() == buttonTypeCancel){
    	    alert.close();
    	    try {
    	    	 event.consume();
			} catch (NullPointerException e) {
				System.out.println("no event, exit normally");
			}
    	}
	}
	
	@FXML
	public void close(){
		this.saveRequest(null);
	}
	
	@FXML
	public void openEnglishVersion(){
		main.changeLocaleAndReload("en");
	}
	@FXML
	public void openFrenchVersion(){
		main.changeLocaleAndReload("fr");
	}
	@FXML
	public void openSpanishVersion(){
		main.changeLocaleAndReload("es");
	}
	@FXML
	public void openItalienVersion(){
		main.changeLocaleAndReload("it");
	}
	@FXML
	public void openJapaneseVersion(){
		main.changeLocaleAndReload("jp");
	}
	@FXML
	public void openChineseVersion(){
		main.changeLocaleAndReload("cn");
	}
	// Add your new language here
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_ANY));
		redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_ANY));
		saveProject.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY));
		saveProject.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY));
		newInterview.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_ANY));
		this.window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				saveRequest(event);
			}
		});
	}

}
