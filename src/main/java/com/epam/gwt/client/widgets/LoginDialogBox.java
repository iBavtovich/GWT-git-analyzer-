package com.epam.gwt.client.widgets;

import com.epam.gwt.client.i18n.GitHubAnalyzerConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.function.BiConsumer;

public class LoginDialogBox extends DialogBox {

    private final GitHubAnalyzerConstants constants = GWT.create(GitHubAnalyzerConstants.class);
    private final VerticalPanel loginPanel;

    public LoginDialogBox(BiConsumer<String, String> onLoginButtonClickAction) {
        final TextBox nameField = new TextBox();
        final TextBox passwordField = new PasswordTextBox();
        final Button sendButton = new Button(constants.loginButtonText());
        sendButton.addStyleName("sendButton");
        sendButton.addClickHandler(event -> onLoginButtonClickAction.accept(nameField.getText(), passwordField.getText()));
        passwordField.addKeyPressHandler(event -> onLoginButtonClickAction.accept(nameField.getText(), passwordField.getText()));
        loginPanel = new VerticalPanel();
        loginPanel.add(nameField);
        loginPanel.add(passwordField);
        loginPanel.add(sendButton);

        setText(constants.dialogBoxTitle());
        add(loginPanel);
    }
}
