/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.data.category;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.LoggerFactory;

import app.owlcms.data.agegroup.AgeGroupRepository;
import app.owlcms.data.athlete.Athlete;
import app.owlcms.utils.LoggerUtils;
import ch.qos.logback.classic.Logger;

/**
 * This class is used to compute the Robi score for athletes based on their body weight. It locates what the athlete's
 * category if the Athlete was competing in a IWF competition.
 */
public class RobiCategories {

    private class RobiComparator implements Comparator<Category> {

        @Override
        public int compare(Category c1, Category c2) {
            try {
                // because we are getting c2 as the fake value being searched, we invert the
                // return value of comparison.

                if (c2.getGender() != c1.getGender()) {
                    int compare = ObjectUtils.compare(c2, c1);
//                    System.err.println(dumpCat(c2) + " " + compare + " " + dumpCat(c1));
                    return -compare;
                }
                // c2 is a fake category where the upper and lower bounds are the athlete's weight
                if (c2.getMinimumWeight() >= c1.getMinimumWeight() && c2.getMaximumWeight() <= c1.getMaximumWeight()) {
//                    System.err.println(dumpCat(c2)+" == "+dumpCat(c1));
                    return 0;
                } else if (c2.getMinimumWeight() > c1.getMaximumWeight()) {
//                    System.err.println(dumpCat(c2)+" >  "+dumpCat(c1));
                    return -1;
                } else {
//                    System.err.println(dumpCat(c2)+" <  "+dumpCat(c1));
                    return 1;
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
            return 0;
        }
    }

    static Logger logger = (Logger) LoggerFactory.getLogger(RobiCategories.class);
    private static ArrayList<Category> jrSrReferenceCategories = null;

    private static ArrayList<Category> ythReferenceCategories;

    public static Category findRobiCategory(Athlete a) {
        if (jrSrReferenceCategories == null) {
            loadJrSrReferenceCategories();
        }
        if (ythReferenceCategories == null) {
            loadYthReferenceCategories();
        }
        RobiCategories x = new RobiCategories();
        List<Category> categories;
        Integer age = a.getAge();
        if (age != null && age <= 17) {
            categories = ythReferenceCategories;
        } else {
            categories = jrSrReferenceCategories;
        }
//        System.err.println("before search " + categories.size());
        int index = Collections.binarySearch(categories,
                new Category(null, a.getBodyWeight(), a.getBodyWeight(), a.getGender(), true, 0, 0, 0, null),
                x.new RobiComparator());

        if (index >= 0) {
            return categories.get(index);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unused")
    private static String dumpCat(Category c) {
        StringBuilder sb = new StringBuilder();
        sb.append("code=");
        sb.append(c.getCode());
        sb.append(" gender=");
        sb.append(c.getGender());
        sb.append(" min=");
        sb.append(c.getMinimumWeight());
        sb.append(" max=");
        sb.append(c.getMaximumWeight());
        return sb.toString();
    }

    private static void loadJrSrReferenceCategories() {
        String localizedName = "/config/AgeGroups.xlsx";
        InputStream localizedResourceAsStream = AgeGroupRepository.class.getResourceAsStream(localizedName);
        try (Workbook workbook = WorkbookFactory.create(localizedResourceAsStream)) {
            Map<String, Category> referenceCategoryMap = AgeGroupRepository.createCategoryTemplates(workbook);
            // get the IWF categories, sorted.
            jrSrReferenceCategories = referenceCategoryMap.values()
                    .stream()
                    .filter(c -> c.getWrSr() > 0)
                    .sorted()
                    // .peek(c -> {System.err.println(c.getCode());})
                    .collect(Collectors.toCollection(ArrayList::new));
            workbook.close();
        } catch (Exception e) {
            logger.error("could not process ageGroup configuration\n{}", LoggerUtils.stackTrace(e));
        }
        Double prevMax = 0.0D;
//        int i = 0;
        for (Category refCat : jrSrReferenceCategories) {
            refCat.setMinimumWeight(prevMax);
//            System.err.println(i + " " + dumpCat(referenceCategories.get(i)));
            prevMax = refCat.getMaximumWeight();
            if (prevMax >= 998.00D) {
                prevMax = 0.0D;
            }
//            i++;
        }
    }

    private static void loadYthReferenceCategories() {
        String localizedName = "/config/AgeGroups.xlsx";
        InputStream localizedResourceAsStream = AgeGroupRepository.class.getResourceAsStream(localizedName);
        try (Workbook workbook = WorkbookFactory.create(localizedResourceAsStream)) {
            Map<String, Category> referenceCategoryMap = AgeGroupRepository.createCategoryTemplates(workbook);
            // get the IWF categories, sorted.
            ythReferenceCategories = referenceCategoryMap.values()
                    .stream()
                    .filter(c -> c.getWrYth() > 0)
                    .sorted()
                    // .peek(c -> {System.err.println(c.getCode());})
                    .collect(Collectors.toCollection(ArrayList::new));
            workbook.close();
        } catch (Exception e) {
            logger.error("could not process ageGroup configuration\n{}", LoggerUtils.stackTrace(e));
        }
        Double prevMax = 0.0D;
//        int i = 0;
        for (Category refCat : ythReferenceCategories) {
            refCat.setMinimumWeight(prevMax);
//            System.err.println(i + " " + dumpCat(ythReferenceCategories.get(i)));
            prevMax = refCat.getMaximumWeight();
            if (prevMax >= 998.00D) {
                prevMax = 0.0D;
            }
//            i++;
        }
    }

}
