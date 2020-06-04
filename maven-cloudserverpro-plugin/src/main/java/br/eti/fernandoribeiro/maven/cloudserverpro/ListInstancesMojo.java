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
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.Instances;
import br.eti.fernandoribeiro.schemas.cloudserverpro.Instance;

import com.sun.jersey.api.client.Client;

/**
 * Lists the instances
 */
@Mojo(name = "list-instances")
public class ListInstancesMojo extends AbstractMojo {
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

		Instances resource = CloudLocawebComBr_Api.instances(client);

		br.eti.fernandoribeiro.schemas.cloudserverpro.Instances instances = resource
				.getAsApplicationXml(br.eti.fernandoribeiro.schemas.cloudserverpro.Instances.class);

		out.println("ID,Name,State");

		for (Instance instance : instances.getInstanceList())
			out.println(instance.getId() + "," + instance.getName() + ","
					+ instance.getState());

	}

}