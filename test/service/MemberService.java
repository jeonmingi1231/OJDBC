package test.service;

import java.sql.SQLException;
import java.util.Scanner;

import test.dao.MemberDAO;
import test.dto.MemberDTO;

public class MemberService {
	MemberDAO memberDAO = new MemberDAO();

	public MemberDTO login(Scanner inputStr) throws SQLException {
		// 로그인 메서드 만들기
		MemberDAO memberDAO = new MemberDAO();

		System.out.print("id를 입력하세요 : ");
		String id = inputStr.next();

		System.out.print("pw를 입력하세요 : ");
		String pw = inputStr.next();

		MemberDTO member = memberDAO.loginCheck(id, pw);
		if (member != null) {
			System.out.println("로그인 성공! 환영합니다!!!!" + member.getMname() + "님");
			return member;
		} else {
			System.out.println("로그인 실패! id 또는 pw 를 확인하세요");
			return null;
		}
	}

	public void addMember(Scanner inputStr) throws SQLException {
		// 회원가입 메서드 만들기
		MemberDAO memberDAO = new MemberDAO();
		MemberDTO memberDTO = new MemberDTO();

		System.out.println("=====회원가입을 진행합니다.=====");
		System.out.print("이름을 입력하세요 : ");
		memberDTO.setMname(inputStr.next());

		System.out.print("id를 입력하세요 : ");
		String id = inputStr.next();

		if (memberDAO.sameId(id)) {
			System.out.println("이미 존재하는 id입니다.");
			return;
		}
		memberDTO.setId(id);

		System.out.print("pw를 입력하세요 : ");
		memberDTO.setPw(inputStr.next());

		memberDAO.addMember(memberDTO);
	}

	public void memberMenu(Scanner inputStr) throws SQLException {
		boolean mRun = true;
		while (mRun) {
			// 회원 정보 메뉴 메서드
			System.out.println("1.회원 전체 보기");
			System.out.println("2.회원 개인 보기");
			System.out.println("3.회원 정보 수정");
			System.out.println("4.회원 정보 삭제");
			System.out.println("5.뒤로가기");
			System.out.print(">>>");
			String choice = inputStr.next();

			switch (choice) {
			case "1":
				showAllMembers();
				break;
			case "2":
				showOneMember(inputStr);
				break;
			case "3":
				updMember(inputStr);
				break;
			case "4":
				delMember(inputStr);
				break;
			case "5":
				mRun = false;
				break;

			default:
				System.out.println("잘못 입력하셨습니다. 1~5번 의 값을 입력해주세요.");
			}
		}

	}

	private void delMember(Scanner inputStr) throws SQLException {
		// 회원 삭제 메서드
		System.out.println("=====================");
		System.out.print("삭제하실 회원 id를 입력하세요 : ");
		String id = inputStr.next();
		memberDAO.delMember(id);
		System.out.println("=====================");
	}

	private void updMember(Scanner inputStr) throws SQLException {
		// 회원 수정 메서드
		System.out.println("=====================");
		System.out.print("수정하실 회원 id를 입력하세요 : ");
		String id = inputStr.next();
		memberDAO.updMember(inputStr, id);
		System.out.println("=====================");
	}

	private void showOneMember(Scanner inputStr) throws SQLException {
		// 회원 개인 보기 메서드
		System.out.println("=====================");
		System.out.print("조회하실 회원 id를 입력하세요 : ");
		String id = inputStr.next();
		memberDAO.showOneMember(id);
		System.out.println("=====================");
	}

	private void showAllMembers() throws SQLException {
		// 회원 전체 보기 메서드
		System.out.println("====================");
		memberDAO.showAllMembers();
		System.out.println("====================");
	}

}
