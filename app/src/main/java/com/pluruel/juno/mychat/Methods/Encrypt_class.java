package com.pluruel.juno.mychat.Methods;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by Juno on 2017-01-11.
 */

public class Encrypt_class {
    private static Encrypt_class mEncrypt_class = null;
    private byte[] encryption_code = new byte[512];

    private Encrypt_class(){

    }
    public static Encrypt_class getInstance(){
        if (mEncrypt_class == null){
            mEncrypt_class = new Encrypt_class();
        }
        return mEncrypt_class;
    }

    public void init(){
        encryption_code = null;
    }

    public void make_new_encryptcode(byte[] original_code){
        byte[] to_make_int = new byte[4];
        int i;
        int seed;
        int position;
        int temp;
        System.arraycopy(original_code, 35, to_make_int, 0, 4);
        position = Functions.byteToint(to_make_int);
        position %= 96;
        if (position < 0)
            position = -position;
        System.arraycopy(original_code, position, to_make_int, 0, 4);
        seed = Functions.byteToint(to_make_int);
        Random mRandom = new Random(seed);

        for (i = 0; i < 125; i++){
            temp = mRandom.nextInt();
            byte[] int_to_byte = ByteBuffer.allocate(4).putInt(temp).array();
            System.arraycopy(int_to_byte, 0, encryption_code, i * 4 , 4);
        }

        String s = new java.math.BigInteger(encryption_code).toString(16);
    }
    public byte[] encrypt_data (byte[] original_byte, int length){
        byte[] encrypted_data = new byte[length];
        for (int i = 0; i < length; i++){
            encrypted_data[i] = (byte) (original_byte[i]^encryption_code[i]);
        }

        return encrypted_data;
    }
    public byte[] Decrypt_data (byte[] receved_data, int length){
        byte[] decrypted_data = new byte[length];
        for (int i = 0; i < length; i++){
            decrypted_data[i] = (byte) (receved_data[i]^encryption_code[i]);
        }

        return decrypted_data;
    }

}
