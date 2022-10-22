package pl.sky0x.app.data;

import java.util.Collection;

public interface DataSystem {

    Collection<Click> getClicks();

    boolean addClick(Click click);
}
