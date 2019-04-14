package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {
	
	private Model model;
	private int[] vettoremesi= {1,2,3,4,5,6,7,8,9,10,11,12};
	private List<Integer> mesi = new ArrayList <Integer>();
	
	

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Integer> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		this.txtResult.clear();
		int mesescelto=this.boxMese.getValue();
		String sequenza=model.trovaSequenza(mesescelto);
		this.txtResult.appendText(sequenza);

	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		this.txtResult.clear();
		int mesescelto= this.boxMese.getValue();
		String umiditaMedia= model.getUmiditaMedia(mesescelto);
		this.txtResult.appendText(umiditaMedia);
	}


	public void inizializzamesi() {
		for(int i=0; i<this.vettoremesi.length;i++) {
			this.mesi.add(this.vettoremesi[i]);
		}
	}
	
	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
	}
	

	
	public void setModel(Model model2) {
		this.model=model2;
		this.inizializzamesi();
		this.boxMese.getItems().addAll(this.mesi);
	}

}
