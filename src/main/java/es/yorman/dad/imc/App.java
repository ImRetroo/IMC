package es.yorman.dad.imc;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class App extends Application {

	private static final String DICTAMEN_DEFAULT = "Bajo peso | Normal | Sobrepeso | Obeso";
	
	// model
	
	private DoubleProperty imc = new SimpleDoubleProperty();
	private DoubleProperty peso = new SimpleDoubleProperty();
	private DoubleProperty altura = new SimpleDoubleProperty();
	private StringProperty dictamenIMC = new SimpleStringProperty(DICTAMEN_DEFAULT);
	
	// view
	
	private TextField pesoF;
	private TextField alturaF;
	private Label imcL;
	private Label dictamenIMCL;
	
	@Override
	public void start(Stage primaryStage) {
		// view 
		pesoF = new TextField();
		pesoF.setPrefColumnCount(4);
		alturaF = new TextField();
		alturaF.setPrefColumnCount(4);
		imcL = new Label();
		dictamenIMCL = new Label();
		
		HBox pesoFila = new HBox(5, new Label("Peso:"), pesoF, new Label("kg"));
		pesoFila.setAlignment(Pos.CENTER);
		
		HBox alturaFila = new HBox(5, new Label("Altura:"), alturaF, new Label("cm"));
		alturaFila.setAlignment(Pos.CENTER);
		
		HBox imcFila = new HBox(5, new Label("IMC:"), imcL);
		imcFila.setAlignment(Pos.CENTER);
		
		VBox root = new VBox(5, pesoFila, alturaFila, imcFila, dictamenIMCL);
		root.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(root, 320, 200);
		
		primaryStage.setTitle("Calculadora IMC");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// bindings
		imcL.textProperty().bind(
			Bindings
				.when(imc.isEqualTo(0))
				.then("(peso * altura ^ 2)")
				.otherwise(imc.asString("%.2f"))
		);
		
		pesoF.textProperty().bindBidirectional(peso, new NumberStringConverter());
		alturaF.textProperty().bindBidirectional(altura, new NumberStringConverter());
		dictamenIMCL.textProperty().bind(dictamenIMC);

		imc.bind(
			Bindings
				.when(altura.greaterThan(0).and(peso.greaterThan(0)))
				.then(peso.divide(altura.divide(100).multiply(altura.divide(100))))
				.otherwise(0)
		);
		
		// listeners
		imc.addListener(this::onCalculateDictamenIMC);
	}

	private void onCalculateDictamenIMC(Observable observable, Number oldValue, Number newValue) {
		if(peso.doubleValue() > 0 && altura.doubleValue() > 0) {
			double imc = newValue.doubleValue();
			if(imc < 18.5) {
				dictamenIMC.set("Bajo peso");
			} else if(imc >= 18.5 && imc < 25) {
				dictamenIMC.set("Normal");
			} else if(imc >= 25 && imc < 30) {
				dictamenIMC.set("Sobrepeso");
			} else if(imc >= 30) {
				dictamenIMC.set("Obeso");
			}
		} else {
			dictamenIMC.set(DICTAMEN_DEFAULT);
		}
	}
}
