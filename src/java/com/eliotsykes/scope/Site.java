package com.eliotsykes.scope;

public enum Site {

    DEFAULT("default"),
    NETFLIX("netflix"),
    BLOCKBUSTER("blockbuster"),
    BETAMAX_RULES("betamaxRules");

    private String id;
    
    public Site(String id) {
      this.id = id;
    }
    
    public String getId() {
      return id;
    }
}
