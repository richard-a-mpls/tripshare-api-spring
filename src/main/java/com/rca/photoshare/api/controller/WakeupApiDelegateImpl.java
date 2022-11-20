package com.rca.photoshare.api.controller;

import com.rca.photoshare.api.WakeupApiDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class WakeupApiDelegateImpl implements WakeupApiDelegate {

    final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public ResponseEntity<Void> wakeup() {
        Map envMap = System.getenv();
        logger.info(envMap);
        return ResponseEntity.ok(null);
    }

}
