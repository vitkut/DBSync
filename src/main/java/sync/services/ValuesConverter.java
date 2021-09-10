package sync.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValuesConverter {

    private static final Logger logger = LoggerFactory.getLogger(ValuesConverter.class);

    private static String fromHexToInt(String hexValue){
        try {
            hexValue = hexValue.replaceAll("0x", "");
            return Integer.valueOf(hexValue, 16).toString();
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return "NULL";
        }
    }

    private static String fromIntToHex(String intValue){
        return String.format("0x%x", Integer.parseInt(intValue));
    }

    public static String convert(String string, String method){
        switch (method){
            case "fromHexToInt":{
                return fromHexToInt(string);
            }
            case "fromIntToHex":{
                return fromIntToHex(string);
            }
            default:{
                logger.error("Can't convert value by method [{}]", method);
                return string;
            }
        }
    }
}
