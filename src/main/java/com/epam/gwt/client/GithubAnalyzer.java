package com.epam.gwt.client;

import com.epam.gwt.client.i18n.GitHubAnalyzerConstants;
import com.epam.gwt.client.i18n.GithubAnalyzerMessages;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.logging.Logger;

public class GithubAnalyzer implements EntryPoint {

    private static final Logger LOG = Logger.getLogger("com.epam.gwt.client.GithubAnalyzer");

    private final GitHubAnalyzerConstants constants = GWT.create(GitHubAnalyzerConstants.class);
    private final GithubAnalyzerMessages messages = GWT.create(GithubAnalyzerMessages.class);
    private final AccountService accountService = new InMemoryAccountService();

    private final FlexTable userTable = new FlexTable();
    private TabPanel tabPanel;

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
        userTable.setText(0, 0, "ID");
        userTable.setText(0, 1, "Login");
        userTable.setText(0, 2, "URL");
        userTable.setText(0, 3, "Type");

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
                            userTable.setText(i + 1, 0, String.valueOf(user.getId()));
                            userTable.setText(i + 1, 1, user.getLogin());
                            userTable.setText(i + 1, 2, user.getUrl());
                            userTable.setText(i + 1, 3, user.getType());
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
            LOG.severe(e.getMessage());
        }


        sendButton.addClickHandler(event -> {
            boolean isValid = accountService.login(nameField.getText(), passwordField.getText());
            if (isValid) {
                dialogBox.hide();
                LOG.info(messages.successfulLogin(nameField.getText()));
                RootPanel.get().remove(dialogBox);

                tabPanel = new TabPanel();
                tabPanel.add(userTable, "Users");
                tabPanel.add(new Label("Repositories shoul be here"), "Repositories");
                tabPanel.add(new Label("Other"), "Other");
                tabPanel.selectTab(0);
                tabPanel.addSelectionHandler(selectedEvent -> History.newItem("page" + selectedEvent.getSelectedItem()));
                RootPanel.get().add(tabPanel);

                History.addValueChangeHandler(changeEvent -> {
                    String historyToken = changeEvent.getValue();
                    Integer tabIndex = Integer.valueOf(historyToken.substring(4));
                    tabPanel.selectTab(tabIndex);
                });

            } else {
                LOG.severe(messages.invalidLogin(nameField.getText()));
            }
        });

        RootPanel.get().add(dialogBox);

        dialogBox.show();
        dialogBox.center();
    }
}
