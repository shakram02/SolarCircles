package com.example.ahmed.solarcircles.graphics.utils;

/**
 * Created by ahmed on 11/22/17.
 */

public class ValueLimiter {
    private float minVal;
    private float maxVal;
    private float position;
    private float delta;

    // Provide smooth transition between values
    enum IncrementDirection {
        Increasing,
        Decreasing,
        None
    }

    private float m_final;
    private float m_internalStep;
    private IncrementDirection m_direction;

    public ValueLimiter(float initial, float min, float max, float delta) {
        this.position = initial;
        this.minVal = min;
        this.maxVal = max;
        this.delta = delta;
        m_internalStep = this.delta / 10.0f;
    }

    public void increment() {
        if ((m_final + delta) > maxVal) return;

        // Positive motion if we didn't hit the limit
        m_final += delta;
        m_direction = IncrementDirection.Increasing;
    }

    private void update() {
        if (m_direction == IncrementDirection.Increasing && position < m_final) {
            position += m_internalStep;

            // Floating point equalities will cause corruption, better check here than in an else
            if (position >= m_final) {
                m_direction = IncrementDirection.None;
            }

        } else if (m_direction == IncrementDirection.Decreasing && position > m_final) {
            position -= m_internalStep;

            // Floating point equalities will cause corruption, better check here than in an else
            if (position <= m_final) {
                m_direction = IncrementDirection.None;
            }
        }
    }

    public float getValue() {

        if (m_direction != IncrementDirection.None) {
            this.update();
        }

        return position;
    }

    public void decrement() {
        if ((m_final - delta) < minVal) return;

        // Negative motion if we didn't hit the limit
        m_final -= delta;
        m_direction = IncrementDirection.Decreasing;
    }

}
