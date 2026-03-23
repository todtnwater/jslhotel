package dto;

public class NoticeDto {
	private String n_no, n_title, n_content, n_attach, n_reg_id, n_position, n_reg_date, n_type;
	private int n_hit;
	private String popupStartDate; 
    private String popupEndDate;
	
	// 공지 목록
	public NoticeDto(String n_no, String n_title, String n_content, String n_position, String n_reg_date, String n_type,
			int n_hit) {
		super();
		this.n_no = n_no;
		this.n_title = n_title;
		this.n_content = n_content;
		this.n_position = n_position;
		this.n_reg_date = n_reg_date;
		this.n_type = n_type;
		this.n_hit = n_hit;
	}	

	// 공지 등록
	public NoticeDto(String n_no, String n_title, String n_content, String n_attach, String n_reg_id, String n_position,
			String n_reg_date, String n_type, String popupStartDate, String popupEndDate) {
		super();
		this.n_no = n_no;
		this.n_title = n_title;
		this.n_content = n_content;
		this.n_attach = n_attach;
		this.n_reg_id = n_reg_id;
		this.n_position = n_position;
		this.n_reg_date = n_reg_date;
		this.n_type = n_type;
		this.popupStartDate = popupStartDate;
		this.popupEndDate = popupEndDate;
	}

	// 공지 상세 조회
	public NoticeDto(String n_no, String n_title, String n_attach, String n_content, String n_position,
			String n_reg_date, String n_type, int n_hit, String popupStartDate, String popupEndDate) {
		super();
		this.n_no = n_no;
		this.n_title = n_title;
		this.n_attach = n_attach;
		this.n_content = n_content;
		this.n_position = n_position;
		this.n_reg_date = n_reg_date;
		this.n_type = n_type;
		this.n_hit = n_hit;
		this.popupStartDate = popupStartDate; 
	    this.popupEndDate = popupEndDate;
	}		

	// 공지 수정
	public NoticeDto(String n_no, String n_title, String n_content, String n_attach, String n_type, 
	                 String popupStartDate, String popupEndDate, String n_reg_date) {
	    super();
	    this.n_no = n_no;
	    this.n_title = n_title;
	    this.n_content = n_content;
	    this.n_attach = n_attach;
	    this.n_type = n_type;
	    this.popupStartDate = popupStartDate; 
	    this.popupEndDate = popupEndDate; 
	    this.n_reg_date = n_reg_date;
	}

	public String getN_no() {
		return n_no;
	}

	public String getN_title() {
		return n_title;
	}

	public String getN_content() {
		return n_content;
	}

	public String getN_reg_id() {
		return n_reg_id;
	}

	public String getN_position() {
		return n_position;
	}

	public String getN_reg_date() {
		return n_reg_date;
	}

	public String getN_type() {
		return n_type;
	}

	public int getN_hit() {
		return n_hit;
	}


	public String getN_attach() {
		return n_attach;
	}
	public String getPopupStartDate() {
        return popupStartDate;
    }

    public String getPopupEndDate() {
        return popupEndDate;
    }
}