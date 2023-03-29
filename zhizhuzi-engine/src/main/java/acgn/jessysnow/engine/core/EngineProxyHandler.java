package acgn.jessysnow.engine.core;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.common.core.pojo.WebSite;
import acgn.jessysnow.driver.DriverFactory;
import acgn.jessysnow.enums.Browsers;
import acgn.jessysnow.parser.DDomParser;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;

public class EngineProxyHandler<T extends WebSite> implements InvocationHandler {
    private final CrawlEngine<T> engine;
    private final Class<T> clazz;
    private final DDomParser<T> dDomParser;

    protected EngineProxyHandler(CrawlEngine<T> engine, Class<T> clazz){
        this.engine = engine;
        this.clazz = clazz;
        this.dDomParser = new DDomParser<>();
    }

    // FIXME cookie append needed
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("boot")){
            return method.invoke(engine, args[0]);
        }else if (method.getParameterCount() == 1
                && method.getParameterTypes()[0].equals(CrawlTask.class)){
            CrawlTask task = (CrawlTask) args[0];
            if (task.isDynamic()) {
                Browsers type;
                if (null == (type = this.engine.getBrowserType())){
                    throw new IllegalStateException("No browser config found");
                }

                // FIXME driver wait
                WebDriver driver = DriverFactory.buildDriver(type);
                WebElement html;
                try {
                    String uri = task.getUri().toString();
                    if (!StringUtils.isEmpty(task.getCookie())){
                        // initialize cookie context
                        driver.get(uri);
                        driver.manage().deleteAllCookies();
                        // FIXME here need cookie add here
                    }

                    driver.get(uri);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

                    html = driver.findElement(By.tagName("html"));
                }finally {
                    DriverFactory.releaseDriver(driver);
                }

                T t = clazz.getConstructor().newInstance();
                dDomParser.parse(html , t);
                t.setTask(task);
                engine.resultPipeline.execute(() -> engine.resConsumer.accept(t));

                if (method.getName().equals("submit")){
                    return t;
                }
            }else{
                return method.invoke(engine, args[0]);
            }
        }
        return null;
    }
}
