package dto;

public class QnaDto {
	
	private String q_no, q_title, q_content, q_type, q_member, q_reg_writer, q_password, q_reg_date, q_update_date;
	private String a_content, a_id, a_position, a_reg_date, a_update_date;
	
	// QnA 문의
	public QnaDto(String q_no, String q_title, String q_content, String q_type, String q_member, String q_reg_writer,
			String q_password, String q_reg_date) {
		this.q_no = q_no;
		this.q_title = q_title;
		this.q_content = q_content;
		this.q_type = q_type;
		this.q_member = q_member;
		this.q_reg_writer = q_reg_writer;
		this.q_password = q_password;
		this.q_reg_date = q_reg_date;
	}

	// Qna 리스트
	public QnaDto(String q_no, String q_title, String q_content, String q_type, String q_member, String q_reg_writer,
			String q_password, String q_reg_date, String a_content) {
		this.q_no = q_no;
		this.q_title = q_title;
		this.q_content = q_content;
		this.q_type = q_type;
		this.q_member = q_member;
		this.q_reg_writer = q_reg_writer;
		this.q_password = q_password;
		this.q_reg_date = q_reg_date;
		this.a_content = a_content;
	}

	// Qna 상세보기
	public QnaDto(String q_no, String q_title, String q_content, String q_type, String q_member, String q_reg_writer,
			String q_password, String q_reg_date, String q_update_date, String a_content, String a_id,
			String a_position, String a_reg_date, String a_update_date) {
		this.q_no = q_no;
		this.q_title = q_title;
		this.q_content = q_content;
		this.q_type = q_type;
		this.q_member = q_member;
		this.q_reg_writer = q_reg_writer;
		this.q_password = q_password;
		this.q_reg_date = q_reg_date;
		this.q_update_date = q_update_date;
		this.a_content = a_content;
		this.a_id = a_id;
		this.a_position = a_position;
		this.a_reg_date = a_reg_date;
		this.a_update_date = a_update_date;
	}

	public String getQ_no() {
		return q_no;
	}

	public String getQ_title() {
		return q_title;
	}

	public String getQ_content() {
		return q_content;
	}

	public String getQ_type() {
		return q_type;
	}

	public String getQ_member() {
		return q_member;
	}

	public String getQ_reg_writer() {
		return q_reg_writer;
	}

	public String getQ_password() {
		return q_password;
	}

	public String getQ_reg_date() {
		return q_reg_date;
	}

	public String getQ_update_date() {
		return q_update_date;
	}

	public String getA_content() {
		return a_content;
	}

	public String getA_id() {
		return a_id;
	}

	public String getA_position() {
		return a_position;
	}

	public String getA_reg_date() {
		return a_reg_date;
	}

	public String getA_update_date() {
		return a_update_date;
	}
	
	
	
	
}
