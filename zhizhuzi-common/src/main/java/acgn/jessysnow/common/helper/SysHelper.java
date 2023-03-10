package acgn.jessysnow.common.helper;

public class SysHelper {
    public static SysType getSysType(){
        String sysType = System.getProperty("os.name").toLowerCase();
        if (sysType.contains("windows")){
            return SysType.Windows;
        }else if (sysType.contains("mac")){
            return SysType.MAC_OS_X;
        }else if (sysType.contains("linux")){
            return SysType.Linux;
        }else if (sysType.contains("freebsd")){
            return SysType.FreeBSD;
        }else {
            return null;
        }
    }

    /**
     * WorkGroupType
     * MAC_OS_X(MAC_OS):Kqueue
     * Windows: NIO
     * Linux: Epoll
     * FreeBSD: Kqueue
     */
    public enum SysType {
        MAC_OS_X, Windows, Linux, FreeBSD
    }
}
