package exam.master.security;

import exam.master.domain.Member;
import exam.master.service.MemberService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class MyUserDetailsService implements UserDetailsService {

  private static final Log log = LogFactory.getLog(MyUserDetailsService.class);

  // DBMS에서 사용자 정보를 찾아주는 서비스 객체
  MemberService memberService;

  public MyUserDetailsService(MemberService memberService) {
    this.memberService = memberService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("MyUser username >>> " + username);
    Member member = memberService.login(username);
    if (member == null) {
      throw new UsernameNotFoundException("해당 사용자가 존재하지 않습니다.");
    }

    log.debug(member);

    return MemberUserDetails.create(member);
  }
}

