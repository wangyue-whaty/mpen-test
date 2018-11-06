package com.mpen.api.service;

import com.mpen.api.domain.MobileApp;

public interface MobileAppService {
    MobileApp get(MobileApp.Type type) throws Exception;
}
