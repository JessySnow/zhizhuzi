package acgn.jessysnow.common.core.interfaces;

import acgn.jessysnow.common.core.pojo.WebSite;

/**
 * Super class(interface) of all parser
 */
public interface Parser<T extends WebSite>{
    T parse(Object html, T site);
}
