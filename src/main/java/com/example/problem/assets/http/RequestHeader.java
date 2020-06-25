package com.example.problem.assets.http;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import lombok.NoArgsConstructor;

public class RequestHeader {

	private final Long xUserId;

	private final String xRoomId;

	public RequestHeader(Long xUserId, String xRoomId) {
		this.xUserId = xUserId;
		this.xRoomId = xRoomId;
	}

	public Long getXUserId() {
		return xUserId;
	}

	public String getXRoomId() {
		return xRoomId;
	}

	public static Builder builder(){
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder{

		private HttpServletRequest request;

		public Builder request(HttpServletRequest request){
			this.request = request;
			return this;
		}

		public RequestHeader build(){
			HashMap<String, String> headerMap = this.getHeaderMap(request);

			return new RequestHeader(
				Long.valueOf(headerMap.get(ConstHeader.X_USER_ID.getKey())),
				headerMap.get(ConstHeader.X_ROOM_ID.getKey())
			);
		}

		private HashMap<String, String> getHeaderMap(HttpServletRequest request) {

			Enumeration<String> headerNames = request.getHeaderNames();
			HashMap<String, String> header = new HashMap<>();
			while (headerNames.hasMoreElements()) {
				String key = headerNames.nextElement();
				String value = request.getHeader(key);
				header.put(key.toLowerCase(), value);
			}
			return header;
		}

		private enum ConstHeader {

			X_ROOM_ID("X-ROOM-ID"),

			X_USER_ID("X-USER-ID");

			private final String key;

			ConstHeader(String key) {
				this.key = key;
			}

			public String getKey() {
				return key.toLowerCase();
			}

		}
	}
}
