/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.components.elements;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;

import app.owlcms.fieldofplay.ProxyBreakTimer;
import app.owlcms.init.OwlcmsSession;
import app.owlcms.uievents.UIEvent;
import app.owlcms.utils.LoggerUtils;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * Countdown timer element.
 */
@SuppressWarnings("serial")
public class BreakTimerElement extends TimerElement {

    final private Logger logger = (Logger) LoggerFactory.getLogger(BreakTimerElement.class);
    final private Logger uiEventLogger = (Logger) LoggerFactory.getLogger("UI" + logger.getName());
    private String parentName = "";

    {
        logger.setLevel(Level.INFO);
        uiEventLogger.setLevel(Level.INFO);
    }

    /**
     * Instantiates a new timer element.
     */
    public BreakTimerElement() {
        // logger./**/warn(LoggerUtils.stackTrace());
    }

    @Override
    public void clientFinalWarning() {
        // ignored
    }

    @Override
    public void clientInitialWarning() {
        // ignored
    }

    /**
     * Set the remaining time when the timer element has been hidden for a long time.
     */
    @Override
    @ClientCallable
    public void clientSyncTime() {
        logger.info("break timer element fetching time");
        OwlcmsSession.withFop(fop -> {
            ProxyBreakTimer breakTimer = fop.getBreakTimer();
            doSetTimer(breakTimer.isIndefinite() ? null : breakTimer.liveTimeRemaining());
        });
        return;
    }

    /**
     * Timer stopped
     *
     * @param remaining Time the remaining time
     */
    @Override
    @ClientCallable
    public void clientTimeOver() {
        logger.info("break time over");
        OwlcmsSession.withFop(fop -> {
            ProxyBreakTimer breakTimer = fop.getBreakTimer();
            if (!breakTimer.isIndefinite()) {
                fop.getBreakTimer().timeOver(this);
            }
        });
    }

    /**
     * Timer stopped
     *
     * @param remaining Time the remaining time
     */
    @Override
    @ClientCallable
    public void clientTimerStopped(double remainingTime) {
        logger.trace("timer stopped from client" + remainingTime);
    }

    public void setParent(String s) {
        parentName = s;
    }

    @Subscribe
    public void slaveBreakDone(UIEvent.BreakDone e) {
        uiEventLogger.debug("&&& break done {} {}", parentName, e.getOrigin());
        doStopTimer();
    }

    @Subscribe
    public void slaveBreakPause(UIEvent.BreakPaused e) {
        uiEventLogger.debug("&&& breakTimer pause {} {}", parentName, e.getOrigin());
        doStopTimer();
    }

    @Subscribe
    public void slaveBreakSet(UIEvent.BreakSetTime e) {
        Integer milliseconds;
        if (e.getEnd() != null) {
            milliseconds = (int) LocalDateTime.now().until(e.getEnd(), ChronoUnit.MILLIS);
        } else {
            milliseconds = e.isIndefinite() ? null : e.getTimeRemaining();
            uiEventLogger.debug("&&& breakTimer set {} {} {} {}", parentName, formatDuration(milliseconds),
                    e.isIndefinite(), LoggerUtils.whereFrom());
        }
        doSetTimer(milliseconds);
    }

    @Subscribe
    public void slaveBreakStart(UIEvent.BreakStarted e) {
        if (e.isDisplayToggle()) {
            return;
        }
        Integer tr = e.isIndefinite() ? null : e.getMillis();
        uiEventLogger.debug("&&& breakTimer start {} {} {} {}", parentName, tr, e.getOrigin(), LoggerUtils.whereFrom());
        doStartTimer(tr, true); // true means "silent".
    }

    /*
     * (non-Javadoc)
     *
     * @see app.owlcms.displays.attemptboard.TimerElement#init()
     */
    @Override
    protected void init() {
        super.init();
        setSilent(true);
        getModel().setSilent(true); // do not emit sounds
    }

    /*
     * @see com.vaadin.flow.component.Component#onAttach(com.vaadin.flow.component. AttachEvent)
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        init();
        OwlcmsSession.withFop(fop -> {
            // sync with current status of FOP
            ProxyBreakTimer breakTimer = fop.getBreakTimer();
            if (breakTimer.isRunning()) {
                if (breakTimer.isIndefinite()) {
                    doStartTimer(null, fop.isEmitSoundsOnServer());
                } else {
                    doStartTimer(breakTimer.liveTimeRemaining(), fop.isEmitSoundsOnServer());
                }
            } else {
                if (breakTimer.isIndefinite()) {
                    doSetTimer(null);
                } else {
                    doSetTimer(breakTimer.getTimeRemainingAtLastStop());
                }
            }
            // we listen on uiEventBus; this method ensures we stop when detached.
            uiEventBusRegister(this, fop);
        });
    }

    private String formatDuration(Integer milliseconds) {
        return milliseconds != null ? DurationFormatUtils.formatDurationHMS(milliseconds) : "null";
    }

}
