/**
 * $Archive: $
 * 
 * Copyright 2005 (C) Pyxis Technologies Inc. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Pyxis
 * Technologies inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Pyxis Technologies.
 * 
 * http://www.pyxis-tech.com
 */
package info.novatec.testit.livingdoc.samples.application.calculator;

/**
 * Demo SUT Application.
 */
public class Calculator {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int sum() {
        return x + y;
    }

    public int difference() {
        return x - y;
    }

    public int product() {
        return x * y;
    }

    public int quotient() {
        return x / y;
    }
}
