package com.mpen.api.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mpen.api.common.Constants;

public class DdbResourceCodeTest {
    @Test
    public void initBookCodeDetailTest() {
        DdbResourceCode.initBookCodeDetail();
        final String[] expectBookIds = { "ff80808152f8de080152f8e542cf0002", "ff80808156ca3d900156caac68a50007",
            "ff80808156ca3d900156cac8350c0009", "ff80808156ca3d900156cacd16e8000d", "ff80808156ca3d900156cace3056000f",
            "ff80808156ca3d900156cacee3fc0011", "ff80808156ca3d900156cacf93420013", "ff80808156ca3d900156cb19feff004c",
            "ff808081533ba5a801533bbd358f0003", "ff808081533ba5a801533bbddf220006", "ff808081533ba5a801533bbe58360008",
            "ff808081581deb4101581e74ac7d0088", "ff8080815847b3010158489abb5600ae", "ff808081567761c2015691ef1d2e06a6",
            "ff808081567761c2015691fd171d06a9", "ff808081567761c2015691ff8b7e06ab", "ff808081567761c20156920b17fb06bd",
            "ff808081567761c20156920d03f706c0", "ff808081567761c20156920f6c5006c4", "ff808081567761c201569201cfde06ad",
            "ff808081567761c201569206d9d206b7", "ff808081567761c201569212caef06ca", "ff808081567761c201569213fdca06cc",
            "ff808081567761c2015692106cfa06c6", "ff808081567761c2015692119de006c8", "ff808081567761c2015692154c5506ce",
            "ff808081567761c201569203814e06af", "ff808081567761c201569208005d06b9", "ff808081567761c201569205449706b5",
            "ff808081567761c201569209573106bb" };
        assertEquals(Constants.codeMap.size(), expectBookIds.length);
        for (String bookIds : expectBookIds) {
            assertTrue(Constants.codeMap.get(bookIds) != null);
        }
    }

    @Test
    public void getCodeTest() {
        DdbResourceCode.initBookCodeDetail();
        final String[][] param = { { "ff80808152f8de080152f8e542cf0002", "1" },
            { "ff80808156ca3d900156caac68a50007", "0" }, { "ff80808156ca3d900156cac8350c0009", "10024" },
            { "ff80808156ca3d900156cacd16e8000d", "10203" } };
        final String[] expectResult = { "ff8080815ba8c242015ba8f2106237ac", "ff8080815bd28a63015bd28fa6ff0ec1",
            "ff808081592703b7015929506f0842c1", "ff808081592703b701592971678166ae" };
        for (int i = 0; i < expectResult.length; i++) {
            final DdbResourceCode code = DdbResourceCode.getCode(param[i][0], Integer.valueOf(param[i][1]));
            assertEquals(code.getId(), expectResult[i]);
        }
    }
}
