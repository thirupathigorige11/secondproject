package com.sumadhura.util;

import java.util.Date;

import org.apache.commons.httpclient.Cookie;



/**
 * Utility class to help convert Cookie objects between Java Servlet Cookie's
 * and Apache HttpClient Cookie's.
*/
public class ApacheCookieUtils {
 /**
  * Method to convert an Apache HttpClient cookie to a Java Servlet cookie.
  *
  * @param apacheCookie the source apache cookie
  * @return a java servlet cookie
  */
 public static Cookie servletCookieFromApacheCookie(org.apache.commons.httpclient.Cookie apacheCookie,String domain) {
  if(apacheCookie == null) {
      return null;
  }
  String name = apacheCookie.getName();
  String value = apacheCookie.getValue();
  org.apache.commons.httpclient.Cookie cookie = new org.apache.commons.httpclient.Cookie(domain, name, value);

  // set the domain
  value = apacheCookie.getDomain();
  if(value != null) {
   cookie.setDomain(domain);
  }
  // path
  value = apacheCookie.getPath();
  if(value != null) {
   cookie.setPath(value);
  }
  // secure
  cookie.setSecure(apacheCookie.getSecure());
  // comment
  value = apacheCookie.getComment();
  if(value != null) {
   cookie.setComment(value);
  }
  // version
  cookie.setVersion(apacheCookie.getVersion());
  // From the Apache source code, maxAge is converted to expiry date using the following formula
  // if (maxAge >= 0) {
        //     setExpiryDate(new Date(System.currentTimeMillis() + maxAge * 1000L));
        // }
  // Reverse this to get the actual max age

  Date expiryDate = apacheCookie.getExpiryDate();
  if(expiryDate != null) {
   // we have to lower down, no other option
  //  cookie.setMaxAge((int) maxAge);
   cookie.setExpiryDate(expiryDate);
  }

  // return the servlet cookie
  return cookie;
 }

 /**
  * Method to convert a Java Servlet cookie to an Apache HttpClient cookie.
  *
  * @param cookie the Java servlet cookie to convert
  * @return the Apache HttpClient cookie
  */
 public static org.apache.commons.httpclient.Cookie apacheCookieFromServletCookie(Cookie cookie) {
  if(cookie == null) {
   return null;
  }

  org.apache.commons.httpclient.Cookie apacheCookie = null;

  // get all the relevant parameters
     String domain = cookie.getDomain();
     String name = cookie.getName();
     String value = cookie.getValue();
     String path = cookie.getPath();
//     int maxAge = cookie.getMaxAge();
     Date maxAge=cookie.getExpiryDate();
     boolean secure = cookie.getSecure();

     // create the apache cookie
     apacheCookie = new org.apache.commons.httpclient.Cookie(domain, name, value, path, maxAge, secure);

     // set additional parameters
     apacheCookie.setComment(cookie.getComment());
     apacheCookie.setVersion(cookie.getVersion());

     // return the apache cookie
     return apacheCookie;
 }

}