/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.displays.topathletes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import app.owlcms.data.athlete.Athlete;
import app.owlcms.data.athlete.Gender;
import app.owlcms.data.athlete.LiftDefinition.Changes;
import app.owlcms.data.athlete.LiftInfo;
import app.owlcms.data.athlete.XAthlete;
import app.owlcms.data.competition.Competition;
import app.owlcms.fieldofplay.FieldOfPlay;
import app.owlcms.i18n.Translator;
import app.owlcms.init.OwlcmsFactory;
import app.owlcms.init.OwlcmsSession;
import app.owlcms.ui.lifting.UIEventProcessor;
import app.owlcms.ui.parameters.DarkModeParameters;
import app.owlcms.ui.shared.RequireLogin;
import app.owlcms.ui.shared.SafeEventBusRegistration;
import app.owlcms.uievents.BreakDisplay;
import app.owlcms.uievents.UIEvent;
import app.owlcms.utils.LoggerUtils;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

/**
 * Class TopSinclair
 *
 * Show athlete lifting order
 *
 */
@SuppressWarnings("serial")
@Tag("topsinclair-template")
@JsModule("./components/TopSinclair.js")
@Route("displays/topsinclair")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Push
public class TopSinclair extends PolymerTemplate<TopSinclair.TopSinclairModel> implements DarkModeParameters,
        SafeEventBusRegistration, UIEventProcessor, BreakDisplay, HasDynamicTitle, RequireLogin, PageConfigurator {

    /**
     * LiftingOrderModel
     *
     * Vaadin Flow propagates these variables to the corresponding Polymer template JavaScript properties. When the JS
     * properties are changed, a "propname-changed" event is triggered.
     * {@link Element.#addPropertyChangeListener(String, String, com.vaadin.flow.dom.PropertyChangeListener)}
     *
     */
    public interface TopSinclairModel extends TemplateModel {

        String getFullName();

        Boolean isHidden();

        Boolean isWideTeamNames();

        void setFullName(String lastName);

        void setHidden(boolean b);

        void setWideTeamNames(boolean b);
    }

    final private static Logger logger = (Logger) LoggerFactory.getLogger(TopSinclair.class);
    final private static Logger uiEventLogger = (Logger) LoggerFactory.getLogger("UI" + logger.getName());

    static {
        logger.setLevel(Level.INFO);
        uiEventLogger.setLevel(Level.INFO);
    }

    private EventBus uiEventBus;

    JsonArray sattempts;
    JsonArray cattempts;
    private double topManSinclair;
    private double topWomanSinclair;
    private List<Athlete> sortedMen;
    private List<Athlete> sortedWomen;
    private boolean darkMode;
    private ContextMenu contextMenu;
    private Location location;
    private UI locationUI;

    /**
     * Instantiates a new results board.
     */
    public TopSinclair() {
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addLink("shortcut icon", "frontend/images/owlcms.ico");
        settings.addFavIcon("icon", "frontend/images/logo.png", "96x96");
        settings.setViewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes");
    }

    @Override
    public void doBreak() {
        OwlcmsSession.withFop(fop -> UIEventProcessor.uiAccess(this, uiEventBus, () -> {
            // just update the display
            doUpdate(fop.getCurAthlete(), null);
        }));
    }

    public void doUpdate(Competition competition) {
        this.getElement().callJsFunction("reset");

        // create copies because we want to change the list
        setSortedMen(competition.getGlobalSinclairRanking(Gender.M));
        setSortedWomen(competition.getGlobalSinclairRanking(Gender.F));

        topManSinclair = 0.0D;
        List<Athlete> sortedMen2 = getSortedMen();
        if (sortedMen2 != null && !sortedMen2.isEmpty()) {
            ListIterator<Athlete> iterMen = sortedMen2.listIterator();
            while (iterMen.hasNext()) {
                Athlete curMan = iterMen.next();
                Double curSinclair = (curMan.getAttemptsDone() <= 3 ? curMan.getSinclairForDelta()
                        : curMan.getSinclair());
                if (curSinclair <= 0) {
                    iterMen.remove();
                } else {
                    if (curSinclair > topManSinclair) {
                        topManSinclair = curSinclair;
                    }
                }
            }
            int minMen = java.lang.Math.min(5, sortedMen2.size());
            setSortedMen(sortedMen2.subList(0, minMen));
        } else {
            setSortedMen(new ArrayList<Athlete>());
        }
//        Athlete topMan = (getSortedMen().size() > 0 ? getSortedMen().get(0) : null);
//        topManSinclair = (topMan != null ? topMan.getSinclairForDelta() : 999.0D);

        topWomanSinclair = 0.0D;
        List<Athlete> sortedWomen2 = getSortedWomen();
        if (sortedWomen2 != null && !sortedWomen2.isEmpty()) {
            ListIterator<Athlete> iterWomen = sortedWomen2.listIterator();
            while (iterWomen.hasNext()) {
                Athlete curWoman = iterWomen.next();
                Double curSinclair = (curWoman.getAttemptsDone() <= 3 ? curWoman.getSinclairForDelta()
                        : curWoman.getSinclair());
                if (curSinclair <= 0) {
                    iterWomen.remove();
                } else {
                    if (curSinclair > topWomanSinclair) {
                        topWomanSinclair = curSinclair;
                    }
                }
            }
            int minWomen = java.lang.Math.min(5, getSortedWomen().size());
            setSortedWomen(getSortedWomen().subList(0, minWomen));
        } else {
            setSortedWomen(new ArrayList<Athlete>());
        }

        updateBottom(getModel());
    }

    public void getAthleteJson(Athlete a, JsonObject ja, Gender g, int needed) {
        String category;
        category = a.getCategory() != null ? a.getCategory().getName() : "";
        ja.put("fullName", a.getFullName() != null ? a.getFullName() : "");
        ja.put("teamName", a.getTeam() != null ? a.getTeam() : "");
        ja.put("yearOfBirth", a.getYearOfBirth() != null ? a.getYearOfBirth().toString() : "");
        Integer startNumber = a.getStartNumber();
        ja.put("startNumber", (startNumber != null ? startNumber.toString() : ""));
        ja.put("category", category != null ? category : "");
        getAttemptsJson(a);
        ja.put("sattempts", sattempts);
        ja.put("cattempts", cattempts);
        ja.put("total", formatInt(a.getTotal()));
        ja.put("bw", String.format("%.2f", a.getBodyWeight()));
        ja.put("sinclair", String.format("%.3f", (a.getAttemptsDone() <= 3 ? a.getSinclairForDelta()
                : a.getSinclair())));
        ja.put("needed", formatInt(needed));
    }

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
        return getTranslation("Scoreboard.TopSinclair");
    }

    @Override
    public boolean isDarkMode() {
        return this.darkMode;
    }

    @Override
    public boolean isIgnoreFopFromURL() {
        return true;
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

//    private String formatAttempt(Integer attemptNo) {
//        return Translator.translate("AttemptBoard_attempt_number", (attemptNo % 3) + 1);
//    }

    @Subscribe
    public void slaveGlobalRankingUpdated(UIEvent.GlobalRankingUpdated e) {
        uiLog(e);
        Competition competition = Competition.getCurrent();

        UIEventProcessor.uiAccess(this, uiEventBus, () -> {
            doUpdate(competition);
        });
    }

    @Subscribe
    public void slaveStartLifting(UIEvent.StartLifting e) {
        uiLog(e);
        UIEventProcessor.uiAccess(this, uiEventBus, e, () -> {
            getModel().setHidden(false);
            this.getElement().callJsFunction("reset");
        });
    }

    public void uiLog(UIEvent e) {
        if (e == null) {
            uiEventLogger.debug("### {} {}", this.getClass().getSimpleName(), LoggerUtils.whereFrom());
        } else {
            uiEventLogger.debug("### {} {} {}", this.getClass().getSimpleName(), e.getClass().getSimpleName(),
                    LoggerUtils.whereFrom());
        }
    }

    protected void doEmpty() {
        logger.trace("doEmpty");
        this.getModel().setHidden(true);
    }

    protected void doUpdate(Athlete a, UIEvent e) {
        logger.debug("doUpdate {} {}", a, a != null ? a.getAttemptsDone() : null);
        UIEventProcessor.uiAccess(this, uiEventBus, e, () -> {
            TopSinclairModel model = getModel();
            if (a != null) {
                model.setFullName(getTranslation("Scoreboard.TopSinclair"));
                updateBottom(model);
            }
        });
    }

    /**
     * Compute Json string ready to be used by web component template
     *
     * CSS classes are pre-computed and passed along with the values; weights are formatted.
     *
     * @param a
     * @return json string with nested attempts values
     */
    protected void getAttemptsJson(Athlete a) {
        sattempts = Json.createArray();
        cattempts = Json.createArray();
        XAthlete x = new XAthlete(a);
        Integer liftOrderRank = x.getLiftOrderRank();
        Integer curLift = x.getAttemptsDone();
        int ix = 0;
        for (LiftInfo i : x.getRequestInfoArray()) {
            JsonObject jri = Json.createObject();
            String stringValue = i.getStringValue();
            boolean notDone = x.getAttemptsDone() < 6;
            String blink = (notDone ? " blink" : "");

            jri.put("goodBadClassName", "veryNarrow empty");
            jri.put("stringValue", "");
            if (i.getChangeNo() >= 0) {
                String trim = stringValue != null ? stringValue.trim() : "";
                switch (Changes.values()[i.getChangeNo()]) {
                case ACTUAL:
                    if (!trim.isEmpty()) {
                        if (trim.contentEquals("-") || trim.contentEquals("0")) {
                            jri.put("goodBadClassName", "veryNarrow fail");
                            jri.put("stringValue", "-");
                        } else {
                            boolean failed = stringValue.startsWith("-");
                            jri.put("goodBadClassName", failed ? "veryNarrow fail" : "veryNarrow good");
                            jri.put("stringValue", formatKg(stringValue));
                        }
                    }
                    break;
                default:
                    if (stringValue != null && !trim.isEmpty()) {
                        String highlight = i.getLiftNo() == curLift && liftOrderRank == 1 ? (" current" + blink)
                                : (i.getLiftNo() == curLift && liftOrderRank == 2) ? " next" : "";
                        jri.put("goodBadClassName", "veryNarrow request");
                        if (notDone) {
                            jri.put("className", highlight);
                        }
                        jri.put("stringValue", stringValue);
                    }
                    break;
                }
            }

            if (ix < 3) {
                sattempts.set(ix, jri);
            } else {
                cattempts.set(ix % 3, jri);
            }
            ix++;
        }
    }

    /*
     * @see com.vaadin.flow.component.Component#onAttach(com.vaadin.flow.component. AttachEvent)
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        logger.debug("onAttach start");
        setDarkMode(this, isDarkMode(), false);
        setWide(false);
        setTranslationMap();
        for (FieldOfPlay fop : OwlcmsFactory.getFOPs()) {
            // we listen on all the uiEventBus.
            uiEventBus = uiEventBusRegister(this, fop);
        }
        Competition competition = Competition.getCurrent();
        doUpdate(competition);
        logger.debug("onAttach end");
    }

    protected void setTranslationMap() {
        JsonObject translations = Json.createObject();
        Enumeration<String> keys = Translator.getKeys();
        while (keys.hasMoreElements()) {
            String curKey = keys.nextElement();
            if (curKey.startsWith("Scoreboard.")) {
                translations.put(curKey.replace("Scoreboard.", ""), Translator.translate(curKey));
            }
        }
        this.getElement().setPropertyJson("t", translations);
    }

    private String formatInt(Integer total) {
        if (total == null || total == 0) {
            return "-";
        } else if (total == -1) {
            return "inv.";// invited lifter, not eligible.
        } else if (total < 0) {
            return "(" + Math.abs(total) + ")";
        } else {
            return total.toString();
        }
    }

    private String formatKg(String total) {
        return (total == null || total.trim().isEmpty()) ? "-"
                : (total.startsWith("-") ? "(" + total.substring(1) + ")" : total);
    }

    private JsonValue getAthletesJson(List<Athlete> list2, boolean overrideTeamWidth) {
        JsonArray jath = Json.createArray();
        int athx = 0;
        List<Athlete> list3 = list2 != null ? Collections.unmodifiableList(list2) : Collections.emptyList();
        if (overrideTeamWidth) {
            // when we are called for the second time, and there was a wide team in the top section.
            // we use the wide team setting for the remaining sections.
            setWide(false);
        }

        for (Athlete a : list3) {
            JsonObject ja = Json.createObject();
            Gender curGender = a.getGender();

            int needed;
            if (curGender == Gender.F) {
                needed = (int) Math
                        .round(Math.ceil((topWomanSinclair - a.getSinclairForDelta()) / a.getSinclairFactor()));
            } else {
                needed = (int) Math
                        .round(Math.ceil((topManSinclair - a.getSinclairForDelta()) / a.getSinclairFactor()));
            }
            getAthleteJson(a, ja, curGender, needed);
            String team = a.getTeam();
            if (team != null && team.length() > Competition.SHORT_TEAM_LENGTH) {
                setWide(true);
            }
            jath.set(athx, ja);
            athx++;
        }
        return jath;
    }

    @SuppressWarnings("unused")
    private Object getOrigin() {
        return this;
    }

    private List<Athlete> getSortedMen() {
        return this.sortedMen;
    }

    private List<Athlete> getSortedWomen() {
        return this.sortedWomen;
    }

    private void setSortedMen(List<Athlete> sortedMen) {
        this.sortedMen = sortedMen;
        logger.debug("sortedMen = {} -- {}", getSortedMen(), LoggerUtils.whereFrom());
    }

    private void setSortedWomen(List<Athlete> sortedWomen) {
        this.sortedWomen = sortedWomen;
        logger.debug("sortedWomen = {} -- {}", getSortedWomen(), LoggerUtils.whereFrom());
    }

    private void setWide(boolean b) {
        getModel().setWideTeamNames(b);
    }

    private void updateBottom(TopSinclairModel model) {
        getModel().setFullName(getTranslation("Scoreboard.TopSinclair"));
        List<Athlete> sortedMen2 = getSortedMen();
        this.getElement().setProperty("topSinclairMen",
                sortedMen2 != null && sortedMen2.size() > 0 ? getTranslation("Scoreboard.TopSinclairMen") : "");
        this.getElement().setPropertyJson("sortedMen", getAthletesJson(sortedMen2, true));

        List<Athlete> sortedWomen2 = getSortedWomen();
        this.getElement().setProperty("topSinclairWomen",
                sortedWomen2 != null && sortedWomen2.size() > 0 ? getTranslation("Scoreboard.TopSinclairWomen") : "");
        this.getElement().setPropertyJson("sortedWomen", getAthletesJson(sortedWomen2, false));

        logger.debug("updateBottom {} {}", sortedWomen2, sortedMen2);
    }

}
