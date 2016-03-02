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
package br.eti.fernandoribeiro.jenkins.cloudserverpro;

import java.io.PrintStream;
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

import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

final class SignatureClientFilter extends ClientFilter {
	private int contentLength;

	private Date date;

	private PrintStream logger;

	private String login;

	private String secretKey;

	public SignatureClientFilter(final PrintStream logger, final String login,
			final int contentLength, final Date date, final String secretKey) {
		this.logger = logger;

		this.login = login;

		this.contentLength = contentLength;

		this.date = date;

		this.secretKey = secretKey;
	}

	@Override
	public ClientResponse handle(final ClientRequest request) {
		ClientResponse result = null;

		try {
			logger.println("Adding signature to request");

			final Map<String, List<Object>> headers = request.getHeaders();

			final List<Object> value = new ArrayList<Object>();

			final DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

			final String timestamp = formatter.format(date);

			final Hex encoder = new Hex();

			final MessageDigest digester = MessageDigest.getInstance("SHA1");

			value.add(login
					+ ":"
					+ timestamp
					+ ":"
					+ Base64.encodeBase64String(encoder.encode(digester
							.digest((login + contentLength + timestamp + secretKey)
									.getBytes()))));

			headers.put(HeaderNames.SIGNATURE, value);

			result = getNext().handle(request);
		} catch (final NoSuchAlgorithmException e) {
			logger.println("Can't handle request");
		}

		return result;
	}

}