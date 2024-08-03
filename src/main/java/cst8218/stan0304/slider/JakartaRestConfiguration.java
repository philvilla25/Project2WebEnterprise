package cst8218.stan0304.slider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


/**
 * Configures Jakarta RESTful Web Services for the application.
 * @author Juneau
 */
@BasicAuthenticationMechanismDefinition
@DatabaseIdentityStoreDefinition(
dataSourceLookup = "${'java:comp/DefaultDataSource'}",
callerQuery = "#{'select password from app.slideruser where userid = ?'}",
groupsQuery = "select groupname from app.slideruser where userid = ?",
hashAlgorithm = PasswordHash.class,
priority = 10
)
@Named
@ApplicationScoped
@ApplicationPath("resources")
public class JakartaRestConfiguration extends Application {
    
}
