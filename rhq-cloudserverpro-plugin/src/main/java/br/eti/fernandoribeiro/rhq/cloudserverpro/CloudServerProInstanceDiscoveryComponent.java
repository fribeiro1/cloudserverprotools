/*
 * Copyright 2013 Fernando Ribeiro
 * 
 * This file is part of Cloud Server Pro Tools.
 *
 * Cloud Server Pro Tools is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Cloud Server Pro Tools is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Cloud Server Pro Tools. If not, see <http://www.gnu.org/licenses/>.
 */
package br.eti.fernandoribeiro.rhq.cloudserverpro;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.ManualAddFacet;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryComponent;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.platform.PlatformComponent;

import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api;
import br.eti.fernandoribeiro.cloudserverpro.api.client.CloudLocawebComBr_Api.InstancesId;
import br.eti.fernandoribeiro.schemas.cloudserverpro.Instance;

import com.sun.jersey.api.client.Client;

public final class CloudServerProInstanceDiscoveryComponent implements
		ResourceDiscoveryComponent<PlatformComponent>,
		ManualAddFacet<PlatformComponent> {

	@Override
	public DiscoveredResourceDetails discoverResource(
			final Configuration pluginConfiguration,
			final ResourceDiscoveryContext<PlatformComponent> context) {
		final Client client = Client.create();

		client.addFilter(new SignatureClientFilter(pluginConfiguration
				.getSimpleValue(PropertyNames.API_LOGIN), 0, new Date(),
				pluginConfiguration
						.getSimpleValue(PropertyNames.API_SECRET_KEY)));

		final InstancesId resource = CloudLocawebComBr_Api.instancesId(client,
				pluginConfiguration.getSimple(PropertyNames.INSTANCE_ID)
						.getIntegerValue());

		final Instance instance = resource.getAsApplicationXml(Instance.class);

		return new DiscoveredResourceDetails(context.getResourceType(),
				instance.getName(), instance.getName(), null, null,
				pluginConfiguration, null);
	}

	@Override
	public Set<DiscoveredResourceDetails> discoverResources(
			final ResourceDiscoveryContext<PlatformComponent> context) {
		return new HashSet<DiscoveredResourceDetails>();
	}

}