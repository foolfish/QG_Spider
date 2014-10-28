package com.lkb.util;
import javax.xml.bind.DatatypeConverter;

public class TestBase64 {
	private static volatile String save = null;
    public static void main(String argv[]) {
        String teststr = "Chenxu001";
        String b64 = DatatypeConverter.printBase64Binary(teststr.getBytes());
        System.out.println(teststr + " = " + b64);
//        try {
//            final int COUNT = 1000000;
//            long start;
//            start = System.currentTimeMillis();
//            for (int i=0; i<COUNT; ++i) {
//                save = new String(DatatypeConverter.parseBase64Binary(b64));
//            }
//            System.out.println("javax.xml took "+(System.currentTimeMillis()-start)+": "+save);
//            start = System.currentTimeMillis();
//            for (int i=0; i<COUNT; ++i) {
//                save = new String(Base64.decodeBase64(b64));
//            }
//            System.out.println("apache took    "+(System.currentTimeMillis()-start)+": "+save);
//            sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
//            start = System.currentTimeMillis();
//            for (int i=0; i<COUNT; ++i) {
//                save = new String(dec.decodeBuffer(b64));
//            }
//            System.out.println("sun took       "+(System.currentTimeMillis()-start)+": "+save);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
}
