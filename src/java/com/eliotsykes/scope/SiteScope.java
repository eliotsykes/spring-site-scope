package com.eliotsykes.scope;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import com.eliotsykes.scope.Site;
import com.eliotsykes.scope.SiteContextService;

public class SiteScope implements Scope {

    private SiteContextService siteContextService;
    private Map<Site, Map<String, Object>> beanRepositories = new HashMap<Site, Map<String, Object>>();

    public SiteScope() {
        initBeanRepositories();
    }

    private void initBeanRepositories() {
        for (Site site : Site.values()) {
            beanRepositories.put(site, new HashMap<String, Object>());
        }
    }

    public Object get(String beanName, ObjectFactory objectFactory) {
        Map beanRepositoryForCurrentSite = getBeanRepositoryForCurrentSite();
        synchronized (beanRepositoryForCurrentSite) {
            if (!beanRepositoryForCurrentSite.containsKey(beanName)) {
                Object bean = objectFactory.getObject();
                beanRepositoryForCurrentSite.put(beanName, bean);
            }
            return beanRepositoryForCurrentSite.get(beanName);
        }
    }

    public Object remove(String beanName) {
        Map beanRepositoryForCurrentSite = getBeanRepositoryForCurrentSite();
        synchronized (beanRepositoryForCurrentSite) {
            return beanRepositoryForCurrentSite.remove(beanName);
        }
    }
    
    private Map<String, Object> getBeanRepositoryForCurrentSite() {
        Site site = siteContextService.currentSite();
        return beanRepositories.get(site);
    }

    public String getConversationId() {
        return currentSite().name();
    }

    private Site currentSite() {
        return siteContextService.currentSite();
    }

    public void registerDestructionCallback(String beanName, Runnable callback) {
      // Implement this if you need it
    }

    public void setSiteContextService(SiteContextService siteContextService) {
        this.siteContextService = siteContextService;
    }

}