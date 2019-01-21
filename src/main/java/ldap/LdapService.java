package ldap;

import com.sun.jndi.ldap.LdapCtxFactory;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;
import javax.naming.directory.SearchResult;

public class LdapService {

    /**
     * @var domainName Authentifizierungsadresse
     * [benutzer@edu.htl-leonding.ac.at]
     *
     * @var serverName LDAP Serveradresse
     *
     * @var username selbsterklärend
     *
     * @var password selbsterklärend
     */
    final String domainName = "edu.htl-leonding.ac.at";
    final String serverName = "addc01.edu.htl-leonding.ac.at";

    private String username = "username";
    private String password = "password";

    public LdapService() {
        System.out.println("Authenticating " + username + "@" + domainName + " through " + serverName);
    }

    public String proofUser(String uname, String pwd) {
        this.username = uname;
        this.password = pwd;

        Hashtable props = new Hashtable();
        /**
         * @var principalName Authentifizierungsformat
         * [benutzer@edu.htl-leonding.ac.at]
         */
        String principalName = username + "@" + domainName;
        props.put(Context.SECURITY_PRINCIPAL, principalName);
        props.put(Context.SECURITY_CREDENTIALS, password);
        DirContext context;

        try {
            context = LdapCtxFactory.getLdapCtxInstance("ldap://" + serverName + '/', props);
            System.out.println("Authentication succeeded!");

            /**
             * LDAP-Tree durchsuchen
             */
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SUBTREE_SCOPE);

            /**
             * @Method context.search
             * @param (BasisURL, Filter, Controls)
             */
            NamingEnumeration<SearchResult> renum = context.search("OU=HTL,DC=EDU,DC=HTL-LEONDING,DC=AC,DC=AT",
                    "cn=" + username, controls);

            // ...wenn User nicht gefunden, dann...
            if (!renum.hasMore()) {
                System.out.println("Cannot locate user information for " + username);
                System.exit(1);
            }
            SearchResult result = renum.next();
            System.out.println(result.getAttributes());

            context.close();
            return result.getAttributes().toString();
        } catch (AuthenticationException a) {
            System.out.println("Authentication failed: " + a);
            System.exit(1);
        } catch (NamingException e) {
            System.out.println("Failed to bind to LDAP / get account information: " + e);
            System.exit(1);
        } catch (Exception e){
            System.out.println("Dude there is another error... " + e.getMessage());
        }

        return "just some wrong user";
    }

}
