/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */

package app.owlcms.ui.lifting;

import org.slf4j.LoggerFactory;

import com.flowingcode.vaadin.addons.ironicons.AvIcons;
import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

import app.owlcms.components.elements.JuryDisplayDecisionElement;
import app.owlcms.data.athlete.Athlete;
import app.owlcms.fieldofplay.FOPEvent;
import app.owlcms.init.OwlcmsSession;
import app.owlcms.ui.shared.AthleteGridContent;
import app.owlcms.ui.shared.AthleteGridLayout;
import app.owlcms.ui.shared.BreakDialog;
import app.owlcms.ui.shared.BreakManagement.CountdownType;
import app.owlcms.uievents.BreakType;
import app.owlcms.uievents.UIEvent;
import app.owlcms.uievents.UIEvent.LiftingOrderUpdated;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * Class AnnouncerContent.
 */
@SuppressWarnings("serial")
@Route(value = "lifting/jury", layout = AthleteGridLayout.class)
public class JuryContent extends AthleteGridContent implements HasDynamicTitle {

    final private static Logger logger = (Logger) LoggerFactory.getLogger(JuryContent.class);
    final private static Logger uiEventLogger = (Logger) LoggerFactory.getLogger("UI" + logger.getName());
    private static final String BUTTON_WIDTH = "5em";
    static {
        logger.setLevel(Level.INFO);
        uiEventLogger.setLevel(Level.INFO);
    }

    private JuryDisplayDecisionElement decisions;
    private HorizontalLayout juryVotingButtons;
    private VerticalLayout juryVotingCenterHorizontally;
    private Icon[] juryIcons;
    private Boolean[] juryVotes;
    private int nbJurors;
    private Notification decisionNotification;

    public JuryContent() {
        // we don't actually inherit behaviour from the superclass because
        // all this does is call init() -- which we override.
        super();
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#add(app.owlcms.data.athlete.Athlete)
     */
    @Override
    public Athlete add(Athlete athlete) {
        // do nothing
        return athlete;
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#delete(app.owlcms.data.athlete.Athlete)
     */
    @Override
    public void delete(Athlete Athlete) {
        // do nothing;
    }

    /**
     * @see com.vaadin.flow.router.HasDynamicTitle#getPageTitle()
     */
    @Override
    public String getPageTitle() {
        return getTranslation("Jury");
    }

    @Subscribe
    public void slaveDown(UIEvent.DownSignal e) {
        // Ignore down signal
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#slaveGroupDone(app.owlcms.uievents.UIEvent.GroupDone)
     */
    @Override
    @Subscribe
    public void slaveGroupDone(UIEvent.GroupDone e) {
        uiEventLogger.debug("### {} {} {} {}", this.getClass().getSimpleName(), e.getClass().getSimpleName(),
                this.getOrigin(), e.getOrigin());
        OwlcmsSession.withFop((fop) -> doUpdateTopBar(fop.getCurAthlete(), 0));
    }

    @Subscribe
    public void slaveRefereeDecision(UIEvent.Decision e) {
        UIEventProcessor.uiAccess(this, uiEventBus, e, () -> {
            int d = e.decision ? 1 : 0;
            String text = getTranslation("NoLift_GoodLift", d, e.getAthlete().getFullName());

            decisionNotification = new Notification();
            // Notification theme styling is done in
            // META-INF/resources/frontend/styles/shared-styles.html
            String themeName = e.decision ? "success" : "error";
            decisionNotification.getElement().getThemeList().add(themeName);

            Div label = new Div();
            label.add(text);
            label.addClickListener((event) -> decisionNotification.close());
            label.setSizeFull();
            label.getStyle().set("font-size", "large");
            decisionNotification.add(label);
            decisionNotification.setPosition(Position.TOP_START);
            // decisionNotification.setDuration(5000);
            decisionNotification.open();
        });
    }

    @Subscribe
    public void slaveTimeStarted(UIEvent.StartTime e) {
        UIEventProcessor.uiAccess(this, uiEventBus, () -> {
            juryVotingButtons.removeAll();
            resetJuryVoting();
            if (decisionNotification != null) {
                decisionNotification.close();
            }
            // referee decisions handle reset on their own, nothing to do.
        });
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#updateGrid(app.owlcms.uievents.UIEvent.LiftingOrderUpdated)
     */
    @Override
    public void slaveUpdateGrid(LiftingOrderUpdated e) {
        // ignore
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#update(app.owlcms.data.athlete.Athlete)
     */
    @Override
    public Athlete update(Athlete athlete) {
        // do nothing
        return athlete;
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#announcerButtons(com.vaadin.flow.component.orderedlayout.HorizontalLayout)
     */
    @Override
    protected HorizontalLayout announcerButtons(FlexLayout announcerBar) {
        // moved down to the jury section
        return new HorizontalLayout(); // juryDeliberationButtons();
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#breakButtons(com.vaadin.flow.component.orderedlayout.FlexLayout)
     */
    @Override
    protected HorizontalLayout breakButtons(FlexLayout announcerBar) {
        // moved down to the jury section
        return new HorizontalLayout(); // juryDeliberationButtons();
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#createTopBar()
     */
    @Override
    protected void createTopBar() {
        super.createTopBar();
        // this hides the back arrow
        getAppLayout().setMenuVisible(false);
    }

    /**
     * @see app.owlcms.ui.shared.AthleteGridContent#decisionButtons(com.vaadin.flow.component.orderedlayout.HorizontalLayout)
     */
    @Override
    protected HorizontalLayout decisionButtons(FlexLayout announcerBar) {
        // moved down to the jury section
        return new HorizontalLayout(); // juryDecisionButtons();
    }

    @Override
    protected void init() {
        init(3);
    }

    protected void init(int nbj) {
        this.setBoxSizing(BoxSizing.BORDER_BOX);
        this.setSizeFull();
        setTopBarTitle("Jury");
        nbJurors = nbj;
        buildJuryBox(this);
        buildRefereeBox(this);
    }

    private Icon bigIcon(VaadinIcon iconDef, String color) {
        Icon icon = iconDef.create();
        icon.setSize("80%");
        icon.getStyle().set("color", color);
        return icon;
    }

    private void buildJuryBox(VerticalLayout juryContainer) {
        HorizontalLayout topRow = new HorizontalLayout();
        Label juryLabel = new Label(getTranslation("JuryDecisions"));
        H3 labelWrapper = new H3(juryLabel);
        labelWrapper.setWidth("15em");
        Label spacer = new Label();
        spacer.setWidth("3em");
        topRow.add(labelWrapper, juryDeliberationButtons(), juryDecisionButtons(), spacer, jurySelectionButtons());
        topRow.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        buildJuryVoting();
        resetJuryVoting();

        juryContainer.setBoxSizing(BoxSizing.BORDER_BOX);
        juryContainer.setMargin(false);
        juryContainer.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        juryContainer.add(topRow);
        juryContainer.setAlignSelf(Alignment.START, topRow);
        juryContainer.add(juryVotingCenterHorizontally);

    }

    private void buildJuryVoting() {
        // center buttons vertically, spread withing proper width
        juryVotingButtons = new HorizontalLayout();
        juryVotingButtons.setBoxSizing(BoxSizing.BORDER_BOX);
        juryVotingButtons.setJustifyContentMode(JustifyContentMode.EVENLY);
        juryVotingButtons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        juryVotingButtons.setHeight("100%");
        juryVotingButtons.setWidth(getNbJurors() == 3 ? "50%" : "85%");
        juryVotingButtons.getStyle().set("background-color", "black");
        juryVotingButtons.setPadding(false);
        juryVotingButtons.setMargin(false);

        // center the button cluster within page width
        juryVotingCenterHorizontally = new VerticalLayout();
        juryVotingCenterHorizontally.setWidthFull();
        juryVotingCenterHorizontally.setBoxSizing(BoxSizing.BORDER_BOX);
        juryVotingCenterHorizontally.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        juryVotingCenterHorizontally.setPadding(true);
        juryVotingCenterHorizontally.setMargin(true);
        juryVotingCenterHorizontally.setHeight("40%");
        juryVotingCenterHorizontally.getStyle().set("background-color", "black");

        juryVotingCenterHorizontally.add(juryVotingButtons);
        return;
    }

    private void buildRefereeBox(VerticalLayout container) {
        Label label = new Label(getTranslation("RefereeDecisions"));
        H3 labelWrapper = new H3(label);
        labelWrapper.setHeight("5%");

        decisions = new JuryDisplayDecisionElement();
        decisions.getElement().setAttribute("theme", "dark");
        Div decisionWrapper = new Div(decisions);
        decisionWrapper.getStyle().set("width", "50%");
        decisionWrapper.getStyle().set("height", "35%");

        HorizontalLayout refContainer = new HorizontalLayout(decisionWrapper);
        refContainer.setBoxSizing(BoxSizing.BORDER_BOX);
        refContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        refContainer.getStyle().set("background-color", "black");
        refContainer.setHeight("40%");
        refContainer.setWidthFull();
        refContainer.setPadding(true);
        refContainer.setMargin(true);

        container.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        container.add(labelWrapper);
        container.setAlignSelf(Alignment.START, labelWrapper);
        container.add(refContainer);
    }

    private void checkAllVoted() {
        UIEventProcessor.uiAccess(this, uiEventBus, () -> {
            boolean allVoted = true;
            for (int i = 0; i < juryVotes.length; i++) {
                if (juryVotes[i] == null) {
                    allVoted = false;
                    break;
                }
            }

            if (allVoted) {
                juryVotingButtons.removeAll();
                for (int i = 0; i < juryVotes.length; i++) {
                    Icon fullSizeIcon;
                    if (juryVotes[i]) {
                        fullSizeIcon = bigIcon(VaadinIcon.CHECK_CIRCLE, "white");
                    } else {
                        fullSizeIcon = bigIcon(VaadinIcon.CLOSE_CIRCLE, "red");
                    }
                    juryVotingButtons.add(fullSizeIcon);
                    juryIcons[i] = fullSizeIcon;
                }
            }
        });
    }

    private Key getBadKey(int i) {
        switch (i) {
        case 0:
            return Key.DIGIT_2;
        case 1:
            return Key.DIGIT_4;
        case 2:
            return Key.DIGIT_6;
        case 3:
            return Key.DIGIT_8;
        case 4:
            return Key.DIGIT_0;
        default:
            return Key.UNIDENTIFIED;
        }
    }

    private Key getGoodKey(int i) {
        switch (i) {
        case 0:
            return Key.DIGIT_1;
        case 1:
            return Key.DIGIT_3;
        case 2:
            return Key.DIGIT_5;
        case 3:
            return Key.DIGIT_7;
        case 4:
            return Key.DIGIT_9;
        default:
            return Key.UNIDENTIFIED;
        }
    }

    private int getNbJurors() {
        return nbJurors;
    }

    private HorizontalLayout juryDecisionButtons() {
        Button good = new Button(IronIcons.DONE.create(), (e) -> {
            OwlcmsSession.withFop(fop -> {
                fop.getFopEventBus().post(new FOPEvent.JuryDecision(fop.getCurAthlete(), this.getOrigin(), true));
            });
        });
        good.getElement().setAttribute("theme", "success");
        good.setWidth(BUTTON_WIDTH);
        good.setVisible(false);

        Button bad = new Button(IronIcons.CLOSE.create(), (e) -> {
            OwlcmsSession.withFop(fop -> {
                fop.getFopEventBus().post(new FOPEvent.JuryDecision(fop.getCurAthlete(), this.getOrigin(), false));
            });
        });
        bad.getElement().setAttribute("theme", "error");
        bad.setWidth(BUTTON_WIDTH);
        bad.setVisible(false);

        HorizontalLayout decisions = new HorizontalLayout(good, bad);
        return decisions;
    }

    private HorizontalLayout juryDeliberationButtons() {
        breakDialog = new BreakDialog(this, BreakType.JURY, CountdownType.INDEFINITE);
        breakButton = new Button(AvIcons.AV_TIMER.create(), (e) -> {
            breakDialog.open();
        });
        quietBreakButton(true);

        // HorizontalLayout buttons = new HorizontalLayout(stopCompetition,
        // resumeCompetition);
        HorizontalLayout buttons = new HorizontalLayout(breakButton);
        buttons.setAlignItems(FlexComponent.Alignment.BASELINE);
        return buttons;
    }

    private Component jurySelectionButtons() {
        Button three = new Button("3", (e) -> {
            OwlcmsSession.withFop(fop -> {
                this.setNbJurors(3);
            });
        });
        three.setWidth("4em");

        Button five = new Button("5", (e) -> {
            OwlcmsSession.withFop(fop -> {
                this.setNbJurors(5);
            });
        });
        five.setWidth("4em");

        HorizontalLayout selection = new HorizontalLayout(three, five);
        return selection;
    }

    private void resetJuryVoting() {
        UIEventProcessor.uiAccess(UI.getCurrent(), uiEventBus, () -> {
            juryIcons = new Icon[getNbJurors()];
            juryVotes = new Boolean[getNbJurors()];
            for (int i = 0; i < getNbJurors(); i++) {
                final int ix = i;
                Icon nonVotedIcon = bigIcon(VaadinIcon.CIRCLE_THIN, "gray");
                juryIcons[ix] = nonVotedIcon;
                juryVotes[ix] = null;
                juryVotingButtons.add(juryIcons[ix], nonVotedIcon);
                UI.getCurrent().addShortcutListener(() -> {
                    Icon votedIcon = bigIcon(VaadinIcon.CIRCLE, "gray");
                    juryVotingButtons.replace(juryIcons[ix], votedIcon);
                    juryIcons[ix] = votedIcon;
                    juryVotes[ix] = true;
                    checkAllVoted();
                }, getGoodKey(i));
                UI.getCurrent().addShortcutListener(() -> {
                    Icon votedIcon = bigIcon(VaadinIcon.CIRCLE, "gray");
                    juryVotingButtons.replace(juryIcons[ix], votedIcon);
                    juryIcons[ix] = votedIcon;
                    juryVotes[ix] = false;
                    checkAllVoted();
                }, getBadKey(i));
            }
        });
    }

    private void setNbJurors(int nbJurors) {
        this.removeAll();
        init(nbJurors);
    }

}
