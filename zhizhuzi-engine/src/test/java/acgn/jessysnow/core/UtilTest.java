package acgn.jessysnow.core;

import acgn.jessysnow.engine.helper.SysHelper;
import org.junit.jupiter.api.Test;

public class UtilTest {
    @Test
    public void SysHelperTest(){
        SysHelper.SysType sysType = SysHelper.getSysType();
        System.out.println(sysType);
    }
}