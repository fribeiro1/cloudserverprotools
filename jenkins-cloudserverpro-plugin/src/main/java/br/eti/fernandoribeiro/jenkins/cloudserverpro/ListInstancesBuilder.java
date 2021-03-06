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

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;

import java.io.PrintStream;
import java.util.Date;

import org.kohsuke.stapler.DataBoundConstructor;

import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.Instances;
import br.eti.fernandoribeiro.schemas.cloudserverpro.Instance;

import com.sun.jersey.api.client.Client;

public class ListInstancesBuilder extends Builder {

	@Extension
	public static class ListInstancesBuilderDescriptor extends
			BuildStepDescriptor<Builder> {

		@Override
		public String getDisplayName() {
			return "Cloud Server Pro: List Instances";
		}

		@Override
		public boolean isApplicable(
				@SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
			return true;
		}

	}

	private String apiLogin;

	private String apiSecretKey;

	@DataBoundConstructor
	public ListInstancesBuilder(String apiLogin, String apiSecretKey) {
		this.apiLogin = apiLogin;

		this.apiSecretKey = apiSecretKey;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build,
			Launcher launcher, BuildListener listener) {
		PrintStream logger = listener.getLogger();

		logger.println("Calling the Cloud Server Pro API");

		Client client = Client.create();

		client.addFilter(new SignatureClientFilter(logger, apiLogin, 0,
				new Date(), apiSecretKey));

		Instances resource = CloudLocawebComBr_Api.instances(client);

		br.eti.fernandoribeiro.schemas.cloudserverpro.Instances instances = resource
				.getAsApplicationXml(br.eti.fernandoribeiro.schemas.cloudserverpro.Instances.class);

		logger.println("ID,Name,State");

		for (Instance instance : instances.getInstanceList())
			logger.println(instance.getId() + "," + instance.getName() + ","
					+ instance.getState());

		return true;
	}

}