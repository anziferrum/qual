package ru.anziferrum.testpay.security;

import org.apache.commons.codec.binary.Base64;
import ru.anziferrum.testpay.exception.E401Exception;

import javax.servlet.http.HttpServletRequest;

/**
 * @author anziferrum
 */
public class HeaderChecker {
    private HttpServletRequest request;
    private String currentHeader;
    private String[] credentials;

    public HeaderChecker(HttpServletRequest request) {
        this.request = request;
    }

    public HeaderChecker isHeaderPresent(String name) throws E401Exception {
        if (request.getHeader(name) != null && !request.getHeader(name).isEmpty()) {
            currentHeader = name;

            return this;
        }
        else {
            throw new E401Exception("Header \"" + name + "\"is missing from request!");
        }
    }

    public HeaderChecker hasValue(String value) throws E401Exception {
        if (value.equals(request.getHeader(currentHeader))) {
            return this;
        } else {
            throw new E401Exception("Header \"" + currentHeader + "\" has other value than \"" + value + "\"!");
        }
    }

    public HeaderChecker startsWith(String prefix) throws E401Exception {
        if (request.getHeader(currentHeader).startsWith(prefix)) {
            return this;
        } else {
            throw new E401Exception("Header \"" + currentHeader + "\" is corrupt!");
        }
    }

    public HeaderChecker extractCredentials(boolean needsDecoding, String splitPattern) throws E401Exception {
        String authorization = request.getHeader(currentHeader);;

        String decoded = needsDecoding ? new String(Base64.decodeBase64(authorization.split(" ")[1])) : authorization;
        credentials = decoded.split(splitPattern);

        if (credentials.length != 2) {
            throw new E401Exception("Cannot extract credentials from string \"" + decoded + "\"!");
        }

        return this;
    }

    public String[] getCredentials() {
        return credentials;
    }
}
