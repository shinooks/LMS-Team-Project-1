package com.sesac.backend.board.constant;

public class BoardConstants {


    // 공통으로 사용되는 상수들
    public static class Common {

        // API 관련
        public static final String HEADER_USER_ID = "X-USER-ID";
        public static final String PARAM_FILE = "file";
        public static final String HEADER_ATTACHMENT_FORMAT = "attachment; filename=\"%s\"";


        // 에러 메시지
        public static final String ERROR_USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";
        public static final String ERROR_NO_PERMISSION = "권한이 없습니다.";
        public static final String ERROR_INTERNAL_SERVER = "내부 서버 오류";
        public static final String ERROR_INVALID_REQUEST = "잘못된 요청 데이터";

        // 로그 메시지
        public static final String LOG_ENTITY_NOT_FOUND = "Entity not found: {}";
        public static final String LOG_INVALID_FILE = "Invalid file: {}";

        // Swagger 파라미터 설명
        public static final String SWAGGER_PARAM_USER_ID = "사용자 ID";

    }


    public static class Board {
        // API 경로
        public static final String API_BOARD_PATH = "/boards";

        // Swagger 관련
        public static final String SWAGGER_TAG_NAME = "Board";
        public static final String SWAGGER_TAG_DESCRIPTION = "게시판 관리 API";

        // API 작업 설명
        public static final String API_OPERATION_CREATE_SUMMARY = "게시판 생성";
        public static final String API_OPERATION_CREATE_DESCRIPTION = "새로운 게시판을 생성합니다.";
        public static final String API_OPERATION_UPDATE_SUMMARY = "게시판 수정";
        public static final String API_OPERATION_UPDATE_DESCRIPTION = "특정 ID를 가진 게시판을 수정합니다.";
        public static final String API_OPERATION_DELETE_SUMMARY = "게시판 삭제";
        public static final String API_OPERATION_DELETE_DESCRIPTION = "특정 ID를 가진 게시판을 삭제합니다.";
        public static final String API_OPERATION_LIST_SUMMARY = "게시판 목록 조회";
        public static final String API_OPERATION_LIST_DESCRIPTION = "모든 게시판의 목록을 조회합니다.";
        public static final String API_OPERATION_GET_SUMMARY = "게시판 상세 조회";
        public static final String API_OPERATION_GET_DESCRIPTION = "특정 ID를 가진 게시판의 상세 정보를 조회합니다.";

        // 성공 메시지
        public static final String SUCCESS_CREATE = "게시판이 성공적으로 생성되었습니다.";
        public static final String SUCCESS_UPDATE = "게시판이 성공적으로 수정되었습니다.";
        public static final String SUCCESS_DELETE = "게시판이 성공적으로 삭제되었습니다.";
        public static final String SUCCESS_LIST = "게시판 목록을 성공적으로 조회했습니다.";
        public static final String SUCCESS_GET = "게시판을 성공적으로 조회했습니다.";

        // 에러 메시지
        public static final String ERROR_NOT_FOUND = "게시판을 찾을 수 없습니다.";
        public static final String ERROR_NO_PERMISSION_CREATE = "게시판 생성 권한이 없습니다. 교직원만 게시판을 생성할 수 있습니다.";
        public static final String ERROR_NO_PERMISSION_UPDATE = "게시판 수정 권한이 없습니다. 교직원만 게시판을 수정할 수 있습니다.";
        public static final String ERROR_NO_PERMISSION_DELETE = "게시판 삭제 권한이 없습니다. 교직원만 게시판을 삭제할 수 있습니다.";
        public static final String ERROR_DUPLICATE_NAME = "이미 존재하는 게시판 이름입니다.";
        public static final String ERROR_DELETE_NOT_ALLOWED = "삭제가 허용되지 않은 게시판입니다.";

        // 로그 메시지
        public static final String LOG_CREATE = "Creating board: {}";
        public static final String LOG_UPDATE = "Updating board: {}";
        public static final String LOG_DELETE = "Deleting board with id: {}";
        public static final String LOG_FETCH = "Fetching board with id: {}";
        public static final String LOG_FETCH_ALL = "Fetching all boards";
        public static final String LOG_ERROR_CREATE = "Error creating board";
        public static final String LOG_ERROR_UPDATE = "Error updating board";
        public static final String LOG_ERROR_DELETE = "Error deleting board";
        public static final String LOG_ERROR_GET = "Error fetching board";
        public static final String LOG_ERROR_FETCH = "Error fetching boards";
        public static final String LOG_ERROR_PERMISSION = "Permission denied for deleting board";
    }

    public static class Post {
        // API 경로
        public static final String API_POST_PATH = "/boards/{boardId}/posts";

        // Swagger 관련
        public static final String SWAGGER_TAG_NAME = "Post";
        public static final String SWAGGER_TAG_DESCRIPTION = "게시글 관리 API";
        public static final String SWAGGER_PARAM_POST_ID = "게시글 ID";
        public static final String SWAGGER_PARAM_BOARD_ID = "게시판 ID";

        // API 작업 설명
        public static final String API_OPERATION_CREATE_SUMMARY = "게시글 작성";
        public static final String API_OPERATION_CREATE_DESCRIPTION = "새로운 게시글을 작성합니다.";
        public static final String API_OPERATION_UPDATE_SUMMARY = "게시글 수정";
        public static final String API_OPERATION_UPDATE_DESCRIPTION = "특정 게시글을 수정합니다.";
        public static final String API_OPERATION_DELETE_SUMMARY = "게시글 삭제";
        public static final String API_OPERATION_DELETE_DESCRIPTION = "특정 게시글을 삭제합니다.";
        public static final String API_OPERATION_GET_SUMMARY = "게시글 조회";
        public static final String API_OPERATION_GET_DESCRIPTION = "특정 게시글을 조회합니다.";
        public static final String API_OPERATION_SEARCH_SUMMARY = "게시글 검색";
        public static final String API_OPERATION_SEARCH_DESCRIPTION = "게시글을 검색합니다.";

        // 성공 메시지
        public static final String SUCCESS_CREATE = "게시글이 성공적으로 작성되었습니다.";
        public static final String SUCCESS_UPDATE = "게시글이 성공적으로 수정되었습니다.";
        public static final String SUCCESS_DELETE = "게시글이 성공적으로 삭제되었습니다.";
        public static final String SUCCESS_GET = "게시글을 성공적으로 조회했습니다.";

        // 에러 메시지
        public static final String ERROR_NOT_FOUND = "게시글을 찾을 수 없습니다.";
        public static final String ERROR_BOARD_NOT_FOUND = "게시판을 찾을 수 없습니다.";
        public static final String ERROR_NO_PERMISSION_MODIFY = "게시글을 수정할 권한이 없습니다.";
        public static final String ERROR_NO_PERMISSION_DELETE = "게시글을 삭제할 권한이 없습니다.";

        // 로그 메시지
        public static final String LOG_CREATE = "Creating post: {}";
        public static final String LOG_UPDATE = "Updating post: {}";
        public static final String LOG_DELETE = "Deleting post: {}";
        public static final String LOG_GET = "Fetching post: {}";
        public static final String LOG_SEARCH = "Searching posts with criteria: {}";
        public static final String LOG_ERROR_CREATE = "Error creating post";
        public static final String LOG_ERROR_UPDATE = "Error updating post";
        public static final String LOG_ERROR_DELETE = "Error deleting post";
        public static final String LOG_ERROR_GET = "Error fetching post";
        public static final String LOG_ERROR_SEARCH = "Error searching posts";
    }

    // PostFile 관련 상수들
    public static class PostFile {
        // API 경로
        public static final String API_POST_FILES = "/boards/{boardId}/posts/{postId}/files";

        // Swagger 관련
        public static final String SWAGGER_TAG_NAME = "PostFile";
        public static final String SWAGGER_TAG_DESCRIPTION = "게시글 첨부파일 관리 API";
        public static final String SWAGGER_PARAM_FILE_DESC = "업로드할 파일";
        public static final String SWAGGER_PARAM_FILE_ID = "파일 ID";
        public static final String SWAGGER_PARAM_BOARD_ID = "게시판 ID";
        public static final String SWAGGER_PARAM_POST_ID = "게시글 ID";

        // API 설명
        public static final String API_OPERATION_UPLOAD = "특정 게시글에 새로운 파일을 업로드합니다.";
        public static final String API_OPERATION_DOWNLOAD = "특정 파일을 다운로드합니다.";
        public static final String API_OPERATION_LIST = "특정 게시글에 대한 모든 첨부파일 목록을 조회합니다.";
        public static final String API_OPERATION_DELETE = "특정 파일을 삭제합니다.";

        // 성공 메시지
        public static final String SUCCESS_UPLOAD = "파일이 성공적으로 업로드되었습니다.";
        public static final String SUCCESS_DOWNLOAD = "파일이 성공적으로 다운로드되었습니다.";
        public static final String SUCCESS_LIST = "첨부파일 목록을 성공적으로 조회했습니다.";
        public static final String SUCCESS_FILE_DELETE = "파일이 성공적으로 삭제되었습니다.";

        // 파일 크기 및 설정 관련
        public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
        public static final int DELETE_AFTER_DAYS = 1;

        // 에러 메시지
        public static final String ERROR_FILE_NOT_FOUND = "파일을 찾을 수 없습니다.";
        public static final String ERROR_POST_NOT_FOUND = "게시글을 찾을 수 없습니다.";
        public static final String ERROR_FILE_SIZE = "파일 크기가 10MB를 초과할 수 없습니다";
        public static final String ERROR_FILE_TYPE = "지원하지 않는 파일 형식입니다";
        public static final String ERROR_FILE_DELETED = "삭제된 파일은 다운로드할 수 없습니다.";
        public static final String ERROR_ALREADY_DELETED = "이미 삭제된 파일입니다.";
        public static final String ERROR_FILE_UPLOAD = "파일 업로드에 실패했습니다";
        public static final String ERROR_FILE_ID_REQUIRED = "파일 ID는 필수입니다.";

        // 로그 메시지
        public static final String LOG_FILE_UPLOAD_ERROR = "파일 업로드 중 오류 발생: {}";
        public static final String LOG_FILE_DELETE_COMPLETE = "파일 영구 삭제 완료: {}";
        public static final String LOG_FILE_DELETE_FAIL = "파일 영구 삭제 실패: {}";
        public static final String LOG_FILE_NOT_FOUND = "파일을 찾을 수 없습니다: {}";
        public static final String LOG_ERROR_DOWNLOADING = "파일 다운로드 중 오류 발생: {}";
        public static final String LOG_ERROR_FETCHING = "파일 목록 조회 중 오류 발생: {}";
        public static final String LOG_ERROR_DELETING = "파일 삭제 중 오류 발생: {}";

        // 캐시 키
        public static final String CACHE_FILE_DOWNLOAD = "file:download";
        public static final String CACHE_FILE_INFO = "file:info";
        public static final String CACHE_FILE_LIST = "file:list";

        // 스케줄러
        public static final String SCHEDULER_DELETE_CRON = "0 * * * * *";
        public static final int DELETE_AFTER_MINUTES = 1; // 1분 후 삭제

        // 허용된 파일 타입
        public static final String[] ALLOWED_FILE_TYPES = {
                "image/",
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        };
    }


    // Comment 관련 상수들
    public static class Comment {
        // API 경로
        public static final String API_COMMENT_PATH = "/boards/{boardId}/posts/{postId}/comments";

        // Swagger 관련
        public static final String SWAGGER_TAG_NAME = "Comment";
        public static final String SWAGGER_TAG_DESCRIPTION = "댓글 관리 API";

        // API 작업 설명
        public static final String API_OPERATION_CREATE = "특정 게시글에 대한 새로운 댓글을 작성합니다.";
        public static final String API_OPERATION_UPDATE = "특정 ID를 가진 댓글을 수정합니다.";
        public static final String API_OPERATION_DELETE = "특정 ID를 가진 댓글을 삭제합니다.";
        public static final String API_OPERATION_LIST = "특정 게시글에 대한 모든 댓글을 조회합니다.";

        // 성공 메시지
        public static final String SUCCESS_CREATE = "댓글이 성공적으로 작성되었습니다.";
        public static final String SUCCESS_UPDATE = "댓글이 성공적으로 수정되었습니다.";
        public static final String SUCCESS_DELETE = "댓글이 성공적으로 삭제되었습니다.";
        public static final String SUCCESS_LIST = "댓글 목록을 성공적으로 조회했습니다.";

        // 에러 메시지
        public static final String ERROR_NOT_FOUND = "댓글을 찾을 수 없습니다.";
        public static final String ERROR_NO_PERMISSION = "권한이 없어서 삭제할 수 없습니다.";
        public static final String ERROR_POST_NOT_FOUND = "게시글을 찾을 수 없습니다.";
        public static final String ERROR_USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";

        // 로그 메시지
        public static final String LOG_CREATE = "게시글 {}에 댓글 작성 중: {}";
        public static final String LOG_UPDATE = "댓글 {} 수정 중: {}";
        public static final String LOG_DELETE = "댓글 삭제 중: {}";
        public static final String LOG_FETCH = "게시글 {}의 댓글 목록 조회 중";
        public static final String LOG_ERROR_CREATE = "댓글 작성 중 오류 발생";
        public static final String LOG_ERROR_UPDATE = "댓글 수정 중 오류 발생";
        public static final String LOG_ERROR_DELETE = "댓글 삭제 중 오류 발생";
        public static final String LOG_ERROR_FETCH = "댓글 목록 조회 중 오류 발생";
        public static final String LOG_BEFORE_UPDATE = "수정 전 익명 상태: {}";
        public static final String LOG_AFTER_UPDATE = "수정 후 익명 상태: {}";

        // 기타 상수
        public static final String ANONYMOUS_USER = "익명";
    }

    public static class PostLike {
        // API 경로
        public static final String API_POST_LIKES = "/boards/{boardId}/posts/{postId}/likes";

        // Swagger 관련
        public static final String SWAGGER_TAG_NAME = "PostLike";
        public static final String SWAGGER_TAG_DESCRIPTION = "게시글 좋아요 관리 API";

        // API 작업 설명
        public static final String API_OPERATION_TOGGLE_SUMMARY = "좋아요 토글";
        public static final String API_OPERATION_TOGGLE_DESCRIPTION = "특정 게시글에 좋아요를 추가하거나 취소합니다.";
        public static final String API_OPERATION_COUNT_SUMMARY = "게시글의 좋아요 수 조회";
        public static final String API_OPERATION_COUNT_DESCRIPTION = "특정 게시글의 좋아요 수를 조회합니다.";
        public static final String API_OPERATION_CHECK_SUMMARY = "사용자의 좋아요 여부 확인";
        public static final String API_OPERATION_CHECK_DESCRIPTION = "특정 사용자가 특정 게시글에 이미 좋아요를 눌렀는지 확인합니다.";

        // 성공 메시지
        public static final String SUCCESS_TOGGLE = "좋아요가 성공적으로 토글되었습니다.";
        public static final String SUCCESS_LIKE = "좋아요가 추가되었습니다.";
        public static final String SUCCESS_UNLIKE = "좋아요가 취소되었습니다.";
        public static final String SUCCESS_COUNT = "좋아요 수를 성공적으로 조회했습니다.";
        public static final String SUCCESS_CHECK = "좋아요 여부를 성공적으로 조회했습니다.";

        // 에러 메시지
        public static final String ERROR_POST_NOT_FOUND = "게시글을 찾을 수 없습니다.";
        public static final String ERROR_TOGGLE = "좋아요 처리 중 오류가 발생했습니다.";

        // 로그 메시지
        public static final String LOG_TOGGLE = "Toggling like for post {} by user {}";
        public static final String LOG_COUNT = "Counting likes for post: {}";
        public static final String LOG_ERROR_TOGGLE = "Error toggling like";
        public static final String LOG_ERROR_COUNT = "Error getting like count";
        public static final String LOG_ERROR_CHECK = "Error checking like status";
        public static final String LOG_ERROR_PERMISSION = "Permission denied for toggling like: {}";
    }
}