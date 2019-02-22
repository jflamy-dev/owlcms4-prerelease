/***
 * Copyright (c) 2018-2019 Jean-François Lamy
 * 
 * This software is licensed under the the Apache 2.0 License amended with the
 * Commons Clause.
 * License text at https://github.com/jflamy/owlcms4/master/License
 * See https://redislabs.com/wp-content/uploads/2018/10/Commons-Clause-White-Paper.pdf
 */
package org.ledocte.owlcms.data.athlete;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Locale;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.ledocte.owlcms.data.athleteSort.AthleteSorter.Ranking;
import org.ledocte.owlcms.data.category.Category;
import org.ledocte.owlcms.data.competition.Competition;
import org.ledocte.owlcms.data.group.Group;
import org.ledocte.owlcms.i18n.Messages;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

import ch.qos.logback.classic.Logger;

/**
 * This class stores all the information related to a particular participant.
 * <p>
 * This class is an example of what not to do. This was designed prior reaching
 * a proper understanding of Hibernate/JPA and of proper separation between
 * Vaadin Containers and persistence frameworks. Live and Learn.
 * <p>
 * All persistent properties are managed by Java Persistance annotations.
 * "Field" access mode is used, meaning that it is the values of the fields that
 * are stored, and not the values returned by the getters. Note that it is often
 * necessary to know when a value has been captured or not -- this is why values
 * are stored as Integers or Doubles, so that we can use null to indicate that a
 * value has not been captured.
 * </p>
 * <p>
 * This allows us to use the getters to return the values as they will be
 * displayed by the application
 * </p>
 * <p>
 * Computed fields are defined as final transient properties and marked as
 *
 * @author jflamy @Transient; the only reason for this is so the JavaBeans
 *         introspection mechanisms find them.
 *         </p>
 *         <p>
 *         This class uses events to notify interested user interface components
 *         that fields or computed values have changed. In this way the user
 *         interface does not have to know that the category field on the screen
 *         is dependent on the bodyweight and the gender -- all the dependency
 *         logic is kept at the business object level.
 *         </p>
 */
@Entity
@Cacheable
public class Athlete {

	/**
	 * Athlete events all derive from this.
	 */
	public class UpdateEvent extends EventObject {
		private static final long serialVersionUID = -126644150054472005L;
		private List<String> propertyIds;

		/**
		 * Constructs a new event with a specified source component.
		 *
		 * @param source      the source component of the event.
		 * @param propertyIds that have been updated.
		 */
		public UpdateEvent(Athlete source, String... propertyIds) {
			super(source);
			this.propertyIds = Arrays.asList(propertyIds);
		}

		/**
		 * Gets the property ids.
		 *
		 * @return the property ids
		 */
		public List<String> getPropertyIds() {
			return propertyIds;
		}

	}

	/**
	 * Listener interface for receiving <code>Athlete.UpdateEvent</code>s.
	 *
	 * @see UpdateEventEvent
	 */
	public interface UpdateEventListener extends java.util.EventListener {

		/**
		 * This method will be invoked when a Athlete.UpdateEvent is fired.
		 *
		 * @param updateEvent the event that has occured.
		 */
		public void updateEvent(Athlete.UpdateEvent updateEvent);
	}

	private static final Logger logger = (Logger) LoggerFactory.getLogger(Athlete.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long version;

	private Integer lotNumber = null;
	private Integer startNumber = null;
	private String firstName = ""; //$NON-NLS-1$
	private String lastName = ""; //$NON-NLS-1$
	private String team = ""; //$NON-NLS-1$

	private String gender = ""; //$NON-NLS-1$

	private LocalDate fullBirthDate = null;

	private Double bodyWeight = null;

	private String membership = ""; //$NON-NLS-1$

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_group")
	private Group group;

	// This is brute force, but having embedded classes does not bring much
	// and we don't want joins or other such logic for the Athlete card.
	// Since the Athlete card is 6 x 4 items, we take the simple route.

	// Note: we use Strings because we need to distinguish actually entered
	// values (such as 0)
	// from empty cells. Using Integers or Doubles would work as well, but many
	// people want to type
	// "-" or other things in the cells, so Strings are actually easier.

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_categ")
	private Category category = null;
	private String snatch1Declaration;
	private String snatch1Change1;
	private String snatch1Change2;
	private String snatch1ActualLift;
	private Date snatch1LiftTime;

	private String snatch2Declaration;
	private String snatch2Change1;
	private String snatch2Change2;
	private String snatch2ActualLift;
	private Date snatch2LiftTime;

	private String snatch3Declaration;
	private String snatch3Change1;
	private String snatch3Change2;
	private String snatch3ActualLift;
	private Date snatch3LiftTime;

	private String cleanJerk1Declaration;
	private String cleanJerk1Change1;
	private String cleanJerk1Change2;
	private String cleanJerk1ActualLift;
	private Date cleanJerk1LiftTime;

	private String cleanJerk2Declaration;
	private String cleanJerk2Change1;
	private String cleanJerk2Change2;
	private String cleanJerk2ActualLift;
	private Date cleanJerk2LiftTime;

	private String cleanJerk3Declaration;
	private String cleanJerk3Change1;
	private String cleanJerk3Change2;
	private String cleanJerk3ActualLift;
	private Date cleanJerk3LiftTime;

	private Integer snatchRank;
	private Integer cleanJerkRank;
	private Integer totalRank;
	private Integer sinclairRank;
	private Integer robiRank;
	private Integer customRank;

	private Float snatchPoints;
	private Float cleanJerkPoints;
	private Float totalPoints; // points based on totalRank
	private Float sinclairPoints;
	private Float customPoints;

	private Integer teamSinclairRank;
	private Integer teamRobiRank;
	private Integer teamSnatchRank;
	private Integer teamCleanJerkRank;

	private Integer teamTotalRank;
	private Integer teamCombinedRank;

	private Boolean teamMember = true; // false if substitute; note that we consider null to be true.;
	private Integer qualifyingTotal = 0;

//	/*
//	 * Computed properties. We create them here because we want the corresponding
//	 * accessors to be discovered by introspection. Setters are not defined (the
//	 * fields are final). Getters perform the required computation.
//	 *
//	 * BEWARE: the variables defined here must NOT be used -- you must be able to
//	 * comment them out and get no compilation errors. All the code should use the
//	 * getters only.
//	 */
//	@Transient
//	final transient String snatch1AutomaticProgression = ""; //$NON-NLS-1$
//
//	@Transient
//	final transient String snatch2AutomaticProgression = ""; //$NON-NLS-1$
//	@Transient
//	final transient String snatch3AutomaticProgression = ""; //$NON-NLS-1$
//
//	@Transient
//	final transient String cleanJerk1AutomaticProgression = ""; //$NON-NLS-1$
//	@Transient
//	final transient String cleanJerk2AutomaticProgression = ""; //$NON-NLS-1$
//
//	@Transient
//	final transient String cleanJerk3AutomaticProgression = ""; //$NON-NLS-1$
//	@Transient
//	final transient Integer bestSnatch = 0;
//	@Transient
//	final transient Integer bestCleanJerk = 0;
//	@Transient
//	final transient Integer medalRank = 0;
//
//	@Transient
//	final transient Integer total = 0;
//	@Transient
//	final transient Integer attemptsDone = 0;
//
//	@Transient
//	final transient Integer snatchAttemptsDone = 0;
//	@Transient
//	final transient Integer cleanJerkAttemptsDone = 0;
//	@Transient
//	Date lastLiftTime = null;
//	@Transient
//	final transient Integer nextAttemptRequestedWeight = 0;

	/** The lift order rank. */
	/*
	 * Non-persistent properties. These properties are used during computations, but
	 * need not be stored in the database
	 */
	@Transient
	Integer liftOrderRank = 0;

	/** The result order rank. */
	@Transient
	Integer resultOrderRank = 0;

	/** The current lifter. */
	@Transient
	boolean currentLifter = false;

	/** The forced as current. */
	@Transient
	boolean forcedAsCurrent = false;

	private Double customScore;

	/**
	 * Checks if is empty.
	 *
	 * @param value the value
	 * @return true, if is empty
	 */
	public static boolean isEmpty(String value) {
		return (value == null) || value.trim()
			.isEmpty();
	}

	/**
	 * Zero if invalid.
	 *
	 * @param value the value
	 * @return the int
	 */
	public static int zeroIfInvalid(String value) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	/**
	 * Instantiates a new athlete.
	 */
	public Athlete() {
		super();
	}

	/**
	 * Failed lift.
	 */
	public void failedLift() {
		logger.debug("{}", this); //$NON-NLS-1$
		final String weight = Integer.toString(-getNextAttemptRequestedWeight());
		doLift(weight);
	}

	/**
	 * Gets the age group.
	 *
	 * @return the ageGroup. M80 if male missing birth date, F70 if female missing
	 *         birth date or missing both gender and birth.
	 */
	public Integer getAgeGroup() {
		Integer yob = this.getYearOfBirth();
		if (yob == null) {
			yob = 1900;
		}
		String gender2 = this.getGender();
		if (gender2 == null || gender2.trim().isEmpty()) {
			gender2 = "F";
		}
		int year1 = Calendar.getInstance().get(Calendar.YEAR);
		final int age = year1 - yob;
		if (age <= 17) {
			return 17;
		} else if (age <= 20) {
			return 20;
		} else if (age < 35) {
			return 34;
		}
		int ageGroup1 = (int) (Math.ceil(age / 5) * 5);

		if (gender2.equals("F") && ageGroup1 >= 70) { //$NON-NLS-1$
			return 70;
		}
		if (gender2.equals("M") && ageGroup1 >= 80) { //$NON-NLS-1$
			return 80;
		}
		// normal case
		return ageGroup1;
	}

	/**
	 * Gets the masters age group interval.
	 *
	 * @return the ageGroup
	 */
	public String getMastersAgeGroupInterval() {
		Integer ageGroup1 = getAgeGroup();
		if (this.getGender()
			.equals("F") && ageGroup1 >= 70) { //$NON-NLS-1$
			return "70+";
		}
		if (this.getGender()
			.equals("M") && ageGroup1 >= 80) { //$NON-NLS-1$
			return "80+";
		}
		if (ageGroup1 == 17) {
			return "0-17";
		} else if (ageGroup1 == 20) {
			return "18-20";
		} else if (ageGroup1 == 34) {
			return "21+";
		}
		return ageGroup1 + "-" + (ageGroup1 + 4);
	}

	/**
	 * Gets the masters gender age group interval.
	 *
	 * @return the masters gender age group interval
	 */
	public String getMastersGenderAgeGroupInterval() {
		String gender2 = getGender();
		if (gender2 == "F")
			gender2 = "W";
		return gender2.toUpperCase() + getMastersAgeGroupInterval();
	}

	/**
	 * Gets the attempts done.
	 *
	 * @return the attemptsDone
	 */
	public Integer getAttemptsDone() {
		return getSnatchAttemptsDone() + getCleanJerkAttemptsDone();
	}

	/**
	 * Gets the best clean jerk.
	 *
	 * @return the bestCleanJerk
	 */
	public Integer getBestCleanJerk() {
		final int cj1 = zeroIfInvalid(cleanJerk1ActualLift);
		final int cj2 = zeroIfInvalid(cleanJerk2ActualLift);
		final int cj3 = zeroIfInvalid(cleanJerk3ActualLift);
		return max(0, cj1, cj2, cj3);
	}

	/**
	 * Gets the best clean jerk attempt number.
	 *
	 * @return the best clean jerk attempt number
	 */
	public int getBestCleanJerkAttemptNumber() {
		int referenceValue = getBestCleanJerk();
		if (referenceValue > 0) {
			if (zeroIfInvalid(cleanJerk3ActualLift) == referenceValue)
				return 6;
			if (zeroIfInvalid(cleanJerk2ActualLift) == referenceValue)
				return 5;
			if (zeroIfInvalid(cleanJerk1ActualLift) == referenceValue)
				return 4;
		}
		return 0; // no match - bomb-out.
	}

	/**
	 * Gets the best result attempt number.
	 *
	 * @return the best result attempt number
	 */
	public int getBestResultAttemptNumber() {
		int referenceValue = getBestCleanJerk();
		if (referenceValue > 0) {
			if (zeroIfInvalid(cleanJerk3ActualLift) == referenceValue)
				return 6;
			if (zeroIfInvalid(cleanJerk2ActualLift) == referenceValue)
				return 5;
			if (zeroIfInvalid(cleanJerk1ActualLift) == referenceValue)
				return 4;
		} else {
			if (referenceValue > 0) {
				referenceValue = getBestSnatch();
				if (zeroIfInvalid(snatch3ActualLift) == referenceValue)
					return 3;
				if (zeroIfInvalid(snatch2ActualLift) == referenceValue)
					return 2;
				if (zeroIfInvalid(snatch1ActualLift) == referenceValue)
					return 1;
			}
		}
		return 0; // no match - bomb-out.
	}

	/**
	 * Gets the best snatch.
	 *
	 * @return the bestSnatch
	 */
	public Integer getBestSnatch() {
		final int sn1 = zeroIfInvalid(snatch1ActualLift);
		final int sn2 = zeroIfInvalid(snatch2ActualLift);
		final int sn3 = zeroIfInvalid(snatch3ActualLift);
		return max(0, sn1, sn2, sn3);
	}

	/**
	 * Gets the best snatch attempt number.
	 *
	 * @return the best snatch attempt number
	 */
	public int getBestSnatchAttemptNumber() {
		int referenceValue = getBestSnatch();
		if (referenceValue > 0) {
			if (zeroIfInvalid(snatch3ActualLift) == referenceValue)
				return 3;
			if (zeroIfInvalid(snatch2ActualLift) == referenceValue)
				return 2;
			if (zeroIfInvalid(snatch1ActualLift) == referenceValue)
				return 1;
		}
		return 0; // no match - bomb-out.

	}

	/**
	 * Set all date fields consistently.
	 *
	 * @param newBirthDateAsDate
	 */

	private void setFullBirthDate(Integer yearOfBirth) {
		if (yearOfBirth != null) {
			this.fullBirthDate = LocalDate.of(yearOfBirth, 1, 1);
		} else {
			this.fullBirthDate = null;
		}
	}

	/**
	 * Sets the full birth date.
	 *
	 * @param fullBirthDate the fullBirthDate to set
	 */
	public void setFullBirthDate(LocalDate fullBirthDate) {
		this.fullBirthDate = fullBirthDate;
	}

	/**
	 * Gets the full birth date.
	 *
	 * @return the fullBirthDate
	 */
	public LocalDate getFullBirthDate() {
		return fullBirthDate;
	}

	/**
	 * Gets the birth date.
	 *
	 * @return the birthDate
	 * @deprecated use
	 */
	@Deprecated
	@Transient
	public Integer getBirthDate() {
		return this.getYearOfBirth();
	};

	/**
	 * Sets the birth date.
	 *
	 * @param birthYear the new birth date
	 */
	@Deprecated
	@Transient
	public void setBirthDate(Integer birthYear) {
		setYearOfBirth(birthYear);
	}

	/**
	 * Gets the year of birth.
	 *
	 * @return the year of birth (1900 if both birthDate and fullBirthDate are null)
	 */
	public Integer getYearOfBirth() {
		if (this.fullBirthDate != null) {
			return fullBirthDate.getYear();
		} else {
			return 1900;
		}
	};

	/**
	 * Sets the year of birth.
	 *
	 * @param birthYear the new year of birth
	 */
	public void setYearOfBirth(Integer birthYear) {
		setFullBirthDate(birthYear);
	}

	/**
	 * Gets the body weight.
	 *
	 * @return the bodyWeight
	 */
	public Double getBodyWeight() {
		return bodyWeight;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	};

	/**
	 * Compute the body weight at the maximum weight of the Athlete's category.
	 * Note: for the purpose of this computation, only "official" categories are
	 * used as the purpose is to totalRank athletes according to their competition
	 * potential.
	 *
	 * @return the category sinclair
	 */
	public Double getCategorySinclair() {
		Category category = getCategory();
		if (category == null)
			return 0.0;
		Double categoryWeight = category.getMaximumWeight();
		final Integer total1 = getTotal();
		if (total1 == null || total1 < 0.1)
			return 0.0;
		if (getGender().equalsIgnoreCase("M")) { //$NON-NLS-1$
			if (categoryWeight < 56.0) {
				categoryWeight = 56.0;
			} else if (categoryWeight > SinclairCoefficients.menMaxWeight()) {
				categoryWeight = SinclairCoefficients.menMaxWeight();
			}
		} else {
			if (categoryWeight < 48.0) {
				categoryWeight = 48.0;
			} else if (categoryWeight > SinclairCoefficients.womenMaxWeight()) {
				categoryWeight = SinclairCoefficients.womenMaxWeight();
			}
		}
		return getSinclair(categoryWeight);
	};

	/**
	 * Gets the clean jerk 1 actual lift.
	 *
	 * @return the clean jerk 1 actual lift
	 */
	public String getCleanJerk1ActualLift() {
		return emptyIfNull(cleanJerk1ActualLift);
	};

	/**
	 * Gets the clean jerk 1 as integer.
	 *
	 * @return the clean jerk 1 as integer
	 */
	public Integer getCleanJerk1AsInteger() {
		return asInteger(cleanJerk1ActualLift);
	}

	/**
	 * As integer.
	 *
	 * @param stringValue the string value
	 * @return the integer
	 */
	protected Integer asInteger(String stringValue) {
		if (stringValue == null)
			return null;
		try {
			return Integer.parseInt(stringValue);
		} catch (NumberFormatException nfe) {
			return null;
		}
	};

	/**
	 * Gets the clean jerk 1 automatic progression.
	 *
	 * @return the clean jerk 1 automatic progression
	 */
	public String getCleanJerk1AutomaticProgression() {
		return "-"; // there is no such thing. //$NON-NLS-1$
	};

	/**
	 * Gets the clean jerk 1 change 1.
	 *
	 * @return the clean jerk 1 change 1
	 */
	public String getCleanJerk1Change1() {
		return emptyIfNull(cleanJerk1Change1);
	};

	/**
	 * Gets the clean jerk 1 change 2.
	 *
	 * @return the clean jerk 1 change 2
	 */
	public String getCleanJerk1Change2() {
		return emptyIfNull(cleanJerk1Change2);
	};

	/**
	 * Gets the clean jerk 1 declaration.
	 *
	 * @return the clean jerk 1 declaration
	 */
	public String getCleanJerk1Declaration() {
		return emptyIfNull(cleanJerk1Declaration);
	};

	/**
	 * Gets the clean jerk 1 lift time.
	 *
	 * @return the clean jerk 1 lift time
	 */
	public Date getCleanJerk1LiftTime() {
		return cleanJerk1LiftTime;
	}

	/**
	 * Gets the clean jerk 2 actual lift.
	 *
	 * @return the clean jerk 2 actual lift
	 */
	public String getCleanJerk2ActualLift() {
		return emptyIfNull(cleanJerk2ActualLift);
	}

	/**
	 * Gets the clean jerk 2 as integer.
	 *
	 * @return the clean jerk 2 as integer
	 */
	public Integer getCleanJerk2AsInteger() {
		return asInteger(cleanJerk2ActualLift);
	}

	/**
	 * Gets the clean jerk 2 automatic progression.
	 *
	 * @return the clean jerk 2 automatic progression
	 */
	public String getCleanJerk2AutomaticProgression() {
		final int prevVal = zeroIfInvalid(cleanJerk1ActualLift);
		return doAutomaticProgression(prevVal);
	}

	/**
	 * Gets the clean jerk 2 change 1.
	 *
	 * @return the clean jerk 2 change 1
	 */
	public String getCleanJerk2Change1() {
		return emptyIfNull(cleanJerk2Change1);
	}

	/**
	 * Gets the clean jerk 2 change 2.
	 *
	 * @return the clean jerk 2 change 2
	 */
	public String getCleanJerk2Change2() {
		return emptyIfNull(cleanJerk2Change2);
	}

	/**
	 * Gets the clean jerk 2 declaration.
	 *
	 * @return the clean jerk 2 declaration
	 */
	public String getCleanJerk2Declaration() {
		return emptyIfNull(cleanJerk2Declaration);
	}

	/**
	 * Gets the clean jerk 2 lift time.
	 *
	 * @return the clean jerk 2 lift time
	 */
	public Date getCleanJerk2LiftTime() {
		return cleanJerk2LiftTime;
	}

	/**
	 * Gets the clean jerk 3 actual lift.
	 *
	 * @return the clean jerk 3 actual lift
	 */
	public String getCleanJerk3ActualLift() {
		return emptyIfNull(cleanJerk3ActualLift);
	}

	/**
	 * Gets the clean jerk 3 as integer.
	 *
	 * @return the clean jerk 3 as integer
	 */
	public Integer getCleanJerk3AsInteger() {
		return asInteger(cleanJerk3ActualLift);
	}

	/**
	 * Gets the clean jerk 3 automatic progression.
	 *
	 * @return the clean jerk 3 automatic progression
	 */
	public String getCleanJerk3AutomaticProgression() {
		final int prevVal = zeroIfInvalid(cleanJerk2ActualLift);
		return doAutomaticProgression(prevVal);
	}

	/**
	 * Gets the clean jerk 3 change 1.
	 *
	 * @return the clean jerk 3 change 1
	 */
	public String getCleanJerk3Change1() {
		return emptyIfNull(cleanJerk3Change1);
	}

	/**
	 * Gets the clean jerk 3 change 2.
	 *
	 * @return the clean jerk 3 change 2
	 */
	public String getCleanJerk3Change2() {
		return emptyIfNull(cleanJerk3Change2);
	}

	/**
	 * Gets the clean jerk 3 declaration.
	 *
	 * @return the clean jerk 3 declaration
	 */
	public String getCleanJerk3Declaration() {
		return emptyIfNull(cleanJerk3Declaration);
	}

	/**
	 * Gets the clean jerk 3 lift time.
	 *
	 * @return the clean jerk 3 lift time
	 */
	public Date getCleanJerk3LiftTime() {
		return cleanJerk3LiftTime;
	}

	/**
	 * Gets the clean jerk attempts done.
	 *
	 * @return the cleanJerkAttemptsDone
	 */
	public Integer getCleanJerkAttemptsDone() {
		// if Athlete signals he wont take his remaining tries, a zero is entered
		// further lifts are not counted.
		int attempts = 0;
		if (!isEmpty(cleanJerk1ActualLift)) {
			attempts++;
		} else {
			return attempts;
		}
		if (!isEmpty(cleanJerk2ActualLift)) {
			attempts++;
		} else {
			return attempts;
		}
		if (!isEmpty(cleanJerk3ActualLift)) {
			attempts++;
		} else {
			return attempts;
		}
		return attempts;
	}

	/**
	 * Gets the clean jerk points.
	 *
	 * @return the clean jerk points
	 */
	public Float getCleanJerkPoints() {
		if (cleanJerkPoints == null)
			return 0.0F;
		return cleanJerkPoints;
	}

	/**
	 * Gets the clean jerk rank.
	 *
	 * @return the clean jerk rank
	 */
	public Integer getCleanJerkRank() {
		return cleanJerkRank;
	}

	/**
	 * Gets the clean jerk total.
	 *
	 * @return total for clean and jerk
	 */
	public int getCleanJerkTotal() {
		final int cleanJerkTotal = max(0,
			zeroIfInvalid(cleanJerk1ActualLift),
			zeroIfInvalid(cleanJerk2ActualLift),
			zeroIfInvalid(cleanJerk3ActualLift));
		return cleanJerkTotal;
	}

	/**
	 * Gets the club.
	 *
	 * @return the club
	 */
	public String getClub() {
		return getTeam();
	}

	/**
	 * Gets the team.
	 *
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * Gets the combined points.
	 *
	 * @return the combined points
	 */
	public Float getCombinedPoints() {
		return getSnatchPoints() + getCleanJerkPoints() + getTotalPoints();
	}

	/**
	 * Gets the current lifter.
	 *
	 * @return the current lifter
	 */
	public boolean getCurrentLifter() {
		return currentLifter;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the forced as current.
	 *
	 * @return the forced as current
	 */
	public boolean getForcedAsCurrent() {
		return forcedAsCurrent;
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
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
	 * Compute the time of last lift for Athlete. Times are only compared within the
	 * same lift type (if a Athlete is at the first attempt of clean and jerk, then
	 * the last lift occurred forever ago.)
	 *
	 * @return null if Athlete has not lifted
	 */
	public Date getPreviousLiftTime() {
		Date max = null; // long ago

		if (getAttemptsDone() <= 3) {
			final Date sn1 = snatch1LiftTime;
			if (sn1 != null) {
				max = sn1;
			} else {
				return max;
			}
			final Date sn2 = snatch2LiftTime;
			if (sn2 != null) {
				max = (max.after(sn2) ? max : sn2);
			} else {
				return max;
			}
			final Date sn3 = snatch3LiftTime;
			if (sn3 != null) {
				max = (max.after(sn3) ? max : sn3);
			} else {
				return max;
			}
		} else {
			final Date cj1 = cleanJerk1LiftTime;
			if (cj1 != null) {
				max = cj1;
			} else {
				return max;
			}
			final Date cj2 = cleanJerk2LiftTime;
			if (cj2 != null) {
				max = (max.after(cj2) ? max : cj2);
			} else {
				return max;
			}
			final Date cj3 = cleanJerk3LiftTime;
			if (cj3 != null) {
				max = (max.after(cj3) ? max : cj3);
			} else {
				return max;
			}
		}

		return max;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the last successful lift time.
	 *
	 * @return the last successful lift time
	 */
	public Date getLastSuccessfulLiftTime() {
		if (zeroIfInvalid(cleanJerk3ActualLift) > 0)
			return getCleanJerk3LiftTime();
		if (zeroIfInvalid(cleanJerk2ActualLift) > 0)
			return getCleanJerk2LiftTime();
		if (zeroIfInvalid(cleanJerk1ActualLift) > 0)
			return getCleanJerk1LiftTime();
		if (zeroIfInvalid(snatch3ActualLift) > 0)
			return getSnatch3LiftTime();
		if (zeroIfInvalid(snatch2ActualLift) > 0)
			return getSnatch2LiftTime();
		if (zeroIfInvalid(snatch1ActualLift) > 0)
			return getSnatch1LiftTime();
		return new Date(0L); // long ago
	}

	/**
	 * Gets the last attempted lift time.
	 *
	 * @return the last attempted lift time
	 */
	public Date getLastAttemptedLiftTime() {
		if (zeroIfInvalid(cleanJerk3ActualLift) != 0)
			return getCleanJerk3LiftTime();
		if (zeroIfInvalid(cleanJerk2ActualLift) != 0)
			return getCleanJerk2LiftTime();
		if (zeroIfInvalid(cleanJerk1ActualLift) != 0)
			return getCleanJerk1LiftTime();
		if (zeroIfInvalid(snatch3ActualLift) != 0)
			return getSnatch3LiftTime();
		if (zeroIfInvalid(snatch2ActualLift) != 0)
			return getSnatch2LiftTime();
		if (zeroIfInvalid(snatch1ActualLift) != 0)
			return getSnatch1LiftTime();
		return new Date(0L); // long ago
	}

	/**
	 * Gets the first attempted lift time.
	 *
	 * @return the first attempted lift time
	 */
	public Date getFirstAttemptedLiftTime() {
		if (zeroIfInvalid(snatch1ActualLift) != 0)
			return getSnatch1LiftTime();
		if (zeroIfInvalid(snatch2ActualLift) != 0)
			return getSnatch2LiftTime();
		if (zeroIfInvalid(snatch3ActualLift) != 0)
			return getSnatch3LiftTime();
		if (zeroIfInvalid(cleanJerk1ActualLift) != 0)
			return getCleanJerk1LiftTime();
		if (zeroIfInvalid(cleanJerk2ActualLift) != 0)
			return getCleanJerk2LiftTime();
		if (zeroIfInvalid(cleanJerk3ActualLift) != 0)
			return getCleanJerk3LiftTime();
		return new Date(Long.MAX_VALUE); // forever in the future
	}

	/**
	 * Gets the attempted lifts.
	 *
	 * @return the attempted lifts
	 */
	public int getAttemptedLifts() {
		int i = 0;
		if (zeroIfInvalid(snatch1ActualLift) != 0)
			i++;
		if (zeroIfInvalid(snatch2ActualLift) != 0)
			i++;
		if (zeroIfInvalid(snatch3ActualLift) != 0)
			i++;
		if (zeroIfInvalid(cleanJerk1ActualLift) != 0)
			i++;
		if (zeroIfInvalid(cleanJerk2ActualLift) != 0)
			i++;
		if (zeroIfInvalid(cleanJerk3ActualLift) != 0)
			i++;
		return i; // long ago
	}

	/**
	 * Gets the lift order rank.
	 *
	 * @return the lift order rank
	 */
	public Integer getLiftOrderRank() {
		return liftOrderRank;
	}

	/**
	 * Gets the lot number.
	 *
	 * @return the lotNumber
	 */
	public Integer getLotNumber() {
		return (lotNumber == null ? 0 : lotNumber);
	}

	/**
	 * Gets the membership.
	 *
	 * @return the membership
	 */
	public String getMembership() {
		return membership;
	}

	/**
	 * Gets the next attempt requested weight.
	 *
	 * @return the nextAttemptRequestedWeight
	 */
	public Integer getNextAttemptRequestedWeight() {
		int attempt = getAttemptsDone() + 1;
		return getRequestedWeightForAttempt(attempt);
	}

	/**
	 * Gets the requested weight for attempt.
	 *
	 * @param attempt the attempt
	 * @return the requested weight for attempt
	 */
	public Integer getRequestedWeightForAttempt(int attempt) {
		switch (attempt) {
		case 1:
			return last(zeroIfInvalid(getSnatch1AutomaticProgression()),
				zeroIfInvalid(snatch1Declaration),
				zeroIfInvalid(snatch1Change1),
				zeroIfInvalid(snatch1Change2));
		case 2:
			return last(zeroIfInvalid(getSnatch2AutomaticProgression()),
				zeroIfInvalid(snatch2Declaration),
				zeroIfInvalid(snatch2Change1),
				zeroIfInvalid(snatch2Change2));
		case 3:
			return last(zeroIfInvalid(getSnatch3AutomaticProgression()),
				zeroIfInvalid(snatch3Declaration),
				zeroIfInvalid(snatch3Change1),
				zeroIfInvalid(snatch3Change2));
		case 4:
			return last(zeroIfInvalid(getCleanJerk1AutomaticProgression()),
				zeroIfInvalid(cleanJerk1Declaration),
				zeroIfInvalid(cleanJerk1Change1),
				zeroIfInvalid(cleanJerk1Change2));
		case 5:
			return last(zeroIfInvalid(getCleanJerk2AutomaticProgression()),
				zeroIfInvalid(cleanJerk2Declaration),
				zeroIfInvalid(cleanJerk2Change1),
				zeroIfInvalid(cleanJerk2Change2));
		case 6:
			return last(zeroIfInvalid(getCleanJerk3AutomaticProgression()),
				zeroIfInvalid(cleanJerk3Declaration),
				zeroIfInvalid(cleanJerk3Change1),
				zeroIfInvalid(cleanJerk3Change2));
		}
		return 0;
	}

	/**
	 * Gets the qualifying total.
	 *
	 * @return the qualifying total
	 */
	public Integer getQualifyingTotal() {
		if (qualifyingTotal == null)
			return 0;
		return qualifyingTotal;
	}

	/**
	 * Gets the rank.
	 *
	 * @return the rank
	 */
	public Integer getRank() {
		return totalRank;
	}

	/**
	 * Gets the registration category.
	 *
	 * @return the registration category
	 */
	public Category getRegistrationCategory() {
		return category;
	}

	/**
	 * Gets the result order rank.
	 *
	 * @return the result order rank
	 */
	public Integer getResultOrderRank() {
		return resultOrderRank;
	}

	/**
	 * Compute the Sinclair total for the Athlete, that is, the total multiplied by
	 * a value that depends on the Athlete's body weight. This value extrapolates
	 * what the Athlete would have lifted if he/she had the bodymass of a
	 * maximum-weight Athlete.
	 *
	 * @return the sinclair-adjusted value for the Athlete
	 */
	public Double getSinclair() {
		final Double bodyWeight1 = getBodyWeight();
		if (bodyWeight1 == null)
			return 0.0;
		return getSinclair(bodyWeight1);
	}

	/**
	 * Gets the sinclair for delta.
	 *
	 * @return a Sinclair value even if snatch has not started
	 */
	public Double getSinclairForDelta() {
		final Double bodyWeight1 = getBodyWeight();
		if (bodyWeight1 == null)
			return 0.0;
		Integer total1 = getBestCleanJerk() + getBestSnatch();
		return getSinclair(bodyWeight1, total1);
	}

	/**
	 * Gets the sinclair.
	 *
	 * @param bodyWeight1 the body weight 1
	 * @return the sinclair
	 */
	public Double getSinclair(Double bodyWeight1) {
		Integer total1 = getTotal();
		return getSinclair(bodyWeight1, total1);
	}

	/**
	 * Gets the robi.
	 *
	 * @return the robi
	 */
	public Double getRobi() {
		Category c;
		if (Competition.getCurrent()
			.isUseRegistrationCategory()) {
			c = getRegistrationCategory();
		} else {
			c = getCategory();
		}

		if (c == null)
			return 0.0;
		if (c.getWr() == null || c.getWr() == 0)
			return 0.0;
		if (c.getRobiA() == null || c.getWr() <= 0.000001)
			return 0.0;
		double robi = c.getRobiA() * Math.pow(getTotal(), c.getRobiB());
		// System.err.println(robi);
		return robi;
	}

	private Double getSinclair(Double bodyWeight1, Integer total1) {
		if (total1 == null || total1 < 0.1)
			return 0.0;
		if (gender == null)
			return 0.0;
		if (gender.equalsIgnoreCase("M")) { //$NON-NLS-1$
			return total1 * sinclairFactor(bodyWeight1,
				SinclairCoefficients.menCoefficient(),
				SinclairCoefficients.menMaxWeight());
		} else {
			return total1 * sinclairFactor(bodyWeight1,
				SinclairCoefficients.womenCoefficient(),
				SinclairCoefficients.womenMaxWeight());
		}
	}

	/**
	 * Gets the sinclair factor.
	 *
	 * @return the sinclair factor
	 */
	public Double getSinclairFactor() {
		if (gender.equalsIgnoreCase("M")) { //$NON-NLS-1$
			return sinclairFactor(this.bodyWeight,
				SinclairCoefficients.menCoefficient(),
				SinclairCoefficients.menMaxWeight());
		} else {
			return sinclairFactor(this.bodyWeight,
				SinclairCoefficients.womenCoefficient(),
				SinclairCoefficients.womenMaxWeight());
		}
	}

	/**
	 * Gets the sinclair rank.
	 *
	 * @return the sinclair rank
	 */
	public Integer getSinclairRank() {
		return sinclairRank;
	}

	/**
	 * Gets the robi rank.
	 *
	 * @return the robi rank
	 */
	public Integer getRobiRank() {
		return robiRank;
	}

	/** The year. */
	static int year = Calendar.getInstance()
		.get(Calendar.YEAR);

	/**
	 * Gets the smm.
	 *
	 * @return the smm
	 */
	public Double getSmm() {
		final Integer birthDate1 = getYearOfBirth();
		if (birthDate1 == null)
			return 0.0;
		return getSinclair() * SinclairCoefficients.getSMMCoefficient(year - birthDate1);
	}

	/**
	 * Gets the snatch 1 actual lift.
	 *
	 * @return the snatch 1 actual lift
	 */
	public String getSnatch1ActualLift() {
		return emptyIfNull(snatch1ActualLift);
	}

	/**
	 * Gets the snatch 1 as integer.
	 *
	 * @return the snatch 1 as integer
	 */
	public Integer getSnatch1AsInteger() {
		return asInteger(snatch1ActualLift);
	}

	/**
	 * Gets the snatch 1 automatic progression.
	 *
	 * @return the snatch 1 automatic progression
	 */
	public String getSnatch1AutomaticProgression() {
		return "-"; // no such thing. //$NON-NLS-1$
	}

	/**
	 * Gets the snatch 1 change 1.
	 *
	 * @return the snatch 1 change 1
	 */
	public String getSnatch1Change1() {
		return emptyIfNull(snatch1Change1);
	}

	/**
	 * Gets the snatch 1 change 2.
	 *
	 * @return the snatch 1 change 2
	 */
	public String getSnatch1Change2() {
		return emptyIfNull(snatch1Change2);
	}

	/**
	 * Gets the snatch 1 declaration.
	 *
	 * @return the snatch 1 declaration
	 */
	public String getSnatch1Declaration() {
		return emptyIfNull(snatch1Declaration);
	}

	/**
	 * Gets the snatch 1 lift time.
	 *
	 * @return the snatch 1 lift time
	 */
	public Date getSnatch1LiftTime() {
		return snatch1LiftTime;
	}

	/**
	 * Gets the snatch 2 actual lift.
	 *
	 * @return the snatch 2 actual lift
	 */
	public String getSnatch2ActualLift() {
		return emptyIfNull(snatch2ActualLift);
	}

	/**
	 * Gets the snatch 2 as integer.
	 *
	 * @return the snatch 2 as integer
	 */
	public Integer getSnatch2AsInteger() {
		return asInteger(snatch2ActualLift);
	}

	/**
	 * Gets the snatch 2 automatic progression.
	 *
	 * @return the snatch 2 automatic progression
	 */
	public String getSnatch2AutomaticProgression() {
		final int prevVal = zeroIfInvalid(snatch1ActualLift);
		return doAutomaticProgression(prevVal);
	}

	/**
	 * Gets the snatch 2 change 1.
	 *
	 * @return the snatch 2 change 1
	 */
	public String getSnatch2Change1() {
		return emptyIfNull(snatch2Change1);
	}

	/**
	 * Gets the snatch 2 change 2.
	 *
	 * @return the snatch 2 change 2
	 */
	public String getSnatch2Change2() {
		return emptyIfNull(snatch2Change2);
	}

	/**
	 * Gets the snatch 2 declaration.
	 *
	 * @return the snatch 2 declaration
	 */
	public String getSnatch2Declaration() {
		return emptyIfNull(snatch2Declaration);
	}

	/**
	 * Gets the snatch 2 lift time.
	 *
	 * @return the snatch 2 lift time
	 */
	public Date getSnatch2LiftTime() {
		return snatch2LiftTime;
	}

	/**
	 * Gets the snatch 3 actual lift.
	 *
	 * @return the snatch 3 actual lift
	 */
	public String getSnatch3ActualLift() {
		return emptyIfNull(snatch3ActualLift);
	}

	/**
	 * Gets the snatch 3 as integer.
	 *
	 * @return the snatch 3 as integer
	 */
	public Integer getSnatch3AsInteger() {
		return asInteger(snatch3ActualLift);
	}

	/**
	 * Gets the snatch 3 automatic progression.
	 *
	 * @return the snatch 3 automatic progression
	 */
	public String getSnatch3AutomaticProgression() {
		final int prevVal = zeroIfInvalid(snatch2ActualLift);
		return doAutomaticProgression(prevVal);
	}

	/**
	 * Gets the snatch 3 change 1.
	 *
	 * @return the snatch 3 change 1
	 */
	public String getSnatch3Change1() {
		return emptyIfNull(snatch3Change1);
	}

	/**
	 * Gets the snatch 3 change 2.
	 *
	 * @return the snatch 3 change 2
	 */
	public String getSnatch3Change2() {
		return emptyIfNull(snatch3Change2);
	}

	/**
	 * Gets the snatch 3 declaration.
	 *
	 * @return the snatch 3 declaration
	 */
	public String getSnatch3Declaration() {
		return emptyIfNull(snatch3Declaration);
	}

	/**
	 * Gets the snatch 3 lift time.
	 *
	 * @return the snatch 3 lift time
	 */
	public Date getSnatch3LiftTime() {
		return snatch3LiftTime;
	}

	/**
	 * Gets the snatch attempts done.
	 *
	 * @return how many snatch attempts have been performed
	 */
	public Integer getSnatchAttemptsDone() {
		// Athlete signals he wont take his remaining tries, a zero is entered
		// further lifts are not counted.
		int attempts = 0;
		if (!isEmpty(snatch1ActualLift)) {
			attempts++;
		} else {
			return attempts;
		}
		if (!isEmpty(snatch2ActualLift)) {
			attempts++;
		} else {
			return attempts;
		}
		if (!isEmpty(snatch3ActualLift)) {
			attempts++;
		} else {
			return attempts;
		}
		return attempts;
	}

	/**
	 * Gets the snatch points.
	 *
	 * @return the snatch points
	 */
	public Float getSnatchPoints() {
		if (snatchPoints == null)
			return 0.0F;
		return snatchPoints;
	}

	/**
	 * Gets the snatch rank.
	 *
	 * @return the snatch rank
	 */
	public Integer getSnatchRank() {
		return snatchRank;
	}

	/**
	 * Gets the snatch total.
	 *
	 * @return total for snatch.
	 */
	public int getSnatchTotal() {
		final int snatchTotal = max(0,
			zeroIfInvalid(snatch1ActualLift),
			zeroIfInvalid(snatch2ActualLift),
			zeroIfInvalid(snatch3ActualLift));
		return snatchTotal;
	}

	/**
	 * Gets the team clean jerk rank.
	 *
	 * @return the team clean jerk rank
	 */
	public Integer getTeamCleanJerkRank() {
		return teamCleanJerkRank;
	}

	/**
	 * Gets the team member.
	 *
	 * @return the team member
	 */
	public Boolean getTeamMember() {
		if (teamMember == null)
			return true;
		return teamMember;
	}

	/**
	 * Checks if is a team member.
	 *
	 * @return true, if is a team member
	 */
	public boolean isATeamMember() {
		if (teamMember == null)
			return true;
		return teamMember;
	}

	/**
	 * Gets the team snatch rank.
	 *
	 * @return the team snatch rank
	 */
	public Integer getTeamSnatchRank() {
		return teamSnatchRank;
	}

	/**
	 * Gets the team total rank.
	 *
	 * @return the team total rank
	 */
	public Integer getTeamTotalRank() {
		return teamTotalRank;
	}

	/**
	 * Total is zero if all three snatches or all three clean&jerks are failed.
	 * Failed lifts are indicated as negative amounts. Total is the sum of all good
	 * lifts otherwise. Null entries indicate that no data has been captured, and
	 * are counted as zero.
	 *
	 * @return the total
	 */
	public Integer getTotal() {
		final int snatchTotal = getSnatchTotal();
		if (snatchTotal == 0)
			return 0;
		final int cleanJerkTotal = getCleanJerkTotal();
		if (cleanJerkTotal == 0)
			return 0;
		return snatchTotal + cleanJerkTotal;
	}

	/**
	 * Gets the total points.
	 *
	 * @return the total points
	 */
	public Float getTotalPoints() {
		if (totalPoints == null)
			return 0.0F;
		return totalPoints;
	}

	/**
	 * Gets the total rank.
	 *
	 * @return the total rank
	 */
	public Integer getTotalRank() {
		return totalRank;
	}

	/**
	 * Checks if is current lifter.
	 *
	 * @return true, if is current lifter
	 */
	public boolean isCurrentLifter() {
		return currentLifter;
	}

	/**
	 * Checks if is forced as current.
	 *
	 * @return true, if is forced as current
	 */
	public boolean isForcedAsCurrent() {
		return forcedAsCurrent;
	}

	/**
	 * Checks if is invited.
	 *
	 * @return true, if is invited
	 */
	public boolean isInvited() {
		final Locale locale = Competition.getCurrent()
			.getLocale();
//        int threshold = Competition.invitedIfBornBefore();
//
//        Integer birthDate2 = getYearOfBirth();

//        return birthDate2 == null
//                || (birthDate2 < threshold)
//                ||
		return membership.equalsIgnoreCase(Messages.getString("Athlete.InvitedAbbreviated", locale)) //$NON-NLS-1$
		// || !getTeamMember()
		;
	}

	/**
	 * Reset forced as current.
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.event.MethodEventSource#removeListener(java.lang.Class,
	 * java.lang.Object)
	 */
	public void resetForcedAsCurrent() {
		this.forcedAsCurrent = false;
	}

	/**
	 * Sets the as current lifter.
	 *
	 * @param currentLifter the new as current lifter
	 */
	public void setAsCurrentLifter(Boolean currentLifter) {
		// if (currentLifter)
		// System.err.println("Athlete.setAsCurrentLifter(): current Athlete is now
		// "+getLastName()+" "+getFirstName());
		this.currentLifter = currentLifter;
		if (currentLifter) {
			logger.info("{} is current Athlete", this);
		}
	}

	/**
	 * Sets the attempts done.
	 *
	 * @param i the new attempts done
	 */
	public void setAttemptsDone(Integer i) {
	}

	/**
	 * Sets the best clean jerk.
	 *
	 * @param i the new best clean jerk
	 */
	public void setBestCleanJerk(Integer i) {
	}

	/**
	 * Sets the best snatch.
	 *
	 * @param i the new best snatch
	 */
	public void setBestSnatch(Integer i) {
	}

	/**
	 * Sets the body weight.
	 *
	 * @param bodyWeight the bodyWeight to set
	 */
	public void setBodyWeight(Double bodyWeight) {
		this.bodyWeight = bodyWeight;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * Sets the clean jerk 1 actual lift.
	 *
	 * @param cleanJerk1ActualLift the new clean jerk 1 actual lift
	 */
	public void setCleanJerk1ActualLift(String cleanJerk1ActualLift) {
		validateActualCleanJerk1(cleanJerk1ActualLift);
		this.cleanJerk1ActualLift = cleanJerk1ActualLift;
		if (zeroIfInvalid(cleanJerk1ActualLift) == 0)
			this.cleanJerk1LiftTime = (null);
		else
			this.cleanJerk1LiftTime = sqlNow();
		logger.info("{} cleanJerk1ActualLift={}", this, cleanJerk1ActualLift);
	}

	public boolean validateActualCleanJerk1(String cleanJerk1ActualLift) {
		try {
			validateActualLift(1,
				getCleanJerk1AutomaticProgression(),
				cleanJerk1Declaration,
				cleanJerk1Change1,
				cleanJerk1Change2,
				cleanJerk1ActualLift);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Sets the clean jerk 1 automatic progression.
	 *
	 * @param s the new clean jerk 1 automatic progression
	 */
	public void setCleanJerk1AutomaticProgression(String s) {
	}

	/**
	 * Sets the clean jerk 1 change 1.
	 *
	 * @param cleanJerk1Change1 the new clean jerk 1 change 1
	 */
	public void setCleanJerk1Change1(String cleanJerk1Change1) {
		if ("0".equals(cleanJerk1Change1)) {
			this.cleanJerk1Change1 = cleanJerk1Change1;
			logger.info("{} cleanJerk1Change1={}", this, cleanJerk1Change1);
			setCleanJerk1ActualLift("0");
			return;
		}
		validateCleanJerk1Change1(cleanJerk1Change1);
		this.cleanJerk1Change1 = cleanJerk1Change1;
		checkStartingTotalsRule(true);

		logger.info("{} cleanJerk1Change1={}", this, cleanJerk1Change1);
	}

	public boolean validateCleanJerk1Change1(String cleanJerk1Change1) throws RuleViolationException {
		validateChange1(1,
			getCleanJerk1AutomaticProgression(),
			cleanJerk1Declaration,
			cleanJerk1Change1,
			cleanJerk1Change2,
			cleanJerk1ActualLift,
			false);
		return true;
	}

	/**
	 * Sets the clean jerk 1 change 2.
	 *
	 * @param cleanJerk1Change2 the new clean jerk 1 change 2
	 */
	public void setCleanJerk1Change2(String cleanJerk1Change2) {
		if ("0".equals(cleanJerk1Change2)) {
			this.cleanJerk1Change2 = cleanJerk1Change2;
			logger.info("{} cleanJerk1Change2={}", this, cleanJerk1Change2);
			setCleanJerk1ActualLift("0");
			return;
		}
		validateCleanJerk1Change2(cleanJerk1Change2);
		this.cleanJerk1Change2 = cleanJerk1Change2;
		checkStartingTotalsRule(true);

		logger.info("{} cleanJerk1Change2={}", this, cleanJerk1Change2);
	}

	public boolean validateCleanJerk1Change2(String cleanJerk1Change2) throws RuleViolationException {
		validateChange2(1,
			getCleanJerk1AutomaticProgression(),
			cleanJerk1Declaration,
			cleanJerk1Change1,
			cleanJerk1Change2,
			cleanJerk1ActualLift,
			false);
		return true;
	}

	/**
	 * Sets the clean jerk 1 declaration.
	 *
	 * @param cleanJerk1Declaration the new clean jerk 1 declaration
	 */
	public void setCleanJerk1Declaration(String cleanJerk1Declaration) {
		if ("0".equals(cleanJerk1Declaration)) {
			this.cleanJerk1Declaration = cleanJerk1Declaration;
			logger.info("{} cleanJerk1Declaration={}", this, cleanJerk1Declaration);
			setCleanJerk1ActualLift("0");
			return;
		}

		validateDeclaration(1,
			getCleanJerk1AutomaticProgression(),
			cleanJerk1Declaration,
			cleanJerk1Change1,
			cleanJerk1Change2,
			cleanJerk1ActualLift,
			false);
		this.cleanJerk1Declaration = cleanJerk1Declaration;
		if (zeroIfInvalid(getSnatch1Declaration()) > 0)
			checkStartingTotalsRule(true);

		logger.info("{} cleanJerk1Declaration={}", this, cleanJerk1Declaration);
	}

	/**
	 * Sets the clean jerk 1 lift time.
	 *
	 * @param date the new clean jerk 1 lift time
	 */
	public void setCleanJerk1LiftTime(java.util.Date date) {
	}

	/**
	 * Sets the clean jerk 2 actual lift.
	 *
	 * @param cleanJerk2ActualLift the new clean jerk 2 actual lift
	 */
	public void setCleanJerk2ActualLift(String cleanJerk2ActualLift) {
		validateActualCleanJerk2(cleanJerk2ActualLift);
		this.cleanJerk2ActualLift = cleanJerk2ActualLift;
		if (zeroIfInvalid(cleanJerk2ActualLift) == 0)
			this.cleanJerk2LiftTime = (null);
		else
			this.cleanJerk2LiftTime = sqlNow();
		logger.info("{} cleanJerk2ActualLift={}", this, cleanJerk2ActualLift);
	}

	public boolean validateActualCleanJerk2(String cleanJerk2ActualLift) {
		try {
			validateActualLift(2,
				getCleanJerk2AutomaticProgression(),
				cleanJerk2Declaration,
				cleanJerk2Change2,
				cleanJerk2Change2,
				cleanJerk2ActualLift);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Sets the clean jerk 2 automatic progression.
	 *
	 * @param s the new clean jerk 2 automatic progression
	 */
	public void setCleanJerk2AutomaticProgression(String s) {
	}

	/**
	 * Sets the clean jerk 2 change 1.
	 *
	 * @param cleanJerk2Change1 the new clean jerk 2 change 1
	 */
	public void setCleanJerk2Change1(String cleanJerk2Change1) {
		if ("0".equals(cleanJerk2Change1)) {
			this.cleanJerk2Change1 = cleanJerk2Change1;
			logger.info("{} cleanJerk2Change1={}", this, cleanJerk2Change1);
			setCleanJerk2ActualLift("0");
			return;
		}
		validateCleanJerk2Change1(cleanJerk2Change1);
		this.cleanJerk2Change1 = cleanJerk2Change1;
		logger.info("{} cleanJerk2Change1={}", this, cleanJerk2Change1);
	}

	public boolean validateCleanJerk2Change1(String cleanJerk2Change1) throws RuleViolationException {
		validateChange1(2,
			getCleanJerk2AutomaticProgression(),
			cleanJerk2Declaration,
			cleanJerk2Change1,
			cleanJerk2Change2,
			cleanJerk2ActualLift,
			false);
		return true;
	}

	/**
	 * Sets the clean jerk 2 change 2.
	 *
	 * @param cleanJerk2Change2 the new clean jerk 2 change 2
	 */
	public void setCleanJerk2Change2(String cleanJerk2Change2) {
		if ("0".equals(cleanJerk2Change2)) {
			this.cleanJerk2Change2 = cleanJerk2Change2;
			logger.info("{} cleanJerk2Change2={}", this, cleanJerk2Change2);
			setCleanJerk2ActualLift("0");
			return;
		}
		validateCleanJerk2Change2(cleanJerk2Change2);
		this.cleanJerk2Change2 = cleanJerk2Change2;
		logger.info("{} cleanJerk2Change2={}", this, cleanJerk2Change2);
	}

	public boolean validateCleanJerk2Change2(String cleanJerk2Change2) throws RuleViolationException {
		validateChange2(2,
			getCleanJerk2AutomaticProgression(),
			cleanJerk2Declaration,
			cleanJerk2Change1,
			cleanJerk2Change2,
			cleanJerk2ActualLift,
			false);
		return true;
	}

	/**
	 * Sets the clean jerk 2 declaration.
	 *
	 * @param cleanJerk2Declaration the new clean jerk 2 declaration
	 */
	public void setCleanJerk2Declaration(String cleanJerk2Declaration) {
		if ("0".equals(cleanJerk2Declaration)) {
			this.cleanJerk2Declaration = cleanJerk2Declaration;
			logger.info("{} cleanJerk2Declaration={}", this, cleanJerk2Declaration);
			setCleanJerk2ActualLift("0");
			return;
		}
		validateDeclaration(2,
			getCleanJerk2AutomaticProgression(),
			cleanJerk2Declaration,
			cleanJerk2Change1,
			cleanJerk2Change2,
			cleanJerk2ActualLift,
			false);
		this.cleanJerk2Declaration = cleanJerk2Declaration;
		logger.info("{} cleanJerk2Declaration={}", this, cleanJerk2Declaration);
	}

	/**
	 * Sets the clean jerk 2 lift time.
	 *
	 * @param cleanJerk2LiftTime the new clean jerk 2 lift time
	 */
	public void setCleanJerk2LiftTime(Date cleanJerk2LiftTime) {
	}

	/**
	 * Sets the clean jerk 3 actual lift.
	 *
	 * @param cleanJerk3ActualLift the new clean jerk 3 actual lift
	 */
	public void setCleanJerk3ActualLift(String cleanJerk3ActualLift) {
		validateActualCleanJerk3(cleanJerk3ActualLift);
		this.cleanJerk3ActualLift = cleanJerk3ActualLift;
		if (zeroIfInvalid(cleanJerk3ActualLift) == 0)
			this.cleanJerk3LiftTime = (null);
		else
			this.cleanJerk3LiftTime = sqlNow();
		logger.info("{} cleanJerk3ActualLift={}", this, cleanJerk3ActualLift);
	}

	public boolean validateActualCleanJerk3(String cleanJerk3ActualLift) throws RuleViolationException {
		validateActualLift(3,
			getCleanJerk3AutomaticProgression(),
			cleanJerk3Declaration,
			cleanJerk3Change1,
			cleanJerk3Change2,
			cleanJerk3ActualLift);
		// throws exception if invalid
		return true;
	}

	/**
	 * Sets the clean jerk 3 automatic progression.
	 *
	 * @param s the new clean jerk 3 automatic progression
	 */
	public void setCleanJerk3AutomaticProgression(String s) {
	}

	/**
	 * Sets the clean jerk 3 change 1.
	 *
	 * @param cleanJerk3Change1 the new clean jerk 3 change 1
	 */
	public void setCleanJerk3Change1(String cleanJerk3Change1) {
		if ("0".equals(cleanJerk3Change1)) {
			this.cleanJerk3Change1 = cleanJerk3Change1;
			logger.info("{} cleanJerk3Change1={}", this, cleanJerk3Change1);
			setCleanJerk3ActualLift("0");
			return;
		}
		validateCleanJerk3Change1(cleanJerk3Change1);
		this.cleanJerk3Change1 = cleanJerk3Change1;
		logger.info("{} cleanJerk3Change1={}", this, cleanJerk3Change1);
	}

	public boolean validateCleanJerk3Change1(String cleanJerk3Change1) throws RuleViolationException {
		validateChange1(3,
			getCleanJerk3AutomaticProgression(),
			cleanJerk3Declaration,
			cleanJerk3Change1,
			cleanJerk3Change2,
			cleanJerk3ActualLift,
			false);
		return true;
	}

	/**
	 * Sets the clean jerk 3 change 2.
	 *
	 * @param cleanJerk3Change2 the new clean jerk 3 change 2
	 */
	public void setCleanJerk3Change2(String cleanJerk3Change2) {
		if ("0".equals(cleanJerk3Change2)) {
			this.cleanJerk3Change2 = cleanJerk3Change2;
			logger.info("{} cleanJerk3Change2={}", this, cleanJerk3Change2);
			setCleanJerk3ActualLift("0");
			return;
		}

		validateCleanJerk3Change2(cleanJerk3Change2);
		this.cleanJerk3Change2 = cleanJerk3Change2;
		logger.info("{} cleanJerk3Change2={}", this, cleanJerk3Change2);
	}

	public boolean validateCleanJerk3Change2(String cleanJerk3Change2) throws RuleViolationException {
		validateChange2(3,
			getCleanJerk3AutomaticProgression(),
			cleanJerk3Declaration,
			cleanJerk3Change1,
			cleanJerk3Change2,
			cleanJerk3ActualLift,
			false);
		return true;
	}

	/**
	 * Sets the clean jerk 3 declaration.
	 *
	 * @param cleanJerk3Declaration the new clean jerk 3 declaration
	 */
	public void setCleanJerk3Declaration(String cleanJerk3Declaration) {
		if ("0".equals(cleanJerk3Declaration)) {
			this.cleanJerk3Declaration = cleanJerk3Declaration;
			logger.info("{} cleanJerk3Declaration={}", this, cleanJerk3Declaration);
			setCleanJerk3ActualLift("0");
			return;
		}
		validateDeclaration(3,
			getCleanJerk3AutomaticProgression(),
			cleanJerk3Declaration,
			cleanJerk3Change1,
			cleanJerk3Change2,
			cleanJerk3ActualLift,
			false);
		this.cleanJerk3Declaration = cleanJerk3Declaration;
		logger.info("{} cleanJerk3Declaration={}", this, cleanJerk3Declaration);
	}

	/**
	 * Sets the clean jerk 3 lift time.
	 *
	 * @param cleanJerk3LiftTime the new clean jerk 3 lift time
	 */
	public void setCleanJerk3LiftTime(Date cleanJerk3LiftTime) {
	}

	/**
	 * Sets the clean jerk attempts done.
	 *
	 * @param i the new clean jerk attempts done
	 */
	public void setCleanJerkAttemptsDone(Integer i) {
	}

	/**
	 * Sets the clean jerk points.
	 *
	 * @param cleanJerkPoints the new clean jerk points
	 */
	public void setCleanJerkPoints(Float cleanJerkPoints) {
		this.cleanJerkPoints = cleanJerkPoints;
	}

	/**
	 * Sets the clean jerk rank.
	 *
	 * @param cleanJerkRank the new clean jerk rank
	 */
	public void setCleanJerkRank(Integer cleanJerkRank) {
		this.cleanJerkRank = cleanJerkRank;
	}

	/**
	 * Sets the club.
	 *
	 * @param club the club to set
	 */
	public void setClub(String club) {
		setTeam(club);
	}

	/**
	 * Sets the team.
	 *
	 * @param club the new team
	 */
	public void setTeam(String club) {
		this.team = club;
	}

	/**
	 * Sets the current lifter.
	 *
	 * @param currentLifter the new current lifter
	 */
	public void setCurrentLifter(boolean currentLifter) {
		this.currentLifter = currentLifter;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the forced as current.
	 *
	 * @param forcedAsCurrent the new forced as current
	 */
	public void setForcedAsCurrent(boolean forcedAsCurrent) {
		logger.debug("setForcedAsCurrent({})", forcedAsCurrent); //$NON-NLS-1$
		this.forcedAsCurrent = forcedAsCurrent;
	}

	/**
	 * Sets the gender.
	 *
	 * @param string the gender to set
	 */
	public void setGender(String string) {
		if (string != null) {
			this.gender = string.toUpperCase();
		} else {
			this.gender = string;
		}
	}

	/**
	 * Sets the competition session.
	 *
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * Sets the last lift time.
	 *
	 * @param d the new last lift time
	 */
	public void setLastLiftTime(Date d) {
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets the lift order rank.
	 *
	 * @param liftOrder the new lift order rank
	 */
	public void setLiftOrderRank(Integer liftOrder) {
		this.liftOrderRank = liftOrder;
	}

	/**
	 * Sets the lot number.
	 *
	 * @param lotNumber the lotNumber to set
	 */
	public void setLotNumber(Integer lotNumber) {
		this.lotNumber = lotNumber;
	}

	/**
	 * Gets the start number.
	 *
	 * @return the start number
	 */
	public Integer getStartNumber() {
		return startNumber;
	}

	/**
	 * Sets the start number.
	 *
	 * @param startNumber the new start number
	 */
	public void setStartNumber(Integer startNumber) {
		this.startNumber = startNumber;
	}

	/**
	 * Sets the membership.
	 *
	 * @param membership the new membership
	 */
	public void setMembership(String membership) {
		this.membership = membership;
	}

	/**
	 * Sets the next attempt requested weight.
	 *
	 * @param i the new next attempt requested weight
	 */
	public void setNextAttemptRequestedWeight(Integer i) {
	}

	/**
	 * Sets the qualifying total.
	 *
	 * @param qualifyingTotal the new qualifying total
	 */
	public void setQualifyingTotal(Integer qualifyingTotal) {
		this.qualifyingTotal = qualifyingTotal;
	}

	/**
	 * Sets the rank.
	 *
	 * @param i the new rank
	 */
	public void setRank(Integer i) {
		this.totalRank = i;
	}

	/**
	 * Sets the registration category.
	 *
	 * @param registrationCategory the new registration category
	 */
	public void setRegistrationCategory(Category registrationCategory) {
		this.category = registrationCategory;
	}

	/**
	 * Sets the result order rank.
	 *
	 * @param resultOrderRank the result order rank
	 * @param rankingType     the ranking type
	 */
	public void setResultOrderRank(Integer resultOrderRank, Ranking rankingType) {
		this.resultOrderRank = resultOrderRank;
	}

	/**
	 * Sets the sinclair rank.
	 *
	 * @param sinclairRank the new sinclair rank
	 */
	public void setSinclairRank(Integer sinclairRank) {
		this.sinclairRank = sinclairRank;
	}

	/**
	 * Sets the robi rank.
	 *
	 * @param robiRank the new robi rank
	 */
	public void setRobiRank(Integer robiRank) {
		this.robiRank = robiRank;
	}

	/**
	 * Sets the snatch 1 actual lift.
	 *
	 * @param snatch1ActualLift the new snatch 1 actual lift
	 */
	public void setSnatch1ActualLift(String snatch1ActualLift) {
		validateActualSnatch1(snatch1ActualLift);
		this.snatch1ActualLift = snatch1ActualLift;
		if (zeroIfInvalid(snatch1ActualLift) == 0)
			this.snatch1LiftTime = null;
		else
			this.snatch1LiftTime = sqlNow();
		logger.info("{} snatch1ActualLift={}", this, snatch1ActualLift);
	}

	public boolean validateActualSnatch1(String snatch1ActualLift) throws RuleViolationException {
		validateActualLift(1,
			getSnatch1AutomaticProgression(),
			snatch1Declaration,
			snatch1Change1,
			snatch1Change2,
			snatch1ActualLift);
		return true;
	}

	private Date sqlNow() {
		return new Date(Calendar.getInstance()
			.getTime()
			.getTime());
	}

	/**
	 * Sets the snatch 1 automatic progression.
	 *
	 * @param s the new snatch 1 automatic progression
	 */
	public void setSnatch1AutomaticProgression(String s) {
	}

	/**
	 * Sets the snatch 1 change 1.
	 *
	 * @param snatch1Change1 the new snatch 1 change 1
	 */
	public void setSnatch1Change1(String snatch1Change1) {
		if ("0".equals(snatch1Change1)) {
			this.snatch1Change1 = snatch1Change1;
			logger.info("{} snatch1Change1={}", this, snatch1Change1);
			setSnatch1ActualLift("0");
			return;
		}
		validateSnatch1Change1(snatch1Change1);
		this.snatch1Change1 = snatch1Change1;
		checkStartingTotalsRule(true);

		logger.info("{} snatch1Change1={}", this, snatch1Change1);
	}

	public boolean validateSnatch1Change1(String snatch1Change1) throws RuleViolationException {
		validateChange1(1,
			getSnatch1AutomaticProgression(),
			snatch1Declaration,
			snatch1Change1,
			snatch1Change2,
			snatch1ActualLift,
			true);
		return true;
	}

	/**
	 * Sets the snatch 1 change 2.
	 *
	 * @param snatch1Change2 the new snatch 1 change 2
	 */
	public void setSnatch1Change2(String snatch1Change2) {
		if ("0".equals(snatch1Change2)) {
			this.snatch1Change2 = snatch1Change2;
			logger.info("{} snatch1Change2={}", this, snatch1Change2);
			setSnatch1ActualLift("0");
			return;
		}
		validateSnatch1Change2(snatch1Change2);
		this.snatch1Change2 = snatch1Change2;
		checkStartingTotalsRule(true);

		logger.info("{} snatch1Change2={}", this, snatch1Change2);
	}

	public boolean validateSnatch1Change2(String snatch1Change2) throws RuleViolationException {
		validateChange2(1,
			getSnatch1AutomaticProgression(),
			snatch1Declaration,
			snatch1Change1,
			snatch1Change2,
			snatch1ActualLift,
			true);
		return true;
	}

	/**
	 * Sets the snatch 1 declaration.
	 *
	 * @param snatch1Declaration the new snatch 1 declaration
	 */
	public void setSnatch1Declaration(String snatch1Declaration) {
		if ("0".equals(snatch1Declaration)) {
			this.snatch1Declaration = snatch1Declaration;
			logger.info("{} snatch1Declaration={}", this, snatch1Declaration);
			setSnatch1ActualLift("0");
			return;
		}
		validateDeclaration(1,
			getSnatch1AutomaticProgression(),
			snatch1Declaration,
			snatch1Change1,
			snatch1Change2,
			snatch1ActualLift,
			true);
		this.snatch1Declaration = snatch1Declaration;
		if (zeroIfInvalid(getCleanJerk1Declaration()) > 0)
			checkStartingTotalsRule(true);

		logger.info("{} snatch1Declaration={}", this, snatch1Declaration);
	}

	/**
	 * Sets the snatch 1 lift time.
	 *
	 * @param snatch1LiftTime the new snatch 1 lift time
	 */
	public void setSnatch1LiftTime(Date snatch1LiftTime) {
	}

	/**
	 * Sets the snatch 2 actual lift.
	 *
	 * @param snatch2ActualLift the new snatch 2 actual lift
	 */
	public void setSnatch2ActualLift(String snatch2ActualLift) {
		validateActualSnatch2(snatch2ActualLift);
		this.snatch2ActualLift = snatch2ActualLift;
		if (zeroIfInvalid(snatch2ActualLift) == 0)
			this.snatch2LiftTime = (null);
		else
			this.snatch2LiftTime = sqlNow();
		logger.info("{} snatch2ActualLift={}", this, snatch2ActualLift);
	}

	public boolean validateActualSnatch2(String snatch2ActualLift) {
		try {
			validateActualLift(3,
				getSnatch2AutomaticProgression(),
				snatch2Declaration,
				snatch2Change2,
				snatch2Change2,
				snatch2ActualLift);
			return true;
		} catch (RuleViolationException e) {
			return false;
		}
	}

	/**
	 * Sets the snatch 2 automatic progression.
	 *
	 * @param s the new snatch 2 automatic progression
	 */
	public void setSnatch2AutomaticProgression(String s) {
	}

	/**
	 * Sets the snatch 2 change 1.
	 *
	 * @param snatch2Change1 the new snatch 2 change 1
	 */
	public void setSnatch2Change1(String snatch2Change1) {
		if ("0".equals(snatch2Change1)) {
			this.snatch2Change1 = snatch2Change1;
			logger.info("{} snatch2Change1={}", this, snatch2Change1);
			setSnatch2ActualLift("0");
			return;
		}
		validateSnatch2Change1(snatch2Change1);
		this.snatch2Change1 = snatch2Change1;
		logger.info("{} snatch2Change1={}", this, snatch2Change1);
	}

	public boolean validateSnatch2Change1(String snatch2Change1) throws RuleViolationException {
		validateChange1(2,
			getSnatch2AutomaticProgression(),
			snatch2Declaration,
			snatch2Change1,
			snatch2Change2,
			snatch2ActualLift,
			true);
		return true;
	}

	/**
	 * Sets the snatch 2 change 2.
	 *
	 * @param snatch2Change2 the new snatch 2 change 2
	 */
	public void setSnatch2Change2(String snatch2Change2) {
		if ("0".equals(snatch2Change2)) {
			this.snatch2Change2 = snatch2Change2;
			logger.info("{} snatch2Change2={}", this, snatch2Change2);
			setSnatch2ActualLift("0");
			return;
		}
		validateSnatch2Change2(snatch2Change2);
		this.snatch2Change2 = snatch2Change2;
		logger.info("{} snatch2Change2={}", this, snatch2Change2);
	}

	public boolean validateSnatch2Change2(String snatch2Change2) throws RuleViolationException {
		validateChange2(2,
			getSnatch2AutomaticProgression(),
			snatch2Declaration,
			snatch2Change1,
			snatch2Change2,
			snatch2ActualLift,
			true);
		return true;
	}

	/**
	 * Sets the snatch 2 declaration.
	 *
	 * @param snatch2Declaration the new snatch 2 declaration
	 */
	public void setSnatch2Declaration(String snatch2Declaration) {
		if ("0".equals(snatch2Declaration)) {
			this.snatch2Declaration = snatch2Declaration;
			logger.info("{} snatch2Declaration={}", this, snatch2Declaration);
			setSnatch2ActualLift("0");
			return;
		}
		validateDeclaration(2,
			getSnatch2AutomaticProgression(),
			snatch2Declaration,
			snatch2Change1,
			snatch2Change2,
			snatch2ActualLift,
			true);
		this.snatch2Declaration = snatch2Declaration;
		logger.info("{} snatch2Declaration={}", this, snatch2Declaration);
	}

	/**
	 * Sets the snatch 2 lift time.
	 *
	 * @param snatch2LiftTime the new snatch 2 lift time
	 */
	public void setSnatch2LiftTime(Date snatch2LiftTime) {
	}

	/**
	 * Sets the snatch 3 actual lift.
	 *
	 * @param snatch3ActualLift the new snatch 3 actual lift
	 */
	public void setSnatch3ActualLift(String snatch3ActualLift) {
		validateActualSnatch3(snatch3ActualLift);
		this.snatch3ActualLift = snatch3ActualLift;
		if (zeroIfInvalid(snatch3ActualLift) == 0)
			this.snatch3LiftTime = (null);
		else
			this.snatch3LiftTime = sqlNow();
		logger.info("{} snatch3ActualLift={}", this, snatch3ActualLift);
	}

	public boolean validateActualSnatch3(String snatch2ActualLift) throws RuleViolationException {
		validateActualLift(3,
			getSnatch3AutomaticProgression(),
			snatch3Declaration,
			snatch3Change1,
			snatch3Change2,
			snatch3ActualLift);
		return true;
	}

	/**
	 * Sets the snatch 3 automatic progression.
	 *
	 * @param s the new snatch 3 automatic progression
	 */
	public void setSnatch3AutomaticProgression(String s) {
	}

	/**
	 * Sets the snatch 3 change 1.
	 *
	 * @param snatch3Change1 the new snatch 3 change 1
	 */
	public void setSnatch3Change1(String snatch3Change1) {
		if ("0".equals(snatch3Change1)) {
			this.snatch3Change1 = snatch3Change1;
			logger.info("{} snatch3Change1={}", this, snatch3Change1);
			setSnatch3ActualLift("0");
			return;
		}
		validateSnatch3Change1(snatch3Change1);
		this.snatch3Change1 = snatch3Change1;
		logger.info("{} snatch3Change1={}", this, snatch3Change1);
	}

	public boolean validateSnatch3Change1(String snatch3Change1) throws RuleViolationException {
		validateChange1(3,
			getSnatch3AutomaticProgression(),
			snatch3Declaration,
			snatch3Change1,
			snatch3Change2,
			snatch3ActualLift,
			true);
		return true;
	}

	/*
	 * General event framework: we implement the com.vaadin.event.MethodEventSource
	 * interface which defines how a notifier can call a method on a listener to
	 * signal that an event has occurred, and how the listener can
	 * register/unregister itself.
	 */

	/**
	 * Sets the snatch 3 change 2.
	 *
	 * @param snatch3Change2 the new snatch 3 change 2
	 */
	public void setSnatch3Change2(String snatch3Change2) {
		if ("0".equals(snatch3Change2)) {
			this.snatch3Change2 = snatch3Change2;
			logger.info("{} snatch3Change2={}", this, snatch3Change2);
			setSnatch3ActualLift("0");
			return;
		}
		validateSnatch3Change2(snatch3Change2);
		this.snatch3Change2 = snatch3Change2;
		logger.info("{} snatch3Change2={}", this, snatch3Change2);
	}

	public boolean validateSnatch3Change2(String snatch3Change2) throws RuleViolationException {
		validateChange2(3,
			getSnatch3AutomaticProgression(),
			snatch3Declaration,
			snatch3Change1,
			snatch3Change2,
			snatch3ActualLift,
			true);
		return true;
	}

	/**
	 * Sets the snatch 3 declaration.
	 *
	 * @param snatch3Declaration the new snatch 3 declaration
	 */
	public void setSnatch3Declaration(String snatch3Declaration) {
		if ("0".equals(snatch3Declaration)) {
			this.snatch3Declaration = snatch3Declaration;
			logger.info("{} snatch3Declaration={}", this, snatch3Declaration);
			setSnatch3ActualLift("0");
			return;
		}
		validateDeclaration(3,
			getSnatch3AutomaticProgression(),
			snatch3Declaration,
			snatch3Change1,
			snatch3Change2,
			snatch3ActualLift,
			true);
		this.snatch3Declaration = snatch3Declaration;
		logger.info("{} snatch3Declaration={}", this, snatch3Declaration);
	}

	/**
	 * Sets the snatch 3 lift time.
	 *
	 * @param snatch3LiftTime the new snatch 3 lift time
	 */
	public void setSnatch3LiftTime(Date snatch3LiftTime) {
	}

	/**
	 * Sets the snatch attempts done.
	 *
	 * @param i the new snatch attempts done
	 */
	public void setSnatchAttemptsDone(Integer i) {
	}

	/**
	 * Sets the snatch points.
	 *
	 * @param snatchPoints the new snatch points
	 */
	public void setSnatchPoints(float snatchPoints) {
		this.snatchPoints = snatchPoints;
	}

	/**
	 * Sets the snatch rank.
	 *
	 * @param snatchRank the new snatch rank
	 */
	public void setSnatchRank(Integer snatchRank) {
		this.snatchRank = snatchRank;
	}

	/**
	 * Sets the team clean jerk rank.
	 *
	 * @param teamCJRank the new team clean jerk rank
	 */
	public void setTeamCleanJerkRank(Integer teamCJRank) {
		this.teamCleanJerkRank = teamCJRank;
	}

	/**
	 * Sets the team combined rank.
	 *
	 * @param teamCombinedRank the new team combined rank
	 */
	public void setTeamCombinedRank(Integer teamCombinedRank) {
		this.teamCombinedRank = teamCombinedRank;
	}

	/**
	 * Sets the team member.
	 *
	 * @param teamMember the new team member
	 */
	public void setTeamMember(Boolean teamMember) {
		this.teamMember = Boolean.TRUE.equals(teamMember);
	}

	/**
	 * Sets the team snatch rank.
	 *
	 * @param teamSnatchRank the new team snatch rank
	 */
	public void setTeamSnatchRank(Integer teamSnatchRank) {
		this.teamSnatchRank = teamSnatchRank;
	}

	/**
	 * Sets the team total rank.
	 *
	 * @param teamTotalRank the new team total rank
	 */
	public void setTeamTotalRank(Integer teamTotalRank) {
		this.teamTotalRank = teamTotalRank;
	}

	/**
	 * Sets the team sinclair rank.
	 *
	 * @param teamSinclairRank the new team sinclair rank
	 */
	public void setTeamSinclairRank(Integer teamSinclairRank) {
		this.teamSinclairRank = teamSinclairRank;
	}

	/**
	 * Sets the team robi rank.
	 *
	 * @param teamRobiRank the new team robi rank
	 */
	public void setTeamRobiRank(Integer teamRobiRank) {
		this.teamRobiRank = teamRobiRank;
	}

	/**
	 * Sets the total.
	 *
	 * @param i the new total
	 */
	public void setTotal(Integer i) {
	}

	/**
	 * Sets the total points.
	 *
	 * @param totalPoints the new total points
	 */
	public void setTotalPoints(float totalPoints) {
		this.totalPoints = totalPoints;
	}

	/**
	 * Sets the total rank.
	 *
	 * @param totalRank the new total rank
	 */
	public void setTotalRank(Integer totalRank) {
		this.totalRank = totalRank;
	}

	/**
	 * Gets the custom score.
	 *
	 * @return the custom score
	 */
	public Double getCustomScore() {
		if (customScore == null || customScore < 0.01)
			return new Double(getTotal());
		return customScore;
	}

	/**
	 * Sets the custom score.
	 *
	 * @param customScore the new custom score
	 */
	public void setCustomScore(Double customScore) {
		this.customScore = customScore;
	}

	/**
	 * Sets the custom rank.
	 *
	 * @param customRank the new custom rank
	 */
	public void setCustomRank(Integer customRank) {
		this.customRank = customRank;
	}

	/**
	 * Gets the custom rank.
	 *
	 * @return the custom rank
	 */
	public Integer getCustomRank() {
		return this.customRank;
	}

	/**
	 * Sets the custom points.
	 *
	 * @param customPoints the new custom points
	 */
	public void setCustomPoints(float customPoints) {
		this.customPoints = customPoints;
	}

	/**
	 * Gets the medal rank.
	 *
	 * @return the medal rank
	 */
	public Integer getMedalRank() {
		Integer i = getRank();
		if (i == null)
			return 0;
		return (i <= 3 ? i : 0);
	}

	/**
	 * Successful lift.
	 */
	public void successfulLift() {
		logger.debug("good lift for {}, listeners={}", this); // , getEventRouter().dumpListeners(this)); //$NON-NLS-1$
		final String weight = Integer.toString(getNextAttemptRequestedWeight());
		doLift(weight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getLastName() + "_" + getFirstName() + "_" + System.identityHashCode(this); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Long dump.
	 *
	 * @return the string
	 */
	public String longDump() {
		final Category category = this.getCategory();
		final Group group = this.getGroup();
		return (new StringBuilder())
			.append(" lastName=" + this.getLastName()) //$NON-NLS-1$
			.append(" firstName=" + this.getFirstName()) //$NON-NLS-1$
			.append(" membership=" + this.getMembership()) //$NON-NLS-1$
			.append(" lotNumber=" + this.getLotNumber()) //$NON-NLS-1$
			.append(" group=" + (group != null ? group.getName() : null)) //$NON-NLS-1$
			.append(" team=" + this.getTeam()) //$NON-NLS-1$
			.append(" gender=" + this.getGender()) //$NON-NLS-1$
			.append(" bodyWeight=" + this.getBodyWeight()) //$NON-NLS-1$
			.append(" birthDate=" + this.getYearOfBirth()) //$NON-NLS-1$
			.append(" category=" + (category != null ? category.getName() //$NON-NLS-1$
				.toLowerCase() : null))
			.append(" actualCategory=" + this.getLongCategory() //$NON-NLS-1$
				.toString()
				.toLowerCase())
			.append(" snatch1ActualLift=" + this.getSnatch1ActualLift()) //$NON-NLS-1$
			.append(" snatch2=" + this.getSnatch2ActualLift()) //$NON-NLS-1$
			.append(" snatch3=" + this.getSnatch3ActualLift()) //$NON-NLS-1$
			.append(" bestSnatch=" + this.getBestSnatch()) //$NON-NLS-1$
			.append(" cleanJerk1ActualLift=" + this.getCleanJerk1ActualLift()) //$NON-NLS-1$
			.append(" cleanJerk2=" + this.getCleanJerk2ActualLift()) //$NON-NLS-1$
			.append(" cleanJerk3=" + this.getCleanJerk3ActualLift()) //$NON-NLS-1$
			.append(" total=" + this.getTotal()) //$NON-NLS-1$
			.append(" totalRank=" + this.getRank()) //$NON-NLS-1$
			.append(" teamMember=" + this.getTeamMember())
			.toString();
	}

	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Gets the sinclair points.
	 *
	 * @return the sinclairPoints
	 */
	public Float getSinclairPoints() {
		return sinclairPoints;
	}

	/**
	 * Gets the custom points.
	 *
	 * @return the customPoints
	 */
	public Float getCustomPoints() {
		return customPoints;
	}

	/**
	 * Gets the team sinclair rank.
	 *
	 * @return the teamSinclairRank
	 */
	public Integer getTeamSinclairRank() {
		return teamSinclairRank;
	}

	/**
	 * Gets the team robi rank.
	 *
	 * @return the teamRobiRank
	 */
	public Integer getTeamRobiRank() {
		return teamRobiRank;
	}

	/**
	 * Gets the team combined rank.
	 *
	 * @return the teamCombinedRank
	 */
	public Integer getTeamCombinedRank() {
		return teamCombinedRank;
	}

	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public static int getYear() {
		return year;
	}

	/**
	 * Withdraw.
	 */
	public void withdraw() {
		if (snatch1ActualLift != null && snatch1ActualLift.trim()
			.isEmpty()) {
			setSnatch1ActualLift("0");
		}
		if (snatch2ActualLift != null && snatch2ActualLift.trim()
			.isEmpty()) {
			setSnatch2ActualLift("0");
		}
		if (snatch3ActualLift != null && snatch3ActualLift.trim()
			.isEmpty()) {
			setSnatch3ActualLift("0");
		}
		if (cleanJerk1ActualLift != null && cleanJerk1ActualLift.trim()
			.isEmpty()) {
			setCleanJerk1ActualLift("0");
		}
		if (cleanJerk2ActualLift != null && cleanJerk2ActualLift.trim()
			.isEmpty()) {
			setCleanJerk2ActualLift("0");
		}
		if (cleanJerk3ActualLift != null && cleanJerk3ActualLift.trim()
			.isEmpty()) {
			setCleanJerk3ActualLift("0");
		}
	}

	/**
	 * @param prevVal
	 * @return
	 */
	private String doAutomaticProgression(final int prevVal) {
		if (prevVal > 0) {
			return Integer.toString(prevVal + 1);
		} else {
			return Integer.toString(Math.abs(prevVal));
		}
	}

	/**
	 * @param Athlete
	 * @param athletes
	 * @param weight
	 */
	private void doLift(final String weight) {
		switch (this.getAttemptsDone() + 1) {
		case 1:
			this.setSnatch1ActualLift(weight);
			break;
		case 2:
			this.setSnatch2ActualLift(weight);
			break;
		case 3:
			this.setSnatch3ActualLift(weight);
			break;
		case 4:
			this.setCleanJerk1ActualLift(weight);
			break;
		case 5:
			this.setCleanJerk2ActualLift(weight);
			break;
		case 6:
			this.setCleanJerk3ActualLift(weight);
			break;
		}
	}

	private String emptyIfNull(String value) {
		return (value == null ? "" : value); //$NON-NLS-1$
	}

	/**
	 * Last.
	 *
	 * @param items the items
	 * @return the integer
	 */
	public static Integer last(Integer... items) {
		int lastIndex = items.length - 1;
		while (lastIndex >= 0) {
			if (items[lastIndex] > 0) {
				return items[lastIndex];
			}
			lastIndex--;
		}
		return 0;
	}

	@SuppressWarnings("unused")
	private Integer getDeclaredAndActuallyAttempted(Integer... items) {
		int lastIndex = items.length - 1;
		if (items.length == 0) {
			return 0;
		}
		while (lastIndex >= 0) {
			if (items[lastIndex] > 0) {
				// if went down from declared weight, then return lower weight
				return (items[lastIndex] < items[0] ? items[lastIndex] : items[0]);
			}
			lastIndex--;
		}
		return 0;
	}

	private Integer max(Integer... items) {
		List<Integer> itemList = Arrays.asList(items);
		final Integer max = Collections.max(itemList);
		return max;
	}

	@SuppressWarnings("unused")
	private Integer max(String... items) {
		List<String> itemList = Arrays.asList(items);
		List<Integer> intItemList = new ArrayList<Integer>(itemList.size());
		for (String curString : itemList) {
			intItemList.add(zeroIfInvalid(curString));
		}
		final Integer max = Collections.max(intItemList);
		return max;
	}

	/**
	 * Compute the Sinclair formula given its parameters.
	 *
	 * @param coefficient
	 * @param maxWeight
	 */
	private Double sinclairFactor(Double bodyWeight1, Double coefficient, Double maxWeight) {
		if (bodyWeight1 == null)
			return 0.0;
		if (bodyWeight1 >= maxWeight) {
			return 1.0;
		} else {
			return Math.pow(10.0, coefficient * (Math.pow(Math.log10(bodyWeight1 / maxWeight), 2)));
		}
	}

	/**
	 * @param curLift
	 * @param actualLift
	 */
	public void validateActualLift(int curLift, String automaticProgression, String declaration, String change1,
			String change2, String actualLift) {
		if (actualLift == null || actualLift.trim()
			.length() == 0)
			return; // allow reset of field.
		final int declaredChanges = last(zeroIfInvalid(declaration), zeroIfInvalid(change1), zeroIfInvalid(change2));
		final int iAutomaticProgression = zeroIfInvalid(automaticProgression);
		final int liftedWeight = zeroIfInvalid(actualLift);
		logger.debug("declaredChanges={} automaticProgression={} liftedWeight={}", //$NON-NLS-1$
			new Object[] { declaredChanges, automaticProgression, liftedWeight });
		if (liftedWeight == 0) {
			// Athlete is not taking try; always ok no matter what was declared.
			return;
		}
		if (declaredChanges == 0 && iAutomaticProgression > 0) {
			// assume data entry is being done without reference to
			// declarations, check if > progression
			if (Math.abs(liftedWeight) >= iAutomaticProgression) {
				return;
			} else {
				throw RuleViolation.liftValueBelowProgression(curLift, actualLift, iAutomaticProgression);
			}
		} else {
			// declarations are being captured, lift must match declared
			// changes.
			final boolean declaredChangesOk = declaredChanges >= iAutomaticProgression;
			final boolean liftedWeightOk = Math.abs(liftedWeight) == declaredChanges;
			if (liftedWeightOk && declaredChangesOk) {
				return;
			} else {
				if (declaredChanges == 0)
					return;
				if (!declaredChangesOk)
					throw RuleViolation.declaredChangesNotOk(curLift,
						declaredChanges,
						iAutomaticProgression,
						iAutomaticProgression + 1);
				if (!liftedWeightOk)
					throw RuleViolation
						.liftValueNotWhatWasRequested(curLift, actualLift, declaredChanges, liftedWeight);
			}
		}
	}

	/**
	 * @param curLift
	 * @param actualLift
	 */
	private void validateChange1(int curLift, String automaticProgression, String declaration, String change1,
			String change2, String actualLift, boolean isSnatch) throws RuleViolationException {
		if (change1 == null || change1.trim()
			.length() == 0)
			return; // allow reset of field.
		int newVal = zeroIfInvalid(change1);
		int prevVal = zeroIfInvalid(automaticProgression);
		if (newVal < prevVal)
			throw RuleViolation.declaredChangesNotOk(curLift, newVal, prevVal);

	}

	/**
	 * @param curLift
	 * @param actualLift
	 */
	private void validateChange2(int curLift, String automaticProgression, String declaration, String change1,
			String change2, String actualLift, boolean isSnatch) throws RuleViolationException {
		if (change2 == null || change2.trim()
			.length() == 0)
			return; // allow reset of field.
		int newVal = zeroIfInvalid(change2);
		int prevVal = zeroIfInvalid(automaticProgression);
		if (newVal < prevVal)
			throw RuleViolation.declaredChangesNotOk(curLift, newVal, prevVal);
	}

	/**
	 * @param curLift
	 * @param actualLift
	 */
	private void validateDeclaration(int curLift, String automaticProgression, String declaration, String change1,
			String change2, String actualLift, boolean isSnatch) throws RuleViolationException {
		if (declaration == null || declaration.trim()
			.length() == 0)
			return; // allow reset of field.
		int newVal = zeroIfInvalid(declaration);
		int iAutomaticProgression = zeroIfInvalid(automaticProgression);
		// allow null declaration for reloading old results.
		if (iAutomaticProgression > 0 && newVal < iAutomaticProgression)
			throw RuleViolation.declarationValueTooSmall(curLift, newVal, iAutomaticProgression);

	}

	/**
	 * Check starting totals rule.
	 *
	 * @param unlessCurrent the unless current
	 */
	public void checkStartingTotalsRule(boolean unlessCurrent) {
		int qualTotal = getQualifyingTotal();
		boolean enforce15_20rule = Competition.getCurrent()
			.isEnforce20kgRule();
		if (qualTotal == 0 || !enforce15_20rule) {
			return;
		}

//        if (!Competition.getCurrent().isMasters())
		{
			int curStartingTotal = 0;
			int snatchRequest = 0;
			int cleanJerkRequest = 0;

			snatchRequest = last(
				zeroIfInvalid(snatch1Declaration),
				zeroIfInvalid(snatch1Change1),
				zeroIfInvalid(snatch1Change2));
			cleanJerkRequest = last(
				zeroIfInvalid(cleanJerk1Declaration),
				zeroIfInvalid(cleanJerk1Change1),
				zeroIfInvalid(cleanJerk1Change2));

			curStartingTotal = snatchRequest + cleanJerkRequest;
			int delta = qualTotal - curStartingTotal;
			String message = null;
			// FIXME: will fail during tests
			Locale locale = UI.getCurrent()
				.getLocale();
			int _20kgRuleValue = this.get20kgRuleValue();
			if (delta > _20kgRuleValue) {
				Integer startNumber2 = this.getStartNumber();
				message = RuleViolation
					.rule15_20Violated(this.getLastName(),
						this.getFirstName(),
						(startNumber2 != null ? startNumber2 : "-"),
						snatchRequest,
						cleanJerkRequest,
						delta - _20kgRuleValue,
						qualTotal)
					.getLocalizedMessage(locale);
			}
			if (message != null) {
				// LoggerUtils.logException(logger, new Exception("check15_20kiloRule traceback
				// "+ message));
				logger.info(message);
				Notification.show(message, -1, Position.MIDDLE);
			}
		}
//        else {
//            int curStartingTotal = 0;
//            int snatch1request = 0;
//            int cleanJerkRequest = 0;
//
//            snatch1request = last(
//                    zeroIfInvalid(snatch1Declaration),
//                    zeroIfInvalid(snatch1Change1),
//                    zeroIfInvalid(snatch1Change2));
//            cleanJerkRequest = last(
//                    zeroIfInvalid(cleanJerk1Declaration),
//                    zeroIfInvalid(cleanJerk1Change1),
//                    zeroIfInvalid(cleanJerk1Change2));
//
//            int _20kgRuleValue = this.get20kgRuleValue();
//            int bestSnatch1 = getBestSnatch();
//            // example: male 55/65 declarations given 135 qual total (120 within 15kg of 135, ok)
//            // athlete does 70 snatch, which is bigger than 15kg gap.
//            // can now declare 50 opening CJ according to 2.4.3
//            curStartingTotal = snatch1request + cleanJerkRequest; // 120
//            int delta = qualTotal - curStartingTotal; // 15 -- no margin of error
//
//            int curForecast = bestSnatch1 + zeroIfInvalid(cleanJerk1Declaration); // 70 + 65 = 135
//            if (curForecast >= qualTotal) {
//                // already predicted to clear the QT, may change the CJ request down.
//                logger.warn("forecast = {}",curForecast);
//                delta = qualTotal - (bestSnatch1 + cleanJerkRequest); // delta = 135 - 135 = 0
//                snatch1request = bestSnatch1;
//                // possible CJ initial request reduction = _20kgRuleValue - delta
//                // can bring CJ down to 50 (15 - 0)
//            }
//
//            String message = null;
//            Locale locale = CompetitionApplication.getCurrentLocale();
//
//            if (delta > _20kgRuleValue) {
//                Integer startNumber2 = this.getStartNumber();
//                message = RuleViolation.rule15_20Violated(this.getLastName(), this.getFirstName(), (startNumber2 != null ? startNumber2 : "-") , snatch1request,
//                        cleanJerkRequest, delta - _20kgRuleValue, qualTotal).getLocalizedMessage(locale);
//            }
//            if (message != null) {
//                // LoggerUtils.logException(logger, new Exception("check15_20kiloRule traceback "+ message));
//                logger.info(message);
//                showMustClickNotification(parentView, message, unlessCurrent);
//            }
//        }
	}

	/**
	 * Show must click notification.
	 *
	 * @param message       the message
	 * @param unlessCurrent the unless current
	 */
	public void showMustClickNotification(String message, boolean unlessCurrent) {
		// FIXME: should be a message caught by UI and displayed if relevant.
		Notification.show(message, -1, Position.MIDDLE);
	}

	/**
	 * Gets the masters long category.
	 *
	 * @return the masters long category
	 */
	public String getMastersLongCategory() {
		String catString;
		String gender1 = getGender().toUpperCase();
		final String mastersAgeCategory = getMastersAgeGroup(gender1);
		final String shortCategory = getShortCategory(gender1);
		catString = mastersAgeCategory + " " + shortCategory;
		return catString;
	}

	/**
	 * Gets the masters long registration category name.
	 *
	 * @return the masters long registration category name
	 */
	public String getMastersLongRegistrationCategoryName() {
		String catString;
		String gender1 = getGender().toUpperCase();
		final String mastersAgeCategory = getMastersAgeGroup(gender1);
		final String shortCategory = getShortRegistrationCategory(gender1);
		catString = mastersAgeCategory + " " + shortCategory;
		return catString;
	}

	/**
	 * Gets the masters age group.
	 *
	 * @return the masters age group
	 */
	public String getMastersAgeGroup() {
		String gender1 = getGender().toUpperCase();
		return getMastersAgeGroup(gender1);
	}

	/**
	 * @param gender1
	 * @return
	 */
	private String getMastersAgeGroup(String gender1) {
		Integer ageGroup1;
		ageGroup1 = getAgeGroup();

		String agePlus = "";
		if ("M".equals(gender1) && ageGroup1 == 80)
			agePlus = "+";
		else if ("F".equals(gender1) && ageGroup1 == 70)
			agePlus = "+";
		else if (ageGroup1 == 17)
			agePlus = "-";
		else if (ageGroup1 == 20)
			agePlus = "-";
		else if (ageGroup1 == 34) {
			ageGroup1 = 21;
		}

		final String mastersAgeCategory = ("F".equals(gender1) ? "W" : "M") + ageGroup1 + agePlus;
		return mastersAgeCategory;
	}

	/**
	 * Create a category acronym without gender.
	 *
	 * @return the short category
	 */
	public String getShortCategory() {
		String gender1 = getGender();
		return getShortCategory(gender1);
	}

	/**
	 * Create a category acronym without gender.
	 *
	 * @param gender1 the gender 1
	 * @return the short category
	 */
	public String getShortCategory(String gender1) {
		final Category category = getCategory();
		if (category == null)
			return "";

		if (Competition.getCurrent()
			.isUseRegistrationCategory()) {
			return getShortRegistrationCategory(gender1);
		}

		String shortCategory = category.getName();
		int gtPos = shortCategory.indexOf(">");
		if (gtPos > 0) {
			return shortCategory.substring(gtPos);
		} else {
			return shortCategory.substring(1);
		}
	}

	/**
	 * Gets the short registration category.
	 *
	 * @param gender1 the gender 1
	 * @return registration category stripped of gender prefix.
	 */
	public String getShortRegistrationCategory(String gender1) {
		final Category category = getRegistrationCategory();
		if (category == null)
			return "?";

		String shortCategory = category.getName();
		int gtPos = shortCategory.indexOf(">");
		if (gtPos > 0) {
			return shortCategory.substring(gtPos);
		} else {
			return shortCategory.substring(1);
		}
	}

	/**
	 * Gets the display category.
	 *
	 * @return the display category
	 */
	public String getDisplayCategory() {
		if (Competition.getCurrent()
			.isMasters()) {
			return getShortCategory();
		} else {
			return getLongCategory();
		}
	}

	/**
	 * Gets the long category.
	 *
	 * @return the long category
	 */
	public String getLongCategory() {
		if (Competition.getCurrent()
			.isUseRegistrationCategory()) {
			Category registrationCategory2 = getRegistrationCategory();
			if (registrationCategory2 == null)
				return "?";
			return registrationCategory2.getName();
		} else if (Competition.getCurrent()
			.isMasters()) {
			return getMastersLongCategory();
		} else {
			Category category = getCategory();
			return (category != null ? category.getName() : "");
		}
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	@Version
	public Long getVersion() {
		return version;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Athlete other = (Athlete) obj;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	/**
	 * Gets the 20 kg rule value.
	 *
	 * @return the 20 kg rule value
	 */
	public int get20kgRuleValue() {
		if (Competition.getCurrent()
			.isMasters()) {
			if ("M".equals(this.getGender())) {
				return 15;
			} else {
				return 10;
			}
		} else if (Competition.getCurrent()
			.isUseOld20_15rule()) {
			if ("M".equals(this.getGender())) {
				return 20;
			} else {
				return 15;
			}
		} else {
			return 20;
		}

	}

	/**
	 * Gets the current automatic.
	 *
	 * @return the current automatic
	 */
	public String getCurrentAutomatic() {
		switch (this.getAttemptsDone() + 1) {
		case 1:
			return this.getSnatch1Declaration();
		case 2:
			return this.getSnatch2AutomaticProgression();
		case 3:
			return this.getSnatch3AutomaticProgression();
		case 4:
			return this.getCleanJerk1Declaration();
		case 5:
			return this.getCleanJerk2AutomaticProgression();
		case 6:
			return this.getCleanJerk3AutomaticProgression();
		}
		return null;
	}

	/**
	 * Gets the current declaration.
	 *
	 * @return the current declaration
	 */
	public String getCurrentDeclaration() {
		switch (this.getAttemptsDone() + 1) {
		case 1:
			return this.getSnatch1Declaration();
		case 2:
			return this.getSnatch2Declaration();
		case 3:
			return this.getSnatch3Declaration();
		case 4:
			return this.getCleanJerk1Declaration();
		case 5:
			return this.getCleanJerk2Declaration();
		case 6:
			return this.getCleanJerk3Declaration();
		}
		return null;
	}

	/**
	 * Gets the current change 1.
	 *
	 * @return the current change 1
	 */
	public String getCurrentChange1() {
		switch (this.getAttemptsDone() + 1) {
		case 1:
			return this.getSnatch1Change1();
		case 2:
			return this.getSnatch2Change1();
		case 3:
			return this.getSnatch3Change1();
		case 4:
			return this.getCleanJerk1Change1();
		case 5:
			return this.getCleanJerk2Change1();
		case 6:
			return this.getCleanJerk3Change1();
		}
		return null;
	}

}
