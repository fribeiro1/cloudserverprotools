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

import static java.lang.System.out;

import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.Images;
import br.eti.fernandoribeiro.schemas.cloudserverpro.Image;

import com.sun.jersey.api.client.Client;

/**
 * Lists the images
 */
@Mojo(name = "list-images")
public class ListImagesMojo extends AbstractMojo {
	/**
	 * A login for the Cloud Server Pro API
	 */
	@Parameter(required = true)
	private String apiLogin;

	/**
	 * The secret key for the specified login
	 */
	@Parameter(required = true)
	private String apiSecretKey;

	public void execute() {
		getLog().info("Calling the Cloud Server Pro API");

		Client client = Client.create();

		client.addFilter(new SignatureClientFilter(getLog(), apiLogin, 0,
				new Date(), apiSecretKey));

		Images resource = CloudLocawebComBr_Api.images(client);

		br.eti.fernandoribeiro.schemas.cloudserverpro.Images images = resource
				.getAsApplicationXml(br.eti.fernandoribeiro.schemas.cloudserverpro.Images.class);

		out.println("ID,Name,Description");

		for (Image image : images.getImageList())
			out.println(image.getId() + "," + image.getName() + ","
					+ image.getDescription());

	}

}