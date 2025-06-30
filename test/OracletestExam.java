package test;

import java.lang.reflect.Member;
import java.sql.SQLException;
import java.util.Scanner;

import test.dao.MemberDAO;
import test.dto.MemberDTO;
import test.service.BoardService;
import test.service.MemberService;

public class OracletestExam {

	// 필드
	public static Scanner inputStr = new Scanner(System.in); // 키보드로 입력 받을 객체
	public static MemberDTO session = null; // 로그인 상태 저장용

	// 생성자

	// 메서드
	public static void main(String[] args) throws SQLException {

		boolean run = true;
		while (run) {
			System.out.println("=====MBC 자유게시판 입니다=====");
			System.out.println("1.로그인 메뉴");
			System.out.println("2.회원메뉴");
			System.out.println("3.게시판 목록");
			System.out.println("4.프로그램 종료");
			System.out.print(">>>");
			String select = inputStr.next();

			switch (select) {
			case "1":
				boolean loginMenuRun = true;
				while (loginMenuRun) {
					System.out.println("=====로그인 메뉴로 진입합니다=====");
					System.out.println("1.로그인");
					System.out.println("2.회원가입");
					System.out.println("3.로그아웃");
					System.out.println("4.로그인 상태 확인");
					System.out.println("5.뒤로가기");
					System.out.print(">>>");
					String loginChoice = inputStr.next();
					MemberService memberService = new MemberService();
					switch (loginChoice) {
					case "1":
						session = memberService.login(inputStr);
						break;
					case "2":
						memberService.addMember(inputStr);
						break;
					case "3":
						session = null; // 로그아웃
						System.out.println("로그아웃 되었습니다.");
						break;
					case "4":
						if (session != null) {
							System.out.println("현재 로그인 중: " + session.getId());
						} else {
							System.out.println("로그인이 되어 있지 않습니다.");
						}
						break;
					case "5":
						loginMenuRun = false;
						break;
					default:
						System.out.println("1~5 번 중에서 선택하세요");

					}// 로그인초이스 switch문 종료
				} // 로그인메뉴 while문 종료
				break;
			case "2":
				System.out.println("=====회원 메뉴로 진입합니다.=====");
				MemberService memberService = new MemberService();
				memberService.memberMenu(inputStr);
				break;
			case "3":

				System.out.println("======게시판 서비스로 진입합니다.=====");
				BoardService boardService = new BoardService();
				boardService.subMenu(inputStr, session);
				break;
			case "4":
				System.out.println("프로그램을 종료합니다.");
				run = false;
				break;
			default:
				System.out.println("1~4번중에서 선택하세요");

			}// 자유게시판 switch문 종료
		} // 자유게시판 while문 종료

	}// 메인 메서드 종료

}// 클래스 종료
