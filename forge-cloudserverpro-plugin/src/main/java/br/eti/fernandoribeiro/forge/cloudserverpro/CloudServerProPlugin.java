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
package br.eti.fernandoribeiro.forge.cloudserverpro;

import java.util.Date;

import javax.inject.Inject;

import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.Plugin;

import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.Images;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.Instances;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.InstancesId;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.InstancesIdReboot;
import br.eti.fernandoribeiro.schemas.cloudserverpro.Image;
import br.eti.fernandoribeiro.schemas.cloudserverpro.Instance;

import com.sun.jersey.api.client.Client;

@Alias("cloudserverpro")
@Help("Manages instances in Cloud Server Pro")
public final class CloudServerProPlugin implements Plugin {
	@Inject
	private Shell shell;

	@Command(value = "list-images", help = "Lists the images")
	public void listImages(
			@Option(name = "apiLogin", required = true, help = "A login for the Cloud Server Pro API") final String apiLogin,
			@Option(name = "apiSecretKey", required = true, help = "The secret key for the specified login") final String apiSecretKey) {
		ShellMessages.info(shell, "Calling the Cloud Server Pro API");

		final Client client = Client.create();

		client.addFilter(new SignatureClientFilter(shell, apiLogin, 0,
				new Date(), apiSecretKey));

		final Images resource = CloudLocawebComBr_Api.images(client);

		final br.eti.fernandoribeiro.schemas.cloudserverpro.Images images = resource
				.getAsApplicationXml(br.eti.fernandoribeiro.schemas.cloudserverpro.Images.class);

		shell.println("ID,Name,Description");

		for (final Image image : images.getImageList())
			shell.println(image.getId() + "," + image.getName() + ","
					+ image.getDescription());

	}

	@Command(value = "list-instances", help = "Lists the instances")
	public void listInstances(
			@Option(name = "apiLogin", required = true, help = "A login for the Cloud Server Pro API") final String apiLogin,
			@Option(name = "apiSecretKey", required = true, help = "The secret key for the specified login") final String apiSecretKey) {
		ShellMessages.info(shell, "Calling the Cloud Server Pro API");

		final Client client = Client.create();

		client.addFilter(new SignatureClientFilter(shell, apiLogin, 0,
				new Date(), apiSecretKey));

		final Instances resource = CloudLocawebComBr_Api.instances(client);

		final br.eti.fernandoribeiro.schemas.cloudserverpro.Instances instances = resource
				.getAsApplicationXml(br.eti.fernandoribeiro.schemas.cloudserverpro.Instances.class);

		shell.println("ID,Name,State");

		for (final Instance instance : instances.getInstanceList())
			shell.println(instance.getId() + "," + instance.getName() + ","
					+ instance.getState());

	}

	@Command(value = "reboot-instance", help = "Reboots the specified instance")
	public void rebootInstance(
			@Option(name = "apiLogin", required = true, help = "A login for the Cloud Server Pro API") final String apiLogin,
			@Option(name = "apiSecretKey", required = true, help = "The secret key for the specified login") final String apiSecretKey,
			@Option(name = "instanceId", required = true, help = "The ID of the instance to reboot") final int instanceId) {
		ShellMessages.info(shell, "Calling the Cloud Server Pro API");

		final Client client = Client.create();

		client.addFilter(new SignatureClientFilter(shell, apiLogin, 0,
				new Date(), apiSecretKey));

		final InstancesIdReboot resource = CloudLocawebComBr_Api
				.instancesIdReboot(client, instanceId);

		resource.postAsvoid();
	}

	@Command(value = "show-instance-detail", help = "Shows the detail of the specified instance")
	public void showServerDetail(
			@Option(name = "apiLogin", required = true, help = "A login for the Cloud Server Pro API") final String apiLogin,
			@Option(name = "apiSecretKey", required = true, help = "The secret key for the specified login") final String apiSecretKey,
			@Option(name = "instanceId", required = true, help = "The ID of the instance to show the detail of") final int instanceId) {
		ShellMessages.info(shell, "Calling the Cloud Server Pro API");

		final Client client = Client.create();

		client.addFilter(new SignatureClientFilter(shell, apiLogin, 0,
				new Date(), apiSecretKey));

		final InstancesId resource = CloudLocawebComBr_Api.instancesId(client,
				instanceId);

		final Instance instance = resource.getAsApplicationXml(Instance.class);

		shell.println("ID,Name,State");

		shell.println(instance.getId() + "," + instance.getName() + ","
				+ instance.getState());
	}

}