package com.epam.gwt.client;

import com.epam.gwt.client.i18n.GithubAnalyzerMessages;
import com.epam.gwt.client.services.AccountService;
import com.epam.gwt.client.services.AccountServiceAsync;
import com.epam.gwt.client.widgets.LoginDialogBox;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

import java.util.logging.Logger;

public class GithubAnalyzer implements EntryPoint {

    private static final Logger LOG = Logger.getLogger("com.epam.gwt.client.GithubAnalyzer");

    private final GithubAnalyzerMessages messages = GWT.create(GithubAnalyzerMessages.class);
    private final AccountServiceAsync accountService = GWT.create(AccountService.class);

    private final FlexTable userTable = new FlexTable();
    private TabPanel tabPanel;
    private LoginDialogBox loginDialogBox;

    @Override
    public void onModuleLoad() {

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

        tabPanel = new TabPanel();
        tabPanel.add(userTable, "Users");
        tabPanel.add(new Label("Repositories should be here"), "Repositories");
        tabPanel.add(new Label("Other"), "Other");
        tabPanel.selectTab(0);
        tabPanel.addSelectionHandler(selectedEvent -> History.newItem("page" + selectedEvent.getSelectedItem()));

        History.addValueChangeHandler(changeEvent -> {
            String historyToken = changeEvent.getValue();
            Integer tabIndex = Integer.valueOf(historyToken.substring(4));
            tabPanel.selectTab(tabIndex);
        });

        loginDialogBox = new LoginDialogBox((userName, password) -> accountService.login(userName, password, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LOG.severe(messages.invalidLogin(userName));
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    loginDialogBox.hide();
                    RootPanel.get().remove(loginDialogBox);
                    RootPanel.get().add(tabPanel);
                    LOG.info(messages.successfulLogin(userName));
                } else {
                    LOG.severe(messages.invalidLogin(userName));
                }
            }
        }));

        RootPanel.get().add(loginDialogBox);

        loginDialogBox.show();
        loginDialogBox.center();

    }
}
