package db_project.miggule.service;

import db_project.miggule.entity.Commentary;
import db_project.miggule.entity.Player;
import db_project.miggule.entity.User;
import db_project.miggule.repository.CommentaryRepository;
import db_project.miggule.repository.PlayerRepository;
import db_project.miggule.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class CommentaryService {
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final CommentaryRepository commentaryRepository;

    public CommentaryService(UserRepository userRepository, PlayerRepository playerRepository, CommentaryRepository commentaryRepository) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.commentaryRepository = commentaryRepository;
    }

    // 1. 코멘토리 등록 로직
    @Transactional
    public Commentary createOfficialCommentary(Integer userId, CreateCommentaryRequest request){
        // 1. 엔터티 조회 및 예외 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자 정보를 찾을 수 없습니다"));

        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new NoSuchElementException("선수 정보를 찾을 수 없습니다."));

        // 2. 권한 검증 (선수 역할 검증)
        if(!user.getUserRole().equals("player")){
            throw new SecurityException("코맨터리는 선수 역할의 유저만이 작성할 수 있습니다");
        }

        // 2. 권한 검증 (선수 유저 id가 선수 id가 같은지 검증)
        if(!Objects.equals(user, player.getUser())){
            throw new SecurityException("선수 유저는 자기 자신의 코맨터리만 남길 수 있습니다.");
        }

        // 3. Commentary 객체 생성
        Commentary commentary = new Commentary(user,player, request.comment());

        commentaryRepository.save(commentary);
        return commentary;
    }

    // 2. 특정 선수 코맨터리 목록 조회 및 공식 여부 판별
    @Transactional(readOnly = true)
    public List<CommentaryResponse> getCommentariesByPlayer(Integer playerId){
        // 1. 레파지토리에서 공식 여부 판별 코맨터리 부르기
        List<Object[]> results = commentaryRepository.findCommentaryAndOfficialStatus(playerId);

        // 2. 전달 받은 result를 가공해 스트림으로 변환
        return results.stream().map(result -> {
            // 데이터 추출
            Commentary commentary = (Commentary)result[0];
            boolean isOfficial = (Boolean) result[1];

            return convertCommentaryResponse(commentary,isOfficial);
        }).toList();
    }

    // 3. 팀별 코맨터리 목록 조회
    @Transactional(readOnly = true)
    public List<CommentaryResponse> getCommentariesByTeamId(Integer teamId){
        // 1. 레파지토리에서 팀별 코맨터리 부르기
        List<Commentary> results = commentaryRepository.findByTeamId(teamId);

        // 2. 전달 받은 result들에서 공식 여부 판별
        return results.stream().map(result-> {
            return convertCommentaryResponse(result,checkIsOfficial(result));
        }).toList();
    }

    // 4. 코멘터리 검색 기능
    @Transactional(readOnly = true)
    public List<CommentaryResponse> getCommentariesByInputString(String inputString){
        // 1. 레파지토리에서 inputString에 포함된 선수 코맨토리 모두 탐색
        List<Commentary> results = commentaryRepository.findByPlayerName(inputString);

        // 2. 전달 받은 result들에서 공식 여부 판별
        return results.stream().map(result -> {
            return convertCommentaryResponse(result, checkIsOfficial(result));
        }).toList();
    }

    // 5. 현재 로그인된 User객체와 PlayerId를 받아 공식 소유자인지 판별
    @Transactional(readOnly = true)
    public boolean isPlayerAccountOwner(User currentUser, Integer playerId){
        if(currentUser == null) return false;

        // Player 엔터티 조회
        Player player = playerRepository.findById(playerId).orElse(null);

        return checkIsPlayerAccount(currentUser,player);
    }
    public record CommentaryResponse(
            Integer commentId,
            String userName,
            String comment,
            boolean isOfficial,
            Integer authorId,
            String createdTime,
            Integer playerId,
            String playerName
    ) {}

    // 공식 여부 판별 메서드
    private boolean checkIsOfficial(Commentary commentary){
        return commentary.getPlayer() != null &&
                commentary.getPlayer().getUser()!=null &&
                commentary.getPlayer().getUser().equals(commentary.getUser());
    }

    // CommentaryResponse 변환 메서드
    private CommentaryResponse convertCommentaryResponse(Commentary commentary, boolean isOfficial){
        return new CommentaryResponse(
                commentary.getComment_id(),
                commentary.getUser().getUsername(),
                commentary.getComment(),
                isOfficial,
                commentary.getUser().getUserId(),
                commentary.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                commentary.getPlayer().getPlayerId(),
                commentary.getPlayer().getName()
        );
    }

    // User 엔터티와 Player 엔터티를 전달받아 공식 계정 소유자인지 판별
    private boolean checkIsPlayerAccount(User currentUser, Player player){
        if(currentUser == null || player == null) return false;
        return currentUser.equals(player.getUser());
    }

    public record CreateCommentaryRequest(Integer playerId, String comment){}
}
