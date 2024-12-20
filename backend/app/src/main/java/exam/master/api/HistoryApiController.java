package exam.master.api;

import exam.master.domain.Member;
import exam.master.dto.HistoryDTO;
import exam.master.dto.MemberDTO;
import exam.master.dto.PromptDTO;
import exam.master.service.HistoryService;
import exam.master.service.MemberService;
import exam.master.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/history")
@CrossOrigin(origins = {"https://weasel.kkamji.net", "http://localhost:5173"}, allowCredentials = "true")
//@CrossOrigin(origins = "*", allowCredentials = "true")
public class HistoryApiController {

  private final HistoryService historyService;
  private final MemberService memberService;

  @DeleteMapping("/delete/{historyId}")
  public ResponseEntity<Integer> delete(@PathVariable("historyId") UUID historyId, HttpSession session) {

    // 히스토리에 있는 memberId와 로그인 한 memberId 유효성 검사하기 위해서 member 객체 추출
    Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

    log.debug("history delete api 호출 >>> " + historyId);
    int count = historyService.deleteHistoryByHistoryIdAndMemberId(historyId, loginMember.getMemberId());

    return ResponseEntity.ok(count);
  }

  @GetMapping("/list")
  public ResponseEntity<List<HistoryDTO>> list(HttpSession session) {
    Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

    MemberDTO loginMemberDTO = memberService.convertToDTO(loginMember);

    List<HistoryDTO> list = historyService.findByMemberDTO(loginMemberDTO);
    
    return ResponseEntity.ok(list);
  }

}
