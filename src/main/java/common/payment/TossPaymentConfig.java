package common.payment;

public class TossPaymentConfig {
    
    public static final String TEST_CLIENT_KEY = "test_ck_XXXXXXX";
    public static final String TEST_SECRET_KEY = "test_sk_XXXXXXX";
    
    public static final String LIVE_CLIENT_KEY = "실제_클라이언트_키";
    public static final String LIVE_SECRET_KEY = "실제_시크릿_키";
    
    private static final boolean IS_TEST_MODE = true;
    
    public static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments/confirm";
    
    public static String getClientKey() {
        return IS_TEST_MODE ? TEST_CLIENT_KEY : LIVE_CLIENT_KEY;
    }
    
    public static String getSecretKey() {
        return IS_TEST_MODE ? TEST_SECRET_KEY : LIVE_SECRET_KEY;
    }
    
    public static boolean isTestMode() {
        return IS_TEST_MODE;
    }
}