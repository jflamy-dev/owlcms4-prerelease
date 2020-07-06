/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.components.elements;

import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

import app.owlcms.publicresults.DecisionReceiverServlet;
import app.owlcms.publicresults.TimerReceiverServlet;
import app.owlcms.uievents.BreakTimerEvent;
import app.owlcms.uievents.DecisionEvent;
import app.owlcms.uievents.TimerEvent;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * ExplicitDecision display element.
 */
@Tag("decision-element")
@JsModule("./components/DecisionElement.js")
public class DecisionElement extends PolymerTemplate<DecisionElement.DecisionModel> {

    /**
     * The Interface DecisionModel.
     */
    public interface DecisionModel extends TemplateModel {

        boolean isEnabled();

        boolean isJury();

        boolean isPublicFacing();

        void setEnabled(boolean b);

        void setJury(boolean juryMode);

        void setPublicFacing(boolean publicFacing);
    }

    final private static Logger logger = (Logger) LoggerFactory.getLogger(DecisionElement.class);
    final private static Logger uiEventLogger = (Logger) LoggerFactory.getLogger("UI" + logger.getName());

    static {
        logger.setLevel(Level.INFO);
        uiEventLogger.setLevel(Level.INFO);
    }

    protected EventBus uiEventBus;
    protected EventBus fopEventBus;
    private UI ui;
    public DecisionElement() {
    }

    public boolean isPublicFacing() {
        return Boolean.TRUE.equals(getModel().isPublicFacing());
    }

    public void setJury(boolean juryMode) {
        getModel().setJury(false);
    }

    public void setPublicFacing(boolean publicFacing) {
        getModel().setPublicFacing(publicFacing);
    }

    @Subscribe
    public void slaveDecision(DecisionEvent de) {
        logger.warn("DecisionElement DecisionEvent {} {}",de.getEventType(), System.identityHashCode(de));
        if (ui == null || ui.isClosing()) return;
        ui.access(() -> {
            if (de.isBreak()) {
                logger.warn("break: slaveDecision disable");
                getModel().setEnabled(false);
            } else {
                switch (de.getEventType()) {
                case DOWN_SIGNAL:
                    this.getElement().callJsFunction("showDown", false, false);
                    break;
                case FULL_DECISION:
                    logger.warn("calling full decision");
                    this.getElement().callJsFunction("showDecisions", false, de.getDecisionLight1(), de.getDecisionLight2(),
                            de.getDecisionLight3());
                    getModel().setEnabled(false);
                    break;
                case RESET:
                    logger.warn("calling reset");
                    getElement().callJsFunction("reset", false);
                    break;
                default:
                    logger.error("unknown decision event type {}", de.getEventType());
                    break;
                }
            }
        });
    }

    @Subscribe
    public void slaveStartTimer(TimerEvent.StartTime e) {
        if (ui == null || ui.isClosing()) return;
        ui.access(() -> {
            getModel().setEnabled(true);
        });
    }

    @Subscribe
    public void slaveStopTimer(TimerEvent.StopTime e) {
        if (ui == null || ui.isClosing()) return;
        ui.access(() -> {
            getModel().setEnabled(true);
        });
    }

    @Subscribe
    public void slaveStartBreakTimer(BreakTimerEvent.BreakStart e) {
        if (ui == null || ui.isClosing()) return;
        ui.access(() -> {
            getModel().setEnabled(true);
        });
    }

    @Subscribe
    public void slaveStopBreakTimer(BreakTimerEvent.BreakPaused e) {
        if (ui == null || ui.isClosing()) return;
        ui.access(() -> {
            getModel().setEnabled(true);
        });
    }
    
    @Subscribe
    public void slaveStopBreakTimer(BreakTimerEvent.BreakDone e) {
        if (ui == null || ui.isClosing()) return;
        ui.access(() -> {
            getModel().setEnabled(true);
        });
    }
    
    protected Object getOrigin() {
        // we use the identity of our parent AttemptBoard or AthleteFacingAttemptBoard
        // to identify
        // our actions.
        return this.getParent().get();
    }

    /*
     * @see com.vaadin.flow.component.Component#onAttach(com.vaadin.flow.component. AttachEvent)
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        this.ui = attachEvent.getUI();
        init();

        DecisionReceiverServlet.getEventBus().register(this);
        TimerReceiverServlet.getEventBus().register(this);
    }


    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        this.ui = null;
        try {
            DecisionReceiverServlet.getEventBus().unregister(this);
        } catch (Exception e) {
        }
        try {
            TimerReceiverServlet.getEventBus().unregister(this);
        } catch (Exception e) {
        }
    }

    private void init() {
        DecisionModel model = getModel();
        model.setPublicFacing(true);
    }
}
