package lorentzonsolutions.rhome.interfaces;

/**
 * Interface for this applications activities.
 *
 * @author Johan Lorentzon
 *
 */

public interface RhomeActivity {
    /**
     * Method initializes logic on views. Even listeners and adapterList is among other things instantiated here.
     */
    void initEvents();

    /**
     * Method sets the variable references to the views used by the activity.
     */
    void assignViews();

}
