package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import common.DBConnection;
import dto.PointDto;

/**
 * 포인트 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * - 포인트 적립/사용/조회/취소
 * - 회원 포인트 잔액 관리
 */
public class PointDao {

    // ❌ 인스턴스 변수 제거 - 멀티스레드 환경에서 공유되어 커넥션 충돌 발생
    // Connection con, PreparedStatement ps, ResultSet rs → 각 메서드 내 로컬 변수로 선언

    /**
     * 커넥션 유효성 검사 후 반환
     * Oracle 미니PC idle timeout 대응
     */
    private Connection getValidConnection() throws Exception {
        Connection conn = DBConnection.getConnection();
        if (conn == null || conn.isClosed()) {
            System.out.println(" 커넥션 끊김 감지, 재연결 시도");
            conn = DBConnection.getConnection();
        }
        if (!conn.isValid(1)) {
            System.out.println(" 커넥션 유효하지 않음, 재연결 시도");
            conn = DBConnection.getConnection();
        }
        return conn;
    }

    /**
     * 포인트 내역 번호 자동 생성 (독립 커넥션 사용)
     * @return 새로운 포인트 번호 (PT00001 형식)
     */
    public String getPointNo() {
        String no = "";
        String sql = "SELECT NVL(MAX(PT_NO), 'PT00000') AS PT_NO " +
                    "FROM JSL_HOTEL_POINT_HISTORY";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                no = rs.getString("PT_NO");
                no = no.substring(2);
                int pointNo = Integer.parseInt(no) + 1;
                DecimalFormat df = new DecimalFormat("PT00000");
                no = df.format(pointNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getPointNo() 오류: " + sql);
        } finally {
            closeQuietly(rs, ps);
            closeConnection(conn);
        }
        return no;
    }

    /**
     * 포인트 내역 번호 자동 생성 (트랜잭션 내 같은 커넥션 사용)
     * @param conn 외부에서 전달받은 커넥션
     * @return 새로운 포인트 번호
     */
    private String getPointNo(Connection conn) {
        String no = "";
        String sql = "SELECT NVL(MAX(PT_NO), 'PT00000') AS PT_NO " +
                    "FROM JSL_HOTEL_POINT_HISTORY";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                no = rs.getString("PT_NO");
                no = no.substring(2);
                int pointNo = Integer.parseInt(no) + 1;
                DecimalFormat df = new DecimalFormat("PT00000");
                no = df.format(pointNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getPointNo(conn) 오류");
        } finally {
            closeQuietly(rs, ps);
            // conn은 닫지 않음 - 호출자가 관리
        }
        return no;
    }

    /**
     * 회원의 현재 포인트 조회 (독립 커넥션 사용)
     * @param memberId 회원 ID
     * @return 보유 포인트
     */
    public int getMemberPoint(String memberId) {
        int point = 0;
        String sql = "SELECT M_POINT FROM JSL_HOTEL_MEMBER WHERE M_ID = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, memberId);
            rs = ps.executeQuery();
            if (rs.next()) {
                point = rs.getInt("M_POINT");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getMemberPoint() 오류");
        } finally {
            closeQuietly(rs, ps);
            closeConnection(conn);
        }
        return point;
    }

    /**
     * 회원의 현재 포인트 조회 (트랜잭션 내 같은 커넥션 사용)
     * @param memberId 회원 ID
     * @param conn 외부에서 전달받은 커넥션
     * @return 보유 포인트
     */
    private int getMemberPoint(String memberId, Connection conn) {
        int point = 0;
        String sql = "SELECT M_POINT FROM JSL_HOTEL_MEMBER WHERE M_ID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, memberId);
            rs = ps.executeQuery();
            if (rs.next()) {
                point = rs.getInt("M_POINT");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getMemberPoint(conn) 오류");
        } finally {
            closeQuietly(rs, ps);
            // conn은 닫지 않음 - 호출자가 관리
        }
        return point;
    }

    /**
     * 포인트 적립
     * @param dto 포인트 DTO
     * @return 성공 시 1, 실패 시 0
     */
    public int earnPoint(PointDto dto) {
        int result = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            // 1. 현재 포인트 조회 (같은 커넥션 사용 - 트랜잭션 일관성 보장)
            int currentPoint = getMemberPoint(dto.getPt_member_id(), conn);
            int newBalance = currentPoint + dto.getPt_amount();

            // 2. 포인트 내역 번호 생성 (같은 커넥션 사용)
            String ptNo = getPointNo(conn);

            // 3. 포인트 내역 저장
            String insertSql = "INSERT INTO JSL_HOTEL_POINT_HISTORY " +
                              "(PT_NO, PT_MEMBER_ID, PT_TYPE, PT_AMOUNT, PT_BALANCE, " +
                              " PT_SOURCE, PT_SOURCE_NO, PT_DESCRIPTION, PT_REG_DATE, " +
                              " PT_EXPIRE_DATE, PT_STATUS) " +
                              "VALUES " +
                              "(?, ?, 'EARN', ?, ?, ?, ?, ?, SYSDATE, " +
                              " ADD_MONTHS(SYSDATE, 12), 'ACTIVE')";

            ps = conn.prepareStatement(insertSql);
            ps.setString(1, ptNo);
            ps.setString(2, dto.getPt_member_id());
            ps.setInt(3, dto.getPt_amount());
            ps.setInt(4, newBalance);
            ps.setString(5, dto.getPt_source());
            ps.setString(6, dto.getPt_source_no());
            ps.setString(7, dto.getPt_description());

            int historyResult = ps.executeUpdate();
            ps.close();
            ps = null;

            // 4. 회원 포인트 업데이트
            String updateSql = "UPDATE JSL_HOTEL_MEMBER " +
                              "SET M_POINT = M_POINT + ?, " +
                              "    M_TOTAL_POINT = M_TOTAL_POINT + ? " +
                              "WHERE M_ID = ?";

            ps = conn.prepareStatement(updateSql);
            ps.setInt(1, dto.getPt_amount());
            ps.setInt(2, dto.getPt_amount());
            ps.setString(3, dto.getPt_member_id());

            int memberResult = ps.executeUpdate();

            // 5. 둘 다 성공하면 커밋
            if (historyResult > 0 && memberResult > 0) {
                conn.commit();
                result = 1;
                System.out.println(" 포인트 적립 완료 - " + dto.getPt_member_id() +
                                 ": +" + dto.getPt_amount() + "P (잔액: " + newBalance + "P)");
            } else {
                conn.rollback();
                System.out.println(" 포인트 적립 실패");
            }

        } catch (Exception e) {
            System.out.println("earnPoint() 오류: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.rollback();
                }
            } catch (Exception e2) {
                System.out.println(" rollback 불가 (커넥션 이미 종료): " + e2.getMessage());
            }
        } finally {
            closeQuietly(null, ps);
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.setAutoCommit(true);
                }
            } catch (Exception e) {
                System.out.println(" setAutoCommit 실패: " + e.getMessage());
            }
            closeConnection(conn);
        }

        return result;
    }

    /**
     * 포인트 사용
     * @param dto 포인트 DTO
     * @return 성공 시 1, 실패 시 0
     */
    public int usePoint(PointDto dto) {
        int result = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            // 1. 현재 포인트 조회
            int currentPoint = getMemberPoint(dto.getPt_member_id(), conn);

            // 2. 포인트 부족 체크
            if (currentPoint < dto.getPt_amount()) {
                System.out.println("포인트 부족 - 보유: " + currentPoint +
                                 "P, 사용: " + dto.getPt_amount() + "P");
                conn.rollback();
                return 0;
            }

            int newBalance = currentPoint - dto.getPt_amount();

            // 3. 포인트 내역 번호 생성
            String ptNo = getPointNo(conn);

            // 4. 포인트 내역 저장
            String insertSql = "INSERT INTO JSL_HOTEL_POINT_HISTORY " +
                              "(PT_NO, PT_MEMBER_ID, PT_TYPE, PT_AMOUNT, PT_BALANCE, " +
                              " PT_SOURCE, PT_SOURCE_NO, PT_DESCRIPTION, PT_REG_DATE, PT_STATUS) " +
                              "VALUES " +
                              "(?, ?, 'USE', ?, ?, ?, ?, ?, SYSDATE, 'ACTIVE')";

            ps = conn.prepareStatement(insertSql);
            ps.setString(1, ptNo);
            ps.setString(2, dto.getPt_member_id());
            ps.setInt(3, dto.getPt_amount());
            ps.setInt(4, newBalance);
            ps.setString(5, dto.getPt_source());
            ps.setString(6, dto.getPt_source_no());
            ps.setString(7, dto.getPt_description());

            int historyResult = ps.executeUpdate();
            ps.close();
            ps = null;

            // 5. 회원 포인트 업데이트
            String updateSql = "UPDATE JSL_HOTEL_MEMBER " +
                              "SET M_POINT = M_POINT - ? " +
                              "WHERE M_ID = ?";

            ps = conn.prepareStatement(updateSql);
            ps.setInt(1, dto.getPt_amount());
            ps.setString(2, dto.getPt_member_id());

            int memberResult = ps.executeUpdate();

            // 6. 둘 다 성공하면 커밋
            if (historyResult > 0 && memberResult > 0) {
                conn.commit();
                result = 1;
                System.out.println("포인트 사용 완료 - " + dto.getPt_member_id() +
                                 ": -" + dto.getPt_amount() + "P (잔액: " + newBalance + "P)");
            } else {
                conn.rollback();
                System.out.println("포인트 사용 실패");
            }

        } catch (Exception e) {
            System.out.println("usePoint() 오류: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.rollback();
                }
            } catch (Exception e2) {
                System.out.println("rollback 불가 (커넥션 이미 종료): " + e2.getMessage());
            }
        } finally {
            closeQuietly(null, ps);
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.setAutoCommit(true);
                }
            } catch (Exception e) {
                System.out.println("setAutoCommit 실패: " + e.getMessage());
            }
            closeConnection(conn);
        }

        return result;
    }

    /**
     * 포인트 취소 (환불 시)
     * @param sourceNo 출처 번호 (예약번호 등)
     * @return 성공 시 1, 실패 시 0
     */
    public int cancelPoint(String sourceNo) {
        int result = 0;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            conn.setAutoCommit(false);

            // 1. 해당 출처의 포인트 내역 조회
            String selectSql = "SELECT PT_MEMBER_ID, PT_AMOUNT " +
                              "FROM JSL_HOTEL_POINT_HISTORY " +
                              "WHERE PT_SOURCE_NO = ? AND PT_STATUS = 'ACTIVE'";

            ps = conn.prepareStatement(selectSql);
            ps.setString(1, sourceNo);
            rs = ps.executeQuery();

            if (rs.next()) {
                String memberId = rs.getString("PT_MEMBER_ID");
                int amount = rs.getInt("PT_AMOUNT");

                rs.close();
                rs = null;
                ps.close();
                ps = null;

                // 2. 포인트 내역 취소 처리
                String updateHistorySql = "UPDATE JSL_HOTEL_POINT_HISTORY " +
                                         "SET PT_STATUS = 'CANCELED' " +
                                         "WHERE PT_SOURCE_NO = ?";

                ps = conn.prepareStatement(updateHistorySql);
                ps.setString(1, sourceNo);
                int historyResult = ps.executeUpdate();
                ps.close();
                ps = null;

                // 3. 회원 포인트 차감
                String updateMemberSql = "UPDATE JSL_HOTEL_MEMBER " +
                                        "SET M_POINT = M_POINT - ? " +
                                        "WHERE M_ID = ?";

                ps = conn.prepareStatement(updateMemberSql);
                ps.setInt(1, amount);
                ps.setString(2, memberId);
                int memberResult = ps.executeUpdate();

                // 4. 커밋
                if (historyResult > 0 && memberResult > 0) {
                    conn.commit();
                    result = 1;
                    System.out.println("✅ 포인트 취소 완료 - " + memberId + ": -" + amount + "P");
                } else {
                    conn.rollback();
                    System.out.println("❌ 포인트 취소 실패");
                }
            } else {
                System.out.println("⚠️ 취소할 포인트 내역 없음 - sourceNo: " + sourceNo);
                conn.rollback();
            }

        } catch (Exception e) {
            System.out.println("cancelPoint() 오류: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.rollback();
                }
            } catch (Exception e2) {
                System.out.println("⚠️ rollback 불가 (커넥션 이미 종료): " + e2.getMessage());
            }
        } finally {
            closeQuietly(rs, ps);
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.setAutoCommit(true);
                }
            } catch (Exception e) {
                System.out.println("⚠️ setAutoCommit 실패: " + e.getMessage());
            }
            closeConnection(conn);
        }

        return result;
    }

    /**
     * 회원의 포인트 내역 조회
     * @param memberId 회원 ID
     * @return 포인트 내역 리스트
     */
    public List<PointDto> getPointHistory(String memberId) {
        List<PointDto> list = new ArrayList<>();
        String sql = "SELECT * FROM JSL_HOTEL_POINT_HISTORY " +
                    "WHERE PT_MEMBER_ID = ? " +
                    "ORDER BY PT_REG_DATE DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, memberId);
            rs = ps.executeQuery();

            while (rs.next()) {
                PointDto dto = new PointDto(
                    rs.getString("PT_NO"),
                    rs.getString("PT_MEMBER_ID"),
                    rs.getString("PT_TYPE"),
                    rs.getInt("PT_AMOUNT"),
                    rs.getInt("PT_BALANCE"),
                    rs.getString("PT_SOURCE"),
                    rs.getString("PT_SOURCE_NO"),
                    rs.getString("PT_DESCRIPTION"),
                    rs.getString("PT_REG_DATE"),
                    rs.getString("PT_EXPIRE_DATE"),
                    rs.getString("PT_STATUS")
                );
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getPointHistory() 오류");
        } finally {
            closeQuietly(rs, ps);
            closeConnection(conn);
        }

        return list;
    }

    /**
     * 멤버십별 적립율 조회
     * @param membership 멤버십 등급
     * @return 객실 적립율 (%)
     */
    public double getRoomPointRate(String membership) {
        double rate = 0.0;
        String sql = "SELECT PR_ROOM_RATE FROM JSL_HOTEL_POINT_RATE " +
                    "WHERE PR_MEMBERSHIP = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, membership.toLowerCase());
            rs = ps.executeQuery();

            if (rs.next()) {
                rate = rs.getDouble("PR_ROOM_RATE");
            }
        } catch (Exception e) {
            // 테이블이 없으면 기본값 사용
            if (membership.equalsIgnoreCase("bronze"))       rate = 0.0;
            else if (membership.equalsIgnoreCase("silver"))  rate = 5.0;
            else if (membership.equalsIgnoreCase("gold"))    rate = 10.0;
            else if (membership.equalsIgnoreCase("premium")) rate = 15.0;
        } finally {
            closeQuietly(rs, ps);
            closeConnection(conn);
        }

        return rate;
    }

    // ===================== 내부 유틸 메서드 =====================

    /**
     * ResultSet, PreparedStatement 조용히 닫기
     */
    private void closeQuietly(ResultSet rs, PreparedStatement ps) {
        try { if (rs  != null) rs.close();  } catch (Exception e) { /* 무시 */ }
        try { if (ps  != null) ps.close();  } catch (Exception e) { /* 무시 */ }
    }

    /**
     * Connection 조용히 닫기
     */
    private void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("⚠️ 커넥션 종료 실패: " + e.getMessage());
        }
    }
}