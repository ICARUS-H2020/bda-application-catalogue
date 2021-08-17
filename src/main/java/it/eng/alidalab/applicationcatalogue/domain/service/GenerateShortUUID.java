/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package it.eng.alidalab.applicationcatalogue.domain.service;

import java.util.UUID;

class GenerateShortUUID
{

    // array de 64+2 digitos
    private final static char[] DIGITS66 = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '-', '.', '_', '~'
    };

    private GenerateShortUUID()
    {
    } // singleton

    public static String next()
    {
        UUID u = UUID.randomUUID();
        return toIDString(u.getMostSignificantBits()) + toIDString(u.getLeastSignificantBits());
    }

    private static String toIDString(long i)
    {
        char[] buf = new char[32];
        int z = 64; // 1 << 6;
        int cp = 32;
        long b = z - 1;
        do
        {
            buf[--cp] = DIGITS66[(int) (i & b)];
            i >>>= 6;
        } while (i != 0);
        return new String(buf, cp, (32 - cp));
    }

}