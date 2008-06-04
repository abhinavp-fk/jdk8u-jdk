/*
 * Copyright 2002 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

/* @test
   @bug 4645302
   @summary Socket with OP_WRITE would get selected only once
   @author kladko
 */

import java.net.*;
import java.nio.*;
import java.nio.channels.*;


public class SelectWrite {

    public static void main(String[] argv) throws Exception {
        ByteServer server = new ByteServer(0);
        // server: accept connection and do nothing
        server.start();
        InetSocketAddress isa = new InetSocketAddress(
                InetAddress.getByName(ByteServer.LOCALHOST), ByteServer.PORT);
        Selector sel = Selector.open();
        SocketChannel sc = SocketChannel.open();
        sc.connect(isa);
        sc.configureBlocking(false);
        sc.register(sel, SelectionKey.OP_WRITE);
        sel.select();
        sel.selectedKeys().clear();
        if (sel.select() == 0) {
            throw new Exception("Select returned zero");
        }
        sc.close();
        sel.close();
    }

}
