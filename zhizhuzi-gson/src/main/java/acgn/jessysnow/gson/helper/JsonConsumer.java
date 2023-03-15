package acgn.jessysnow.gson.helper;

import acgn.jessysnow.gson.pojo.Json;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

// TODO Simplify
@Log4j2
public class JsonConsumer extends WebsiteConsumer {
    public static void toConsole(Json json){
        if (null == json){
            log.error("Can't consume a website which is null");
            return ;
        }

        Field[] fields = json.getClass().getDeclaredFields();
        for (Field field : fields){
            Object result = null;
            try {
                result = field.get(json);
            } catch (IllegalAccessException ignored) {}
            _printIt(result, field);
        }
    }

    private static void _printIt(Object result, Field field){
        if (result == null){
            log.error("{} field is null", field.getName());
            return ;
        }

        if ((result instanceof List<?>) || (result instanceof Object[])){
            System.out.printf("%s(%s):\n", field.getName(), field.getClass());
            recursivePrint(result, 0);
        }else {
            System.out.printf("%s(%s) : %s\n", field.getName(), result.getClass(), result);
        }
    }

    private static void recursivePrint(Object sequence, int depth){
        char[] TB_SPC = new char[depth];
        Arrays.fill(TB_SPC,' ');
        String TB_SPS = new String(TB_SPC);
        if (sequence instanceof Object[] array){
            Arrays.stream(array).forEach(item -> recursivePrint(item, depth + 1));
            System.out.println();
        }else if (sequence instanceof List<?> list){
            list.forEach(item -> recursivePrint(item, depth + 1));
            System.out.println();
        }else{
            System.out.printf("%s%s", TB_SPS, sequence);
        }
    }
}
