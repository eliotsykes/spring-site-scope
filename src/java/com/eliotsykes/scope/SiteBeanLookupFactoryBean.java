package com.eliotsykes.scope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class SiteBeanLookupFactoryBean implements FactoryBean, BeanFactoryAware, BeanNameAware {

    private static final String SCOPED_TARGET_PREFIX = "scopedTarget.";
    private String beanName;
    private BeanFactory beanFactory;
    private SiteContextService siteContextService;
    private Class objectType;

    public SiteBeanLookupFactoryBean(Class objectType) {
        this.objectType = objectType;
    }

    public Object getObject() throws Exception {
        Site currentSite = siteContextService.currentSite();
        Object bean = lookupBeanForSite(currentSite);
        if (null == bean) {
            bean = lookupBeanForSite(Site.DEFAULT);
        }
        Assert.notNull(bean, "Unable to find bean");
        return bean;
    }

    private Object lookupBeanForSite(Site site) {
        String siteSpecificBeanName = siteSpecificBeanName(site);
        if (beanFactory.containsBean(siteSpecificBeanName)) {
            return beanFactory.getBean(siteSpecificBeanName);
        }
        return null;
    }
    
    private String siteSpecificBeanName(Site site) {
        return site.getId() + StringUtils.capitalize(beanName);
    }

    public boolean isSingleton() {
        return true;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setBeanName(String name) {
        this.beanName = removeScopedTargetPrefix(name);
    }

    private String removeScopedTargetPrefix(String beanName) {
        if (!beanName.startsWith(SCOPED_TARGET_PREFIX)) {
            throw new IllegalStateException(
              "Use the <aop:scoped-proxy/> tag within the SiteBeanLookupFactoryBean definition.");
        }
        return beanName.replace(SCOPED_TARGET_PREFIX, "");
    }

    public void setSiteContextService(SiteContextService siteContextService) {
        this.siteContextService = siteContextService;
    }

    public Class getObjectType() {
        return objectType;
    }

}
