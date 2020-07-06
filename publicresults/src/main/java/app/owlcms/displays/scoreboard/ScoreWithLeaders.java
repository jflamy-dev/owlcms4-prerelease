/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.displays.scoreboard;

import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import app.owlcms.components.elements.AthleteTimerElement;
import app.owlcms.components.elements.BreakTimerElement;
import app.owlcms.components.elements.DecisionElement;
import app.owlcms.components.elements.unload.UnloadObserver;
import app.owlcms.i18n.Translator;
import app.owlcms.publicresults.DecisionReceiverServlet;
import app.owlcms.publicresults.TimerReceiverServlet;
import app.owlcms.publicresults.UpdateReceiverServlet;
import app.owlcms.ui.parameters.DarkModeParameters;
import app.owlcms.ui.parameters.QueryParameterReader;
import app.owlcms.uievents.BreakTimerEvent;
import app.owlcms.uievents.BreakType;
import app.owlcms.uievents.DecisionEvent;
import app.owlcms.uievents.DecisionEventType;
import app.owlcms.uievents.UpdateEvent;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import elemental.json.Json;
import elemental.json.impl.JreJsonFactory;

/**
 * Class Scoreboard
 *
 * Show athlete 6-attempt results
 *
 */
@Tag("scoreleader-template")
@JsModule("./components/ScoreWithLeaders.js")
@Route("displays/scoreleader")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Push
public class ScoreWithLeaders extends PolymerTemplate<ScoreWithLeaders.ScoreboardModel>
        implements QueryParameterReader, DarkModeParameters, HasDynamicTitle {

    /**
     * ScoreboardModel
     *
     * Vaadin Flow propagates these variables to the corresponding Polymer template JavaScript properties. When the JS
     * properties are changed, a "propname-changed" event is triggered.
     * {@link Element.#addPropertyChangeListener(String, String, com.vaadin.flow.dom.PropertyChangeListener)}
     *
     */
    public interface ScoreboardModel extends TemplateModel {
        String getAttempt();

        String getCategoryName();

        String getCompetitionName();

        String getFullName();

        Integer getStartNumber();

        String getTeamName();

        Integer getWeight();

        Boolean isHidden();

        Boolean isWideTeamNames();

        void setAttempt(String formattedAttempt);

        void setCategoryName(String categoryName);

        void setCompetitionName(String competitionName);

        void setFullName(String lastName);

        void setGroupName(String name);

        void setHidden(boolean b);

        void setLiftsDone(String formattedDone);

        void setStartNumber(Integer integer);

        void setTeamName(String teamName);

        void setWeight(Integer weight);

        void setWideTeamNames(boolean b);
    }

    final private static Logger logger = (Logger) LoggerFactory.getLogger(ScoreWithLeaders.class);
    final private static Logger uiEventLogger = (Logger) LoggerFactory.getLogger("UI" + logger.getName());

    static {
        logger.setLevel(Level.INFO);
        uiEventLogger.setLevel(Level.INFO);
    }

    @Id("timer")
    private AthleteTimerElement timer; // Flow creates it

    @Id("breakTimer")
    private BreakTimerElement breakTimer; // Flow creates it

    @Id("decisions")
    private DecisionElement decisions; // Flow creates it

    private boolean darkMode;
    private ContextMenu contextMenu;
    private Location location;
    private UI locationUI;
    private UI ui;
    private String fopName;
    private boolean needReset = false;

    /**
     * Instantiates a new results board.
     */
    public ScoreWithLeaders() {
        setDarkMode(true);
    }

//    private void doBreak(UpdateEvent bte) {
//        if (ui == null || ui.isClosing())
//            return;
//        ui.access(() -> {
//            logger.warn("starting break");
//            ScoreboardModel model = getModel();
//            BreakType breakType = bte.getBreakType();
//            String groupName = bte.getGroupName();
//            model.setFullName(Translator.translate("Group_number", groupName) + " &ndash; " + inferMessage(breakType));
//            model.setTeamName("");
//            model.setAttempt("");
//            model.setHidden(false);
//
////            updateBottom(model, computeLiftType(fop.getCurAthlete()));
//            logger.warn("$$$ scoreWithLeaders calling doBreak()");
//            this.getElement().callJsFunction("doBreak");
//        });
//    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public UI getLocationUI() {
        return this.locationUI;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("ScoreboardWLeadersTitle");
    }

    @Override
    public boolean isDarkMode() {
        return darkMode;
    }

    @Override
    public boolean isIgnoreGroupFromURL() {
        return true;
    }

    @Override
    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    @Override
    public void setDarkMode(boolean dark) {
        this.darkMode = dark;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void setLocationUI(UI locationUI) {
        this.locationUI = locationUI;
    }

    @Subscribe
    public void slaveGlobalRankingUpdated(UpdateEvent e) {
        logger.warn("received UpdateEventt {}", e);
        ui.access(() -> {
            String athletes = e.getAthletes();
            String leaders = e.getLeaders();
            String translationMap = e.getTranslationMap();

            JreJsonFactory jreJsonFactory = new JreJsonFactory();
            this.getElement().setPropertyJson("leaders",
                    leaders != null ? jreJsonFactory.parse(leaders) : Json.createNull());
            this.getElement().setPropertyJson("athletes",
                    athletes != null ? jreJsonFactory.parse(athletes) : Json.createNull());
            this.getElement().setPropertyJson("t",
                    translationMap != null ? jreJsonFactory.parse(translationMap) : Json.createNull());

            getModel().setCompetitionName(e.getCompetitionName());
            getModel().setAttempt(e.getAttempt());
            getModel().setFullName(e.getFullName());
            String groupName = e.getGroupName();
            getModel().setGroupName(groupName);
            getModel().setHidden(e.getHidden());
            getModel().setStartNumber(e.getStartNumber());
            getModel().setTeamName(e.getTeamName());
            getModel().setWeight(e.getWeight());
            getModel().setCategoryName(e.getCategoryName());
            getModel().setWideTeamNames(e.getWideTeamNames());
            String liftsDone = e.getLiftsDone();
            getModel().setLiftsDone(liftsDone);

            if ("BREAK".equals(e.getFopState()) && e.getBreakType() == BreakType.GROUP_DONE) {
                logger.warn("group is done");
                doDone(e.getFullName());
                needReset = true;
            } else if ("BREAK".equals(e.getFopState())) {
                logger.warn("in a break {}", e.getBreakType());
                this.getElement().callJsFunction("doBreak");
                needReset = true;
            } else if (!needReset) {
            } else {
                logger.warn("resetting becase of ranking update");
                this.getElement().callJsFunction("reset");
                needReset = false;
            }
        });
    }

    @Subscribe
    public void slaveDecisionEvent(DecisionEvent e) {
        logger.warn("received DecisionEvent {}", e);
        DecisionEventType eventType = e.getEventType();
        switch (eventType) {
        case DOWN_SIGNAL:
            if (ui == null || ui.isClosing())
                return;
            ui.access(() -> {
                getModel().setHidden(false);
                this.getElement().callJsFunction("down");
            });
            break;
        case RESET:
//            if (e.isDone()) {
//                doDone(e.getGroupName());
//            } else 
        {
            if (ui == null || ui.isClosing())
                return;
            ui.access(() -> {
                getModel().setHidden(false);
                this.getElement().callJsFunction("reset");
            });
        }
            ;
            break;
        case FULL_DECISION:
            if (ui == null || ui.isClosing())
                return;
            ui.access(() -> {
                getModel().setHidden(false);
                this.getElement().callJsFunction("refereeDecision");
            });
            break;
        default:
            break;
        }
    }

    @Subscribe
    public void slaveBreakDone(BreakTimerEvent.BreakDone e) {
        logger.warn("swl BreakDone");
        this.getElement().callJsFunction("reset");
        needReset = false;
    }

    protected void doEmpty() {
        this.getModel().setHidden(true);
    }

    /** @see com.vaadin.flow.component.Component#onAttach(com.vaadin.flow.component.AttachEvent) */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        logger.warn("registering ScoreWithLeaders {}", System.identityHashCode(this));
        UpdateReceiverServlet.getEventBus().register(this);
        DecisionReceiverServlet.getEventBus().register(this);
        TimerReceiverServlet.getEventBus().register(this);

        UnloadObserver unloadObserver = UnloadObserver.get(false);
        unloadObserver.addUnloadListener((e) -> {
            logger.warn("closing {}: unregister {} from event busses", e.getSource(), this);
            try {
                UpdateReceiverServlet.getEventBus().unregister(this);
                DecisionReceiverServlet.getEventBus().unregister(this);
                TimerReceiverServlet.getEventBus().unregister(this);
            } catch (Exception ex) {
            }
            UnloadObserver.remove();
        });
        ui = UI.getCurrent();
        ui.add(unloadObserver);

        setDarkMode(this, isDarkMode(), false);
        UpdateEvent initEvent = UpdateReceiverServlet.sync(getFopName());
        if (initEvent != null) {
            slaveGlobalRankingUpdated(initEvent);
            timer.slaveOrderUpdated(initEvent);
        } else {
            getModel().setFullName("Waiting for update from competition site.");
            getModel().setGroupName("");
            getElement().callJsFunction("doDone");
        }
    }

    /** @see com.vaadin.flow.component.Component#onDetach(com.vaadin.flow.component.DetachEvent) */
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        UpdateReceiverServlet.getEventBus().unregister(this);
        DecisionReceiverServlet.getEventBus().unregister(this);
        TimerReceiverServlet.getEventBus().unregister(this);
    }

    /** @see app.owlcms.ui.parameters.QueryParameterReader#setFopName(java.lang.String) */
    @Override
    public void setFopName(String fopName) {
        this.fopName = fopName;
    }

    private String getFopName() {
        return fopName;
    }

    @SuppressWarnings("unused")
    private String inferMessage(BreakType bt) {
        if (bt == null) {
            return Translator.translate("PublicMsg.CompetitionPaused");
        }
        switch (bt) {
        case FIRST_CJ:
            return Translator.translate("PublicMsg.TimeBeforeCJ");
        case FIRST_SNATCH:
            return Translator.translate("PublicMsg.TimeBeforeSnatch");
        case BEFORE_INTRODUCTION:
            return Translator.translate("PublicMsg.BeforeIntroduction");
        case DURING_INTRODUCTION:
            return Translator.translate("PublicMsg.DuringIntroduction");
        case TECHNICAL:
            return Translator.translate("PublicMsg.CompetitionPaused");
        case JURY:
            return Translator.translate("PublicMsg.JuryDeliberation");
        case GROUP_DONE:
            return Translator.translate("PublicMsg.GroupDone");
        default:
            return "";
        }
    }

    private void doDone(String str) {
        if (str == null) {
            doEmpty();
        } else {
//            getModel().setFullName(getTranslation("Group_number_results", groupName));
            getModel().setFullName(str);
            this.getElement().callJsFunction("groupDone");
        }
    }

}