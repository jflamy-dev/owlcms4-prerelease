/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.data.group;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.slf4j.LoggerFactory;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import app.owlcms.data.platform.Platform;
import app.owlcms.utils.LoggerUtils;
import ch.qos.logback.classic.Logger;

/**
 * The Class Group.
 */
@Entity(name = "CompetitionGroup")
@Cacheable
public class Group implements Comparable<Group> {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    private final static DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder().parseLenient()
            .appendPattern(DATE_FORMAT).toFormatter();
    final private Logger logger = (Logger) LoggerFactory.getLogger(Group.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** The platform. */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, optional = true, fetch = FetchType.EAGER)
    Platform platform;

    /** The competition short date time. */
    private LocalDateTime competitionTime;
    private LocalDateTime weighInTime;

    private String name;

    private String announcer;
    private String marshall;
    private String technicalController;
    private String timeKeeper;

    private String referee1;
    private String referee2;
    private String referee3;

    private String jury1;
    private String jury2;
    private String jury3;
    private String jury4;
    private String jury5;

    @Column(columnDefinition = "boolean default false")
    private boolean done;

    /**
     * Instantiates a new group.
     */
    public Group() {
    }

    /**
     * Instantiates a new group.
     *
     * @param groupName the group name
     */
    public Group(String groupName) {
        this.name = groupName;
        final LocalDateTime now = LocalDateTime.now();
        this.setWeighInTime(now);
        this.setCompetitionTime(now);
    }

    /**
     * Instantiates a new group.
     *
     * @param groupName   the group name
     * @param weighin     the weighin
     * @param competition the competition
     */
    public Group(String groupName, LocalDateTime weighin, LocalDateTime competition) {
        this.name = groupName;
        this.setWeighInTime(weighin);
        this.setCompetitionTime(competition);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Group obj) {
        if (this == obj) {
            return 0;
        }
        if (obj == null) {
            return -1;
        }
        Group other = obj;
        if (name == null) {
            if (other.name != null) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (other.name != null) {
                return name.compareTo(other.name);
            } else {
                return -1;
            }
        }
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
        Group other = (Group) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(announcer, other.announcer) && Objects.equals(competitionTime, other.competitionTime)
                && Objects.equals(id, other.id) && Objects.equals(jury1, other.jury1)
                && Objects.equals(jury2, other.jury2) && Objects.equals(jury3, other.jury3)
                && Objects.equals(jury4, other.jury4) && Objects.equals(jury5, other.jury5)
                && Objects.equals(marshall, other.marshall) && Objects.equals(platform, other.platform)
                && Objects.equals(referee1, other.referee1)
                && Objects.equals(referee2, other.referee2) && Objects.equals(referee3, other.referee3)
                && Objects.equals(technicalController, other.technicalController)
                && Objects.equals(timeKeeper, other.timeKeeper) && Objects.equals(weighInTime, other.weighInTime);
    }

    /**
     * Gets the announcer.
     *
     * @return the announcer
     */
    public String getAnnouncer() {
        return announcer;
    }

    /**
     * Gets the competition short date time.
     *
     * @return the competition time
     */
    public String getCompetitionShortDateTime() {
        String formatted = "";
        try {
            LocalDateTime competitionTime2 = getCompetitionTime();
            formatted = competitionTime2 == null ? "" : DATE_TIME_FORMATTER.format(competitionTime2);
        } catch (Exception e) {
            logger.error(LoggerUtils.stackTrace(e));
        }
        return formatted;
    }

    /**
     * Gets the competition time.
     *
     * @return the competition time
     */
    public LocalDateTime getCompetitionTime() {
        return competitionTime;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the jury.
     *
     * @return the jury
     */
    public String getJury() {
        List<String> jurors = Arrays.asList(jury1, jury2, jury3, jury4, jury5);
        Iterables.removeIf(jurors, Predicates.isNull());
        return String.join(", ", jurors);
    }

    /**
     * @return the jury1
     */
    public String getJury1() {
        return jury1;
    }

    /**
     * @return the jury2
     */
    public String getJury2() {
        return jury2;
    }

    /**
     * @return the jury3
     */
    public String getJury3() {
        return jury3;
    }

    /**
     * @return the jury4
     */
    public String getJury4() {
        return jury4;
    }

    /**
     * @return the jury5
     */
    public String getJury5() {
        return jury5;
    }

    /**
     * Gets the marshall.
     *
     * @return the marshall
     */
    public String getMarshall() {
        return marshall;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the platform.
     *
     * @return the platformName on which group will be lifting
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Gets the referee 1.
     *
     * @return the referee 1
     */
    public String getReferee1() {
        return referee1;
    }

    /**
     * Gets the referee 2.
     *
     * @return the referee 2
     */
    public String getReferee2() {
        return referee2;
    }

    /**
     * Gets the referee 3.
     *
     * @return the referee 3
     */
    public String getReferee3() {
        return referee3;
    }

    /**
     * Gets the technical controller.
     *
     * @return the technical controller
     */
    public String getTechnicalController() {
        return technicalController;
    }

    /**
     * Gets the time keeper.
     *
     * @return the time keeper
     */
    public String getTimeKeeper() {
        return timeKeeper;
    }

    /**
     * Gets the weigh in short date time.
     *
     * @return the weigh-in time (two hours before competition, normally)
     */
    public String getWeighInShortDateTime() {
        String formatted = "";
        try {
            LocalDateTime weighInTime2 = getWeighInTime();
            formatted = weighInTime2 == null ? "" : DATE_TIME_FORMATTER.format(weighInTime2);
        } catch (Exception e) {
            logger.error(LoggerUtils.stackTrace(e));
        }
        return formatted;
    }

    /**
     * Gets the weigh in time.
     *
     * @return the weigh-in time (two hours before competition, normally)
     */
    public LocalDateTime getWeighInTime() {
        return weighInTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(announcer, competitionTime, id, jury1, jury2, jury3, jury4, jury5, marshall, name, platform,
                referee1, referee2, referee3, technicalController, timeKeeper, weighInTime);
    }

    public boolean isDone() {
        return done;
    }

    /**
     * Sets the announcer.
     *
     * @param announcer the new announcer
     */
    public void setAnnouncer(String announcer) {
        this.announcer = announcer;
    }

    /**
     * Sets the competition time.
     *
     * @param c the competition time to set
     */
    public void setCompetitionTime(LocalDateTime c) {
        this.competitionTime = c;
    }

    public void setDone(boolean b) {
        logger.debug("done? {} previous={} done={} [{}]", getName(), this.done, b, LoggerUtils.whereFrom());
        if (this.done != b) {
            this.done = b;
            GroupRepository.save(this);
        }
    }

    /**
     * @param jury1 the jury1 to set
     */
    public void setJury1(String jury1) {
        this.jury1 = jury1;
    }

    /**
     * @param jury2 the jury2 to set
     */
    public void setJury2(String jury2) {
        this.jury2 = jury2;
    }

    /**
     * @param jury3 the jury3 to set
     */
    public void setJury3(String jury3) {
        this.jury3 = jury3;
    }

    /**
     * @param jury4 the jury4 to set
     */
    public void setJury4(String jury4) {
        this.jury4 = jury4;
    }

    /**
     * @param jury5 the jury5 to set
     */
    public void setJury5(String jury5) {
        this.jury5 = jury5;
    }

    /**
     * Sets the marshall.
     *
     * @param announcer the new marshall
     */
    public void setMarshall(String announcer) {
        this.marshall = announcer;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the platform.
     *
     * @param platform the new platform
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    /**
     * Sets the referee 1.
     *
     * @param referee1 the new referee 1
     */
    public void setReferee1(String referee1) {
        this.referee1 = referee1;
    }

    /**
     * Sets the referee 2.
     *
     * @param referee2 the new referee 2
     */
    public void setReferee2(String referee2) {
        this.referee2 = referee2;
    }

    /**
     * Sets the referee 3.
     *
     * @param referee3 the new referee 3
     */
    public void setReferee3(String referee3) {
        this.referee3 = referee3;
    }

    /**
     * Sets the technical controller.
     *
     * @param technicalController the new technical controller
     */
    public void setTechnicalController(String technicalController) {
        this.technicalController = technicalController;
    }

    /**
     * Sets the time keeper.
     *
     * @param timeKeeper the new time keeper
     */
    public void setTimeKeeper(String timeKeeper) {
        this.timeKeeper = timeKeeper;
    }

    /**
     * Sets the weigh in time.
     *
     * @param w the weigh-in time to set
     */
    public void setWeighInTime(LocalDateTime w) {
        this.weighInTime = w;
    }

    @Override
    public String toString() {
        return getName();
    }

}
