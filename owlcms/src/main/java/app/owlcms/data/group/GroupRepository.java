/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.data.group;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.LoggerFactory;

import app.owlcms.data.athlete.Athlete;
import app.owlcms.data.jpa.JPAService;
import app.owlcms.utils.LoggerUtils;
import ch.qos.logback.classic.Logger;

/**
 * GroupRepository.
 *
 */
public class GroupRepository {

    static Logger logger = (Logger) LoggerFactory.getLogger(GroupRepository.class);

    /**
     * Delete.
     *
     * @param Group the group
     */

    public static void delete(Group groupe) {
        if (groupe.getId() == null) {
            return;
        }
        JPAService.runInTransaction(em -> {
            try {
                // this is the only case where group needs to know its athletes, so we do a
                // query instead of adding a relationship.
                Query aQ = em.createQuery("select a from Athlete a join a.group g where g.id = :groupId");
                aQ.setParameter("groupId", groupe.getId());
                @SuppressWarnings("unchecked")
                List<Athlete> aL = aQ.getResultList();
                for (Athlete a : aL) {
                    a.setGroup(null);
                }
                em.flush();
                em.remove(em.contains(groupe) ? groupe : em.merge(groupe));
                em.flush();
            } catch (Exception e) {
                logger.error(LoggerUtils.stackTrace(e));
            }
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    public static Group doFindByName(String name, EntityManager em) {
        Query query = em.createQuery("select u from CompetitionGroup u where u.name=:name");
        query.setParameter("name", name);
        return (Group) query.getResultList().stream().findFirst().orElse(null);
    }

    /**
     * Find all.
     *
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public static List<Group> findAll() {
        return JPAService.runInTransaction(
                em -> em.createQuery("select c from CompetitionGroup c order by c.name").getResultList());
    }

    public static Group findByName(String name) {
        return JPAService.runInTransaction(em -> {
            return doFindByName(name, em);
        });
    }

    /**
     * Gets group by id
     *
     * @param id the id
     * @param em entity manager
     * @return the group, null if not found
     */
    @SuppressWarnings("unchecked")
    public static Group getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from CompetitionGroup u where u.id=:id");
        query.setParameter("id", id);
        return (Group) query.getResultList().stream().findFirst().orElse(null);
    }

    /**
     * Save.
     *
     * @param Group the group
     * @return the group
     */
    public static Group save(Group Group) {
        return JPAService.runInTransaction(em -> em.merge(Group));
    }

    @SuppressWarnings("unchecked")
    public static List<Group> doFindAll(EntityManager em) {
        return em.createQuery("select c from CompetitionGroup c order by c.name").getResultList();
    }

}
