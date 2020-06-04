/*
 * Copyright 2012 Fernando Ribeiro
 * 
 * This file is part of Cloud Server Pro Tools.
 *
 * Cloud Server Pro Tools is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Cloud Server Pro Tools is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Cloud Server Pro Tools. If not, see <http://www.gnu.org/licenses/>.
 */
package br.eti.fernandoribeiro.maven.cloudserverpro;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.maven.plugin.logging.Log;

import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

class SignatureClientFilter extends ClientFilter {
	private int contentLength;

	private Date date;

	private String login;

	private String secretKey;

	private Log log;

	public SignatureClientFilter(Log log, String login, int contentLength,
			Date date, String secretKey) {
		this.log = log;

		this.login = login;

		this.contentLength = contentLength;

		this.date = date;

		this.secretKey = secretKey;
	}

	@Override
	public ClientResponse handle(ClientRequest request) {
		ClientResponse result = null;

		try {
			log.info("Adding signature to request");

			Map<String, List<Object>> headers = request.getHeaders();

			List<Object> value = new ArrayList<Object>();

			DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

			String timestamp = formatter.format(date);

			Hex encoder = new Hex();

			MessageDigest digester = MessageDigest.getInstance("SHA1");

			value.add(login
					+ ":"
					+ timestamp
					+ ":"
					+ Base64.encodeBase64String(encoder.encode(digester
							.digest((login + contentLength + timestamp + secretKey)
									.getBytes()))));

			headers.put(HeaderNames.SIGNATURE, value);

			result = getNext().handle(request);
		} catch (NoSuchAlgorithmException e) {
			log.error("Can't handle request", e);
		}

		return result;
	}

}