package acgn.jessysnow.engine.core;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.common.core.pojo.WebSite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EngineProxyHandler<T extends WebSite> implements InvocationHandler {
    private final CrawlEngine<T> engine;

    protected EngineProxyHandler(CrawlEngine<T> engine){
        this.engine = engine;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("boot")){
            return method.invoke(engine, args[0]);
        }else if (!methodName.equals("close")){
            CrawlTask task = (CrawlTask) args[0];
            if (task.isDynamic()) {
                switch (methodName) {
                    case "execute" -> {
                        return "execute";
                    }
                    case "blockExecute" -> {
                        return "blockExecute";
                    }
                    case "submit" -> {
                        return "submit";
                    }
                }
            }else{
                return method.invoke(engine, args[0]);
            }
        }
        return null;
    }
}
