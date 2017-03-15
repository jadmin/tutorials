/*
 * @(#)StrongRandom.java	2012-3-8
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * A Enhance version of <code>java.util.Random</code>.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: StrongRandom.java 2012-3-8 11:32:46 Exp $
 */
public class StrongRandom extends Random {
	
    static final long serialVersionUID = 4398490852824852845L;
    private Random random;

    private boolean weakRandom;

    protected final static String SESSION_ID_RANDOM_ALGORITHM = "SHA1PRNG";
    protected final static String SESSION_ID_RANDOM_ALGORITHM_ALT = "IBMSecureRandom";


    public StrongRandom() {
        try {
            // This operation may block on some systems with low entropy. See
            // this page
            // for workaround suggestions:
            // http://docs.codehaus.org/display/JETTY/Connectors+slow+to+startup
            random = SecureRandom.getInstance(SESSION_ID_RANDOM_ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            try {
                random = SecureRandom.getInstance(SESSION_ID_RANDOM_ALGORITHM_ALT);
                weakRandom = false;
            }
            catch (NoSuchAlgorithmException e_alt) {
                random = new Random();
                weakRandom = true;
            }
        }

        random.setSeed(random.nextLong() ^ System.currentTimeMillis() ^ hashCode() ^ Runtime.getRuntime().freeMemory());
    }


    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }


    @Override
    public void nextBytes(byte[] bytes) {
        random.nextBytes(bytes);
    }


    @Override
    public double nextDouble() {
        return random.nextDouble();
    }


    @Override
    public float nextFloat() {
        return random.nextFloat();
    }


    @Override
    public double nextGaussian() {
        return random.nextGaussian();
    }


    @Override
    public int nextInt() {
        return random.nextInt();
    }


    @Override
    public int nextInt(int n) {
        return random.nextInt(n);
    }


    @Override
    public long nextLong() {
        if (weakRandom) {
            return Runtime.getRuntime().freeMemory() ^ System.currentTimeMillis() ^ random.nextLong();
        }
        return random.nextLong();
    }
}
