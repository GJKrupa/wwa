package uk.me.krupa.wwa.service.initialisation;

/**
 * Service that handles initialisation of the application.  Loads reference data if required and creates test users.
 */
public interface InitialisationService {

    /**
     * Initialise the application if required.
     */
    void initialise();

}
