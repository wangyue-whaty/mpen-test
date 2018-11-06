package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbResourceCode;
import com.mpen.api.service.ResourceCodeService;

@Component
public class ResourceCodeServiceImpl implements ResourceCodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceCodeServiceImpl.class);

    @Override
    public List<DdbResourceCode> getByCatalogId(String bookId, String catalogId) {
        final ArrayList<DdbResourceCode> codeList = Constants.codeMap.get(bookId);
        if (codeList == null || StringUtils.isBlank(catalogId)) {
            return null;
        }
        final Map<String, DdbResourceCode> codeMap = new HashMap<>();
        for (DdbResourceCode code : codeList) {
            if (catalogId.equals(code.getFkCatalogId()) && StringUtils.isNotBlank(code.getText())) {
                codeMap.put(code.getText(), code);
            }
        }
        final List<DdbResourceCode> list = new ArrayList<>(codeMap.values());
        Collections.sort(list, (c1, c2) -> {
            return c1.getNumber() - c2.getNumber();
        });
        return list;
    }
}
