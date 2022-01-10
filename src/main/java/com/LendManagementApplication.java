package com;

import com.service.FriendService;
import com.service.LendService;
import org.springframework.stereotype.Service;

@Service
public class LendManagementApplication {
    private final FriendService friendService;
    private final LendService lendService;

    public LendManagementApplication(FriendService friendService, LendService lendService) {
        this.friendService = friendService;
        this.lendService = lendService;
    }
}
