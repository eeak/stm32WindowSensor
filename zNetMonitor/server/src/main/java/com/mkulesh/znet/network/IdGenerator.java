/*
 * stm32WindowSensor: RF window sensors: STM32L + RFM69 + Android
 *
 * Copyright (C) 2019. Mikhail Kulesh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a copy of the GNU General
 * Public License along with this program.
 */

package com.mkulesh.znet.network;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generate a value suitable for use in setId(int). This value will not collide with ID values generated at
 * build time by aapt for R.id.
 */
public class IdGenerator
{
    private static final int INITIAL_VALUE = 1;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(INITIAL_VALUE);
    public static void reset()
    {
        set(INITIAL_VALUE);
    }

    private static void set(int i)
    {
        sNextGeneratedId.set(i + 1);
    }

    static int generateId()
    {
        for (; ; )
        {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF)
                newValue = INITIAL_VALUE; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue))
            {
                return result;
            }
        }
    }

}
