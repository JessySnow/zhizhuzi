package acgn.jessysnow.engine.core;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.common.core.pojo.WebSite;
import acgn.jessysnow.driver.DriverFactory;
import acgn.jessysnow.enums.Browsers;
import acgn.jessysnow.parser.DDomParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;

public class EngineProxyHandler<T extends WebSite> implements InvocationHandler {
    private final CrawlEngine<T> engine;
    private final Class<T> clazz;
    private final Browsers type;

    protected EngineProxyHandler(CrawlEngine<T> engine, Class<T> clazz, Browsers type){
        this.engine = engine;
        this.clazz = clazz;
        this.type = type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("boot")){
            return method.invoke(engine, args[0]);
        }else if (!methodName.equals("close")){
            CrawlTask task = (CrawlTask) args[0];
            if (task.isDynamic()) {
                Browsers type;
                if (null == (type = this.engine.getBrowserType())){
                    throw new IllegalStateException("No browser config found");
                }

                // FIXME driver wait
                WebDriver driver = DriverFactory.buildDriver(type);
                String uri = task.getUri().toString();
                driver.get(uri);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

                T t = clazz.getConstructor().newInstance();
                new DDomParser<>().parse(driver.findElement(By.tagName("html")), t);
                t.setTask(task);
                engine.resConsumer.accept(t);
                if (method.getName().equals("submit")){
                    return t;
                }
                DriverFactory.releaseDriver(driver);
            }else{
                return method.invoke(engine, args[0]);
            }
        }
        return null;
    }
}
