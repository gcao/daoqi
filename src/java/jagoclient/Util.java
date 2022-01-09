package jagoclient;

import BlowfishJ.BlowfishECB;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2005-7-18
 * Time: 11:35:41
 */
public class Util {
    private static final String MY_KEY = "a3fdFH334fiIU";

    private static final String ENC_PREFIX = "ENCPTD:";

    public static String blowfishDecrypt(String blowkey, String input)
            throws IOException {
        BlowfishECB becb = new BlowfishECB(blowkey.getBytes());
        byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(input);
        String cleartext = "";

        for (int i = 0; i < (bytes.length / 8); i++) {
            byte[] bytes8 = new byte[8];

            for (int j = 0; j < 8; j++) {
                bytes8[j] = bytes[(i * 8) + j];
            }

            becb.decrypt(bytes8);
            cleartext += new String(bytes8);
        }

        int len = cleartext.length();

        try {
            String result = cleartext.substring(0, (len - 8 + (int) cleartext
                    .charAt(len - 1)));

            return result;
        } catch (Exception e) {
            return cleartext;
        }
    }

    public static String blowfishEncrypt(String blowkey, String input) {
        BlowfishECB blowfish = new BlowfishECB(blowkey.getBytes());

        // copy all bytes of string into the buffer (use network byte order)
        int nStrLen = input.length();

        byte[] inbuf = new byte[((nStrLen / 8) + 1) * 8];

        int nPos = 0;

        for (int i = 0; i < input.length(); i++) {
            char cActChar = input.charAt(i);
            inbuf[nPos++] = (byte) (cActChar & 0x0ff);
        }

        // pad the rest with the PKCS5 scheme
        byte bPadVal = (byte) (input.length() % 8);

        while (nPos < inbuf.length) {
            inbuf[nPos++] = bPadVal;
        }

        blowfish.encrypt(inbuf);

        return new sun.misc.BASE64Encoder().encode(inbuf);
    }

    public static String encrypt(String input) {
        return blowfishEncrypt(MY_KEY, input);
    }

    public static String decrypt(String input) {
        try {
            return blowfishDecrypt(MY_KEY, input);
        } catch (Exception e) {
            return input;
        }
    }

    public boolean isEncrypted(String input) {
        return input.startsWith(ENC_PREFIX);
    }
}
