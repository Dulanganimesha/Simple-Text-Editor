package Controller;

import javafx.event.ActionEvent;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditorFormController {

    public AnchorPane pneFind;
    public TextField txtFind;
    public TextArea txtEditor;
    public AnchorPane pneReplace;
    public TextField txtReplace;
    public Button btnReplace;
    public ColorPicker clrPicker;
    public AnchorPane root;
    public MenuBar mnuText;

    private PrinterJob printerJob;

    private int findOffset = -1;
    private final List<Index> searchList = new ArrayList();
    private int searchIndex = 0;
    private String filename;

    public void initialize(){
        pneFind.setVisible(false);
        pneReplace.setVisible(false);
        this.printerJob = PrinterJob.createPrinterJob();
        txtFind.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                Pattern pattern = Pattern.compile(newValue);
                Matcher matcher = pattern.matcher(txtEditor.getText());
                searchList.clear();

                while(matcher.find()){
                    searchList.add(new Index(matcher.start(), matcher.end()));
                }
            }catch(PatternSyntaxException e){
                findOffset = 0;
            }
        });
    }

    private void searchMatches(String query){
        try {
            Pattern regExp = Pattern.compile(query);
            Matcher matcher = regExp.matcher(txtEditor.getText());

            searchList.clear();

            while (matcher.find()) {
                searchList.add(new Index(matcher.start(), matcher.end()));
            }

            if (searchList.isEmpty()){
                findOffset = -1;
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void mnuitemnew_OnAction(ActionEvent actionEvent) {

        txtFind.clear();
        txtFind.requestFocus();
        txtReplace.clear();
        txtReplace.requestFocus();
    }

    public void mnuItemOpen_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Text Files", "*.txt", "*.html"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*"));
        File file = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());

        if(file == null){
            return;
        }

        txtEditor.clear();

        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)){

            String line = null;

            while((line = bufferedReader.readLine()) != null){
                txtEditor.appendText(line + '\n');

            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public void mnuItemFind_OnAction(ActionEvent actionEvent) {
        pneFind.setVisible(true);
        txtFind.requestFocus();
    }

    public void mnuItemReplace_OnAction(ActionEvent actionEvent) {
        pneReplace.setVisible(true);
        txtReplace.requestFocus();
    }

    public void mnuItemSelectAll_OnAction(ActionEvent actionEvent) {
        txtEditor.selectAll();
    }

    public void btnNext_OnAction(ActionEvent actionEvent) {
//        String search = txtFind.getText();
//        String text = txtEditor.getText();
////        Pattern pattern = Pattern.compile(search);
////        Matcher matcher = pattern.matcher(text);
////
////        boolean exit = matcher.find(findOffset);
////        if(exit){
////            findOffset = matcher.start() + 1;
////            txtEditor.selectRange(matcher.start(), matcher.end());
////        }
//        int start = text.indexOf(search , findOffset);
//        if(start != -1){
//            findOffset = start + 1;
//            txtEditor.selectRange(start, start + search.length());
//        }

        if(!searchList.isEmpty()){
            if(findOffset == -1){
                findOffset = 0;
            }
            txtEditor.selectRange(searchList.get(findOffset).startingIndex, searchList.get(findOffset).endIndex);

            findOffset++;
            if(findOffset >= searchList.size()){
                findOffset = 0;
            }
        }
    }

    public void btnPrevious_OnAction(ActionEvent actionEvent) {
        if(!searchList.isEmpty()){
            if(findOffset == -1){
                findOffset = searchList.size() - 1;
            }
        }
        txtEditor.selectRange(searchList.get(findOffset).startingIndex, searchList.get(findOffset).endIndex);
        findOffset--;
        if(findOffset<0){
            findOffset = searchList.size() - 1;
        }

    }

    public void btnReplace_OnAction(ActionEvent actionEvent) {

        if (findOffset == -1) return;
        txtEditor.replaceText(searchList.get(findOffset).startingIndex, searchList.get(findOffset).endIndex, txtReplace.getText());
        searchMatches(txtFind.getText());


    }


    public void btnReplaceAll_OnAction(ActionEvent actionEvent) {

        for (int i = 0; i < searchList.size(); i++) {
            System.out.println(searchList.size());
            txtEditor.replaceText(searchList.get(1).startingIndex, searchList.get(1).endIndex, txtReplace.getText());
            searchMatches(txtFind.getText());
        }
    }

    public void mnuSave_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());
        if(file == null){
            return;
        }

        try(FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw)){
            bw.write(txtEditor.getText());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void mnuSaveAs_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As File");
        File file1 = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());
        if(file1 == null){
            return;
        }

        try(FileWriter fw = new FileWriter(file1);
            BufferedWriter bw = new BufferedWriter(fw)){
                bw.write(txtEditor.getText());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void mnuPageSetup_OnAction(ActionEvent actionEvent) {
        printerJob.showPageSetupDialog(txtEditor.getScene().getWindow());
    }

    public void mnuPrint_OnAction(ActionEvent actionEvent) {
        printerJob.showPrintDialog(txtEditor.getScene().getWindow());
        printerJob.printPage(txtEditor.lookup("Test"));
    }



    public void clrPicker_OnAction(ActionEvent actionEvent) {

    }


    static class Index{
        int startingIndex;
        int endIndex;

        public Index(int startingIndex, int endIndex) {
            this.startingIndex = startingIndex;
            this.endIndex = endIndex;
        }
    }
}
