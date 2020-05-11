package polito.it.Dashboard_Dati_Covid_19_Italia;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import polito.it.Dashboard_Dati_Covid_19_Italia.db.DatiCovidItaliaDAO;
import polito.it.Dashboard_Dati_Covid_19_Italia.model.DatiPerPercentuali;
import polito.it.Dashboard_Dati_Covid_19_Italia.model.DatoNazionale;
import polito.it.Dashboard_Dati_Covid_19_Italia.model.DatoRegionale;
import polito.it.Dashboard_Dati_Covid_19_Italia.model.Model;

public class FXMLController {

	private Model model;
	DatiCovidItaliaDAO dao;
	TreeMap<LocalDate, DatoNazionale> datiNazionali;
	LinkedList<DatoRegionale> datiRegionaliPerGiornata;
	LinkedList<DatoRegionale> datiRegionaliPerRegione;
	TreeMap<String, DatiPerPercentuali> datiPerPercentuali;



	 @FXML // ResourceBundle that was given to the FXMLLoader
	    private ResourceBundle resources;

	    @FXML // URL location of the FXML file that was given to the FXMLLoader
	    private URL location;

	    @FXML // fx:id="bottonePreparaDati"
	    private Button bottonePreparaDati; // Value injected by FXMLLoader

	    @FXML // fx:id="comboBoxData"
	    private ComboBox<LocalDate> comboBoxData; // Value injected by FXMLLoader

	    @FXML // fx:id="comboBoxRegione"
	    private ComboBox<String> comboBoxRegione; // Value injected by FXMLLoader

	    @FXML // fx:id="bottoneTerapie"
	    private Button bottoneTerapie; // Value injected by FXMLLoader

	    @FXML // fx:id="bottonePercentuale"
	    private Button bottonePercentuale; // Value injected by FXMLLoader

	    @FXML // fx:id="bottoneR0"
	    private Button bottoneR0; // Value injected by FXMLLoader

	    @FXML // fx:id="bottoneMortalita"
	    private Button bottoneMortalita; // Value injected by FXMLLoader

	    @FXML // fx:id="txtResult"
	    private TextArea txtResult; // Value injected by FXMLLoader

	    @FXML // fx:id="graficoResult"
	    private TextArea graficoResult; // Value injected by FXMLLoader

	    @FXML // fx:id="bottoneSimulazione"
	    private Button bottoneSimulazione; // Value injected by FXMLLoader
	    
	@FXML
	void avviaSimulazione(ActionEvent event) {

	}

	@FXML
	void calcolaPercentualePopolazioneContagiata(ActionEvent event) {

		txtResult.clear();
		LocalDate dataDesiderata = comboBoxData.getValue();
		String regioneDesiderata = comboBoxRegione.getValue();
		if (dataDesiderata == null || regioneDesiderata == null) {
			txtResult.appendText("Selezionare una data e una regione!!");
			return;
		}
		float percentualeContagiati = model.calcolaPercentualeAmmalatiPerLuogoNelGiorno(regioneDesiderata,
				dataDesiderata.toString());
		txtResult.appendText("La percentuale di popolazione contagiata in \n" + regioneDesiderata + " il giorno "
				+ dataDesiderata.toString() + " è del " + percentualeContagiati + "%");

	}

	@FXML
	void calcolaPostiLiberiInTerapiaIntensiva(ActionEvent event) {

		txtResult.clear();
		LocalDate dataDesiderata = comboBoxData.getValue();
		String regioneDesiderata = comboBoxRegione.getValue();
		if (dataDesiderata == null || regioneDesiderata == null) {
			txtResult.appendText("Selezionare una data e una regione!!");
			return;
		}

		int postiLiberi = model.calcolaPostiLiberiTerapiaIntensivaPerLuogoNelGiorno(regioneDesiderata,
				dataDesiderata.toString());
		txtResult.appendText("Nella regione " + regioneDesiderata + " il giorno " + dataDesiderata.toString()
				+ " erano \nancora disponibili " + postiLiberi + " letti in terapia intensiva");

	}

	@FXML
	void calcolaTassoDiContagiosita(ActionEvent event) {
		txtResult.clear();
		LocalDate dataDesiderata = comboBoxData.getValue();
		String regioneDesiderata = comboBoxRegione.getValue();
		if (dataDesiderata == null || regioneDesiderata == null) {
			txtResult.appendText("Selezionare una data e una regione!!");
			return;
		}

		float tassoContagiosita = model.calcolaTassoContagiosità(regioneDesiderata, dataDesiderata.toString());
		txtResult.appendText("Il tasso di contagiosità R0 in " + regioneDesiderata + " il giorno \n"
				+ dataDesiderata.toString() + " è approssimabile allo " + tassoContagiosita);
	}

	@FXML
	void calcolaTassoDiMortalita(ActionEvent event) {
		txtResult.clear();
		LocalDate dataDesiderata = comboBoxData.getValue();
		String regioneDesiderata = comboBoxRegione.getValue();
		if (dataDesiderata == null || regioneDesiderata == null) {
			txtResult.appendText("Selezionare una data e una regione!!");
			return;

		}

		float tassoMortalita = model.calcolaTassoMortalitaPerLuogoNelGiorno(regioneDesiderata,
				dataDesiderata.toString());
		txtResult.appendText("Il tasso di mortalità in " + regioneDesiderata + " il giorno \n"
				+ dataDesiderata.toString() + " è dello " + tassoMortalita + "%");
		
	}

	@FXML
	void preparaDati(ActionEvent event) {
		this.dao = new DatiCovidItaliaDAO();
		this.datiNazionali = new TreeMap<>(dao.estraiDatiNazionali());
		this.datiRegionaliPerGiornata = new LinkedList<>();
		this.datiRegionaliPerRegione = new LinkedList<>();
		this.datiPerPercentuali = new TreeMap<>(dao.estraiDatiPerPercentuali());
		this.model = new Model();
		LinkedList<LocalDate> elencoDate = new LinkedList<LocalDate>(datiNazionali.keySet());
		LocalDate partenza = LocalDate.parse("2020-02-29");

		for (LocalDate d : elencoDate)
			if (d.isAfter(partenza))
				comboBoxData.getItems().add(d);
		comboBoxRegione.getItems().addAll(datiPerPercentuali.keySet());
		

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert bottonePreparaDati != null : "fx:id=\"bottonePreparaDati\" was not injected: check your FXML file 'Scene.fxml'.";
		assert comboBoxData != null : "fx:id=\"comboBoxData\" was not injected: check your FXML file 'Scene.fxml'.";
		assert comboBoxRegione != null : "fx:id=\"comboBoxRegione\" was not injected: check your FXML file 'Scene.fxml'.";
		assert bottoneTerapie != null : "fx:id=\"bottoneTerapie\" was not injected: check your FXML file 'Scene.fxml'.";
		assert bottonePercentuale != null : "fx:id=\"bottonePercentuale\" was not injected: check your FXML file 'Scene.fxml'.";
		assert bottoneR0 != null : "fx:id=\"bottoneR0\" was not injected: check your FXML file 'Scene.fxml'.";
		assert bottoneMortalita != null : "fx:id=\"bottoneMortalita\" was not injected: check your FXML file 'Scene.fxml'.";
		assert graficoResult != null : "fx:id=\"graficoResult\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
		assert bottoneSimulazione != null : "fx:id=\"bottoneSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;

	}
}