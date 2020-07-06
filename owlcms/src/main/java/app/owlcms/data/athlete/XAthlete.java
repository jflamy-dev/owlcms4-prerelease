/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.data.athlete;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.LoggerFactory;

import app.owlcms.data.agegroup.AgeGroup;
import app.owlcms.data.athlete.LiftDefinition.Stage;
import app.owlcms.data.category.Category;
import app.owlcms.data.group.Group;
import ch.qos.logback.classic.Logger;

/**
 * Simplified API to access an athlete.
 *
 * Once this refactoring is over, Athlete will be a data class for storage.
 *
 * @author Jean-François Lamy
 *
 */
public class XAthlete extends Athlete {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(XAthlete.class);

    private Athlete a;

    public XAthlete(Athlete a) {
        this.a = a;
    }

    @Override
    public void clearLifts() {
        a.clearLifts();
    }

    /**
     * @param obj
     * @return
     * @see app.owlcms.data.athlete.Athlete#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return a.equals(obj);
    }

    /**
     *
     * @see app.owlcms.data.athlete.Athlete#failedLift()
     */
    @Override
    public void failedLift() {
        a.failedLift();
    }

    @Override
    public Integer getAge() {
        return super.getAge();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getAgeGroup()
     */
    @Override
    public AgeGroup getAgeGroup() {
        return a.getAgeGroup();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getAttemptedLifts()
     */
    @Override
    public int getAttemptedLifts() {
        return getAttemptsDone();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getAttemptNumber()
     */
    @Override
    public Integer getAttemptNumber() {
        return a.getAttemptNumber();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getAttemptsDone()
     */
    @Override
    public Integer getAttemptsDone() {
        int changeNo = LiftDefinition.Changes.ACTUAL.ordinal();
        try {
            int i = 0;
            while (i < LiftDefinition.NBLIFTS) {
                Method method = LiftDefinition.lifts[i].getters[changeNo];
                Object value = method.invoke(a);
                if (value == null || ((String) value).isEmpty()) {
                    return i;
                }
                i++;
            }
            return LiftDefinition.NBLIFTS;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getBestCleanJerk()
     */
    @Override
    public Integer getBestCleanJerk() {
        return getBest(LiftDefinition.Changes.ACTUAL, LiftDefinition.Stage.CLEANJERK).value;
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getBestCleanJerkAttemptNumber()
     */
    @Override
    public int getBestCleanJerkAttemptNumber() {
        int liftNo = getBest(LiftDefinition.Changes.ACTUAL, LiftDefinition.Stage.CLEANJERK).liftNo;
        return liftNo + 1;
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getBestResultAttemptNumber()
     */
    @Override
    public int getBestResultAttemptNumber() {
        int bestNo = getBestCleanJerkAttemptNumber();
        if (bestNo <= 0) {
            bestNo = getBestSnatchAttemptNumber();
        }
        return bestNo;
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getBestSnatch()
     */
    @Override
    public Integer getBestSnatch() {
        return getBest(LiftDefinition.Changes.ACTUAL, LiftDefinition.Stage.SNATCH).value;
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getBestSnatchAttemptNumber()
     */
    @Override
    public int getBestSnatchAttemptNumber() {
        int liftNo = getBest(LiftDefinition.Changes.ACTUAL, LiftDefinition.Stage.SNATCH).liftNo;
        return liftNo + 1;
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getBodyWeight()
     */
    @Override
    public Double getBodyWeight() {
        return a.getBodyWeight();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getBWCategory()
     */
    @Override
    public String getBWCategory() {
        return a.getBWCategory();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCategory()
     */
    @Override
    public Category getCategory() {
        return a.getCategory();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCategorySinclair()
     */
    @Override
    public Double getCategorySinclair() {
        return a.getCategorySinclair();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk1ActualLift()
     */
    @Override
    public String getCleanJerk1ActualLift() {
        return a.getCleanJerk1ActualLift();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk1AsInteger()
     */
    @Override
    public Integer getCleanJerk1AsInteger() {
        return a.getCleanJerk1AsInteger();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk1AutomaticProgression()
     */
    @Override
    public String getCleanJerk1AutomaticProgression() {
        return a.getCleanJerk1AutomaticProgression();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk1Change1()
     */
    @Override
    public String getCleanJerk1Change1() {
        return a.getCleanJerk1Change1();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk1Change2()
     */
    @Override
    public String getCleanJerk1Change2() {
        return a.getCleanJerk1Change2();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk1Declaration()
     */
    @Override
    public String getCleanJerk1Declaration() {
        return a.getCleanJerk1Declaration();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk1LiftTime()
     */
    @Override
    public LocalDateTime getCleanJerk1LiftTime() {
        return a.getCleanJerk1LiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk2ActualLift()
     */
    @Override
    public String getCleanJerk2ActualLift() {
        return a.getCleanJerk2ActualLift();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk2AsInteger()
     */
    @Override
    public Integer getCleanJerk2AsInteger() {
        return a.getCleanJerk2AsInteger();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk2AutomaticProgression()
     */
    @Override
    public String getCleanJerk2AutomaticProgression() {
        return a.getCleanJerk2AutomaticProgression();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk2Change1()
     */
    @Override
    public String getCleanJerk2Change1() {
        return a.getCleanJerk2Change1();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk2Change2()
     */
    @Override
    public String getCleanJerk2Change2() {
        return a.getCleanJerk2Change2();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk2Declaration()
     */
    @Override
    public String getCleanJerk2Declaration() {
        return a.getCleanJerk2Declaration();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk2LiftTime()
     */
    @Override
    public LocalDateTime getCleanJerk2LiftTime() {
        return a.getCleanJerk2LiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk3ActualLift()
     */
    @Override
    public String getCleanJerk3ActualLift() {
        return a.getCleanJerk3ActualLift();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk3AsInteger()
     */
    @Override
    public Integer getCleanJerk3AsInteger() {
        return a.getCleanJerk3AsInteger();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk3AutomaticProgression()
     */
    @Override
    public String getCleanJerk3AutomaticProgression() {
        return a.getCleanJerk3AutomaticProgression();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk3Change1()
     */
    @Override
    public String getCleanJerk3Change1() {
        return a.getCleanJerk3Change1();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk3Change2()
     */
    @Override
    public String getCleanJerk3Change2() {
        return a.getCleanJerk3Change2();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk3Declaration()
     */
    @Override
    public String getCleanJerk3Declaration() {
        return a.getCleanJerk3Declaration();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerk3LiftTime()
     */
    @Override
    public LocalDateTime getCleanJerk3LiftTime() {
        return a.getCleanJerk3LiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerkAttemptsDone()
     */
    @Override
    public Integer getCleanJerkAttemptsDone() {
        return a.getCleanJerkAttemptsDone();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerkPoints()
     */
    @Override
    public Integer getCleanJerkPoints() {
        return a.getCleanJerkPoints();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerkRank()
     */
    @Override
    public Integer getCleanJerkRank() {
        return a.getCleanJerkRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCleanJerkTotal()
     */
    @Override
    public int getCleanJerkTotal() {
        return a.getCleanJerkTotal();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getClub()
     */
    @Override
    public String getClub() {
        return a.getClub();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCombinedPoints()
     */
    @Override
    public Integer getCombinedPoints() {
        return a.getCombinedPoints();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCurrentAutomatic()
     */
    @Override
    public String getCurrentAutomatic() {
        return a.getCurrentAutomatic();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCurrentChange1()
     */
    @Override
    public String getCurrentChange1() {
        return a.getCurrentChange1();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCurrentDeclaration()
     */
    @Override
    public String getCurrentDeclaration() {
        return a.getCurrentDeclaration();
    }

    public LiftInfo getCurrentRequestInfo() {
        return getRequestInfo(getAttemptsDone());
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCustomPoints()
     */
    @Override
    public Integer getCustomPoints() {
        return a.getCustomPoints();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCustomRank()
     */
    @Override
    public Integer getCustomRank() {
        return a.getCustomRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCustomScore()
     */
    @Override
    public Double getCustomScore() {
        return a.getCustomScore();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getCustomScoreComputed()
     */
    @Override
    public Double getCustomScoreComputed() {
        return a.getCustomScoreComputed();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getDisplayCategory()
     */
    @Override
    public String getDisplayCategory() {
        return a.getDisplayCategory();
    }

    @Override
    public Integer getEntryTotal() {
        return super.getEntryTotal();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getFirstAttemptedLiftTime()
     */
    @Override
    public LocalDateTime getFirstAttemptedLiftTime() {
        return a.getFirstAttemptedLiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getFirstName()
     */
    @Override
    public String getFirstName() {
        return a.getFirstName();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getForcedAsCurrent()
     */
    @Override
    public boolean getForcedAsCurrent() {
        return a.getForcedAsCurrent();
    }

    @Override
    public String getFormattedBirth() {
        return super.getFormattedBirth();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getFullBirthDate()
     */
    @Override
    public LocalDate getFullBirthDate() {
        return a.getFullBirthDate();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getFullId()
     */
    @Override
    public String getFullId() {
        return a.getFullId();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getFullName()
     */
    @Override
    public String getFullName() {
        return a.getFullName();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getGender()
     */
    @Override
    public Gender getGender() {
        return a.getGender();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getGroup()
     */
    @Override
    public Group getGroup() {
        return a.getGroup();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getId()
     */
    @Override
    public Long getId() {
        return a.getId();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getLastAttemptedLiftTime()
     */
    @Override
    public LocalDateTime getLastAttemptedLiftTime() {
        return a.getLastAttemptedLiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getLastName()
     */
    @Override
    public String getLastName() {
        return a.getLastName();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getLastSuccessfulLiftTime()
     */
    @Override
    public LocalDateTime getLastSuccessfulLiftTime() {
        return a.getLastSuccessfulLiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getLiftOrderRank()
     */
    @Override
    public Integer getLiftOrderRank() {
        return a.getLiftOrderRank();
    }

    @Override
    public Logger getLogger() {
        return super.getLogger();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getLongCategory()
     */
    @Override
    public String getLongCategory() {
        return a.getLongCategory();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getLotNumber()
     */
    @Override
    public Integer getLotNumber() {
        return a.getLotNumber();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getMastersAgeGroup()
     */
    @Override
    public String getMastersAgeGroup() {
        return a.getMastersAgeGroup();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getMastersAgeGroupInterval()
     */
    @Override
    public String getMastersAgeGroupInterval() {
        return a.getMastersAgeGroupInterval();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getMastersGenderAgeGroupInterval()
     */
    @Override
    public String getMastersGenderAgeGroupInterval() {
        return a.getMastersGenderAgeGroupInterval();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getMedalRank()
     */
    @Override
    public Integer getMedalRank() {
        return a.getMedalRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getMembership()
     */
    @Override
    public String getMembership() {
        return a.getMembership();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getNextAttemptRequestedWeight()
     */
    @Override
    public Integer getNextAttemptRequestedWeight() {
        return a.getNextAttemptRequestedWeight();
    }

    @Override
    public Double getPresumedBodyWeight() {
        return super.getPresumedBodyWeight();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getPreviousLiftTime()
     */
    @Override
    public LocalDateTime getPreviousLiftTime() {
        return a.getPreviousLiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getQualifyingTotal()
     */
    @Override
    public Integer getQualifyingTotal() {
        return a.getQualifyingTotal();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getRank()
     */
    @Override
    public Integer getRank() {
        return a.getRank();
    }

    /**
     * @param attempt
     * @return
     * @see app.owlcms.data.athlete.Athlete#getRequestedWeightForAttempt(int)
     */
    @Override
    public Integer getRequestedWeightForAttempt(int attempt) {
        return a.getRequestedWeightForAttempt(attempt);
    }

    public LiftInfo[] getRequestInfoArray() {
        LiftInfo[] infoArray = new LiftInfo[LiftDefinition.NBLIFTS];
        for (int i = 0; i < LiftDefinition.NBLIFTS; i++) {
            infoArray[i] = getRequestInfo(i);
        }
        return infoArray;
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getRobi()
     */
    @Override
    public Double getRobi() {
        return a.getRobi();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getRobiRank()
     */
    @Override
    public Integer getRobiRank() {
        return a.getRobiRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getRoundedBodyWeight()
     */
    @Override
    public String getRoundedBodyWeight() {
        return a.getRoundedBodyWeight();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSinclair()
     */
    @Override
    public Double getSinclair() {
        return a.getSinclair();
    }

    /**
     * @param bodyWeight1
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSinclair(java.lang.Double)
     */
    @Override
    public Double getSinclair(Double bodyWeight1) {
        return a.getSinclair(bodyWeight1);
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSinclairFactor()
     */
    @Override
    public Double getSinclairFactor() {
        return a.getSinclairFactor();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSinclairForDelta()
     */
    @Override
    public Double getSinclairForDelta() {
        return a.getSinclairForDelta();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSinclairPoints()
     */
    @Override
    public Float getSinclairPoints() {
        return a.getSinclairPoints();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSinclairRank()
     */
    @Override
    public Integer getSinclairRank() {
        return a.getSinclairRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSmm()
     */
    @Override
    public Double getSmm() {
        return a.getSmm();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch1ActualLift()
     */
    @Override
    public String getSnatch1ActualLift() {
        return a.getSnatch1ActualLift();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch1AsInteger()
     */
    @Override
    public Integer getSnatch1AsInteger() {
        return a.getSnatch1AsInteger();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch1AutomaticProgression()
     */
    @Override
    public String getSnatch1AutomaticProgression() {
        return a.getSnatch1AutomaticProgression();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch1Change1()
     */
    @Override
    public String getSnatch1Change1() {
        return a.getSnatch1Change1();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch1Change2()
     */
    @Override
    public String getSnatch1Change2() {
        return a.getSnatch1Change2();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch1Declaration()
     */
    @Override
    public String getSnatch1Declaration() {
        return a.getSnatch1Declaration();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch1LiftTime()
     */
    @Override
    public LocalDateTime getSnatch1LiftTime() {
        return a.getSnatch1LiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch2ActualLift()
     */
    @Override
    public String getSnatch2ActualLift() {
        return a.getSnatch2ActualLift();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch2AsInteger()
     */
    @Override
    public Integer getSnatch2AsInteger() {
        return a.getSnatch2AsInteger();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch2AutomaticProgression()
     */
    @Override
    public String getSnatch2AutomaticProgression() {
        return a.getSnatch2AutomaticProgression();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch2Change1()
     */
    @Override
    public String getSnatch2Change1() {
        return a.getSnatch2Change1();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch2Change2()
     */
    @Override
    public String getSnatch2Change2() {
        return a.getSnatch2Change2();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch2Declaration()
     */
    @Override
    public String getSnatch2Declaration() {
        return a.getSnatch2Declaration();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch2LiftTime()
     */
    @Override
    public LocalDateTime getSnatch2LiftTime() {
        return a.getSnatch2LiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch3ActualLift()
     */
    @Override
    public String getSnatch3ActualLift() {
        return a.getSnatch3ActualLift();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch3AsInteger()
     */
    @Override
    public Integer getSnatch3AsInteger() {
        return a.getSnatch3AsInteger();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch3AutomaticProgression()
     */
    @Override
    public String getSnatch3AutomaticProgression() {
        return a.getSnatch3AutomaticProgression();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch3Change1()
     */
    @Override
    public String getSnatch3Change1() {
        return a.getSnatch3Change1();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch3Change2()
     */
    @Override
    public String getSnatch3Change2() {
        return a.getSnatch3Change2();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch3Declaration()
     */
    @Override
    public String getSnatch3Declaration() {
        return a.getSnatch3Declaration();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatch3LiftTime()
     */
    @Override
    public LocalDateTime getSnatch3LiftTime() {
        return a.getSnatch3LiftTime();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatchAttemptsDone()
     */
    @Override
    public Integer getSnatchAttemptsDone() {
        return a.getSnatchAttemptsDone();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatchPoints()
     */
    @Override
    public Integer getSnatchPoints() {
        return a.getSnatchPoints();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatchRank()
     */
    @Override
    public Integer getSnatchRank() {
        return a.getSnatchRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getSnatchTotal()
     */
    @Override
    public int getSnatchTotal() {
        return a.getSnatchTotal();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getStartNumber()
     */
    @Override
    public Integer getStartNumber() {
        return a.getStartNumber();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTeam()
     */
    @Override
    public String getTeam() {
        return a.getTeam();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTeamCleanJerkRank()
     */
    @Override
    public Integer getTeamCleanJerkRank() {
        return a.getTeamCleanJerkRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTeamCombinedRank()
     */
    @Override
    public Integer getTeamCombinedRank() {
        return a.getTeamCombinedRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTeamRobiRank()
     */
    @Override
    public Integer getTeamRobiRank() {
        return a.getTeamRobiRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTeamSinclairRank()
     */
    @Override
    public Integer getTeamSinclairRank() {
        return a.getTeamSinclairRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTeamSnatchRank()
     */
    @Override
    public Integer getTeamSnatchRank() {
        return a.getTeamSnatchRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTeamTotalRank()
     */
    @Override
    public Integer getTeamTotalRank() {
        return a.getTeamTotalRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTotal()
     */
    @Override
    public Integer getTotal() {
        return a.getTotal();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTotalPoints()
     */
    @Override
    public Integer getTotalPoints() {
        return a.getTotalPoints();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getTotalRank()
     */
    @Override
    public Integer getTotalRank() {
        return a.getTotalRank();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#getYearOfBirth()
     */
    @Override
    public Integer getYearOfBirth() {
        return a.getYearOfBirth();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#hashCode()
     */
    @Override
    public int hashCode() {
        return a.hashCode();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#isDeclaring()
     */
    @Override
    public int isDeclaring() {
        return a.isDeclaring();
    }

    @Override
    public boolean isEligibleForIndividualRanking() {
        return a.isEligibleForIndividualRanking();
    }

    @Override
    public boolean isEligibleForTeamRanking() {
        return a.isEligibleForTeamRanking();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#isForcedAsCurrent()
     */
    @Override
    public boolean isForcedAsCurrent() {
        return a.isForcedAsCurrent();
    }

    @Override
    public boolean isValidation() {
        return a.isValidation();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#longDump()
     */
    @Override
    public String longDump() {
        return a.longDump();
    }

    /**
     *
     * @see app.owlcms.data.athlete.Athlete#resetForcedAsCurrent()
     */
    @Override
    public void resetForcedAsCurrent() {
        a.resetForcedAsCurrent();
    }

    /**
     * @param i
     * @see app.owlcms.data.athlete.Athlete#setAttemptsDone(java.lang.Integer)
     */
    @Override
    public void setAttemptsDone(Integer i) {
        a.setAttemptsDone(i);
    }

    /**
     * @param i
     * @see app.owlcms.data.athlete.Athlete#setBestCleanJerk(java.lang.Integer)
     */
    @Override
    public void setBestCleanJerk(Integer i) {
        a.setBestCleanJerk(i);
    }

    /**
     * @param i
     * @see app.owlcms.data.athlete.Athlete#setBestSnatch(java.lang.Integer)
     */
    @Override
    public void setBestSnatch(Integer i) {
        a.setBestSnatch(i);
    }

    /**
     * @param bodyWeight
     * @see app.owlcms.data.athlete.Athlete#setBodyWeight(java.lang.Double)
     */
    @Override
    public void setBodyWeight(Double bodyWeight) {
        a.setBodyWeight(bodyWeight);
    }

    /**
     * @param category
     * @see app.owlcms.data.athlete.Athlete#setCategory(app.owlcms.data.category.Category)
     */
    @Override
    public void setCategory(Category category) {
        a.setCategory(category);
    }

    /**
     * @param cleanJerk1ActualLift
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk1ActualLift(java.lang.String)
     */
    @Override
    public void setCleanJerk1ActualLift(String cleanJerk1ActualLift) {
        a.setCleanJerk1ActualLift(cleanJerk1ActualLift);
    }

    /**
     * @param s
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk1AutomaticProgression(java.lang.String)
     */
    @Override
    public void setCleanJerk1AutomaticProgression(String s) {
        a.setCleanJerk1AutomaticProgression(s);
    }

    /**
     * @param cleanJerk1Change1
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk1Change1(java.lang.String)
     */
    @Override
    public void setCleanJerk1Change1(String cleanJerk1Change1) {
        a.setCleanJerk1Change1(cleanJerk1Change1);
    }

    /**
     * @param cleanJerk1Change2
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk1Change2(java.lang.String)
     */
    @Override
    public void setCleanJerk1Change2(String cleanJerk1Change2) {
        a.setCleanJerk1Change2(cleanJerk1Change2);
    }

    /**
     * @param cleanJerk1Declaration
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk1Declaration(java.lang.String)
     */
    @Override
    public void setCleanJerk1Declaration(String cleanJerk1Declaration) {
        a.setCleanJerk1Declaration(cleanJerk1Declaration);
    }

    @Override
    public void setCleanJerk1LiftTime(LocalDateTime cleanJerk1LiftTime) {
        super.setCleanJerk1LiftTime(cleanJerk1LiftTime);
    }

    /**
     * @param cleanJerk2ActualLift
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk2ActualLift(java.lang.String)
     */
    @Override
    public void setCleanJerk2ActualLift(String cleanJerk2ActualLift) {
        a.setCleanJerk2ActualLift(cleanJerk2ActualLift);
    }

    /**
     * @param s
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk2AutomaticProgression(java.lang.String)
     */
    @Override
    public void setCleanJerk2AutomaticProgression(String s) {
        a.setCleanJerk2AutomaticProgression(s);
    }

    /**
     * @param cleanJerk2Change1
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk2Change1(java.lang.String)
     */
    @Override
    public void setCleanJerk2Change1(String cleanJerk2Change1) {
        a.setCleanJerk2Change1(cleanJerk2Change1);
    }

    /**
     * @param cleanJerk2Change2
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk2Change2(java.lang.String)
     */
    @Override
    public void setCleanJerk2Change2(String cleanJerk2Change2) {
        a.setCleanJerk2Change2(cleanJerk2Change2);
    }

    /**
     * @param cleanJerk2Declaration
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk2Declaration(java.lang.String)
     */
    @Override
    public void setCleanJerk2Declaration(String cleanJerk2Declaration) {
        a.setCleanJerk2Declaration(cleanJerk2Declaration);
    }

    @Override
    public void setCleanJerk2LiftTime(LocalDateTime cleanJerk2LiftTime) {
        super.setCleanJerk2LiftTime(cleanJerk2LiftTime);
    }

    /**
     * @param cleanJerk3ActualLift
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk3ActualLift(java.lang.String)
     */
    @Override
    public void setCleanJerk3ActualLift(String cleanJerk3ActualLift) {
        a.setCleanJerk3ActualLift(cleanJerk3ActualLift);
    }

    /**
     * @param s
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk3AutomaticProgression(java.lang.String)
     */
    @Override
    public void setCleanJerk3AutomaticProgression(String s) {
        a.setCleanJerk3AutomaticProgression(s);
    }

    /**
     * @param cleanJerk3Change1
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk3Change1(java.lang.String)
     */
    @Override
    public void setCleanJerk3Change1(String cleanJerk3Change1) {
        a.setCleanJerk3Change1(cleanJerk3Change1);
    }

    /**
     * @param cleanJerk3Change2
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk3Change2(java.lang.String)
     */
    @Override
    public void setCleanJerk3Change2(String cleanJerk3Change2) {
        a.setCleanJerk3Change2(cleanJerk3Change2);
    }

    /**
     * @param cleanJerk3Declaration
     * @see app.owlcms.data.athlete.Athlete#setCleanJerk3Declaration(java.lang.String)
     */
    @Override
    public void setCleanJerk3Declaration(String cleanJerk3Declaration) {
        a.setCleanJerk3Declaration(cleanJerk3Declaration);
    }

    @Override
    public void setCleanJerk3LiftTime(LocalDateTime cleanJerk3LiftTime) {
        super.setCleanJerk3LiftTime(cleanJerk3LiftTime);
    }

    /**
     * @param i
     * @see app.owlcms.data.athlete.Athlete#setCleanJerkAttemptsDone(java.lang.Integer)
     */
    @Override
    public void setCleanJerkAttemptsDone(Integer i) {
        a.setCleanJerkAttemptsDone(i);
    }

    /**
     * @param cleanJerkPoints
     * @see app.owlcms.data.athlete.Athlete#setCleanJerkPoints(java.lang.Float)
     */
    @Override
    public void setCleanJerkPoints(Integer cleanJerkPoints) {
        a.setCleanJerkPoints(cleanJerkPoints);
    }

    /**
     * @param cleanJerkRank
     * @see app.owlcms.data.athlete.Athlete#setCleanJerkRank(java.lang.Integer)
     */
    @Override
    public void setCleanJerkRank(Integer cleanJerkRank) {
        a.setCleanJerkRank(cleanJerkRank);
    }

    /**
     * @param club
     * @see app.owlcms.data.athlete.Athlete#setClub(java.lang.String)
     */
    @Override
    public void setClub(String club) {
        a.setClub(club);
    }

    /**
     * @param customPoints
     * @see app.owlcms.data.athlete.Athlete#setCustomPoints(float)
     */
    @Override
    public void setCustomPoints(Integer customPoints) {
        a.setCustomPoints(customPoints);
    }

    /**
     * @param customRank
     * @see app.owlcms.data.athlete.Athlete#setCustomRank(java.lang.Integer)
     */
    @Override
    public void setCustomRank(Integer customRank) {
        a.setCustomRank(customRank);
    }

    /**
     * @param customScore
     * @see app.owlcms.data.athlete.Athlete#setCustomScore(java.lang.Double)
     */
    @Override
    public void setCustomScore(Double customScore) {
        a.setCustomScore(customScore);
    }

    @Override
    public void setEligibleForIndividualRanking(boolean eligibleForIndividualRanking) {
        a.setEligibleForIndividualRanking(eligibleForIndividualRanking);
    }

    @Override
    public void setEligibleForTeamRanking(boolean eligibleForTeamRanking) {
        a.setEligibleForTeamRanking(eligibleForTeamRanking);
    }

    /**
     * @param firstName
     * @see app.owlcms.data.athlete.Athlete#setFirstName(java.lang.String)
     */
    @Override
    public void setFirstName(String firstName) {
        a.setFirstName(firstName);
    }

    /**
     * @param forcedAsCurrent
     * @see app.owlcms.data.athlete.Athlete#setForcedAsCurrent(boolean)
     */
    @Override
    public void setForcedAsCurrent(boolean forcedAsCurrent) {
        a.setForcedAsCurrent(forcedAsCurrent);
    }

    /**
     * @param fullBirthDate
     * @see app.owlcms.data.athlete.Athlete#setFullBirthDate(java.time.LocalDate)
     */
    @Override
    public void setFullBirthDate(LocalDate fullBirthDate) {
        a.setFullBirthDate(fullBirthDate);
    }

    /**
     * @param string
     * @see app.owlcms.data.athlete.Athlete#setGender(java.lang.String)
     */
    @Override
    public void setGender(Gender gender) {
        a.setGender(gender);
    }

    /**
     * @param group
     * @see app.owlcms.data.athlete.Athlete#setGroup(app.owlcms.data.group.Group)
     */
    @Override
    public void setGroup(Group group) {
        a.setGroup(group);
    }

    /**
     * @param lastName
     * @see app.owlcms.data.athlete.Athlete#setLastName(java.lang.String)
     */
    @Override
    public void setLastName(String lastName) {
        a.setLastName(lastName);
    }

    /**
     * @param liftOrder
     * @see app.owlcms.data.athlete.Athlete#setLiftOrderRank(java.lang.Integer)
     */
    @Override
    public void setLiftOrderRank(Integer liftOrder) {
        a.setLiftOrderRank(liftOrder);
    }

    /**
     * @param lotNumber
     * @see app.owlcms.data.athlete.Athlete#setLotNumber(java.lang.Integer)
     */
    @Override
    public void setLotNumber(Integer lotNumber) {
        a.setLotNumber(lotNumber);
    }

    /**
     * @param membership
     * @see app.owlcms.data.athlete.Athlete#setMembership(java.lang.String)
     */
    @Override
    public void setMembership(String membership) {
        a.setMembership(membership);
    }

    /**
     * @param i
     * @see app.owlcms.data.athlete.Athlete#setNextAttemptRequestedWeight(java.lang.Integer)
     */
    @Override
    public void setNextAttemptRequestedWeight(Integer i) {
        a.setNextAttemptRequestedWeight(i);
    }

    @Override
    public void setPresumedBodyWeight(Double presumedBodyWeight) {
        super.setPresumedBodyWeight(presumedBodyWeight);
    }

    @Override
    public void setPresumedCategory(Category category) {
        super.setPresumedCategory(category);
    }

    /**
     * @param qualifyingTotal
     * @see app.owlcms.data.athlete.Athlete#setQualifyingTotal(java.lang.Integer)
     */
    @Override
    public void setQualifyingTotal(Integer qualifyingTotal) {
        a.setQualifyingTotal(qualifyingTotal);
    }

    /**
     * @param i
     * @see app.owlcms.data.athlete.Athlete#setRank(java.lang.Integer)
     */
    @Override
    public void setRank(Integer i) {
        a.setRank(i);
    }

    /**
     * @param robiRank
     * @see app.owlcms.data.athlete.Athlete#setRobiRank(java.lang.Integer)
     */
    @Override
    public void setRobiRank(Integer robiRank) {
        a.setRobiRank(robiRank);
    }

    /**
     * @param sinclairRank
     * @see app.owlcms.data.athlete.Athlete#setSinclairRank(java.lang.Integer)
     */
    @Override
    public void setSinclairRank(Integer sinclairRank) {
        a.setSinclairRank(sinclairRank);
    }

    /**
     * @param snatch1ActualLift
     * @see app.owlcms.data.athlete.Athlete#setSnatch1ActualLift(java.lang.String)
     */
    @Override
    public void setSnatch1ActualLift(String snatch1ActualLift) {
        a.setSnatch1ActualLift(snatch1ActualLift);
    }

    /**
     * @param s
     * @see app.owlcms.data.athlete.Athlete#setSnatch1AutomaticProgression(java.lang.String)
     */
    @Override
    public void setSnatch1AutomaticProgression(String s) {
        a.setSnatch1AutomaticProgression(s);
    }

    /**
     * @param snatch1Change1
     * @see app.owlcms.data.athlete.Athlete#setSnatch1Change1(java.lang.String)
     */
    @Override
    public void setSnatch1Change1(String snatch1Change1) {
        a.setSnatch1Change1(snatch1Change1);
    }

    /**
     * @param snatch1Change2
     * @see app.owlcms.data.athlete.Athlete#setSnatch1Change2(java.lang.String)
     */
    @Override
    public void setSnatch1Change2(String snatch1Change2) {
        a.setSnatch1Change2(snatch1Change2);
    }

    /**
     * @param snatch1Declaration
     * @see app.owlcms.data.athlete.Athlete#setSnatch1Declaration(java.lang.String)
     */
    @Override
    public void setSnatch1Declaration(String snatch1Declaration) {
        a.setSnatch1Declaration(snatch1Declaration);
    }

    @Override
    public void setSnatch1LiftTime(LocalDateTime snatch1LiftTime) {
        super.setSnatch1LiftTime(snatch1LiftTime);
    }

    /**
     * @param snatch2ActualLift
     * @see app.owlcms.data.athlete.Athlete#setSnatch2ActualLift(java.lang.String)
     */
    @Override
    public void setSnatch2ActualLift(String snatch2ActualLift) {
        a.setSnatch2ActualLift(snatch2ActualLift);
    }

    /**
     * @param s
     * @see app.owlcms.data.athlete.Athlete#setSnatch2AutomaticProgression(java.lang.String)
     */
    @Override
    public void setSnatch2AutomaticProgression(String s) {
        a.setSnatch2AutomaticProgression(s);
    }

    /**
     * @param snatch2Change1
     * @see app.owlcms.data.athlete.Athlete#setSnatch2Change1(java.lang.String)
     */
    @Override
    public void setSnatch2Change1(String snatch2Change1) {
        a.setSnatch2Change1(snatch2Change1);
    }

    /**
     * @param snatch2Change2
     * @see app.owlcms.data.athlete.Athlete#setSnatch2Change2(java.lang.String)
     */
    @Override
    public void setSnatch2Change2(String snatch2Change2) {
        a.setSnatch2Change2(snatch2Change2);
    }

//	/**
//	 * @param resultOrderRank
//	 * @param rankingType
//	 * @see app.owlcms.data.athlete.Athlete#setResultOrderRank(java.lang.Integer, app.owlcms.data.athleteSort.AthleteSorter.Ranking)
//	 */
//	@Override
//	public void setResultOrderRank(Integer resultOrderRank, Ranking rankingType) {
//		a.setResultOrderRank(resultOrderRank, rankingType);
//	}

    /**
     * @param snatch2Declaration
     * @see app.owlcms.data.athlete.Athlete#setSnatch2Declaration(java.lang.String)
     */
    @Override
    public void setSnatch2Declaration(String snatch2Declaration) {
        a.setSnatch2Declaration(snatch2Declaration);
    }

    @Override
    public void setSnatch2LiftTime(LocalDateTime snatch2LiftTime) {
        super.setSnatch2LiftTime(snatch2LiftTime);
    }

    /**
     * @param snatch3ActualLift
     * @see app.owlcms.data.athlete.Athlete#setSnatch3ActualLift(java.lang.String)
     */
    @Override
    public void setSnatch3ActualLift(String snatch3ActualLift) {
        a.setSnatch3ActualLift(snatch3ActualLift);
    }

    /**
     * @param s
     * @see app.owlcms.data.athlete.Athlete#setSnatch3AutomaticProgression(java.lang.String)
     */
    @Override
    public void setSnatch3AutomaticProgression(String s) {
        a.setSnatch3AutomaticProgression(s);
    }

    /**
     * @param snatch3Change1
     * @see app.owlcms.data.athlete.Athlete#setSnatch3Change1(java.lang.String)
     */
    @Override
    public void setSnatch3Change1(String snatch3Change1) {
        a.setSnatch3Change1(snatch3Change1);
    }

    /**
     * @param snatch3Change2
     * @see app.owlcms.data.athlete.Athlete#setSnatch3Change2(java.lang.String)
     */
    @Override
    public void setSnatch3Change2(String snatch3Change2) {
        a.setSnatch3Change2(snatch3Change2);
    }

    /**
     * @param snatch3Declaration
     * @see app.owlcms.data.athlete.Athlete#setSnatch3Declaration(java.lang.String)
     */
    @Override
    public void setSnatch3Declaration(String snatch3Declaration) {
        a.setSnatch3Declaration(snatch3Declaration);
    }

    @Override
    public void setSnatch3LiftTime(LocalDateTime snatch3LiftTime) {
        super.setSnatch3LiftTime(snatch3LiftTime);
    }

    /**
     * @param i
     * @see app.owlcms.data.athlete.Athlete#setSnatchAttemptsDone(java.lang.Integer)
     */
    @Override
    public void setSnatchAttemptsDone(Integer i) {
        a.setSnatchAttemptsDone(i);
    }

    /**
     * @param snatchPoints
     * @see app.owlcms.data.athlete.Athlete#setSnatchPoints(float)
     */
    @Override
    public void setSnatchPoints(Integer snatchPoints) {
        a.setSnatchPoints(snatchPoints);
    }

    /**
     * @param snatchRank
     * @see app.owlcms.data.athlete.Athlete#setSnatchRank(java.lang.Integer)
     */
    @Override
    public void setSnatchRank(Integer snatchRank) {
        a.setSnatchRank(snatchRank);
    }

    /**
     * @param startNumber
     * @see app.owlcms.data.athlete.Athlete#setStartNumber(java.lang.Integer)
     */
    @Override
    public void setStartNumber(Integer startNumber) {
        a.setStartNumber(startNumber);
    }

    /**
     * @param club
     * @see app.owlcms.data.athlete.Athlete#setTeam(java.lang.String)
     */
    @Override
    public void setTeam(String club) {
        a.setTeam(club);
    }

    /**
     * @param teamCJRank
     * @see app.owlcms.data.athlete.Athlete#setTeamCleanJerkRank(java.lang.Integer)
     */
    @Override
    public void setTeamCleanJerkRank(Integer teamCJRank) {
        a.setTeamCleanJerkRank(teamCJRank);
    }

    /**
     * @param teamCombinedRank
     * @see app.owlcms.data.athlete.Athlete#setTeamCombinedRank(java.lang.Integer)
     */
    @Override
    public void setTeamCombinedRank(Integer teamCombinedRank) {
        a.setTeamCombinedRank(teamCombinedRank);
    }

    /**
     * @param teamRobiRank
     * @see app.owlcms.data.athlete.Athlete#setTeamRobiRank(java.lang.Integer)
     */
    @Override
    public void setTeamRobiRank(Integer teamRobiRank) {
        a.setTeamRobiRank(teamRobiRank);
    }

    /**
     * @param teamSinclairRank
     * @see app.owlcms.data.athlete.Athlete#setTeamSinclairRank(java.lang.Integer)
     */
    @Override
    public void setTeamSinclairRank(Integer teamSinclairRank) {
        a.setTeamSinclairRank(teamSinclairRank);
    }

    /**
     * @param teamSnatchRank
     * @see app.owlcms.data.athlete.Athlete#setTeamSnatchRank(java.lang.Integer)
     */
    @Override
    public void setTeamSnatchRank(Integer teamSnatchRank) {
        a.setTeamSnatchRank(teamSnatchRank);
    }

    /**
     * @param teamTotalRank
     * @see app.owlcms.data.athlete.Athlete#setTeamTotalRank(java.lang.Integer)
     */
    @Override
    public void setTeamTotalRank(Integer teamTotalRank) {
        a.setTeamTotalRank(teamTotalRank);
    }

    /**
     * @param i
     * @see app.owlcms.data.athlete.Athlete#setTotal(java.lang.Integer)
     */
    @Override
    public void setTotal(Integer i) {
        a.setTotal(i);
    }

    /**
     * @param totalPoints
     * @see app.owlcms.data.athlete.Athlete#setTotalPoints(float)
     */
    @Override
    public void setTotalPoints(Integer totalPoints) {
        a.setTotalPoints(totalPoints);
    }

    /**
     * @param totalRank
     * @see app.owlcms.data.athlete.Athlete#setTotalRank(java.lang.Integer)
     */
    @Override
    public void setTotalRank(Integer totalRank) {
        a.setTotalRank(totalRank);
    }

    @Override
    public void setValidation(boolean b) {
        a.setValidation(b);
    }

    /**
     * @param birthYear
     * @see app.owlcms.data.athlete.Athlete#setYearOfBirth(java.lang.Integer)
     */
    @Override
    public void setYearOfBirth(Integer birthYear) {
        a.setYearOfBirth(birthYear);
    }

    /**
     *
     * @see app.owlcms.data.athlete.Athlete#successfulLift()
     */
    @Override
    public void successfulLift() {
        a.successfulLift();
    }

    /**
     * @return
     * @see app.owlcms.data.athlete.Athlete#toString()
     */
    @Override
    public String toString() {
        return a.toString();
    }

    /**
     * @param curLift
     * @param automaticProgression
     * @param declaration
     * @param change1
     * @param change2
     * @param actualLift
     * @see app.owlcms.data.athlete.Athlete#validateActualLift(int, java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void validateActualLift(int curLift, String automaticProgression, String declaration, String change1,
            String change2, String actualLift) {
        a.validateActualLift(curLift, automaticProgression, declaration, change1, change2, actualLift);
    }

    /**
     * @param cleanJerk1ActualLift
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk1ActualLift(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk1ActualLift(String cleanJerk1ActualLift) throws RuleViolationException {
        return a.validateCleanJerk1ActualLift(cleanJerk1ActualLift);
    }

    /**
     * @param cleanJerk1Change1
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk1Change1(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk1Change1(String cleanJerk1Change1) throws RuleViolationException {
        return a.validateCleanJerk1Change1(cleanJerk1Change1);
    }

    /**
     * @param cleanJerk1Change2
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk1Change2(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk1Change2(String cleanJerk1Change2) throws RuleViolationException {
        return a.validateCleanJerk1Change2(cleanJerk1Change2);
    }

    @Override
    public boolean validateCleanJerk1Declaration(String cleanJerk1Declaration) throws RuleViolationException {
        return a.validateCleanJerk1Declaration(cleanJerk1Declaration);
    }

    /**
     * @param cleanJerk2ActualLift
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk2ActualLift(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk2ActualLift(String cleanJerk2ActualLift) throws RuleViolationException {
        return a.validateCleanJerk2ActualLift(cleanJerk2ActualLift);
    }

    /**
     * @param cleanJerk2Change1
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk2Change1(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk2Change1(String cleanJerk2Change1) throws RuleViolationException {
        return a.validateCleanJerk2Change1(cleanJerk2Change1);
    }

    /**
     * @param cleanJerk2Change2
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk2Change2(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk2Change2(String cleanJerk2Change2) throws RuleViolationException {
        return a.validateCleanJerk2Change2(cleanJerk2Change2);
    }

    @Override
    public boolean validateCleanJerk2Declaration(String cleanJerk2Declaration) throws RuleViolationException {
        return a.validateCleanJerk2Declaration(cleanJerk2Declaration);
    }

    /**
     * @param cleanJerk3ActualLift
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk3ActualLift(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk3ActualLift(String cleanJerk3ActualLift) throws RuleViolationException {
        return a.validateCleanJerk3ActualLift(cleanJerk3ActualLift);
    }

    /**
     * @param cleanJerk3Change1
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk3Change1(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk3Change1(String cleanJerk3Change1) throws RuleViolationException {
        return a.validateCleanJerk3Change1(cleanJerk3Change1);
    }

    /**
     * @param cleanJerk3Change2
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateCleanJerk3Change2(java.lang.String)
     */
    @Override
    public boolean validateCleanJerk3Change2(String cleanJerk3Change2) throws RuleViolationException {
        return a.validateCleanJerk3Change2(cleanJerk3Change2);
    }

    @Override
    public boolean validateCleanJerk3Declaration(String cleanJerk3Declaration) throws RuleViolationException {
        return a.validateCleanJerk3Declaration(cleanJerk3Declaration);
    }

    /**
     * @param snatch1ActualLift
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch1ActualLift(java.lang.String)
     */
    @Override
    public boolean validateSnatch1ActualLift(String snatch1ActualLift) throws RuleViolationException {
        return a.validateSnatch1ActualLift(snatch1ActualLift);
    }

    /**
     * @param snatch1Change1
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch1Change1(java.lang.String)
     */
    @Override
    public boolean validateSnatch1Change1(String snatch1Change1) throws RuleViolationException {
        return a.validateSnatch1Change1(snatch1Change1);
    }

    /**
     * @param snatch1Change2
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch1Change2(java.lang.String)
     */
    @Override
    public boolean validateSnatch1Change2(String snatch1Change2) throws RuleViolationException {
        return a.validateSnatch1Change2(snatch1Change2);
    }

    @Override
    public boolean validateSnatch1Declaration(String snatch1Declaration) throws RuleViolationException {
        return a.validateSnatch1Declaration(snatch1Declaration);
    }

    /**
     * @param snatch2ActualLift
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch2ActualLift(java.lang.String)
     */
    @Override
    public boolean validateSnatch2ActualLift(String snatch2ActualLift) throws RuleViolationException {
        return a.validateSnatch2ActualLift(snatch2ActualLift);
    }

    /**
     * @param snatch2Change1
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch2Change1(java.lang.String)
     */
    @Override
    public boolean validateSnatch2Change1(String snatch2Change1) throws RuleViolationException {
        return a.validateSnatch2Change1(snatch2Change1);
    }

    /**
     * @param snatch2Change2
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch2Change2(java.lang.String)
     */
    @Override
    public boolean validateSnatch2Change2(String snatch2Change2) throws RuleViolationException {
        return a.validateSnatch2Change2(snatch2Change2);
    }

    @Override
    public boolean validateSnatch2Declaration(String snatch2Declaration) throws RuleViolationException {
        return a.validateSnatch2Declaration(snatch2Declaration);
    }

    /**
     * @param snatch3ActualLift
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch3ActualLift(java.lang.String)
     */
    @Override
    public boolean validateSnatch3ActualLift(String snatch3ActualLift) throws RuleViolationException {
        return a.validateSnatch3ActualLift(snatch3ActualLift);
    }

    /**
     * @param snatch3Change1
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch3Change1(java.lang.String)
     */
    @Override
    public boolean validateSnatch3Change1(String snatch3Change1) throws RuleViolationException {
        return a.validateSnatch3Change1(snatch3Change1);
    }

    /**
     * @param snatch3Change2
     * @return
     * @throws RuleViolationException
     * @see app.owlcms.data.athlete.Athlete#validateSnatch3Change2(java.lang.String)
     */
    @Override
    public boolean validateSnatch3Change2(String snatch3Change2) throws RuleViolationException {
        return a.validateSnatch3Change2(snatch3Change2);
    }

    @Override
    public boolean validateSnatch3Declaration(String snatch3Declaration) throws RuleViolationException {
        return a.validateSnatch3Declaration(snatch3Declaration);
    }

    /**
     * @param unlessCurrent
     * @see app.owlcms.data.athlete.Athlete#checkStartingTotalsRule(boolean)
     */
    @Override
    public boolean validateStartingTotalsRule(String snatch1Declaration, String snatch1Change1, String snatch1Change2,
            String cleanJerk1Declaration, String cleanJerk1Change1, String cleanJerk1Change2) {
        return a.validateStartingTotalsRule(snatch1Declaration, snatch1Change1, snatch1Change2, cleanJerk1Declaration,
                cleanJerk1Change1, cleanJerk1Change2);
    }

    /**
     *
     * @see app.owlcms.data.athlete.Athlete#withdraw()
     */
    @Override
    public void withdraw() {
        a.withdraw();
    }

    @Override
    protected Integer asInteger(String stringValue) {
        return super.asInteger(stringValue);
    }

    protected LiftInfo getBest(LiftDefinition.Changes change, Stage stage) {
        try {
            int liftNo = stage.inclUpper;
            int changeNo = 0;
            String stringValue = null;
            boolean found = false;
            while (!found && liftNo > stage.inclLower) {
                changeNo = change.ordinal();
                stringValue = (String) LiftDefinition.lifts[liftNo].getters[changeNo].invoke(a);
                if (stringValue != null) {
                    found = true;
                } else {
                    liftNo--;
                }
            }
            if (found) {
                return new LiftInfo(stage, liftNo, changeNo, stringValue);
            } else {
                return new LiftInfo(stage, -1, changeNo, null);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private LiftInfo getRequestInfo(Integer liftNo) {
        try {
            int changeNo = LiftDefinition.NBCHANGES - 1;
            String stringValue = null;
            boolean found = false;
            while (!found && changeNo >= 0) {
                Method method = LiftDefinition.lifts[liftNo].getters[changeNo];
                stringValue = (String) method.invoke(a);
                boolean zeroKgAutomaticChange = (changeNo == 0 && "0".equals(stringValue));
                if (stringValue != null && !stringValue.isEmpty() && !zeroKgAutomaticChange) {
                    found = true;
                } else {
                    changeNo--;
                }
            }
            if (found) {
                return new LiftInfo(LiftDefinition.lifts[liftNo].stage, liftNo, changeNo, stringValue);
            } else {
                return new LiftInfo(LiftDefinition.lifts[liftNo].stage, liftNo, -1, null);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }

}