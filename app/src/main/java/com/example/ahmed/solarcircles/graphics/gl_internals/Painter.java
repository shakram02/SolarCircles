package com.example.ahmed.solarcircles.graphics.gl_internals;

/**
 * Provides a coloring interface to objects, for different type of backing
 * colors in the GL program (attribute, uniform, ...etc)
 */

public interface Painter {
    void paint();
}
