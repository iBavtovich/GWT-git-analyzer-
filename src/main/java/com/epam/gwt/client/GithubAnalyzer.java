package com.epam.gwt.client;

import com.epam.gwt.client.i18n.GitHubAnalyzerConstants;
import com.epam.gwt.client.i18n.GithubAnalyzerMessages;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.logging.Logger;

public class GithubAnalyzer implements EntryPoint {

    private static final Logger LOG = Logger.getLogger("com.epam.gwt.client.GithubAnalyzer");

    private final GitHubAnalyzerConstants constants = GWT.create(GitHubAnalyzerConstants.class);
    private final GithubAnalyzerMessages messages = GWT.create(GithubAnalyzerMessages.class);
    private final AccountService accountService = new InMemoryAccountService();

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

        //REST
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "https://api.github.com/users");
        requestBuilder.setHeader("Content-Type", "application/json; charset=utf-8");

        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    int statusCode = response.getStatusCode();
                    if (statusCode == 200) {
                        String jsonResponse = response.getText();
                        JsArray<User> users = JsonUtils.safeEval(jsonResponse);
                        for (int i = 0; i < users.length(); i++) {
                            User user = users.get(i);
                            LOG.info("User" + user.getLogin());
                        }
                    } else {
                        LOG.severe("Response status code: " + statusCode);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    LOG.severe(exception.getMessage());
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }

        sendButton.addClickHandler(event -> {
            boolean isValid = accountService.login(nameField.getText(), passwordField.getText());
            if (isValid) {
                dialogBox.hide();
                LOG.info(messages.successfulLogin(nameField.getText()));
                RootPanel.get().remove(dialogBox);
            } else {
                LOG.severe(messages.invalidLogin(nameField.getText()));
            }
        });

        RootPanel.get().add(dialogBox);

        dialogBox.show();
        dialogBox.center();
    }
}
