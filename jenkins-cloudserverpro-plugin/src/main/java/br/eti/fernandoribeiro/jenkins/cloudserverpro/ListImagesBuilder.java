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
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.Images;
import br.eti.fernandoribeiro.schemas.cloudserverpro.Image;

import com.sun.jersey.api.client.Client;

public final class ListImagesBuilder extends Builder {

	@Extension
	public static final class ListImagesBuilderDescriptor extends
			BuildStepDescriptor<Builder> {

		@Override
		public String getDisplayName() {
			return "Cloud Server Pro: List Images";
		}

		@Override
		public boolean isApplicable(
				@SuppressWarnings("rawtypes") final Class<? extends AbstractProject> jobType) {
			return true;
		}

	}

	private String apiLogin;

	private String apiSecretKey;

	@DataBoundConstructor
	public ListImagesBuilder(final String apiLogin, final String apiSecretKey) {
		this.apiLogin = apiLogin;

		this.apiSecretKey = apiSecretKey;
	}

	@Override
	public boolean perform(final AbstractBuild<?, ?> build,
			final Launcher launcher, final BuildListener listener) {
		final PrintStream logger = listener.getLogger();

		logger.println("Calling the Cloud Server Pro API");

		final Client client = Client.create();

		client.addFilter(new SignatureClientFilter(logger, apiLogin, 0,
				new Date(), apiSecretKey));

		final Images resource = CloudLocawebComBr_Api.images(client);

		final br.eti.fernandoribeiro.schemas.cloudserverpro.Images images = resource
				.getAsApplicationXml(br.eti.fernandoribeiro.schemas.cloudserverpro.Images.class);

		logger.println("ID,Name,Description");

		for (final Image image : images.getImageList())
			logger.println(image.getId() + "," + image.getName() + ","
					+ image.getDescription());

		return true;
	}

}