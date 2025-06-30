package test.service;

import java.sql.SQLException;
import java.util.Scanner;

import oracle.net.ns.SessionAtts;
import test.dao.BoardDAO;
import test.dto.BoardDTO;
import test.dto.MemberDTO;

public class BoardService {

	// 필드
	public BoardDAO boardDAO = new BoardDAO();
	// 생성자

	// 메서드
	public void subMenu(Scanner inputStr, MemberDTO session) throws SQLException {
		boolean subRun = true;
		while (subRun) {
			// 게시판 서비스 메뉴
			System.out.println("=====MBC 게시판 서비스 입니다.=====");
			System.out.println("1.게시글 작성");
			System.out.println("2.게시글 모두보기");
			System.out.println("3.게시글 조회보기");
			System.out.println("4.게시글 수정");
			System.out.println("5.게시글 삭제");
			System.out.print(">>>");
			String subSelect = inputStr.next();

			switch (subSelect) {
			case "1":
				if (session == null) {
					System.out.println("로그인 후 이용 가능합니다.");
				} else {
					System.out.println("게시글 작성 페이지로 진입합니다.");
					insertBoard(session, boardDAO, inputStr);
				}

				break;
			case "2":
				System.out.println("게시글 모두 보기 페이지로 진입합니다.");
				selectAll();
				break;
			case "3":
				System.out.println("게시글 조회 보기 페이지로 진입합니다.");
				readOne(inputStr);
				break;
			case "4":
				if (session == null) {
					System.out.println("로그인 후 이용 가능합니다.");
				} else {
					System.out.println("게시글 수정 페이지로 진입합니다.");
					modify(inputStr, session);
				}
				break;
			case "5":
				if (session == null) {
					System.out.println("로그인 후 이용 가능합니다.");
				} else {
					System.out.println("게시글 삭제 페이지로 진입합니다.");
					deleteOne(inputStr, session);
				}
				break;
			default:
				subRun = false;
				System.out.println("1~5까지만 입력해주세요.");

			}
		}
	}

	private void deleteOne(Scanner inputStr, MemberDTO session) throws SQLException {
		// 게시글 삭제 메서드
		System.out.print("삭제하려는 게시글의 번호를 입력하세요! : ");
		Scanner inputInt = new Scanner(System.in);
		int selectBno = inputStr.nextInt(); // 게시글 int 번호 입력

		BoardDTO post = boardDAO.getPostByNo(selectBno);
		if (post == null) {
			System.out.println("해당 게시글이 없습니다.");
			return;
		}
		if (!post.getBwriter().equals(session.getId())) {
			// 게시글 작성자와 로그인한 아이디가 다르면 삭제 불가
			System.out.println("본인이 작성한 글만 삭제할 수 있습니다.");
			return;
		}
		boardDAO.deleteOne(selectBno);
		System.out.println("게시글이 삭제 되었습니다.");
	}

	private void modify(Scanner inputStr, MemberDTO session) throws SQLException {
		// 게시글 수정 메서드
		System.out.print("수정하려는 게시글의 제목을 입력하세요! : ");
		String title = inputStr.next();

		BoardDTO post = boardDAO.readOneReturn(title);
		if (post == null) {
			System.out.println("해당 게시글이 없습니다.");
			return;
		}
		if (!post.getBwriter().equals(session.getId())) {
			System.out.println("본인이 작성한 글만 수정할 수 있습니다.");
			return;
		}
		boardDAO.modify(inputStr, title);
		System.out.println("===========끝=============");
	}

	private void readOne(Scanner inputStr) throws SQLException {
		// 개시글 조회 보기 메서드 만들기
		System.out.print("조회하실 게시글 제목을 입력하세요! : ");
		String title = inputStr.next();

		boardDAO.readOne(title);
		System.out.println("===========끝=============");
	}

	private void selectAll() throws SQLException {
		// 게시글 모두 보기 메서드 만들기
		System.out.println("===================");
		System.out.println("=====MBC 게시판 목록 입니다.=====");
		boardDAO.selectAll();
		System.out.println("====================");

	}

	private void insertBoard(MemberDTO session, BoardDAO boardDAO, Scanner inputStr) throws SQLException {
		// 게시글 작성 메서드 만들기
		BoardDTO boardDTO = new BoardDTO(); // 게시글 정보를 담을 빈 객체 생성

		boardDTO.setBwriter(session.getId()); // 로그인 된 아이디로 작성자 설정

		System.out.print("제목 : ");
		boardDTO.setBtitle(inputStr.next()); // 키보드로 제목을 입력받아 DTO에 저장

		Scanner inputLine = new Scanner(System.in);
		System.out.print("내용 : ");
		boardDTO.setBcontent(inputLine.nextLine()); // 내용에는 띄어쓰기 있을수 있으니까 nextLine 생성

		boardDAO.insertBoard(); // DB에 게시글 저장 요청
		System.out.println("=====게시글 작성 종료=====");
	}

}
