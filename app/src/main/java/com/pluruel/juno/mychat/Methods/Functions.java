package com.pluruel.juno.mychat.Methods;


import android.graphics.Paint;
import android.widget.TextView;

import com.pluruel.juno.mychat.Variables.Header;

import java.nio.ByteBuffer;


/**
 * Created by Juno on 2016-11-17.
 */

public class Functions {
    // 구동에 필요한 여러가지 함수를 담고 있는 Class.


    public static int Big_to_Little_Endian(int a){
        byte b[] = new byte[8];
        b[0] = (byte) (a >> 0 & 0xff);
        b[1] = (byte) (a >> 8 & 0xff);
        b[2] = (byte) (a >> 16 & 0xff);
        b[3] = (byte) (a >> 24 & 0xff);
        return ((b[0] & 0xff) << 24 | (b[1] & 0xff) <<16 | (b[2] & 0xff)<<8 | (b[3] & 0xff));
    }

    public static int byteToint(byte[] arr){
        return ((arr[3] & 0xff) <<24 | (arr[2] & 0xff) <<16 | (arr[1] & 0xff)<<8 | (arr[0] & 0xff));
    }

    public static byte[] making_packet_chat(String s){
        Encrypt_class mEncrypte_class;
        mEncrypte_class = Encrypt_class.getInstance();
        ByteBuffer packet = null;
        packet = ByteBuffer.allocate(s.getBytes().length + 13);
        byte mes[] = s.getBytes();
        packet.putInt(Header.MyIntCode.getStart_code());
        packet.put(Header.MyIntCode.CHAT);
        int little_len = Functions.Big_to_Little_Endian(s.getBytes().length);
        packet.putInt(little_len);
        String e = mes.toString();
        byte encrypted_mes[] = mEncrypte_class.encrypt_data(mes, s.getBytes().length);
        String f = new String(encrypted_mes);
        packet.put(encrypted_mes);
        packet.putInt(Header.MyIntCode.getEnd_code());
        packet.position(0);
        byte sendbuf[] = new byte[s.getBytes().length+13];
        packet.get(sendbuf);
        packet.flip();
        return sendbuf;
    }

    public static byte[] making_packet_sys(String s){
        ByteBuffer packet = null;
        packet = ByteBuffer.allocate(s.getBytes().length + 13);
        byte mes[] = s.getBytes();
        packet.putInt(Header.MyIntCode.getStart_code());
        packet.put(Header.MyIntCode.SYS);
        int little_len = Functions.Big_to_Little_Endian(s.getBytes().length);
        packet.putInt(little_len);
        packet.put(mes);
        packet.putInt(Header.MyIntCode.getEnd_code());
        packet.position(0);
        byte sendbuf[] = new byte[s.getBytes().length+13];
        packet.get(sendbuf);
        packet.flip();
        return sendbuf;
    }
    private static SizeRuler mSizeRuler;

    public static int apply_new_line(TextView tv){
        mSizeRuler = SizeRuler.getInstance();
        Paint paint = tv.getPaint();
        String str = tv.getText().toString();
        int width = mSizeRuler.get_width_size() * 7 / 10;
        int startidx = 0;
        int endidx = paint.breakText(str, true, width, null);
        String temp = str.substring(startidx,endidx);
        while(true){
            startidx = endidx;
            str = str.substring(startidx);
            if(str.length() == 0) break;
            endidx = paint.breakText(str, true, width, null);
            temp += "\n" + str.substring(0,endidx);
        }
        tv.setText(temp);
        int lines2 = temp.split("[\n|\r]").length;
        return lines2;
    }


}


