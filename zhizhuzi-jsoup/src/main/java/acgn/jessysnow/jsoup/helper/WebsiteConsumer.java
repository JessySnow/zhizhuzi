package acgn.jessysnow.jsoup.helper;

import acgn.jessysnow.jsoup.annotation.Nodes;
import acgn.jessysnow.jsoup.pojo.WebSite;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Consume Website we crawl
 */
@Log4j2
public class WebsiteConsumer {
    public static void toConsole(WebSite webSite) {
        if (null == webSite){
            log.error("Can't consume a website which is null");
            return ;
        }

        Field[] fields = webSite.getClass().getDeclaredFields();
        for (Field field : fields){
            Annotation[] annotations = field.getAnnotations();
            int index = 0;
            for (; index < annotations.length; ++ index){
                if (annotations[index].annotationType().equals(Nodes.class)){
                    break;
                }
            }
            if(index == annotations.length){
                continue;
            }

            field.setAccessible(true);
            Object o = null;

            // Do noting
            try {
                o = field.get(webSite);
            } catch (IllegalAccessException ignored){}

            _printIt(o, field);
        }
    }

    private static void _printIt(Object result, Field field){
        if (result == null){
            log.error("{} field is null", field.getName());
            return ;
        }

        if (result.getClass().equals(String.class)){
            String fieldName = field.getName();
            System.out.printf("%s : %s", fieldName, result);
        }else if (result instanceof List<?> list){
            System.out.println(field.getName());
            if (list.size() > 0 && !list.get(0).getClass().equals(String.class)){
                log.error("{} field's type unsupported", field.getName());
            }
            list.forEach(item -> System.out.printf("%s ", item));
        }
    }
}
