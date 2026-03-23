package common;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    private static final String SFTP_HOST = "XXX.XXX.XXX.XXX";
    private static final int SFTP_PORT = 22;
    private static final String SFTP_USER = "XXXXXX";
    private static final String SFTP_PASS = "XXXXXX";
    private static final String SFTP_BASE_PATH = "/XXX/";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, java.io.IOException {
        
        // /image/room/xxx.jpg → room/xxx.jpg
        // /image/notice/xxx.jpg → notice/xxx.jpg
        // 모든 경로 지원
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        String fileName = pathInfo.substring(1); // 앞의 / 제거
        
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        
        try {
            // SFTP 연결
            JSch jsch = new JSch();
            session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASS);
            
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            
            // 파일 다운로드
            String remotePath = SFTP_BASE_PATH + fileName;
            inputStream = channelSftp.get(remotePath);
            
            // MIME 타입 설정
            String mimeType = getServletContext().getMimeType(fileName);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            
            // 캐시 설정 (성능 향상 - 1년)
            response.setHeader("Cache-Control", "public, max-age=31536000");
            response.setDateHeader("Expires", System.currentTimeMillis() + 31536000000L);
            
            // 파일 전송
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            System.out.println(">>> [ImageServlet] 이미지 서빙 성공: " + fileName);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>> [ImageServlet] 이미지 서빙 실패: " + fileName + " - " + e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "이미지를 찾을 수 없습니다.");
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                if (channelSftp != null) channelSftp.disconnect();
                if (channel != null) channel.disconnect();
                if (session != null) session.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}