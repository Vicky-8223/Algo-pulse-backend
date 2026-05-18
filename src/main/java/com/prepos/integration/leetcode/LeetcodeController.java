package com.prepos.integration.leetcode;


import com.prepos.auth.repository.UserRepository;
import com.prepos.integration.leetcode.client.LeetcodeClient;
import com.prepos.integration.leetcode.dto.LeetcodeProfileDTO;
import com.prepos.integration.leetcode.service.LeetcodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leetcode")
@RequiredArgsConstructor
public class LeetcodeController {
    private final LeetcodeService leetcodeService;

    @GetMapping("/{username}")
    public LeetcodeProfileDTO getUserProfile(@PathVariable("username") String username){
         return leetcodeService.getUserProfile(username);
    }

}
