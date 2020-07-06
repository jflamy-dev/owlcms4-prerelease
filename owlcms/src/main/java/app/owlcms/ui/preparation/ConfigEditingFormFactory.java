/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.ui.preparation;

import java.util.Collection;

import org.slf4j.LoggerFactory;
import org.vaadin.crudui.crud.CrudOperation;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;

import app.owlcms.data.config.Config;
import app.owlcms.data.config.ConfigRepository;
import app.owlcms.i18n.Translator;
import app.owlcms.ui.crudui.OwlcmsCrudFormFactory;
import app.owlcms.ui.shared.CustomFormFactory;
import app.owlcms.uievents.EventForwarder;
import ch.qos.logback.classic.Logger;

@SuppressWarnings("serial")
public class ConfigEditingFormFactory
        extends OwlcmsCrudFormFactory<Config>
        implements CustomFormFactory<Config> {

    @SuppressWarnings("unused")
    private ConfigContent origin;
    @SuppressWarnings("unused")
    private Logger logger = (Logger) LoggerFactory.getLogger(ConfigRepository.class);

    ConfigEditingFormFactory(Class<Config> domainType, ConfigContent origin) {
        super(domainType);
        this.origin = origin;
    }

    @Override
    public Config add(Config Config) {
        ConfigRepository.save(Config);
        return Config;
    }

    @Override
    public Binder<Config> buildBinder(CrudOperation operation, Config domainObject) {
        return super.buildBinder(operation, domainObject);
    }

    @Override
    public String buildCaption(CrudOperation operation, Config config) {
        return Translator.translate("Config.Titles");
    }

    @Override
    public Component buildFooter(CrudOperation operation, Config domainObject,
            ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener,
            ComponentEventListener<ClickEvent<Button>> postOperationCallBack,
            ComponentEventListener<ClickEvent<Button>> deleteButtonClickListener, boolean shortcutEnter,
            Button... buttons) {
        return super.buildFooter(operation, domainObject, cancelButtonClickListener, postOperationCallBack,
                deleteButtonClickListener, false, buttons);
    }

    @Override
    public Component buildNewForm(CrudOperation operation, Config domainObject, boolean readOnly,
            ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener,
            ComponentEventListener<ClickEvent<Button>> operationButtonClickListener) {
        return this.buildNewForm(operation, domainObject, readOnly, cancelButtonClickListener,
                operationButtonClickListener, null);
    }

    @Override
    public Component buildNewForm(CrudOperation operation, Config config, boolean readOnly,
            ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener,
            ComponentEventListener<ClickEvent<Button>> updateButtonClickListener,
            ComponentEventListener<ClickEvent<Button>> deleteButtonClickListener, Button... buttons) {

        binder = buildBinder(operation, config);

        FormLayout accessLayout = accessForm();
        FormLayout publicResultsLayout = publicResultsForm();

        Component footer = this.buildFooter(operation, config, cancelButtonClickListener,
                c -> {
                    this.update(config);
                }, deleteButtonClickListener, false);

        VerticalLayout mainLayout = new VerticalLayout(
                accessLayout, separator(),
                publicResultsLayout, separator(),
                footer);
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);

        binder.readBean(config);
        return mainLayout;
    }

    @Override
    public Button buildOperationButton(CrudOperation operation, Config domainObject,
            ComponentEventListener<ClickEvent<Button>> gridCallBackAction) {
        return super.buildOperationButton(operation, domainObject, gridCallBackAction);
    }

    @Override
    public TextField defineOperationTrigger(CrudOperation operation, Config domainObject,
            ComponentEventListener<ClickEvent<Button>> action) {
        return super.defineOperationTrigger(operation, domainObject, action);
    }

    @Override
    public void delete(Config config) {
        ConfigRepository.delete(config);
    }

    @Override
    public Collection<Config> findAll() {
        // will not be called, handled by the grid.
        return null;
    }

    @Override
    public boolean setErrorLabel(BinderValidationStatus<?> validationStatus, boolean showErrorOnFields) {
        return super.setErrorLabel(validationStatus, showErrorOnFields);
    }

    @Override
    public Config update(Config config) {
        Config saved = ConfigRepository.save(config);
        EventForwarder.changeUpdateKey(Config.getUpdateKeyParam());
        EventForwarder.changeRemoteURL(Config.getUpdateURLParam());
        return saved;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void bindField(HasValue field, String property, Class<?> propertyType) {
        binder.forField(field);
        super.bindField(field, property, propertyType);
    }

    private FormLayout accessForm() {
        FormLayout configLayout = createLayout();
        Component title = createTitle("Config.AccessControlTitle");
        configLayout.add(title);
        configLayout.setColspan(title, 2);
        
        TextField accessListField = new TextField();
        accessListField.setWidthFull();
        configLayout.addFormItem(accessListField, Translator.translate("Config.AccessList"));
        binder.forField(accessListField)
                .withNullRepresentation("")
                .bind(Config::getIpAccessList, Config::setIpAccessList);

        PasswordField passwordField = new PasswordField();
        passwordField.setWidthFull();
        configLayout.addFormItem(passwordField, Translator.translate("Config.PasswordOrPIN"));
        binder.forField(passwordField)
                .withNullRepresentation("")
                .bind(Config::getPin, Config::setPin);
        
        return configLayout;
    }

    private FormLayout createLayout() {
        FormLayout layout = new FormLayout();
//        layout.setWidth("1024px");
        layout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP),
                new ResponsiveStep("800px", 2, LabelsPosition.TOP));
        return layout;
    }

    private Component createTitle(String string) {
        H4 title = new H4(Translator.translate(string));
        title.getStyle().set("margin-top", "0");
        title.getStyle().set("margin-bottom", "0");
        return title;
    }

    private FormLayout publicResultsForm() {
        FormLayout layout = createLayout();
        Component title = createTitle("Config.PublicResultsTitle");
        layout.add(title);
        layout.setColspan(title, 2);

        TextField federationField = new TextField();
        federationField.setWidthFull();
        layout.addFormItem(federationField, Translator.translate("Config.publicResultsURL"));
        binder.forField(federationField)
                .withNullRepresentation("")
                .bind(Config::getPublicResultsURL, Config::setPublicResultsURL);

        PasswordField updateKey = new PasswordField();
        updateKey.setWidthFull();
        layout.addFormItem(updateKey, Translator.translate("Config.UpdateKey"));
        binder.forField(updateKey)
                .withNullRepresentation("")
                .bind(Config::getUpdatekey, Config::setUpdatekey);

        return layout;
    }

    private Hr separator() {
        Hr hr = new Hr();
        hr.getStyle().set("margin-top", "0.5em");
        hr.getStyle().set("margin-bottom", "1.0em");
        hr.getStyle().set("background-color", "var(--lumo-contrast-30pct)");
        hr.getStyle().set("height", "2px");
        return hr;
    }

}