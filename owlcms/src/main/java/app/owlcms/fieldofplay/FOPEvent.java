/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.fieldofplay;

import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.LoggerFactory;

import app.owlcms.data.athlete.Athlete;
import app.owlcms.data.group.Group;
import app.owlcms.ui.shared.BreakManagement.CountdownType;
import app.owlcms.uievents.BreakType;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * The subclasses of FOPEvent are all the events that can take place on the field of play.
 *
 * @author owlcms
 */
public class FOPEvent {

    /**
     * Class BarbellOrPlatesChanged
     */
    static public class BarbellOrPlatesChanged extends FOPEvent {

        public BarbellOrPlatesChanged(Object origin) {
            super(origin);
        }
    }

    /**
     * Class BreakPaused.
     */
    static public class BreakPaused extends FOPEvent {

        public BreakPaused(Object origin) {
            super(origin);
        }
    }

    /**
     * Class BreakStarted.
     */
    static public class BreakStarted extends FOPEvent {

        private BreakType breakType;

        private CountdownType countdownType;

        private Integer timeRemaining;

        private LocalDateTime targetTime;

        public BreakStarted(BreakType bType, CountdownType cType, Integer timeRemaining, LocalDateTime targetTime,
                Object origin) {
            super(origin);
            this.setBreakType(bType);
            this.setCountdownType(cType);
            this.timeRemaining = timeRemaining;
            this.targetTime = targetTime;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            BreakStarted other = (BreakStarted) obj;
            return breakType == other.breakType && countdownType == other.countdownType
                    && Objects.equals(targetTime, other.targetTime)
                    && Objects.equals(timeRemaining, other.timeRemaining);
        }

        public BreakType getBreakType() {
            return breakType;
        }

        public CountdownType getCountdownType() {
            return countdownType;
        }

        public LocalDateTime getTargetTime() {
            return targetTime;
        }

        public Integer getTimeRemaining() {
            return timeRemaining;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(breakType, countdownType, targetTime, timeRemaining);
            return result;
        }

        public boolean isIndefinite() {
            if (countdownType != null) {
                return countdownType == CountdownType.INDEFINITE;
            } else {
                return breakType == BreakType.JURY || breakType == BreakType.TECHNICAL
                        || breakType == BreakType.GROUP_DONE;
            }
        }

        public void setBreakType(BreakType breakType) {
            this.breakType = breakType;
        }

        public void setCountdownType(CountdownType countdownType) {
            this.countdownType = countdownType;
        }

        public void setTargetTime(LocalDateTime targetTime) {
            this.targetTime = targetTime;
        }

        public void setTimeRemaining(Integer timeRemaining) {
            this.timeRemaining = timeRemaining;
        }

        @Override
        public String toString() {
            return "BreakStarted [breakType=" + breakType + ", countdownType=" + countdownType + ", timeRemaining="
                    + timeRemaining + ", targetTime=" + targetTime + "]";
        }
    }

    /**
     * Report an individual decision.
     *
     * No subclassing relationship with {@link ExplicitDecision} because of different @Subscribe requirements
     */
    static public class DecisionFullUpdate extends FOPEvent {
        public Boolean ref1;

        public Boolean ref2;

        public Boolean ref3;
        public Integer ref1Time;
        public Integer ref2Time;
        public Integer ref3Time;

        public DecisionFullUpdate(Object origin, Athlete athlete, Boolean ref1, Boolean ref2, Boolean ref3,
                Integer long1, Integer long2, Integer long3) {
            super(athlete, origin);
            this.ref1 = ref1;
            this.ref2 = ref2;
            this.ref3 = ref3;
            this.ref1Time = long1;
            this.ref2Time = long2;
            this.ref3Time = long3;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            DecisionFullUpdate other = (DecisionFullUpdate) obj;
            return Objects.equals(ref1, other.ref1) && Objects.equals(ref1Time, other.ref1Time)
                    && Objects.equals(ref2, other.ref2) && Objects.equals(ref2Time, other.ref2Time)
                    && Objects.equals(ref3, other.ref3) && Objects.equals(ref3Time, other.ref3Time);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(ref1, ref1Time, ref2, ref2Time, ref3, ref3Time);
            return result;
        }

    }

    /**
     * The Class DecisionReset.
     */
    static public class DecisionReset extends FOPEvent {

        public DecisionReset(Object object) {
            super(object);
        }

    }

    static public class DecisionUpdate extends FOPEvent {

        public boolean decision;

        public int refIndex;

        public DecisionUpdate(Object origin, int refIndex, boolean decision) {
            super(origin);
            this.refIndex = refIndex;
            this.decision = decision;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            DecisionUpdate other = (DecisionUpdate) obj;
            return decision == other.decision && refIndex == other.refIndex;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(decision, refIndex);
            return result;
        }

        @Override
        public String toString() {
            return "[decision=" + decision + ", refIndex=" + refIndex + "]";
        }

    }

    /**
     * The Class DownSignal.
     */
    static public class DownSignal extends FOPEvent {

        public DownSignal(Object origin) {
            super(origin);
        }

    }

    /**
     * The Class ExplicitDecision.
     */
    static public class ExplicitDecision extends FOPEvent {

        /** The decision. */
        public Boolean success = null;

        public Boolean ref1;

        public Boolean ref2;
        public Boolean ref3;

        /**
         * Instantiates a new referee decision.
         *
         * @param decision the decision
         * @param ref1
         * @param ref2
         * @param ref3
         */
        public ExplicitDecision(Athlete athlete, Object origin, boolean decision, Boolean ref1, Boolean ref2,
                Boolean ref3) {
            super(athlete, origin);
            logger.trace("referee decision for {}", athlete);
            this.success = decision;
            this.ref1 = ref1;
            this.ref2 = ref2;
            this.ref3 = ref3;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ExplicitDecision other = (ExplicitDecision) obj;
            return Objects.equals(ref1, other.ref1) && Objects.equals(ref2, other.ref2)
                    && Objects.equals(ref3, other.ref3) && Objects.equals(success, other.success);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(ref1, ref2, ref3, success);
            return result;
        }
    }

    static public class ForceTime extends FOPEvent {

        public int timeAllowed;

        public ForceTime(int timeAllowed, Object object) {
            super(object);
            this.timeAllowed = timeAllowed;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ForceTime other = (ForceTime) obj;
            return timeAllowed == other.timeAllowed;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(timeAllowed);
            return result;
        }
    }

    static public class JuryDecision extends FOPEvent {
        /** The decision. */
        public Boolean success = null;

        public JuryDecision(Athlete athlete, Object origin, boolean decision) {
            super(athlete, origin);
            logger.trace("jury decision for {}", athlete);
            this.success = decision;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            JuryDecision other = (JuryDecision) obj;
            return Objects.equals(success, other.success);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(success);
            return result;
        }

    }

    /**
     * The Class StartLifting.
     */
    static public class StartLifting extends FOPEvent {

        public StartLifting(Object origin) {
            super(origin);
        }

    }

    static public class SwitchGroup extends FOPEvent {

        private Group group;

        public SwitchGroup(Group g, Object origin) {
            super(origin);
            this.group = g;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            SwitchGroup other = (SwitchGroup) obj;
            return Objects.equals(group, other.group);
        }

        public Group getGroup() {
            return group;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(group);
            return result;
        }

    }

    static public class TimeOver extends FOPEvent {

        public TimeOver(Object origin) {
            super(origin);
        }

    }

    /**
     * The Class StartTime.
     */
    static public class TimeStarted extends FOPEvent {

        public TimeStarted(Object object) {
            super(object);
        }

    }

    /**
     * The Class StopTime.
     */
    static public class TimeStopped extends FOPEvent {

        public TimeStopped(Object object) {
            super(object);
        }

    }

    // /**
    // * The Class AthleteAnnounced.
    // */
    // static public class AthleteAnnounced extends FOPEvent {
    //
    // public AthleteAnnounced(Object object) {
    // super(object);
    // }
    //
    // }

    /**
     * Class WeightChange.
     */
    static public class WeightChange extends FOPEvent {

        public WeightChange(Object origin, Athlete a) {
            super(a, origin);
        }

    }

    final Logger logger = (Logger) LoggerFactory.getLogger(FOPEvent.class);

    {
        logger.setLevel(Level./**/DEBUG);
    }

    /**
     * When a FOPEvent (for example stopping the clock) is handled, it is often reflected as a series of UIEvents (for
     * example, all the displays running the clock get told to stop it). The user interface that gave the order doesn't
     * want to be notified again, so we memorize which user interface element created the original order so it can
     * ignore it.
     */
    protected Object origin;

    protected Athlete athlete;

    private long timestamp;

    public FOPEvent(Athlete athlete, Object origin) {
        this.athlete = athlete;
        this.origin = origin;
        this.timestamp = System.currentTimeMillis();
    }

    FOPEvent(Object origin) {
        this(null, origin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FOPEvent other = (FOPEvent) obj;
        return Objects.equals(athlete, other.athlete) && Objects.equals(origin, other.origin);
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public Object getOrigin() {
        return origin;
    }

    @Override
    public int hashCode() {
        // by default, events are always different, unless they override hashcode.
        // this is because some events such as WeightChange do not currently carry their value,
        // so they come out as always duplicates unless we put a time stamp.
        return Objects.hash(athlete, origin, timestamp, this.getClass());
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

}
