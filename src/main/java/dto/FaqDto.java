package dto;

public class FaqDto {

	private String f_no, f_type, f_question, f_answer, f_reg_id, 
					f_position, f_reg_date, f_update_date;

	
	// 전체항목
	public FaqDto(String f_no, String f_type, String f_question, String f_answer, String f_reg_id, String f_position,
			String f_reg_date, String f_update_date) {
		this.f_no = f_no;
		this.f_type = f_type;
		this.f_question = f_question;
		this.f_answer = f_answer;
		this.f_reg_id = f_reg_id;
		this.f_position = f_position;
		this.f_reg_date = f_reg_date;
		this.f_update_date = f_update_date;
	}

	public String getF_no() {
		return f_no;
	}

	public String getF_type() {
		return f_type;
	}

	public String getF_question() {
		return f_question;
	}

	public String getF_answer() {
		return f_answer;
	}

	public String getF_reg_id() {
		return f_reg_id;
	}

	public String getF_position() {
		return f_position;
	}

	public String getF_reg_date() {
		return f_reg_date;
	}

	public String getF_update_date() {
		return f_update_date;
	}
	
	
	
	
	
}
