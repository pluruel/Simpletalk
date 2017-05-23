package com.pluruel.juno.mychat.Methods;

import java.util.Random;

/**
 * Created by Juno on 2017-01-17.
 */

public class IntroText_Returner {
    private static IntroText_Returner mIntro_Text_Returer = null;
    private Random pick_text = null;
    private static String Intro_text_list[] = {
            "채팅창 청소 중.",
            "막말 하는 유저 쫒아내는 중.",
            "심심해 미치겠는 사람 확인 중.",
            "잠수타는 사람 쫒아내는 중",
            "채팅창 광 내는 중",
            "어디서 구린내 안 나는지 확인 중",
            "채팅창 따뜻하게 데우는 중",
            "말풍선 터트리는 중",
            "채팅에 쓸 말풍선 만드는 중",
            "철물점에서 뺀찌 사오는 중",
            "마스터에게 팁 주는 중",
            };
    private long time = -1;
    private IntroText_Returner(){
        time = System.currentTimeMillis();
         pick_text = new Random(time);
    }
    public static IntroText_Returner getInstance(){
        if (mIntro_Text_Returer == null)
            mIntro_Text_Returer = new IntroText_Returner();
        return mIntro_Text_Returer;
    }

    public String Return_text(){
        int picked_int = pick_text.nextInt();
        int picked_one = picked_int % 11;
        if (picked_one < 0)
            picked_one = -picked_one;
        return Intro_text_list[picked_one];
    }

}
