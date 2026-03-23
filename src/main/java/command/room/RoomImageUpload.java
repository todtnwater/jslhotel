package command.room;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import common.CommonUtil;
import dao.RoomImageDao;
import dto.RoomImageDto;

public class RoomImageUpload {

	public void execute(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("application/json; charset=utf-8");

		try {
			PrintWriter out = response.getWriter();

			// 임시 로컬 저장 경로
			String tempPath = CommonUtil.getRoomImageTempDir(request);
			File tempDir = new File(tempPath);
			if (!tempDir.exists()) {
				tempDir.mkdirs();
			}

			int maxSize = 10 * 1024 * 1024;

			MultipartRequest multi = new MultipartRequest(
				request, tempPath, maxSize, "utf-8", new DefaultFileRenamePolicy()
			);

			String roomNo = multi.getParameter("roomNo");
			File file = multi.getFile("roomImage");

			if (file != null && roomNo != null) {
				String fileName = file.getName();
				String filePath = "/room/" + fileName;

				System.out.println(">>> 파일 업로드 시작: " + fileName);
				
				// SFTP로 DB 서버에 업로드 (uploadRoomToSFTP 사용)
				boolean uploadSuccess = CommonUtil.uploadRoomToSFTP(file, fileName);

				if (uploadSuccess) {
					System.out.println(">>> SFTP 업로드 성공, DB 저장 시작");
					
					// DB에 경로 저장
					RoomImageDao dao = new RoomImageDao();
					String nextImgNo = dao.getNextImageNo();
					int nextOrder = dao.getNextDisplayOrder(roomNo);
					boolean isFirstImage = (nextOrder == 1);

					RoomImageDto dto = new RoomImageDto();
					dto.setRi_img_no(nextImgNo);
					dto.setRi_room_no(roomNo);
					dto.setRi_img_path(filePath);
					dto.setRi_img_name(fileName);
					dto.setRi_img_type(fileName.substring(fileName.lastIndexOf(".") + 1));
					dto.setRi_img_size((int) file.length());
					dto.setRi_is_main(isFirstImage ? "Y" : "N");
					dto.setRi_display_order(nextOrder);

					int result = dao.insertRoomImage(dto);

					// 임시 파일 삭제
					file.delete();

					if (result > 0) {
						System.out.println(">>> DB 저장 성공");
						out.print("{\"success\": true, \"path\": \"" + filePath + "\"}");
					} else {
						System.out.println(">>> DB 저장 실패");
						// DB 저장 실패시 SFTP에서도 삭제
						CommonUtil.deleteRoomFromSFTP(fileName);
						out.print("{\"success\": false, \"message\": \"DB 저장 실패\"}");
					}
				} else {
					file.delete();
					System.out.println(">>> SFTP 업로드 실패");
					out.print("{\"success\": false, \"message\": \"SFTP 업로드 실패\"}");
				}
			} else {
				System.out.println(">>> 파일 또는 객실번호 없음");
				out.print("{\"success\": false, \"message\": \"파일 또는 객실번호 없음\"}");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(">>> 예외 발생: " + e.getMessage());
			try {
				PrintWriter out = response.getWriter();
				out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}