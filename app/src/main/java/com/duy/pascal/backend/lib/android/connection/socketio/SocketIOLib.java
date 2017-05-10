/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.lib.android.connection.socketio;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.annotations.PascalMethod;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Duy on 10-May-17.
 */

public class SocketIOLib implements PascalLibrary {

    public static final String NAME = "SocketIO".toLowerCase();

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @PascalMethod(description = "create a socket object with ip and port")
    public Socket createSocket(StringBuilder ip, int port) throws IOException {
        return new Socket(ip.toString(), port);
    }
}
