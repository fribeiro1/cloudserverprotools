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
package br.eti.fernandoribeiro.cloudserverpro.cli;

import static java.lang.System.out;

import java.util.Date;
import java.util.logging.Logger;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.Instances;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.InstancesId;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.InstancesIdReboot;
import br.eti.fernandoribeiro.schemas.cloudserverpro.Instance;

import com.sun.jersey.api.client.Client;

@Component
public class InstanceCommands implements CommandMarker {
	private static Logger LOGGER = Logger
			.getLogger(InstanceCommands.class.getName());

	@CliCommand(value = "cloudserverpro list-instances", help = "Lists the instances")
	public void listInstances(
			@CliOption(key = { "apiLogin" }, mandatory = true, help = "A login for the Cloud Server Pro API") String apiLogin,
			@CliOption(key = { "apiSecretKey" }, mandatory = true, help = "The secret key for the specified login") String apiSecretKey) {
		LOGGER.info("Calling the Cloud Server Pro API");

		Client client = Client.create();

		client.addFilter(new SignatureClientFilter(apiLogin, 0, new Date(),
				apiSecretKey));

		Instances resource = CloudLocawebComBr_Api.instances(client);

		br.eti.fernandoribeiro.schemas.cloudserverpro.Instances instances = resource
				.getAsApplicationXml(br.eti.fernandoribeiro.schemas.cloudserverpro.Instances.class);

		out.println("ID,Name,State");

		for (Instance instance : instances.getInstanceList())
			out.println(instance.getId() + "," + instance.getName() + ","
					+ instance.getState());

	}

	@CliCommand(value = "cloudserverpro reboot-instance", help = "Reboots the specified instance")
	public void rebootInstance(
			@CliOption(key = { "apiLogin" }, mandatory = true, help = "A login for the Cloud Server Pro API") String apiLogin,
			@CliOption(key = { "apiSecretKey" }, mandatory = true, help = "The secret key for the specified login") String apiSecretKey,
			@CliOption(key = { "instanceId" }, mandatory = true, help = "The ID of the instance to reboot") int instanceId) {
		LOGGER.info("Calling the Cloud Server Pro API");

		Client client = Client.create();

		client.addFilter(new SignatureClientFilter(apiLogin, 0, new Date(),
				apiSecretKey));

		LOGGER.finer("Instance ID = " + instanceId);

		InstancesIdReboot resource = CloudLocawebComBr_Api
				.instancesIdReboot(client, instanceId);

		resource.postAsvoid();
	}

	@CliCommand(value = "cloudserverpro show-instance-detail", help = "Shows the detail of the specified instance")
	public void showInstanceDetail(
			@CliOption(key = { "apiLogin" }, mandatory = true, help = "A login for the Cloud Server Pro API") String apiLogin,
			@CliOption(key = { "apiSecretKey" }, mandatory = true, help = "The secret key for the specified login") String apiSecretKey,
			@CliOption(key = { "instanceId" }, mandatory = true, help = "The ID of the instance to show the detail of") int instanceId) {
		LOGGER.info("Calling the Cloud Server Pro API");

		Client client = Client.create();

		client.addFilter(new SignatureClientFilter(apiLogin, 0, new Date(),
				apiSecretKey));

		InstancesId resource = CloudLocawebComBr_Api.instancesId(client,
				instanceId);

		Instance instance = resource.getAsApplicationXml(Instance.class);

		out.println("ID,Name,State");

		out.println(instance.getId() + "," + instance.getName() + ","
				+ instance.getState());
	}

}