package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.FrameRequest;
import com.ibrahimahmads.github.vbooth.entity.Frame;

import java.util.List;
import java.util.UUID;

public interface FrameService {
    Frame saveFrame(FrameRequest payload);
    List<Frame> getAllFrames();
    Frame updateFrame(UUID id,FrameRequest payload);
    void deleteFrame(UUID id);
}
