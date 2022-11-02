package pl.sky0x.app.data;

import java.util.Collection;

public interface DataSystem {

    /**
     * This method is used to retrieve clicks
     * @return Clicks from database/api
     */
    Collection<Click> getClicks();

    /**
     * This method is used to adding click to database/api
     * @param click that sending
     * @return the operation was successful
     */
    boolean addClick(Click click);
}
