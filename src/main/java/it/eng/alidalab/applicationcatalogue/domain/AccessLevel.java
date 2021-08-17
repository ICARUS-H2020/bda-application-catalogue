package it.eng.alidalab.applicationcatalogue.domain;

public class AccessLevel {
    public static enum ACCESS_LEVEL
    {
        PRIVATE,
        TEAM,
        PUBLIC;

        private ACCESS_LEVEL()
        {
        }
    }
}
