package acgn.jessysnow.jsoup.helper;

import acgn.jessysnow.jsoup.annotation.Nodes;
import acgn.jessysnow.jsoup.pojo.WebSite;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

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

        if (result instanceof List<?> list && list.size() > 0){
            System.out.printf("%s(%s): \n\t", field.getName(), result.getClass());
            for (int i = 1; i <= list.size(); i++) {
                if (i % 5 == 0){
                    System.out.printf("%s\n\t", list.get(i - 1));
                }else{
                    System.out.printf("%s, ", list.get(i - 1));
                }
            }
            System.out.println();
        }else{
            System.out.printf("%s(%s) : %s\n", field.getName(), result.getClass(), result);
        }
    }
}
