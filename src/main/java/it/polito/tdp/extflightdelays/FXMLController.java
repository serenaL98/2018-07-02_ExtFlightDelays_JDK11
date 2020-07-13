package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare ai branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField distanzaMinima;

    @FXML
    private Button btnAnalizza;

    @FXML
    private ComboBox<Airport> cmbBoxAeroportoPartenza;

    @FXML
    private Button btnAeroportiConnessi;

    @FXML
    private TextField numeroVoliTxtInput;

    @FXML
    private Button btnCercaItinerario;

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {

    	txtResult.clear();
    	
    	String minima = this.distanzaMinima.getText();
    	
    	try {
    		int miglia = Integer.parseInt(minima);
    		
    		txtResult.appendText("Crea grafo...");
    		
    		this.model.creaGrafo(miglia);
    		
    		txtResult.appendText("\n#VERICI: "+this.model.numeroVertici());
    		txtResult.appendText("\n#ARCHI: "+this.model.numeroArchi());
    		
    	} catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero intero!");
    		return;
    	}
    	
    	this.cmbBoxAeroportoPartenza.getItems().addAll(this.model.elencoAeroporti());
    	
    	this.btnAeroportiConnessi.setDisable(false);
    	
    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {

    	txtResult.clear();
    	
    	Airport partenza = this.cmbBoxAeroportoPartenza.getValue();
    	
    	if(partenza == null) {
    		txtResult.setText("Selezionare un aeroporto di partenza dal menù!");
    		return;
    	}
    	
    	txtResult.appendText("Aeroporti connessi a "+partenza.getAirportName()+":\n"+this.model.aeroportiConnessi(partenza));
    	
    	this.btnCercaItinerario.setDisable(false);
    	
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {

    	txtResult.clear();
    	
    	Airport partenza = this.cmbBoxAeroportoPartenza.getValue();
    	
    	String distanza = this.numeroVoliTxtInput.getText();
    	
    	try {
    		int miglia = Integer.parseInt(distanza);
    		
    		txtResult.appendText("Itinerario a partire da "+ partenza.getAirportName()+":\n"+this.model.itinerario(partenza, miglia));
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero intero positivo.");
    		return;
    	}
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.btnAeroportiConnessi.setDisable(true);
		this.btnCercaItinerario.setDisable(true);
	}
}
