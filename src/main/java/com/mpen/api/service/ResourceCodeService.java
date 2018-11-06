package com.mpen.api.service;

import java.util.List;

import com.mpen.api.domain.DdbResourceCode;

public interface ResourceCodeService {
    List<DdbResourceCode> getByCatalogId(String bookId, String catalogId);
}
