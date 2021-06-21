package Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditorFormController {

    public AnchorPane pneFind;
    public TextField txtFind;
    public TextArea txtEditor;
    public AnchorPane pneReplace;
    public TextField txtReplace;

    private int findOffset = -1;
    private List<Index> searchList = new ArrayList();
    private int searchIndex = 0;

    public void initialize(){
        pneFind.setVisible(false);
        pneReplace.setVisible(false);
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

        }
    }

    public void mnuitemnew_OnAction(ActionEvent actionEvent) {

        txtFind.clear();
        txtFind.requestFocus();
        txtReplace.clear();
        txtReplace.requestFocus();
    }

    public void mnuItemCut_OnAction(ActionEvent actionEvent) {
    }

    public void mnuItemCopy_OnAction(ActionEvent actionEvent) {
    }

    public void mnuItemPaste_OnAction(ActionEvent actionEvent) {
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



    class Index{
        int startingIndex;
        int endIndex;

        public Index(int startingIndex, int endIndex) {
            this.startingIndex = startingIndex;
            this.endIndex = endIndex;
        }
    }
}
