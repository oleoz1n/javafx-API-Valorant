package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PrimaryController implements Initializable{
    @FXML private FlowPane flowPane;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarPersonagem();
    }


    public void carregarPersonagem() {

        try {
            var url = new URL("https://valorant-api.com/v1/agents?language=pt-BR");
            var con = url.openConnection();

            con.connect();

            var is = con.getInputStream();

            var reader = new BufferedReader(new InputStreamReader(is));

            var json = reader.readLine();

            var lista = jsonParaLista(json);


           mostrarPersonagens(lista);

        } catch (IOException e) {
            e.printStackTrace();
               return;
        }

    }

    private List<Personagem> jsonParaLista(String json) throws IOException {
        var mapper = new ObjectMapper();
        var results = mapper.readTree(json).get("data");
        List<Personagem> personagens = new ArrayList<>();
        
        

        results.forEach(personagem -> {
            try {
                var p = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(personagem.toString(), Personagem.class);
                personagens.add(p);
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        return personagens;
    }

    private void mostrarPersonagens(List<Personagem> lista) {
        flowPane.getChildren().clear();
        lista.forEach(personagem -> {
            var image = new ImageView(new Image(personagem.getDisplayIcon()));
            image.setFitWidth(200);
            image.setFitHeight(200);
            var labelName = new Label(personagem.getDisplayName());
            labelName.setStyle("-fx-font-weight: bold");
            var labelDescription = new Label(personagem.getDescription());
            labelDescription.setWrapText(true);
            var VBoxPersonagem = new VBox();
            VBoxPersonagem.setPrefWidth(200);
            VBoxPersonagem.getChildren().addAll(image, labelName, labelDescription);
            flowPane.getChildren().add(
                VBoxPersonagem
            );
        });
    }
}
