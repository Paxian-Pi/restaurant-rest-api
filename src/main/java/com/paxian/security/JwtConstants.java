package com.paxian.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtConstants {

    public static final String SECRET = "mySecreteKey";
    public static final int EXPIRATION_TIME = 900000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public static final Logger LOGGER = LoggerFactory.getLogger(JwtConstants.class);
}
