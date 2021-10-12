package com.rca.photoshare.api.controller;

import com.rca.photoshare.api.WakeupApiDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class WakeupApiDelegateImpl implements WakeupApiDelegate {

    @Override
    public ResponseEntity<Void> wakeup() {
        return ResponseEntity.ok(null);
    }

}
