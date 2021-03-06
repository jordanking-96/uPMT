/*****************************************************************************
 * LaunchingScreenController.java
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
import java.util.ResourceBundle;

import application.Main;
import javafx.scene.*;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Projet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class LaunchingScreenController implements Initializable{

	private @FXML Button btn_nouveauProjet;
	private @FXML Button btn_ouvrirProjet;
	private Main m_main;
	private @FXML ListView<Projet> tousLesProjets;
	private Stage window;
	
	public LaunchingScreenController(Main main,Stage window) {
		// TODO Auto-generated constructor stub
		m_main = main;
		this.window = window;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//TODO: in this part, we prompt a lauchingScreen Dialog for open or create new project
		ObservableList<Projet> items = FXCollections.observableArrayList (m_main.getProjects());
		tousLesProjets.setItems(items);
		tousLesProjets.getFocusModel().focus(0);
		tousLesProjets.setCellFactory(new Callback<ListView<Projet>, ListCell<Projet>>() {
	        @Override
	        public ListCell<Projet> call(ListView<Projet> param) {
	            
	        	ListCell<Projet> cell = new ListCell<Projet>() {

	                @Override
	                protected void updateItem(Projet item, boolean empty) {
	                    super.updateItem(item, empty);
	                    if (item != null) {
	                    	setText(item.getName());
	                    } else {
	                    	setText(""); 
	                    }
	                }
	            };
	            return cell;
	        }
	    });
	}
	
	public void NewProjectDialog(){
		// TODO: This prompt a new stage to help create a new project
		
		Stage promptWindow = new Stage();
		promptWindow.setTitle(m_main._langBundle.getString("new_project"));
		
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/NouveauProjetDialogLayout.fxml"));
            loader.setController(new NewProjectDialogController(m_main,promptWindow,window));
            loader.setResources(m_main._langBundle);
            BorderPane layout = (BorderPane) loader.load();
			Scene main = new Scene(layout,404,250);
			promptWindow.setScene(main);
			promptWindow.showAndWait();
			
		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}
	}
	
	public void OpenProjectDialog(){
		//TODO: open the selected project in the right pane and close the window
		if (tousLesProjets.getSelectionModel().getSelectedItem() == null){
			
		} else {
			m_main.setCurrentProject(tousLesProjets.getSelectionModel().getSelectedItem());
			m_main.launchMainView();
			window.close();
		}
		
	}
	
}
