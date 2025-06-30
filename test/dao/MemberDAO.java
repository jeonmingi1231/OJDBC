package test.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import test.dto.MemberDTO;

public class MemberDAO {
	// 필드
	public MemberDTO memberDTO = new MemberDTO();

	public Connection connection = null;
	public Statement statement = null;
	public PreparedStatement preparedStatement = null;
	public ResultSet resultSet = null;
	public int result = 0;

	// 기본생성자
	public MemberDAO() { // db연결 설정 수행
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// db연결 주소 생성
			connection = DriverManager.getConnection
					("jdbc:oracle:thin:@192.168.0.157:1521:xe", "oracletest",
					"oracletest");

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 이름이나, ojdbc6.jar 파일이 잘못 되었습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("url, id, pw가 잘못 되었습니다. MemberDAO에 기본생성자를 확인하세요.");
			e.printStackTrace();
		}
		// 기본생성자 종료

		// 메서드
	}

	public MemberDTO loginCheck(String id, String pw) throws SQLException {
		// 로그인 확인용 메서드 (로그인 시 id,pw 검사 후 회원정보 반환)
		try {
			String sql = "SELECT * FROM MEMBER WHERE ID=? AND PW =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, pw);
			// 쿼리 실행
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				// 로그인 성공 시 , 회원 정보 DTO 객체에 세팅 후 반환
				MemberDTO session = new MemberDTO();

				session.setMno(resultSet.getInt("mno"));
				session.setMname(resultSet.getString("mname"));
				session.setId(resultSet.getString("id"));
				session.setPw(resultSet.getString("pw"));
				session.setRegidate(resultSet.getDate("regidate"));
				return session;
			}
		} catch (SQLException e) {
			System.out.println("오류 발생 : loginCheck 메서드와 sql문을 확인하세요.");
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (preparedStatement != null)
				preparedStatement.close();

		}
		// 로그인 실패시 null 반환.
		return null;
	}

	public boolean sameId(String id) throws SQLException {
		// 입력된 id가 db에 존재하는지 확인하는 메서드 (회원가입시 중복 체크용)
		try {
			String sql = "SELECT ID FROM MEMBER WHERE ID =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			// 결과가 있으면 true, 없으면 false 반환.
			return resultSet.next();
		} catch (SQLException e) {
			System.out.println("오류 발생 : sameId 메서드와 sql문을 확인하세요.");
			e.printStackTrace();
			return false;
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (preparedStatement != null)
				preparedStatement.close();

		}

	}

	public void addMember(MemberDTO memberDTO) throws SQLException {
		// 회원가입용 DAO
		String sql = "INSERT INTO MEMBER(MNO, MNAME, ID, PW, REGIDATE)"
				+ "VALUES(MEMBER_SEQ.NEXTVAL, ?, ?, ?, SYSDATE)";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, memberDTO.getMname());
			preparedStatement.setString(2, memberDTO.getId());
			preparedStatement.setString(3, memberDTO.getPw());
			// 쿼리 확인용 출력 (디버깅 목적)
			System.out.println("쿼리 확인 : " + sql);
			// 쿼리 실행 후 영향 받은 행 수를 result에 저장
			result = preparedStatement.executeUpdate();

			if (result > 0) {
				// 1개 이상의 행이 영향 받으면 성공 및 커밋
				System.out.println(result + "개의 회원이 등록되었습니다.");
				connection.commit();
			} else {
				System.out.println("쿼리 실행 결과 : " + result);
				System.out.println("입력 실패!!!");
				connection.rollback();
			}

		} catch (SQLException e) {
			System.out.println("오류 발생 : addMember 메서드와 sql문을 확인하세요.");
			e.printStackTrace();
		} finally {
			preparedStatement.close();

		}
	}

	public void showAllMembers() throws SQLException {
		// 회원 전체 보기 DAO
		// DB에서 MEMBER 테이블의 내용을 가져오는 쿼리문
		try {
			String sql = "SELECT MNO, MNAME, ID, PW, REGIDATE FROM MEMBER ORDER BY REGIDATE DESC";
			// 쿼리문 실행 객체 생성
			statement = connection.createStatement();
			// 쿼리문을 실행하여 resultSet에 저장
			resultSet = statement.executeQuery(sql);
			System.out.println("번호\t 이름\t id\t pw\t 회원등록일\t ");
			while (resultSet.next()) {
				System.out.print(resultSet.getInt("mno") + "\t");
				System.out.print(resultSet.getString("mname") + "\t");
				System.out.print(resultSet.getString("id") + "\t");
				System.out.print(resultSet.getString("pw") + "\t");
				System.out.println(resultSet.getDate("regidate") + "\t");
			}
			System.out.println("=======끝=======");
		} catch (SQLException e) {
			System.out.println("selectAll()메서드에 쿼리문이 잘못 되었습니다.");
			e.printStackTrace();
		} finally {
			resultSet.close();
			statement.close();

		}

	}

	public void showOneMember(String id) throws SQLException {
		// 개인 회원 보기 DAO
		try {
			String sql = "SELECT MNO, MNAME, ID, PW, REGIDATE FROM MEMBER WHERE ID=?";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, id);
			// 쿼리 실행
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				MemberDTO memberDTO = new MemberDTO(); // 빈객체 생성
				// resultSet 에서 한행씩 읽어서 dto에 세팅
				memberDTO.setMno(resultSet.getInt("mno"));
				memberDTO.setMname(resultSet.getString("Mname"));
				memberDTO.setId(resultSet.getString("id"));
				memberDTO.setPw(resultSet.getString("pw"));
				memberDTO.setRegidate(resultSet.getDate("regidate"));
				// db에 있는 행에 객체 넣기 완료
				// 조회한 회원 정보 출력
				System.out.println("=====검색한 회원 정보=====");
				System.out.println("번호 : " + memberDTO.getMno());
				System.out.println("이름 : " + memberDTO.getMname());
				System.out.println("id : " + memberDTO.getId());
				System.out.println("pw : " + memberDTO.getPw());
				System.out.println("가입일 : " + memberDTO.getRegidate());
			} else {
				System.out.println("없는 회원입니다.");
			}
		} catch (SQLException e) {
			System.out.println("오류 발생 : showOneMember 메서드와 sql문을 확인하세요.");
			e.printStackTrace();
		} finally {
			resultSet.close();
			preparedStatement.close();

		}
	}

	public void updMember(Scanner inputStr, String id) throws SQLException {
		// 회원 수정 메서드
		MemberDTO memberDTO = new MemberDTO(); // 빈객체 생성

		System.out.println("수정할 회원의 새로운 id/pw 를 입력하세요!");
		System.out.print("새로운 id 를 입력하세요 : ");
		memberDTO.setId(inputStr.next());

		System.out.print("새로운 pw 를 입력하세요 : ");
		memberDTO.setPw(inputStr.next());

		try {
			String sql = "UPDATE MEMBER SET ID=? , PW=?, REGIDATE=SYSDATE WHERE ID=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, memberDTO.getId());
			preparedStatement.setString(2, memberDTO.getPw());
			preparedStatement.setString(3, id);

			result = preparedStatement.executeUpdate(); // 쿼리 실행

			if (result > 0) {
				System.out.println(result + "개의 회원이 수정되었습니다.");
				connection.commit();
			} else {
				System.out.println("수정 실패");

			}

		} catch (SQLException e) {
			System.out.println("오류 발생 : updMember 메서드와 sql문을 확인하세요.");
			e.printStackTrace();
		} finally {
			preparedStatement.close();

		}

	}

	public void delMember(String id) throws SQLException {
		// 회원 삭제 메서드
		try {
			String sql = "DELETE FROM MEMBER WHERE ID=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, id);

			result = preparedStatement.executeUpdate();

			if (result > 0) {
				System.out.println(result + "1개의 회원이 삭제 되었습니다.");
				connection.commit();
			} else {
				System.out.println("삭제실패!");
				connection.rollback();
			} // if문 종료
			System.out.println("===========================");
			showAllMembers(); // 삭제 후 전체 리스트 출력
		} catch (SQLException e) {
			System.out.println("오류 발생 : delMember 메서드와 sql문을 확인하세요.");
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connection.close();

		}

	}

}
