package test.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import test.dto.BoardDTO;

public class BoardDAO {

	// 필드
	public BoardDTO boardDTO = new BoardDTO();
	public Connection connection = null;
	public Statement statement = null;
	public PreparedStatement preparedStatement = null;
	public ResultSet resultSet = null;
	public int result = 0;

	// 기본 생성자
	public BoardDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 1단계 ojdbc6.jar 호출
			connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.157:1521:xe", "oracletest",
					"oracletest"); // 2단계
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 이름이나, ojdbc6.jar 파일이 잘못 되었습니다.");
			e.printStackTrace();
			System.exit(0); // 강제 종료
		} catch (SQLException e) {
			System.out.println("url, id, pw가 잘못 되었습니다. BoardDAO에 기본생성자를 확인하세요");
			e.printStackTrace();
			System.exit(0); // 강제 종료
		}

	}

	// 메서드
	public void insertBoard() throws SQLException {
		try {
			String sql = "INSERT INTO BOARD(BNO, BTITLE, BCONTENT, BWRITER, BDATE)"
					+ " VALUES(BOARD_SEQ.NEXTVAL, ?, ?, ?,SYSDATE)";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, boardDTO.getBtitle());
			preparedStatement.setString(2, boardDTO.getBcontent());
			preparedStatement.setString(3, boardDTO.getBwriter());
			System.out.println("쿼리 확인 : " + sql);

			result = preparedStatement.executeUpdate();

			if (result > 0) {
				System.out.println(result + "1개의 게시글이 등록되었습니다.");
				connection.commit();
			} else {
				System.out.println("쿼리 결과 : " + result);
				System.out.println("등록 실패!!!!");
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("오류발생 : inserBoard 메서드와 쿼리문 확인하세요.");
			e.printStackTrace();
		} finally {
			preparedStatement.close();
		
		}

	}

	public void selectAll() throws SQLException {
		// 게시글 전체 보는 메서드
		try {
			String sql = "SELECT BNO, BTITLE, BWRITER, BDATE FROM BOARD ORDER BY BDATE DESC";

			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			System.out.println("번호\t 제목\t 이름\t 작성일\t");
			while (resultSet.next()) {
				// ResultSet의 다음 행으로 이동하며 각 컬럼 데이터 출력
				System.out.print(resultSet.getInt("bno") + "\t");
				System.out.print(resultSet.getString("btitle") + "\t");
				System.out.print(resultSet.getString("bwriter") + "\t");
				System.out.println(resultSet.getDate("bdate") + "\t");
			}
			System.out.println("==============끝=====================");
		} catch (SQLException e) {
			System.out.println("selectAll 메서드와 쿼리문을 확인하세요.");
			e.printStackTrace();
		} finally {
			resultSet.close();
			statement.close();
			
		}

	}

	public void readOne(String title) throws SQLException {
		// 게시글 조회 메서드
		try {
			String sql = "SELECT BNO, BTITLE, BCONTENT, BWRITER, BDATE FROM BOARD WHERE BTITLE =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, title);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				// 검색 결과가 있으면 DTO 객체에 저장 후 출력
				BoardDTO boardDTO = new BoardDTO();

				boardDTO.setBno(resultSet.getInt("bno"));
				boardDTO.setBtitle(resultSet.getString("btitle"));
				boardDTO.setBcontent(resultSet.getString("bcontent"));
				boardDTO.setBwriter(resultSet.getString("bwriter"));
				boardDTO.setBdate(resultSet.getDate("bdate"));
				// 데이터 베이스에 있는 행을 객체에 넣기 완료

				System.out.println("=========================");
				System.out.println("번호 : " + boardDTO.getBno());
				System.out.println("제목 : " + boardDTO.getBtitle());
				System.out.println("내용 : " + boardDTO.getBcontent());
				System.out.println("작성자 : " + boardDTO.getBwriter());
				System.out.println("작성일 : " + boardDTO.getBdate());
			} else {
				System.out.println("해당하는 게시물이 없습니다. ");
			}

		} catch (SQLException e) {
			System.out.println("예외발생 : readOne 메서드와 쿼리문을 확인하세요");
			e.printStackTrace();
		} finally {
			resultSet.close();
			preparedStatement.close();
			
		}

	}

	public BoardDTO readOneReturn(String title) {
		try {
			String sql = "SELECT bno, btitle, bcontent, bwriter, bdate FROM board WHERE btitle = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, title);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				boardDTO = new BoardDTO();
				boardDTO.setBno(resultSet.getInt("bno"));
				boardDTO.setBtitle(resultSet.getString("btitle"));
				boardDTO.setBcontent(resultSet.getString("bcontent"));
				boardDTO.setBwriter(resultSet.getString("bwriter"));
				boardDTO.setBdate(resultSet.getDate("bdate"));
			}
		} catch (SQLException e) {
			System.out.println("readOneReturn메서드와 쿼리문을 확인해주세요.");
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return boardDTO;

	}

	public void modify(Scanner inputStr, String title) throws SQLException {
		// 제목을 찾아서 수정하기.
		BoardDTO boardDTO = new BoardDTO();
		Scanner inputLine = new Scanner(System.in);

		System.out.print("수정할 게시글의 제목을 입력해주세요! : ");
		boardDTO.setBtitle(inputStr.next());

		System.out.println("내용을 수정해주세요!");
		boardDTO.setBcontent(inputLine.nextLine());
		try {
			// 제목을 기준으로 제목과 내용 수정, 작성일 sysdate로 갱신하는 쿼리
			String sql = "update board set btitle=? , bcontent =? , bdate=sysdate where btitle=? ";

			preparedStatement = connection.prepareStatement(sql);

			// ?에 각각 새 제목, 새 내용, 수정할 기존 제목 순서로 바인딩
			preparedStatement.setString(1, boardDTO.getBtitle());
			preparedStatement.setString(2, boardDTO.getBcontent());
			preparedStatement.setString(3, title);

			result = preparedStatement.executeUpdate(); // 쿼리문 실행후 결과를 정수로 보냄

			if (result > 0) {
				System.out.println(result + "개의 데이터가 수정 되었습니다. ");
				connection.commit(); // 영구 저장
			} else {
				System.out.println("수정이 되지 않았습니다.");
				connection.rollback();
			}

		} catch (SQLException e) {
			System.out.println("예외발생 : modify메서드와 쿼리문을 확인해주세요.");
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			
		}

	}

	public BoardDTO getPostByNo(int bno) {
		// 게시글 번호로 단일 게시글 조회 후 dto 리턴
		try {
			String sql = "SELECT bno, btitle, bcontent, bwriter, bdate FROM board WHERE bno = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, bno);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				boardDTO = new BoardDTO();
				boardDTO.setBno(resultSet.getInt("bno"));
				boardDTO.setBtitle(resultSet.getString("btitle"));
				boardDTO.setBcontent(resultSet.getString("bcontent"));
				boardDTO.setBwriter(resultSet.getString("bwriter"));
				boardDTO.setBdate(resultSet.getDate("bdate"));
			}

		} catch (SQLException e) {
			System.out.println("getPostByNo메서드와 쿼리문을 확인하세요.");
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return boardDTO;
	}

	public void deleteOne(int selectBno) throws SQLException {
		// 게시물의 번호를 받아 삭제한다
		try {
			String sql = "delete from board where bno = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, selectBno);

			result = preparedStatement.executeUpdate();

			if (result > 0) {
				System.out.println(result + "게시물이 삭제 되었습니다.");
				connection.commit();
			} else {
				System.out.println("게시물이 삭제되지 않았습니다.");
				connection.rollback();
			}

			System.out.println("=======================");
			selectAll(); // 삭제후 전체 리스트 보기
		} catch (SQLException e) {
			System.out.println("예외발생 : deleteOne() 메서드와 sql문을 확인하세요!!!");
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connection.close();
		

		}
	}

}
