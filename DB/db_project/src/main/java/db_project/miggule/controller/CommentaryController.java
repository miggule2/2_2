package db_project.miggule.controller;

import db_project.miggule.entity.Player;
import db_project.miggule.entity.Team;
import db_project.miggule.entity.User;
import db_project.miggule.repository.PlayerRepository;
import db_project.miggule.repository.TeamRepository;
import db_project.miggule.repository.UserRepository;
import db_project.miggule.service.CommentaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/commentaries")
public class CommentaryController {
    private final CommentaryService commentaryService;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    public CommentaryController(CommentaryService commentaryService, TeamRepository teamRepository, PlayerRepository playerRepository, UserRepository userRepository) {
        this.commentaryService = commentaryService;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    // 모든 팀 목록을 가져와 네비게이션 필터에 사용할 수 있도록 Model에 추가하는 메서드
    private void addTeamListToModel(Model model){
        model.addAttribute("teams", teamRepository.findAll());
    }

    // 1. 팀별 코멘터리 조회
    @GetMapping("/team")
    public String getCommentariesByTeam(@RequestParam(required = false)Integer teamId, Model model) {
        List<CommentaryService.CommentaryResponse> commentaries;
        Team team = null;
        if(teamId != null){
            // 팀별 조회 목록
            commentaries = commentaryService.getCommentariesByTeamId(teamId);
            team = teamRepository.findById(teamId).orElse(null);
            model.addAttribute("listTitle", (team!=null) ? team.getTeamName() : null  + "코멘토리 목록");
        } else{
            commentaries = commentaryService.getCommentariesByTeamId(1);
            team = teamRepository.findById(1).orElse(null);
            model.addAttribute("listTitle",(team!=null) ? team.getTeamName() : null  + "코멘토리 목록");
        }

        model.addAttribute("commentaries", commentaries);
        model.addAttribute("teamId", teamId);
        addTeamListToModel(model);
        return "commentaries_list";
    }

    // 2. 특정 선수 조회
    @GetMapping("/search")
    public String searchCommentariesByInput(
            @RequestParam(required = false)String inputString, Model model
    ){
        List<CommentaryService.CommentaryResponse> commentaries = List.of();
        if(inputString != null) {
            commentaries = commentaryService.getCommentariesByInputString(inputString);
            model.addAttribute("listTitle", inputString.trim() + " 코멘토리 목록");
        } else{
            model.addAttribute("listTitle","코멘토리 목록");
        }
        model.addAttribute("commentaries", commentaries);
        model.addAttribute("searchPlayer", inputString);
        addTeamListToModel(model);
        return "commentaries_search";
    }

    // 3. 선수 상세 코멘토리 조회
    @GetMapping("/players/{playerId}")
    public String viewPlayerCommentary(@PathVariable(required = false)Integer playerId, Model model, Principal principal){
        // 1. 선수 정보 조회
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchElementException("해당 선수를 찾을 수 없습니다"));

        // 2. 특정 선수 코멘터리 조회
        List<CommentaryService.CommentaryResponse> commentaries = commentaryService.getCommentariesByPlayer(playerId);

        model.addAttribute("commentaries", commentaries);
        model.addAttribute("player", player);

        // 권한 제어 로직
        Integer currentUserId= null;
        boolean isPlayerAccountOwner = false;

        if(principal != null){
            //principle을 통해 userId를 가져와 User객체 조회
            User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);

            if(currentUser != null){
                currentUserId = currentUser.getUserId();

                isPlayerAccountOwner = commentaryService.isPlayerAccountOwner(currentUser, playerId);
            }
        }

        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("isPlayerAccountOwner", isPlayerAccountOwner);

        return "player_commentary";
    }

    // 4. 코멘터리 생성
    @PostMapping
    public String createOfficialCommentary(
            @ModelAttribute CommentaryService.CreateCommentaryRequest request,
            Principal principal
    ){
        if(principal == null) {
            throw new SecurityException("로그인이 필요합니다");
        }

        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(()-> new SecurityException("로그인 정보를 찾을 수 없습니다"));

        try{
            commentaryService.createOfficialCommentary(currentUser.getUserId(),request);
            return "redirect:/commentaries/players/" + request.playerId();
        } catch (SecurityException e){
            // 권한 부족 (선수가 아니거나 자신의 프로필이 아닐 경우) 오류 처리
            return "redirect:/error?message=" + e.getMessage();
        }
    }
}
