package com.ssafy.board.controller;

import com.ssafy.board.model.Member;
import com.ssafy.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 사용자가 로그인을 요청하면 이 메서드가 호출됩니다.
     * @param loginDto 로그인 정보를 담고 있는 LoginDto 객체입니다. 이 객체에는 사용자 ID와 비밀번호가 포함되어 있습니다.
     * @return 로그인 성공 시 사용자의 토큰을 반환하고, 실패 시 에러 메시지를 반환합니다.
     */
    @PostMapping("/login")
    @ResponseBody
    public String logIn(@RequestBody LoginDto loginDto) {
        return memberService.logIn(loginDto.getUserId(), loginDto.getUserPwd());
    }

    /**
     * 사용자가 회원가입을 요청하면 이 메서드가 호출됩니다.
     * @param member 회원 정보를 담고 있는 Member 객체입니다. 이 객체에는 사용자 ID, 비밀번호, 이메일 등의 회원 정보가 포함되어 있습니다.
     * @return 회원가입 성공 시 사용자의 토큰을 반환하고, 실패 시 에러 메시지를 반환합니다.
     */
    @PostMapping("/signup")
    @ResponseBody
    public String signUp(@RequestBody Member member) {
        return memberService.signUp(member);
    }
}
