package com.epam.gwt.client;

import com.epam.gwt.client.i18n.GitHubAnalyzerConstants;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GithubAnalyzer implements EntryPoint {

    private final GitHubAnalyzerConstants constants = GWT.create(GitHubAnalyzerConstants.class);

    @Override
    public void onModuleLoad() {
        final TextBox nameField = new TextBox();
        nameField.setText("GWT User");
        final TextBox passwordField = new PasswordTextBox();
        final Button sendButton = new Button(constants.loginButtonText());
        sendButton.addStyleName("sendButton");

        VerticalPanel loginPanel = new VerticalPanel();
        loginPanel.add(nameField);
        loginPanel.add(passwordField);
        loginPanel.add(sendButton);

        DialogBox dialogBox = new DialogBox();
        dialogBox.setText(constants.dialogBoxTitle());
        dialogBox.setAnimationEnabled(true);
        dialogBox.add(loginPanel);

        sendButton.addClickHandler(event -> Window.alert("Successfully logged in: " + nameField.getText()));

        RootPanel.get().add(dialogBox);

        dialogBox.show();
        dialogBox.center();
    }
}
